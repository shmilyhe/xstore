package io.shmilyhe.xmlstore.api;

/*
 * @(#)Aaaa.java   1.0  2011-9-7
 * 
 * Copyright (c)	2010-2015. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */


import java.util.EventListener;
import java.util.List;

import io.shmilyhe.xmlstore.exception.CrudException;

/**
 * 实现该接口以支持CRUD(Create/Read/Update/Delete)操作。
 * 
 * @auther 黄炳雄
 * @version 1.0  2011-9-7
 */
public interface Crud<T extends Crudable> {
	
	/**
	 * 返回所有。
	 * 
	 * @return	永远不返回null，找不到就返回长度为0的List。
	 */
	public List<T> getAll();
	
	/**
	 * 根据ID返回对象。
	 * 
	 * @return 找不到就返回null。
	 */
	public T get(Long id);

	/**
	 * 删除全部。保证事务性。
	 */
	public void removeAll(List<T> list) throws CrudException;
	
	/**
	 * 添加或更新全部。不存在就添加，存在就更新。保证事务性。
	 */
	public void saveOrUpdateAll(List<T> list) throws CrudException;

//	/**
//	 * 添加全部，保证事务性。
//	 */
//	public void saveAll(List<T> list) throws CrudException;
//	
//	/**
//	 * 更新全部，保证事务性。
//	 */
//	public void updateAll(List<T> list) throws CrudException;
//	
//	/**
//	 * 添加。
//	 */
//	public void save(T t) throws CrudException;
//	
//	/**
//	 * 更新，不能更新主键。
//	 */
//	public void update(T t) throws CrudException;
//	
	/**
	 * 删除。
	 */
	public void remove(T t) throws CrudException;
	
	/**
	 * 添加或更新。不存在就添加，存在就更新。
	 */
	public void saveOrUpdate(T t) throws CrudException;
	
	public interface Listener<T extends Crudable> extends EventListener {
		public void savedOrUpdated(T t);
		public void removed(T t);
	}
}
