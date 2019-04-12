package com.eshore.xmlstore.core;

/*
 * @(#)CrudImpl.java   1.0  2011-9-26
 * 
 * Copyright (c)	2010-2015. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.eshore.xmlstore.api.*;
import com.eshore.xmlstore.exception.CrudException;



/**
 * CRUD实现类。<p>
 * 
 * 使用缓存保证读取高性能，因此比较适合于偶尔修改然后大量读取的场景。<p>
 * 
 * 该类线程安全，多个线程的读取可同时进行不互斥，但读取与写入互斥，所以写入时读取会被阻塞。
 * 
 * @author 黄炳雄
 * @version 1.0  2011-9-26
 */
@SuppressWarnings( {"rawtypes", "unchecked"} )
public class CrudImpl<T extends Crudable> implements Crud<T> {

	private GenericDao<T> genericDao;
	protected Cache<T> cache;
	private Listener[] listeners = new Listener[0];

	private ReadWriteLock lock = new ReentrantReadWriteLock();
	protected Lock read = lock.readLock();
	protected Lock write = lock.writeLock();
	
	/**
	 * 初始化，使用其它方法前必须先调用该方法。
	 */
	public void init() {
		write.lock();
		try {
			initCache();
			reload();
		} finally {
			write.unlock();
		}
	}
	
	protected void initCache() {
		cache = new Cache<T>();
	}
	
	private void reload() {
		List<T> list = genericDao.getAll();
		cache.clear();
		for(T t : list) {
			cache.add(t);
		}
	}

	public T get(Long id) {
		read.lock();
		try {
			return cache.get(id);
		} finally {
			read.unlock();
		}
	}

	public List<T> getAll() {
		read.lock();
		try {
			return cache.getAll();
		} finally {
			read.unlock();
		}
	}

	public void saveOrUpdateAll(List<T> list) throws CrudException {
		write.lock();
		try {
			for(T t : list) {
				Long id = t.getId();
				if(id == null || id <= 0) {
					cache.add(t);
				} else {
					cache.update(t);
				}
			}
			afterUpdateCache(list);
			
			for(T t : list) {
				for(Listener listener : listeners) {
					listener.savedOrUpdated(t);
				}
			}

			genericDao.saveAll(cache.getAll());
		} catch (Exception ex) {
			init();
			throw ex instanceof CrudException ? (CrudException) ex : new CrudException(ex.getMessage(), ex);
		} finally {
			write.unlock();
		}
	}

	public void removeAll(List<T> list) throws CrudException {
		write.lock();
		try {
			for(T t : list) {
				cache.remove(t);
			}
			afterRemoveCache(list);
			
			for(T t : list) {
				for(Listener listener : listeners) {
					listener.removed(t);
				}
			}

			genericDao.saveAll(cache.getAll());
		} catch (Exception ex) {
			init();
			throw ex instanceof CrudException ? (CrudException) ex : new CrudException(ex.getMessage(), ex);
		} finally {
			write.unlock();
		}
	}

	protected void afterUpdateCache(List<T> list) { }
	protected void afterRemoveCache(List<T> list) { }

	public void saveOrUpdate(T t) throws CrudException {
		saveOrUpdateAll(Arrays.asList(t));
	}

	public void remove(T t) throws CrudException {
		removeAll(Arrays.asList(t));
	}
	
	public void setGenericDao(GenericDao<T> genericDao) {
		this.genericDao = genericDao;
	}

	public void setListeners(Listener[] listeners) {
		this.listeners = listeners;
	}
}
