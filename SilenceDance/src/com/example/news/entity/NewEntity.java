package com.example.news.entity;

public class NewEntity {
	private Long id;  //ID����
	private String keywords; //�ؼ���
	private String title;// ���� 
	private String description;// ��� 
	private String img;// ͼƬ 
	private int loreclass;//����
	private int count;// ������ 
	private int rcount;// ������ 
	private int fcount;// �ղ��� 
	private Long time;// ����ʱ�� 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getLoreclass() {
		return loreclass;
	}
	public void setLoreclass(int loreclass) {
		this.loreclass = loreclass;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getRcount() {
		return rcount;
	}
	public void setRcount(int rcount) {
		this.rcount = rcount;
	}
	public int getFcount() {
		return fcount;
	}
	public void setFcount(int fcount) {
		this.fcount = fcount;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}

}
