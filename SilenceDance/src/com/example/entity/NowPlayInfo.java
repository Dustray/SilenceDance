package com.example.entity;

public class NowPlayInfo {

	private static int[] NOW_ids;// ��������ID��ʱ����

	private static String[] NOW_titles;// ������ʱ����

	private static String[] NOW_artists;// ����������

	private static int NOW_position;// �������λ��
	
	private static int PLAYs=0;//����״̬��Ĭ����ͣ
	
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
