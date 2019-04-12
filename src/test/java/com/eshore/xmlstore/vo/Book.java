package com.eshore.xmlstore.vo;

import com.eshore.xmlstore.api.Crudable;

public class Book implements Crudable{
	private long id;
	private String name;
	private double price;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id=id;
	}
}
