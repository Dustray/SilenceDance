package com.example.adapter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.silencedance.R;
import com.example.entity.CircleDynamicInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CircleDynamicAdapter extends ArrayAdapter<CircleDynamicInfo> {

	private int resourceId;
	private Context con;

	public CircleDynamicAdapter(Context context, int textViewResourceId,
			List<CircleDynamicInfo> objects) { // 第一个参数是上下文环境，第二个参数是每一项的子布局，第三个参数是数据
		super(context, textViewResourceId, objects);
		con = context;
		resourceId = textViewResourceId; // 获取子布局
	}

	// getView方法在每个子项被滚动到屏幕内的时候都会被调用，每次都将布局重新加载一边
	@Override
	public View getView(int position, View convertView, ViewGroup parent) { // 第一个参数表示位置，第二个参数表示缓存布局，第三个表示绑定的view对象
		View view;
		ViewHolder viewHolder; // 实例ViewHolder，当程序第一次运行，保存获取到的控件，提高效率
		if (convertView == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(getContext()).inflate(resourceId, null); // convertView为空代表布局没有被加载过，即getView方法没有被调用过，需要创建得到子布局，非固定的，和子布局id有关
			viewHolder.tvDynamicUserName = (TextView) view
					.findViewById(R.id.tvDynamicUserName); // 获取控件,只需要调用一遍，调用过后保存在ViewHolder中
			viewHolder.tvDynamicContent = (TextView) view
					.findViewById(R.id.tvDynamicContent);
			viewHolder.tvDynamicDate = (TextView) view
					.findViewById(R.id.tvDynamicDate);
			viewHolder.tvDynamicDiscuss = (TextView) view
					.findViewById(R.id.tvDynamicDiscuss);
			viewHolder.ivShowDynamicPicture = (ImageView) view
					.findViewById(R.id.ivShowDynamicPicture);
			view.setTag(viewHolder);
		} else { // convertView不为空代表布局被加载过，只需要将convertView的值取出即可
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		CircleDynamicInfo dynamic = getItem(position); // 实例指定位置的实体类

		viewHolder.tvDynamicUserName.setText(dynamic.getDynamicUserName()); // 获取实体类信息
		viewHolder.tvDynamicContent.setText(dynamic.getDynamicContent());
		viewHolder.tvDynamicDate.setText(dynamic.getDynamicDate());
		viewHolder.tvDynamicDiscuss.setText(dynamic.getDynamicDiscuss());
		if ( !TextUtils.isEmpty(dynamic.getDynamicPictureUrl())) {
			viewHolder.ivShowDynamicPicture.setVisibility(View.VISIBLE); // 设置可见
		}else{
			viewHolder.ivShowDynamicPicture.setVisibility(View.GONE); // 设置不可见
		}
		// 显示图片
		Glide.with(con).load(dynamic.getDynamicPictureUrl())
				.into(viewHolder.ivShowDynamicPicture);     

		return view;
	}
}

class ViewHolder { // 当布局加载过后，保存获取到的控件信息
	TextView tvDynamicUserName;// 好友名称
	TextView tvDynamicContent;// 动态内容
	TextView tvDynamicDate;// 动态时间
	TextView tvDynamicDiscuss;// 动态评论
	ImageView ivShowDynamicPicture;// 动态配图
}


