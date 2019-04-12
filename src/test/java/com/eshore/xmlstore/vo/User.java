package com.eshore.xmlstore.vo;

import java.util.List;

import com.eshore.xmlstore.api.Crudable;

public class User implements Crudable {
	private long id;
	
	private String name;
	private String title;
	private String age;
	
	private List<Book> bookList;
	public List<Book> getBookList() {
		return bookList;
	}
	public void setBookList(List<Book> bookList) {
		this.bookList = bookList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id=id;
	}
	
	public String toString(){
		return "name:"+name+"\t title:"+title+" \tage:"+age;
	}
	public String getAge() {
		return age;
	}
	
	public void setAge(String age) {
		this.age = age;
	}

}
