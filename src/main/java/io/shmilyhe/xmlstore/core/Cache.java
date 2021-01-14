package io.shmilyhe.xmlstore.core;

/*
 * @(#)CacheBuilder.java   1.0  2011-9-26
 * 
 * Copyright (c)	2010-2015. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.reflect.FieldUtils;

import io.shmilyhe.xmlstore.api.Crudable;
import io.shmilyhe.xmlstore.exception.CrudConstraintException;



/**
 * 缓存器，支持创建索引以便高性能查询。<p>
 * 
 * 允许Key为null，但Key为null的对象无法通过该Key获取，只能通过其它Key或getAll()方法获取。<p>
 * 
 * 非线程安全。<p>
 * 
 * 用法示例：
 * <pre>
 * Cache<AvpInfo> cache = new Cache<AvpInfo>();
 * cache.createIndex("name", "name");
 * cache.createIndex("code", "code", "vendorId");
 * cache.add(avpInfo1);
 * cache.add(avpInfo2);
 * System.out.println(cache.get(1L));
 * System.out.println(cache.get("name", "Event-Timestamp"));
 * System.out.println(cache.get("code", 268, 0));
 * </pre>
 * 
 * @auther 黄炳雄
 * @version 1.0  2011-9-26
 */
public class Cache<T extends Crudable> {

	private Long seq = 1L;
	private HashMap<String, String[]> keyFields = new HashMap<String, String[]>();

	private ArrayList<T> list = new ArrayList<T>();
	private HashMap<Long, T> idCache = new HashMap<Long, T>();
	private HashMap<String, HashMap<Key, T>> keyCaches = new HashMap<String, HashMap<Key, T>>();
	
	/**
	 * 创建索引，创建后可以使用{@link #get(String, Object...)}高性能的查询对象。
	 * 
	 * @param keyName	索引名。
	 * @param fields	要索引的字段（一个或多个）
	 */
	public void createIndex(String keyName, String ... fields) {
		keyFields.put(keyName, fields);
		keyCaches.put(keyName, new HashMap<Key, T>());
	}
	
	/**
	 * 通过ID查询对象。算法复杂度为O(1)。
	 */
	public T get(Long id) {
		return idCache.get(id);
	}
	
	/**
	 * 通过索引键查询对象。算法复杂度为O(1)。
	 * 
	 * @param keyName 索引名。由{@link #createIndex(String, String...)}方法创建。
	 * @param values 要查询的条件值。
	 * @return
	 */
	public T get(String keyName, Object... values) {
		return keyCaches.get(keyName).get(new Key(values));
	}
	
	/**
	 * 返回所有。
	 * 
	 * @return 永远不返回null，找不到就返回长度为0的List。
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		//浅拷贝，可防止List被意外修改，但无法防止List元素的属性被修改。
		return (List<T>) list.clone();
	}
	
	/**
	 * 向Cache中添加对象。
	 * 
	 * @throws CrudConstraintException 违反唯一性约束时抛出（如id重复，索引键重复）
	 */
	public void add(T t) throws CrudConstraintException {
		if(t.getId() == null || t.getId() <= 0) {
			t.setId(seq++);
		} else {
			seq = (seq > t.getId()) ? seq : t.getId() + 1;
		}
		checkConstraintWhenAdding(t);
		list.add(t);
		idCache.put(t.getId(), t);
		for(String keyName : keyCaches.keySet()) {
			Key key = getKey(t, keyFields.get(keyName));
			if(!key.isNull()) {
				keyCaches.get(keyName).put(key, t);
			}
		}
	}
	
	/**
	 * 更新Cache中的对象。
	 * 
	 * @throws CrudConstraintException 违反唯一性约束时抛出（如索引键重复）
	 */
	public void update(T t) throws CrudConstraintException {
		checkConstraintWhenUpdating(t);
		try {
			T old = idCache.get(t.getId());
			if(old == t) {
				//如果更新的对象与Cache中的对象内存地址相同，意味着Cache中的对象已经被意外修改，需要重建索引。
				//如果不重建，在某些情况下会导致旧key清除不掉。
				rebuildKeyCaches();
				return;
			}
			for(String keyName : keyCaches.keySet()) {
				HashMap<Key, T> map = keyCaches.get(keyName);
				String[] fields = keyFields.get(keyName);
				Key oldKey = getKey(old, fields);
				Key newKey = getKey(t, fields);
				map.remove(oldKey);
				map.put(newKey, t);
			}
			BeanUtils.copyProperties(old, t);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private void rebuildKeyCaches() {
		for(String keyName : keyCaches.keySet()) {
			HashMap<Key, T> map = keyCaches.get(keyName);
			map.clear();
			for(T t : list) {
				Key key = getKey(t, keyFields.get(keyName));
				map.put(key, t);
			}
		}
	}
	
	private void checkConstraintWhenAdding(T t) throws CrudConstraintException {
		if(idCache.containsKey(t.getId())) {
			throw new CrudConstraintException("id:" + t.getId());
		}
		for(String keyName : keyCaches.keySet()) {
			Key key = getKey(t, keyFields.get(keyName));
			if(keyCaches.get(keyName).containsKey(key)) {
				String fields = Arrays.asList(keyFields.get(keyName)).toString();
				String values = Arrays.asList(key.getFields()).toString();
				throw new CrudConstraintException(fields + ":" + values);
			}
		}
	}
	
	private void checkConstraintWhenUpdating(T t) throws CrudConstraintException {
		for(String keyName : keyCaches.keySet()) {
			Key key = getKey(t, keyFields.get(keyName));
			T old = keyCaches.get(keyName).get(key);
			if(old != null && !old.getId().equals(t.getId())) {
				String fields = Arrays.asList(keyFields.get(keyName)).toString();
				String values = Arrays.asList(key.getFields()).toString();
				throw new CrudConstraintException(fields + ":" + values);
			}
		}
	}

	/**
	 * 移除Cache中的对象。
	 * 
	 * @return	被移除的对象，如果找不到返回null。
	 */
	public T remove(T t) {
		T old = idCache.remove(t.getId());
		list.remove(old);
		for(String keyName : keyCaches.keySet()) {
			Key key = getKey(t, keyFields.get(keyName));
			keyCaches.get(keyName).remove(key);
		}
		return old;
	}
	
	/**
	 * 清除Cache全部对象。
	 */
	public void clear() {
		list.clear();
		idCache.clear();
		for(String keyName : keyCaches.keySet()) {
			keyCaches.get(keyName).clear();
		}
	}
	
	private Key getKey(T t, String[] fields) {
		try {
			Key key = new Key(fields.length);
			for(int i = 0; i < fields.length; i++) {
				String fieldName = fields[i];
				Field field = FieldUtils.getField(t.getClass(), fieldName, true);
				Object value = FieldUtils.readField(field, t);
				key.set(i, value);
			}
			return key;
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
	}
}
