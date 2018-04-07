package com.example.adapter;

import java.util.List;

import com.example.entity.VideoInfo;
import com.example.silencedance.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class VideoInfoAdapter extends ArrayAdapter<VideoInfo> {

	private int resourceId;
	
	public VideoInfoAdapter(Context context, int textViewResourceId,
			List<VideoInfo> objects) {
		super(context, textViewResourceId, objects);
		resourceId=textViewResourceId; 
	}
	
	public View getView(int position, View convertView, ViewGroup parent) { //��һ��������ʾλ�ã��ڶ���������ʾ���沼�֣���������ʾ�󶨵�view����
		View view;
		VideoInfoHolder holder;    //ʵ��ViewHolder���������һ�����У������ȡ���Ŀؼ������Ч��  
		if(convertView==null){
			holder=new VideoInfoHolder();
			view=LayoutInflater.from(getContext()).inflate(resourceId,null);   //convertViewΪ�մ�����û�б����ع�����getView����û�б����ù�����Ҫ�����õ��Ӳ��֣��ǹ̶��ģ����Ӳ���id�й�    
			holder.tvVideoInfoName=(TextView) view.findViewById(R.id.tvVideoInfoName);   //��ȡ�ؼ�,ֻ��Ҫ����һ�飬���ù��󱣴���ViewHolder�� 
			holder.tvVideoInfoUrl=(TextView) view.findViewById(R.id.tvVideoInfoUrl);
			holder.tvVideoInfoTime=(TextView) view.findViewById(R.id.tvVideoInfoTime);
			view.setTag(holder);
		}else{       //convertView��Ϊ�մ����ֱ����ع���ֻ��Ҫ��convertView��ֵȡ������  
			view=convertView;
			holder=(VideoInfoHolder) view.getTag();
		}
		VideoInfo videoInfo = getItem(position);     //ʵ��ָ��λ�õ�ʵ����
		
		holder.tvVideoInfoName.setText(videoInfo.getWebVideoInfoName());   //��ȡʵ������Ϣ
		holder.tvVideoInfoUrl.setText(videoInfo.getWebVideoLink());
		holder.tvVideoInfoTime.setText(videoInfo.getWebVideoInfoTime());
		
		return view;
	}
}

class VideoInfoHolder{    //�����ּ��ع��󣬱����ȡ���Ŀؼ���Ϣ
	TextView tvVideoInfoTime;//ʱ��
	TextView tvVideoInfoName;//��Ƶ����
	TextView tvVideoInfoUrl;//��Ƶ����
}