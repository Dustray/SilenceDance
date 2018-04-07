package com.example.table;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class CircleDynamic extends BmobObject{

	private String dynamicUserName;
	private String dynamicContent;
	private String dynamicDiscuss;
	private BmobFile dynamicPicture;
	
	
	public BmobFile getDynamicPicture() {
		return dynamicPicture;
	}
	public void setDynamicPicture(BmobFile dynamicPicture) {
		this.dynamicPicture = dynamicPicture;
	}
	public String getDynamicUserName() {
		return dynamicUserName;
	}
	public void setDynamicUserName(String dynamicUserName) {
		this.dynamicUserName = dynamicUserName;
	}
	public String getDynamicContent() {
		return dynamicContent;
	}
	public void setDynamicContent(String dynamicContent) {
		this.dynamicContent = dynamicContent;
	}
	public String getDynamicDiscuss() {
		return dynamicDiscuss;
	}
	public void setDynamicDiscuss(String dynamicDiscuss) {
		this.dynamicDiscuss = dynamicDiscuss;
	}
	
}
