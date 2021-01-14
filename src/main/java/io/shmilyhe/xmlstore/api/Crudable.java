package io.shmilyhe.xmlstore.api;

/*
 * @(#)Curdable.java   1.0  2011-9-7
 * 
 * Copyright (c)	2010-2015. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */


/**
 * 实现该接口的POJO可以作为CRUD操作的参数和返回值。
 * 
 * @auther 黄炳雄
 * @version 1.0  2011-9-7
 */
public interface Crudable {

	/**
	 * 主键。
	 */
	public Long getId();

	/**
	 * @see #getId
	 */
	public void setId(Long id);
}
