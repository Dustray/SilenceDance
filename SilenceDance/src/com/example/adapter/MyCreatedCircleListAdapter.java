package com.example.adapter;

import java.util.List;

import com.example.silencedance.R;
import com.example.table.VideoLink;
import com.example.tools.WebVideoBitmap;
import com.hyphenate.chat.EMGroup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyCreatedCircleListAdapter extends ArrayAdapter<EMGroup> {

	private int resourceId;
	private List<EMGroup> myCreatedList;
	public MyCreatedCircleListAdapter(Context context, int textViewResourceId,
			List<EMGroup> objects) {
		super(context, textViewResourceId, objects);
		resourceId=textViewResourceId; 
		myCreatedList = objects;
		// TODO Auto-generated constructor stub
	}
	
	public View getView(int position, View convertView, ViewGroup parent) { //��һ��������ʾλ�ã��ڶ���������ʾ���沼�֣���������ʾ�󶨵�view����
		//Log.i("MainActivity","position:"+position);
		View view;
		MyCreatedCircleHolder viewHolder;    //ʵ��ViewHolder���������һ�����У������ȡ���Ŀؼ������Ч��  
		if(convertView==null){
			viewHolder=new MyCreatedCircleHolder();
			view=LayoutInflater.from(getContext()).inflate(resourceId,null);   //convertViewΪ�մ�����û�б����ع�����getView����û�б����ù�����Ҫ�����õ��Ӳ��֣��ǹ̶��ģ����Ӳ���id�й�    
			viewHolder.myCreatedCircleName=(TextView) view.findViewById(R.id.myCreatedCircleName);   //��ȡ�ؼ�,ֻ��Ҫ����һ�飬���ù��󱣴���ViewHolder�� 
			viewHolder.myCreatedCircleID=(TextView) view.findViewById(R.id.myCreatedCircleID);
			view.setTag(viewHolder);
		}else{       //convertView��Ϊ�մ����ֱ����ع���ֻ��Ҫ��convertView��ֵȡ������  
			view=convertView;
			viewHolder=(MyCreatedCircleHolder) view.getTag();
		}   //ʵ��ָ��λ�õ�ʵ����
		
		viewHolder.myCreatedCircleName.setText(myCreatedList.get(position).getGroupName());   //��ȡʵ������Ϣ
		viewHolder.myCreatedCircleID.setText(myCreatedList.get(position).getGroupId());
		
		return view;
	}
}

class MyCreatedCircleHolder{    //�����ּ��ع��󣬱����ȡ���Ŀؼ���Ϣ
	TextView myCreatedCircleName;//��Ƶ����
	TextView myCreatedCircleID;//��Ƶ����
}