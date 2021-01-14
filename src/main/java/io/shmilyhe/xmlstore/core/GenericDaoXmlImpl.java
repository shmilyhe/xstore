package io.shmilyhe.xmlstore.core;

/*
 * @(#)AvpInfoDao.java   1.0  2011-8-20
 * 
 * Copyright (c)	2010-2015. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;









import java.util.logging.Logger;

import com.thoughtworks.xstream.XStream;

import io.shmilyhe.xmlstore.api.GenericDao;
import io.shmilyhe.xmlstore.exception.CrudException;
import io.shmilyhe.xmlstore.utils.ReflectUtils;

/**
 * 把对象保存到XML中，或放过来。
 * 
 * @auther 黄炳雄
 * @version 1.0  2011-8-20
 */
@SuppressWarnings("rawtypes")
public class GenericDaoXmlImpl implements GenericDao {
	public static final int MODE_DEFAULT=1;
	public static final int MODE_HASH=1;
	public static final int MODE_OTHER=2;
	private static int BUCKET_SIZE=100;
	private final static String FOLDER = "conf/xstream";
	private final static String FILENAME_PATTERN = "%s.xml";

	private Class[] classes;
	private List<String> excludeFields;
	private List<String> useTagFields;
	private String fileName;
	private XStream xstream;
	private final static Logger log=Logger.getLogger(GenericDaoXmlImpl.class.getName()) ;//= //LoggerFactory.getLogger(GenericDaoXmlImpl.class);
	private String name;
	private int mode=0;
	
	
	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public void setFile(String name){
		this.name=name;
	}

	
	public void init() {
		try {
			if(fileName==null){
			String dir =GenericDaoXmlImpl.class.getProtectionDomain().getCodeSource().getLocation().getFile();
			int off=dir.indexOf("WEB-INF");
			if(off>-1){
				dir=dir.substring(0, off-1);
			}
			File pf = new File(dir).getParentFile();
			StringBuffer pathBuffer = new StringBuffer();
			//File dataDir = new File(pf.getAbsolutePath()+File.separator+FOLDER);
			pathBuffer.append(pf.getAbsolutePath()).append(File.separator).append(FOLDER);
			//String folder = Thread.currentThread().getContextClassLoader().getResource(FOLDER).getPath();
			String name = null;//String.format(FILENAME_PATTERN, classes[0].getSimpleName());
			
			if(this.name==null){
				name=String.format(FILENAME_PATTERN, classes[0].getSimpleName());
			}else{
				name=this.name;
			}
			
			if(mode==MODE_HASH){
				int mod =name.hashCode()%BUCKET_SIZE;
				if(mod<0){
					pathBuffer.append(File.separator).append(Math.abs(mod))
					.append(File.separator).append("a");
				}else{
					pathBuffer.append(File.separator).append(mod);
				}
				//dataDir = new File(pf.getAbsolutePath()+File.separator+FOLDER+File.separator+mod);
			}else{
				
			}
			File dataDir = new File(pathBuffer.toString());
			if(!dataDir.exists()){
				dataDir.mkdirs();
			}
			pathBuffer.append(File.separator).append(name);
			fileName = pathBuffer.toString();//dataDir + File.separator + name;
			log.info("aaa="+fileName);
			xstream = getXStream(classes);
			}
			createFileIfNotExist();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private void createFileIfNotExist() {
		if(!new File(fileName).exists()) {
			this.saveAll(new ArrayList());
		}
	}

	private XStream getXStream(Class[] classes) throws ClassNotFoundException {
		XStream xs = new XStream();
		for(Class clazz : classes) {
			xs.alias(clazz.getSimpleName(), clazz);
			List<Field> fields = ReflectUtils.getAllFields(clazz);
			for(Field field : fields) {
				String fieldQualifiedName = clazz.getName() + "." + field.getName();
				if(useTagFields == null || !useTagFields.contains(fieldQualifiedName)) {
					xs.useAttributeFor(clazz, field.getName());
				}
			}
		}
		if(excludeFields != null) {
			for(String field : excludeFields) {
				xs.omitField(parseClass(field), parseFieldName(field));
			}
		}
        return xs;
	}
	
	private Class parseClass(String field) throws ClassNotFoundException {
		String className = field.substring(0, field.lastIndexOf('.'));
		return Class.forName(className);
	}
	
	private String parseFieldName(String field) {
		return field.substring(field.lastIndexOf('.') + 1);
	}
	
	public List getAll() {
		Reader reader = null;
		try {
			log.info("bbb="+fileName);
			reader = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
			return (List) xstream.fromXML(reader);
		} catch (Exception ex) {
			throw new CrudException("读取XML失败", ex);
		} finally {
			try { reader.close(); } catch (Exception ex) {}
		}
	}
	
	public void saveAll(List list) throws CrudException {
		Writer writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
			xstream.toXML(list, writer);
		} catch (Exception ex) {
			throw new CrudException("保存XML失败", ex);
		} finally {
			try { writer.close(); } catch (Exception ex) {}
		}
	}
	
	public void setClasses(Class[] classes) {
		this.classes = classes;
	}

	public void setExcludeFields(List<String> excludeFields) {
		this.excludeFields = excludeFields;
	}

	public void setUseTagFields(List<String> useTagFields) {
		this.useTagFields = useTagFields;
	}


	public int getMode() {
		return mode;
	}


	public void setMode(int mode) {
		this.mode = mode;
	}
}
