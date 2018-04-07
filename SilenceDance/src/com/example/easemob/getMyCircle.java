package com.example.easemob;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

public class getMyCircle {

	public List<EMGroup> getMyCircleFromLocal(){
		List<EMGroup> circleList = EMClient.getInstance().groupManager().getAllGroups();
		return circleList;
	}
	
	public List<EMGroup> getMyCircleFromNet(Context context){
		List<EMGroup> circleList = null;
		try {
			circleList = EMClient.getInstance().groupManager()
					.getJoinedGroupsFromServer();
		} catch (HyphenateException e) {
			e.printStackTrace();
			Log.i("mytag","1"+e.toString());
			//Toast.makeText(context, "获取网络舞队列表失败", Toast.LENGTH_SHORT).show();
		}
		return circleList;
	}
}
