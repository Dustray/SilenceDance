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
	
	public View getView(int position, View convertView, ViewGroup parent) { //第一个参数表示位置，第二个参数表示缓存布局，第三个表示绑定的view对象
		//Log.i("MainActivity","position:"+position);
		View view;
		MyCreatedCircleHolder viewHolder;    //实例ViewHolder，当程序第一次运行，保存获取到的控件，提高效率  
		if(convertView==null){
			viewHolder=new MyCreatedCircleHolder();
			view=LayoutInflater.from(getContext()).inflate(resourceId,null);   //convertView为空代表布局没有被加载过，即getView方法没有被调用过，需要创建得到子布局，非固定的，和子布局id有关    
			viewHolder.myCreatedCircleName=(TextView) view.findViewById(R.id.myCreatedCircleName);   //获取控件,只需要调用一遍，调用过后保存在ViewHolder中 
			viewHolder.myCreatedCircleID=(TextView) view.findViewById(R.id.myCreatedCircleID);
			view.setTag(viewHolder);
		}else{       //convertView不为空代表布局被加载过，只需要将convertView的值取出即可  
			view=convertView;
			viewHolder=(MyCreatedCircleHolder) view.getTag();
		}   //实例指定位置的实体类
		
		viewHolder.myCreatedCircleName.setText(myCreatedList.get(position).getGroupName());   //获取实体类信息
		viewHolder.myCreatedCircleID.setText(myCreatedList.get(position).getGroupId());
		
		return view;
	}
}

class MyCreatedCircleHolder{    //当布局加载过后，保存获取到的控件信息
	TextView myCreatedCircleName;//视频名称
	TextView myCreatedCircleID;//视频链接
}