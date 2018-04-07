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
			List<CircleDynamicInfo> objects) { // ��һ�������������Ļ������ڶ���������ÿһ����Ӳ��֣�����������������
		super(context, textViewResourceId, objects);
		con = context;
		resourceId = textViewResourceId; // ��ȡ�Ӳ���
	}

	// getView������ÿ�������������Ļ�ڵ�ʱ�򶼻ᱻ���ã�ÿ�ζ����������¼���һ��
	@Override
	public View getView(int position, View convertView, ViewGroup parent) { // ��һ��������ʾλ�ã��ڶ���������ʾ���沼�֣���������ʾ�󶨵�view����
		View view;
		ViewHolder viewHolder; // ʵ��ViewHolder���������һ�����У������ȡ���Ŀؼ������Ч��
		if (convertView == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(getContext()).inflate(resourceId, null); // convertViewΪ�մ�����û�б����ع�����getView����û�б����ù�����Ҫ�����õ��Ӳ��֣��ǹ̶��ģ����Ӳ���id�й�
			viewHolder.tvDynamicUserName = (TextView) view
					.findViewById(R.id.tvDynamicUserName); // ��ȡ�ؼ�,ֻ��Ҫ����һ�飬���ù��󱣴���ViewHolder��
			viewHolder.tvDynamicContent = (TextView) view
					.findViewById(R.id.tvDynamicContent);
			viewHolder.tvDynamicDate = (TextView) view
					.findViewById(R.id.tvDynamicDate);
			viewHolder.tvDynamicDiscuss = (TextView) view
					.findViewById(R.id.tvDynamicDiscuss);
			viewHolder.ivShowDynamicPicture = (ImageView) view
					.findViewById(R.id.ivShowDynamicPicture);
			view.setTag(viewHolder);
		} else { // convertView��Ϊ�մ����ֱ����ع���ֻ��Ҫ��convertView��ֵȡ������
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		CircleDynamicInfo dynamic = getItem(position); // ʵ��ָ��λ�õ�ʵ����

		viewHolder.tvDynamicUserName.setText(dynamic.getDynamicUserName()); // ��ȡʵ������Ϣ
		viewHolder.tvDynamicContent.setText(dynamic.getDynamicContent());
		viewHolder.tvDynamicDate.setText(dynamic.getDynamicDate());
		viewHolder.tvDynamicDiscuss.setText(dynamic.getDynamicDiscuss());
		if ( !TextUtils.isEmpty(dynamic.getDynamicPictureUrl())) {
			viewHolder.ivShowDynamicPicture.setVisibility(View.VISIBLE); // ���ÿɼ�
		}else{
			viewHolder.ivShowDynamicPicture.setVisibility(View.GONE); // ���ò��ɼ�
		}
		// ��ʾͼƬ
		Glide.with(con).load(dynamic.getDynamicPictureUrl())
				.into(viewHolder.ivShowDynamicPicture);     

		return view;
	}
}

class ViewHolder { // �����ּ��ع��󣬱����ȡ���Ŀؼ���Ϣ
	TextView tvDynamicUserName;// ��������
	TextView tvDynamicContent;// ��̬����
	TextView tvDynamicDate;// ��̬ʱ��
	TextView tvDynamicDiscuss;// ��̬����
	ImageView ivShowDynamicPicture;// ��̬��ͼ
}


