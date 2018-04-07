package com.example.tools;

import android.util.Log;

public class StringParsing {

	// demo: *^&join&teamid?146025545484&musicname?Сƻ��

	private String ParsingStatement(String strOrder, int wordID) {
		String rtnStr = "null";
		strOrder = strOrder.replace("\"", "");
		String[] ss = strOrder.split("&");
		String typeStr = ss[1];
		String[] teamID = ss[2].split("\\?");
		String[] musicName = ss[3].split("\\?");
		// Log.d("main", "test:"+strOrder+"�ָ�"+ss[3].split("\\?"));

		switch (wordID) {
		case 1:
			rtnStr = typeStr;
			break;
		case 2:
			rtnStr = teamID[1];
			break;

		case 3:
			rtnStr = musicName[1];

			Log.d("main", "test:" + musicName[1]);
			break;
		}
		return rtnStr;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param strOrder
	 * @return
	 */
	public String getTypeByOrder(String strOrder) {

		return ParsingStatement(strOrder, 1);
	}

	/**
	 * ��ȡteamID
	 * 
	 * @param strOrder
	 * @return
	 */
	public String getTeamIDByOrder(String strOrder) {
		return ParsingStatement(strOrder, 2);
	}

	/**
	 * ��ȡmusicName
	 * 
	 * @param strOrder
	 * @return
	 */
	public String getMusicNameByOrder(String strOrder) {

		// Log.d("main", "test:"+strOrder);
		return ParsingStatement(strOrder, 3);
	}

}
