package com.example.adapter;

import java.util.List;

import com.example.entity.DanceMusicListInfo;
import com.example.silencedance.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MusicListWithDanceAdapter extends ArrayAdapter<DanceMusicListInfo> {    // �����������ͱ�ʾ��Ҫ�������������

	private int resourceId;
	private Context con;
	private List<DanceMusicListInfo> danceMusicListInfos;

	public MusicListWithDanceAdapter(Context context, int textViewResourceId,
			List<DanceMusicListInfo> objects) {                         // ��һ�������������Ļ������ڶ���������ÿһ����Ӳ��֣�����������������
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;                   //��ȡ�Ӳ���
		this.con=context;
		this.danceMusicListInfos=objects;
	}

	@Override         //getView������ÿ�������������Ļ�ڵ�ʱ�򶼻ᱻ���ã�ÿ�ζ����������¼���һ��
	public View getView(int position, View convertView, ViewGroup parent) {//��һ��������ʾλ�ã��ڶ���������ʾ���沼�֣���������ʾ�󶨵�view����
		View view;
		DanceViewHolder viewHolder;                  //ʵ��DanceViewHolder���������һ�����У������ȡ���Ŀؼ������Ч��
		if(convertView==null){  
			viewHolder=new DanceViewHolder();
			view = LayoutInflater.from(getContext()).inflate(//convertViewΪ�մ�����û�б����ع�����getView����û�б����ù�����Ҫ����
					resourceId, null);          // �õ��Ӳ��֣��ǹ̶��ģ����Ӳ���id�й�
			viewHolder.musicname = (TextView) view.findViewById(R.id.musicname);//��ȡ�ؼ�,ֻ��Ҫ����һ�飬���ù��󱣴���DanceViewHolder��
			viewHolder.singer = (TextView) view.findViewById(R.id.singer);   //��ȡ�ؼ�
			view.setTag(viewHolder);
		}else{
			view=convertView;           //convertView��Ϊ�մ����ֱ����ع���ֻ��Ҫ��convertView��ֵȡ������
			viewHolder=(DanceViewHolder) view.getTag();
		}
		
		DanceMusicListInfo music = getItem(position);//ʵ��ָ��λ�õ�ˮ��
		viewHolder.musicname.setText(music.getMusicName());    
		viewHolder.singer.setText(music.getAirtistName()); 
		
		return view;

	}
	/**
	 * ʱ���ת��
	 * 
	 * @param time
	 * @return
	 */
	public String toTime(int time) {

		time /= 1000;
		int minute = time / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}
} 

class DanceViewHolder{      //�����ּ��ع��󣬱����ȡ���Ŀؼ���Ϣ��
	TextView musicname,singer;
}
