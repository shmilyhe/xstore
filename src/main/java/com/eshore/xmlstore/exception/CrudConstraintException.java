package com.eshore.xmlstore.exception;



/**
 * CRUD违反约束条件（如唯一性约束）时抛出。
 * 
 * @auther 黄炳雄
 * @version 1.0  2011-9-26
 */
public class CrudConstraintException extends CrudException {

	private static final long serialVersionUID = -4703925814495019405L;

	public CrudConstraintException(String msg) {
		super(msg);
	}

	public CrudConstraintException(String msg, Exception ex) {
		super(msg, ex);
	}
}
