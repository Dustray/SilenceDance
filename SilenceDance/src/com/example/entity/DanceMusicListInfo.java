package com.example.entity;

import cn.bmob.v3.BmobObject;

public class DanceMusicListInfo extends BmobObject {
	
	/** ��Ȧ�� */
	private String teamName;
	/** ���� */
	private String musicName;
	/** ������ */
	private String airtistName;
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getMusicName() {
		return musicName;
	}
	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}
	public String getAirtistName() {
		return airtistName;
	}
	public void setAirtistName(String airtistName) {
		this.airtistName = airtistName;
	}
	
}
