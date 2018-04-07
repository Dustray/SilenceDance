package com.example.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import com.example.entity.Music;
import com.example.listener.OnLoadSearchFinishListener;


public class SearchUtils {
	/** �����ؼ��ֵ�ַ */
	public static String KEY_SEARCH_URL = "http://www.xiami.com/search/song?key=";
	/** ID�ӿڵ�ַ */
	public static String ID_SEARCH_URL = "http://www.xiami.com/song/playlist/id/";

	/**
	 * ץȡ����id
	 * 
	 * @param �����ؼ���
	 * @param listener
	 *            ��ɼ���
	 */
	public static void getIds(String input, OnLoadSearchFinishListener listener) {
		List<String> allIds = new ArrayList<String>();
		String key = deCondeKey(input);// �����û�����ؼ���Ϊ UTF-8
		Document document = null;
		try {
			document = Jsoup.connect(KEY_SEARCH_URL + key).get();// jsoup��������ƴ�Ӷ��ɵ������ַ���
			Elements elements = document.getElementsByClass("track_list");// ѡ�����ǩ
			if (elements.size() != 0) {
				Elements all = elements.get(0).getElementsByClass("chkbox");
				int size = all.size();
				for (int i = 0; i < size; i++) {
					String id = all.get(i).select("input").attr("value");
					if (!StringUtils.isEmpty(id)) {
						allIds.add(id);// ��Ϊ�յĻ�����id list�У����ڳ���ץȡ���Ժ�ͳһ����
					}
				}
				if (listener != null) {
					if (allIds.size() == 0) {
						listener.onLoadFiler();// id list��СΪ0 ˵��û�л�ȡ�����ݣ�ץȡʧ��
					} else {
						// ͳһ����id�ӿڵ�ַ�����ٴ�ץȡ
						listener.onLoadSucess(getOnlineSearchList(allIds));
					}
				}
			}

		} catch (IOException e) {
			listener.onLoadFiler();
			e.printStackTrace();
		}
	}

	/**
	 * ����id ��ȡ��������
	 * 
	 * @param ids
	 *            ��װid ��list
	 * @return ��װ�õ�list<music> ����listviewչʾ
	 */
	private static List<Music> getOnlineSearchList(List<String> ids) {
		List<Music> musicList = new ArrayList<Music>();
		int idSize = ids.size();
		for (int i = 0; i < idSize; i++) {
			String postUrl = ID_SEARCH_URL + ids.get(i);
			try {
				Document d = Jsoup.connect(postUrl).get();// ������ӦID�Ľӿڵ�ַ
				Elements element = d.select("trackList");
				for (Element e : element) {
					Music music = new Music();
					music.setMusicId(ids.get(i));
					music.setMusciName(getSubString(e.select("title").text()));
					music.setAirtistName(e.select("artist").text());
					music.setSmallAlumUrl(e.select("pic").text());
					music.setBigAlumUrl(e.select("album_pic").text());
					music.setLrcUrl(e.select("lyric").text());
					music.setAlbumName(e.select("album_name").text());
					// �Լ��ܹ���ĸ������ߵ�ַ���н���
					music.setPath(StringUtils.decodeMusicUrl(e.select(
							"location").text()));
					musicList.add(music);// ���ݻ�ȡ�ɹ� ��װ��list
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return musicList;
	}

	/**
	 * ��������
	 * 
	 * @return
	 */
	private static String getSubString(String name) {
		int start = name.indexOf("[", 3) + 1;
		int end = name.indexOf("]");
		return name.substring(start, end);
	}

	private static String deCondeKey(String input) {
		try {
			String key = URLEncoder.encode(input, "UTF-8");
			return key;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
}
