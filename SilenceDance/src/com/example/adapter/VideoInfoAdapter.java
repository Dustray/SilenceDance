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
	
	public View getView(int position, View convertView, ViewGroup parent) { //第一个参数表示位置，第二个参数表示缓存布局，第三个表示绑定的view对象
		View view;
		VideoInfoHolder holder;    //实例ViewHolder，当程序第一次运行，保存获取到的控件，提高效率  
		if(convertView==null){
			holder=new VideoInfoHolder();
			view=LayoutInflater.from(getContext()).inflate(resourceId,null);   //convertView为空代表布局没有被加载过，即getView方法没有被调用过，需要创建得到子布局，非固定的，和子布局id有关    
			holder.tvVideoInfoName=(TextView) view.findViewById(R.id.tvVideoInfoName);   //获取控件,只需要调用一遍，调用过后保存在ViewHolder中 
			holder.tvVideoInfoUrl=(TextView) view.findViewById(R.id.tvVideoInfoUrl);
			holder.tvVideoInfoTime=(TextView) view.findViewById(R.id.tvVideoInfoTime);
			view.setTag(holder);
		}else{       //convertView不为空代表布局被加载过，只需要将convertView的值取出即可  
			view=convertView;
			holder=(VideoInfoHolder) view.getTag();
		}
		VideoInfo videoInfo = getItem(position);     //实例指定位置的实体类
		
		holder.tvVideoInfoName.setText(videoInfo.getWebVideoInfoName());   //获取实体类信息
		holder.tvVideoInfoUrl.setText(videoInfo.getWebVideoLink());
		holder.tvVideoInfoTime.setText(videoInfo.getWebVideoInfoTime());
		
		return view;
	}
}

class VideoInfoHolder{    //当布局加载过后，保存获取到的控件信息
	TextView tvVideoInfoTime;//时间
	TextView tvVideoInfoName;//视频名字
	TextView tvVideoInfoUrl;//视频链接
}