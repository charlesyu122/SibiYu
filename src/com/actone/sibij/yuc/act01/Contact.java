package com.actone.sibij.yuc.act01;

public class Contact {
	
	long id;
	String appContactsId;
	String name;
	String number;
	
	// Constructors
	public Contact(){}
	public Contact(String id, String name, String num){
		this.appContactsId = id;
		this.name = name;
		this.number = num;
	}
	
	// Setters
	public void setId(long id){
		this.id = id;
	}
	
	public void setAppId(String appId){
		this.appContactsId = appId;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setNumber(String number){
		this.number = number;
	}
}
