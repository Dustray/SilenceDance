package com.example.news.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.example.news.entity.NewEntity;
import com.example.silencedance.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewAdapter extends ArrayAdapter<NewEntity> {    // �����������ͱ�ʾ��Ҫ�������������

	private int resourceId;
	private Context con;
	private List<NewEntity> newLists;

	public NewAdapter(Context context, int textViewResourceId,
			List<NewEntity> objects) {                         // ��һ�������������Ļ������ڶ���������ÿһ����Ӳ��֣�����������������
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;                   //��ȡ�Ӳ���
		this.con=context;
		this.newLists=objects;
	}

	@Override         //getView������ÿ�������������Ļ�ڵ�ʱ�򶼻ᱻ���ã�ÿ�ζ����������¼���һ��
	public View getView(int position, View convertView, ViewGroup parent) {//��һ��������ʾλ�ã��ڶ���������ʾ���沼�֣���������ʾ�󶨵�view����
		View view;
		ViewHolder viewHolder;                  //ʵ��ViewHolder���������һ�����У������ȡ���Ŀؼ������Ч��
		if(convertView==null){  
			viewHolder=new ViewHolder();
			view = LayoutInflater.from(getContext()).inflate(//convertViewΪ�մ�����û�б����ع�����getView����û�б����ù�����Ҫ����
					resourceId, null);          // �õ��Ӳ��֣��ǹ̶��ģ����Ӳ���id�й�
			viewHolder.ivItemPicture = (ImageView) view.findViewById(R.id.ivItemPicture);//��ȡ�ؼ�,ֻ��Ҫ����һ�飬���ù��󱣴���ViewHolder��
			viewHolder.tvItemTitle = (TextView) view.findViewById(R.id.tvItemTitle);   //��ȡ�ؼ�
			view.setTag(viewHolder);
		}else{
			view=convertView;           //convertView��Ϊ�մ����ֱ����ع���ֻ��Ҫ��convertView��ֵȡ������
			viewHolder=(ViewHolder) view.getTag();
		}
		
		NewEntity newOne = getItem(position);//ʵ��ָ��λ�õ�ˮ��
		
		//viewHolder.ivItemPicture.setImageResource(R.drawable.);
		//viewHolder.ivItemPicture.setImageBitmap(BitmapFactory.decodeFile(newOne.getImg()));
		Glide.with(con).load(newLists.get(position).getImg()).into(viewHolder.ivItemPicture);
		viewHolder.tvItemTitle.setText(newOne.getTitle());      
		return view;

	}
} 
class ViewHolder{      //�����ּ��ع��󣬱����ȡ���Ŀؼ���Ϣ��
	ImageView ivItemPicture;
	TextView tvItemTitle;
}
