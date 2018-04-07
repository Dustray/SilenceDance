package com.example.adapter;

import java.util.List;

import com.example.silencedance.R;
import com.example.table.VideoLink;
import com.example.tools.WebVideoBitmap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoLinkAdapter extends ArrayAdapter<VideoLink> {

	private int resourceId;
	
	public VideoLinkAdapter(Context context, int textViewResourceId,
			List<VideoLink> objects) {
		super(context, textViewResourceId, objects);
		resourceId=textViewResourceId; 
		// TODO Auto-generated constructor stub
	} 
	
	public View getView(int position, View convertView, ViewGroup parent) { //��һ��������ʾλ�ã��ڶ���������ʾ���沼�֣���������ʾ�󶨵�view����
		//Log.i("MainActivity","position:"+position);
		View view;
		Holder viewHolder;    //ʵ��ViewHolder���������һ�����У������ȡ���Ŀؼ������Ч��  
		if(convertView==null){
			viewHolder=new Holder();
			view=LayoutInflater.from(getContext()).inflate(resourceId,null);   //convertViewΪ�մ�����û�б����ع�����getView����û�б����ù�����Ҫ�����õ��Ӳ��֣��ǹ̶��ģ����Ӳ���id�й�    
			viewHolder.video_name=(TextView) view.findViewById(R.id.video_name);   //��ȡ�ؼ�,ֻ��Ҫ����һ�飬���ù��󱣴���ViewHolder�� 
			viewHolder.video_link=(TextView) view.findViewById(R.id.video_link);
			viewHolder.video_image = (ImageView)view.findViewById(R.id.videoImage);
			view.setTag(viewHolder);
		}else{       //convertView��Ϊ�մ����ֱ����ع���ֻ��Ҫ��convertView��ֵȡ������  
			view=convertView;
			viewHolder=(Holder) view.getTag();
		}
		VideoLink videoLink = getItem(position);     //ʵ��ָ��λ�õ�ʵ����
		
		viewHolder.video_name.setText(videoLink.getWebVideoName());   //��ȡʵ������Ϣ
		viewHolder.video_link.setText(videoLink.getWebVideoLink());
		
		viewHolder.video_image.setImageResource(R.drawable.cyclic_4);
		return view;
	}
}

class Holder{    //�����ּ��ع��󣬱����ȡ���Ŀؼ���Ϣ
	TextView video_name;//��Ƶ����
	TextView video_link;//��Ƶ����
	ImageView video_image;//����ͼ
}