package com.example.adapter;

import java.util.List;

import com.example.silencedance.R;
import com.example.tools.ContextApplication;
import com.hyphenate.chat.EMGroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CircleListAdapter extends ArrayAdapter<EMGroup> {
	SharedPreferences pref;
	private int resourceId;

	public CircleListAdapter(Context context, int textViewResourceId,
			List<EMGroup> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	public View getView(int position, View convertView, ViewGroup parent) {// 第一个参数表示位置，第二个参数表示缓存布局，第三个表示绑定的view对象
		View view;
		ViewHolders viewHolder; // 实例ViewHolder，当程序第一次运行，保存获取到的控件，提高效率
		if (convertView == null) {
			viewHolder = new ViewHolders();
			view = LayoutInflater.from(getContext()).inflate(// convertView为空代表布局没有被加载过，即getView方法没有被调用过，需要创建
					resourceId, null); // 得到子布局，非固定的，和子布局id有关
			viewHolder.circleName = (TextView) view
					.findViewById(R.id.circleName);// 获取控件,只需要调用一遍，调用过后保存在ViewHolder中
			viewHolder.itsMine = (TextView) view.findViewById(R.id.itsMine); // 获取控件
			view.setTag(viewHolder);
		} else {
			view = convertView; // convertView不为空代表布局被加载过，只需要将convertView的值取出即可
			viewHolder = (ViewHolders) view.getTag();
		}

		EMGroup grouplist = getItem(position);// 实例指定位置的水果

		viewHolder.circleName.setText(grouplist.getGroupName());// 获得指定位置水果的id
		pref = ContextApplication.getAppContext().getSharedPreferences(
				"userInfo", 0);
		String userPhone = pref.getString("userPhone", "");
		viewHolder.itsMine.setVisibility(View.INVISIBLE);
		if (grouplist.getOwner().equals(userPhone)) {
			viewHolder.itsMine.setVisibility(View.VISIBLE);
		}
		return view;

	}
}

class ViewHolders { // 当布局加载过后，保存获取到的控件信息。
	TextView circleName;
	TextView itsMine;
}
