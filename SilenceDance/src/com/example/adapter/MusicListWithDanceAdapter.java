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

public class MusicListWithDanceAdapter extends ArrayAdapter<DanceMusicListInfo> {    // 适配器，泛型表示想要适配的数据类型

	private int resourceId;
	private Context con;
	private List<DanceMusicListInfo> danceMusicListInfos;

	public MusicListWithDanceAdapter(Context context, int textViewResourceId,
			List<DanceMusicListInfo> objects) {                         // 第一个参数是上下文环境，第二个参数是每一项的子布局，第三个参数是数据
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;                   //获取子布局
		this.con=context;
		this.danceMusicListInfos=objects;
	}

	@Override         //getView方法在每个子项被滚动到屏幕内的时候都会被调用，每次都将布局重新加载一边
	public View getView(int position, View convertView, ViewGroup parent) {//第一个参数表示位置，第二个参数表示缓存布局，第三个表示绑定的view对象
		View view;
		DanceViewHolder viewHolder;                  //实例DanceViewHolder，当程序第一次运行，保存获取到的控件，提高效率
		if(convertView==null){  
			viewHolder=new DanceViewHolder();
			view = LayoutInflater.from(getContext()).inflate(//convertView为空代表布局没有被加载过，即getView方法没有被调用过，需要创建
					resourceId, null);          // 得到子布局，非固定的，和子布局id有关
			viewHolder.musicname = (TextView) view.findViewById(R.id.musicname);//获取控件,只需要调用一遍，调用过后保存在DanceViewHolder中
			viewHolder.singer = (TextView) view.findViewById(R.id.singer);   //获取控件
			view.setTag(viewHolder);
		}else{
			view=convertView;           //convertView不为空代表布局被加载过，只需要将convertView的值取出即可
			viewHolder=(DanceViewHolder) view.getTag();
		}
		
		DanceMusicListInfo music = getItem(position);//实例指定位置的水果
		viewHolder.musicname.setText(music.getMusicName());    
		viewHolder.singer.setText(music.getAirtistName()); 
		
		return view;

	}
	/**
	 * 时间的转换
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

class DanceViewHolder{      //当布局加载过后，保存获取到的控件信息。
	TextView musicname,singer;
}
