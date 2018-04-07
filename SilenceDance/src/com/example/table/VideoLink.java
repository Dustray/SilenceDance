package com.example.table;

import cn.bmob.v3.BmobObject;

@SuppressWarnings("serial")
public class VideoLink extends BmobObject {
	private String webVideoName;

	private String webVideoLink;
	private String webVideoInfoName;

	public String getWebVideoInfoName() {
		return webVideoInfoName;
	}

	public void setWebVideoInfoName(String webVideoInfoName) {
		this.webVideoInfoName = webVideoInfoName;
	}

	public String getWebVideoName() {
		return webVideoName;
	}

	public void setWebVideoName(String webVideoName) {
		this.webVideoName = webVideoName;
	}

	public String getWebVideoLink() {
		return webVideoLink;
	}

	public void setWebVideoLink(String webVideoLink) {
		this.webVideoLink = webVideoLink;
	}
	
}
