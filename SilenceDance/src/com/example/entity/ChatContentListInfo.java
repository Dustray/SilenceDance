package com.example.entity;

public class ChatContentListInfo {
	private String content;
	public static final  int send=1;
	public static final  int  receiver=2;
	private int flag;
	private String time;
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public ChatContentListInfo(String content, int flag,String time) {
		super();
		setContent(content);
		setFlag(flag);
		setTime(time);
		
	}
	
}
