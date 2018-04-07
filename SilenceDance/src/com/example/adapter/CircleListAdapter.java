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

	public View getView(int position, View convertView, ViewGroup parent) {// ��һ��������ʾλ�ã��ڶ���������ʾ���沼�֣���������ʾ�󶨵�view����
		View view;
		ViewHolders viewHolder; // ʵ��ViewHolder���������һ�����У������ȡ���Ŀؼ������Ч��
		if (convertView == null) {
			viewHolder = new ViewHolders();
			view = LayoutInflater.from(getContext()).inflate(// convertViewΪ�մ�����û�б����ع�����getView����û�б����ù�����Ҫ����
					resourceId, null); // �õ��Ӳ��֣��ǹ̶��ģ����Ӳ���id�й�
			viewHolder.circleName = (TextView) view
					.findViewById(R.id.circleName);// ��ȡ�ؼ�,ֻ��Ҫ����һ�飬���ù��󱣴���ViewHolder��
			viewHolder.itsMine = (TextView) view.findViewById(R.id.itsMine); // ��ȡ�ؼ�
			view.setTag(viewHolder);
		} else {
			view = convertView; // convertView��Ϊ�մ����ֱ����ع���ֻ��Ҫ��convertView��ֵȡ������
			viewHolder = (ViewHolders) view.getTag();
		}

		EMGroup grouplist = getItem(position);// ʵ��ָ��λ�õ�ˮ��

		viewHolder.circleName.setText(grouplist.getGroupName());// ���ָ��λ��ˮ����id
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

class ViewHolders { // �����ּ��ع��󣬱����ȡ���Ŀؼ���Ϣ��
	TextView circleName;
	TextView itsMine;
}
