package com.eshore.xmlstore.core;

/*
 * @(#)Key.java   1.0  2011-9-26
 * 
 * Copyright (c)	2010-2015. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 键值，支持多列，主要用作Map的Key。
 * 
 * @auther 黄炳雄
 * @version 1.0  2011-9-26
 */
public class Key {
	
	private Object[] fields;
	
	public Key(Object... fields) {
		this.fields = fields;
	}
	
	public Key(int n) {
		this.fields = new Object[n];
	}
	
	public void set(int i, Object obj) {
		this.fields[i] = obj;
	}
	
	public Object[] getFields() {
		return this.fields;
	}
	
	public boolean isNull() {
		for(Object obj : this.fields) {
			if(obj != null) {
				return false;
			}
		}
		return true;
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Key that = (Key) obj;
		return new EqualsBuilder().append(this.fields, that.fields).isEquals();
	}
	
	public int hashCode() {
		return new HashCodeBuilder().append(fields).toHashCode();
	}
	
	public String toString() {
		return new ToStringBuilder(this).append(fields).toString();
	}
}
