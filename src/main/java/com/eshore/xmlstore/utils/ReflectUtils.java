package com.eshore.xmlstore.utils;

/*
 * @(#)ReflectUtils.java   1.0  2011-9-28
 * 
 * Copyright (c)	2010-2015. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * 反射工具类。
 * 
 * @auther 黄炳雄
 * @version 1.0  2011-9-28
 */
public class ReflectUtils {

	/**
	 * 得到指定类的所有Field（包括从父类继承的）。
	 * 
	 * @return 永远不返回null，找不到就返回长度为0的List。
	 */
	public static List<Field> getAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			fields.addAll(Arrays.asList(c.getDeclaredFields()));
		}
		return fields;
	}

	public static void main(String[] args) {
		List<Field> fields = getAllFields(ReflectUtils.class);
		System.out.println(fields);
	}
}

