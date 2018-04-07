package com.example.table;

import cn.bmob.v3.BmobObject;

public class UserFeedBack extends BmobObject{

	private String userName;
	private String userFeedBack;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserFeedBack() {
		return userFeedBack;
	}
	public void setUserFeedBack(String userFeedBack) {
		this.userFeedBack = userFeedBack;
	}
}
