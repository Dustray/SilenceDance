package com.example.entity;

public class CircleDynamicInfo {

	private String dynamicUserName;
	private String dynamicContent;
	private String dynamicDiscuss;
	private String dynamicPictureUrl;

	private String dynamicDate;

	public String getDynamicPictureUrl() {
		return dynamicPictureUrl;
	}

	public void setDynamicPictureUrl(String dynamicPictureUrl) {
		this.dynamicPictureUrl = dynamicPictureUrl;
	}

	public String getDynamicDate() {
		return dynamicDate;
	}

	public void setDynamicDate(String dynamicDate) {
		this.dynamicDate = dynamicDate;
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

	public CircleDynamicInfo(String dynamicUserName, String dynamicContent,
			String dynamicDiscuss, String dynamicDate) {
		super();
		this.dynamicUserName = dynamicUserName;
		this.dynamicContent = dynamicContent;
		this.dynamicDiscuss = dynamicDiscuss;
		this.dynamicDate = dynamicDate;
	}

	public CircleDynamicInfo() {
		super();
	}

}
