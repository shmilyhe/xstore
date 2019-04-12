package com.eshore.xmlstore.api;

import java.util.List;

import com.eshore.xmlstore.exception.CrudException;



/**
 * 通用DAO。
 * 
 * @auther 黄炳雄
 * @version 1.0  2011-8-20
 */
public interface GenericDao<T> {
	
	/**
	 * 返回所有。
	 */
	public List<T> getAll() throws CrudException;
	
	/**
	 * 保存所有，旧数据将全部被覆盖。
	 */
	public void saveAll(List<T> list) throws CrudException;
}