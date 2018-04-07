package com.example.entity;

public class NowPlayInfo {

	private static int[] NOW_ids;// 保存音乐ID临时数组

	private static String[] NOW_titles;// 标题临时数组

	private static String[] NOW_artists;// 保存艺术家

	private static int NOW_position;// 点击音乐位置
	
	private static int PLAYs=0;//播放状态，默认暂停
	
	private static boolean isFirstIn = false;

	
	public static boolean isFirstIn() {
		return isFirstIn;
	}

	public static void setFirstIn(boolean isFirstIn) {
		NowPlayInfo.isFirstIn = isFirstIn;
	}

	public static int[] getNOW_ids() {
		return NOW_ids;
	}

	public static void setNOW_ids(int[] nOW_ids) {
		NOW_ids = nOW_ids;
	}

	public static String[] getNOW_titles() {
		return NOW_titles;
	}

	public static void setNOW_titles(String[] nOW_titles) {
		NOW_titles = nOW_titles;
	}

	public static String[] getNOW_artists() {
		return NOW_artists;
	}

	public static void setNOW_artists(String[] nOW_artists) {
		NOW_artists = nOW_artists;
	}

	public static int getNOW_position() {
		return NOW_position;
	}

	public static void setNOW_position(int nOW_position) {
		NOW_position = nOW_position;
	}

	public static int getPLAYs() {
		return PLAYs;
	}

	public static void setPLAYs(int pLAYs) {
		PLAYs = pLAYs;
	}

	
}
