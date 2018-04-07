package com.example.entity;

import java.io.Serializable;

public class Music implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** ���� */
	private String musciName;
	/** ������ */
	private String airtistName;
	/** ����·�� */
	private String path;
	/** ר���� */
	private String albumName;
	/** СͼURL */
	private String smallAlumUrl;
	/** ��ͼURL */
	private String bigAlumUrl;
	/** ����id */
	private String musicId;
	/** ��ʵ�ַ */
	private String lrcUrl;

	public String getMusciName() {
		return musciName;
	}

	public void setMusciName(String musciName) {
		this.musciName = musciName;
	}

	public String getAirtistName() {
		return airtistName;
	}

	public void setAirtistName(String airtistName) {
		this.airtistName = airtistName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getSmallAlumUrl() {
		return smallAlumUrl;
	}

	public void setSmallAlumUrl(String smallAlumUrl) {
		this.smallAlumUrl = smallAlumUrl;
	}

	public String getBigAlumUrl() {
		return bigAlumUrl;
	}

	public void setBigAlumUrl(String bigAlumUrl) {
		this.bigAlumUrl = bigAlumUrl;
	}

	public String getMusicId() {
		return musicId;
	}

	public void setMusicId(String musicId) {
		this.musicId = musicId;
	}

	public String getLrcUrl() {
		return lrcUrl;
	}

	public void setLrcUrl(String lrcUrl) {
		this.lrcUrl = lrcUrl;
	}

}
