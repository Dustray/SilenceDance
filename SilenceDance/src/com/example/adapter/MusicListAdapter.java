package com.example.adapter;

import java.util.List;

import cn.bmob.v3.listener.SaveListener;

import com.example.contants.ContantsRegister;
import com.example.easemob.getMyCircle;
import com.example.entity.DanceMusicListInfo;
import com.example.entity.Music;
import com.example.silencedance.MusicListActivity;
import com.example.silencedance.MyCircleListDialogActivity;
import com.example.silencedance.PlayMusicWithDanceActivity;
import com.example.silencedance.R;
import com.example.silencedance.RegisterActivity;
import com.example.silencedance.StartActivity;
import com.example.table.User;
import com.hyphenate.chat.EMGroup;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 若要想自定义布局，那么定义一个适配器。 目的在于把控件存放到指定的位置。 我们这里用游标把它遍历出来，以及上下文的位置
 */
public class MusicListAdapter extends BaseAdapter {


	private Context mcontext;// 上下文
	private Cursor mcursor;// 游标

	private String[] _artists;
	private String[] _titles;
	
	private String mCircleID;

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param cursor
	 */
	public MusicListAdapter(Context context, Cursor cursor, String[] artists, String[] titles, String circleID) {
		mcontext = context;
		mcursor = cursor;
		_artists = artists;
		_titles = titles;
		mCircleID = circleID;
		
		
	}
	public int getCount() {
		return mcursor.getCount();// 返回游标行数
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	// 获取视图

	public View getView(int position, View convertView, ViewGroup parent) {
		
		convertView = LayoutInflater.from(mcontext).inflate(
				R.layout.musiclist_item, null);// 用LayoutInflater装载布局。
		mcursor.moveToPosition(position);// 将游标移动到某位置
		ImageView images = (ImageView) convertView.findViewById(R.id.listitem);// 通过convertView找ID。

		images.setImageResource(R.drawable.music_head_icon_mini);

		TextView music_title = (TextView) convertView
				.findViewById(R.id.musicname);

		music_title.setText(mcursor.getString(0));

		TextView music_singer = (TextView) convertView
				.findViewById(R.id.singer);

		music_singer.setText(mcursor.getString(2));

		TextView music_time = (TextView) convertView.findViewById(R.id.time);

		music_time.setText(toTime(mcursor.getInt(1)));

		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.list_triangle);
//		MyListener myListener = new MyListener(position);
//		imageView.setOnClickListener(myListener);
		final int mposition=position;
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				submit(mposition);
			}
		});
		
		return convertView;
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

	public void submit(int position) {
		DanceMusicListInfo dmli = new DanceMusicListInfo();// 实例化实体类
		
		if(mCircleID == ""){
			Toast.makeText(mcontext, "添加失败，未选择任何舞队", Toast.LENGTH_SHORT)
			.show();
			return;
		}
		dmli.setTeamName(mCircleID); // 封装数据
		dmli.setMusicName(_titles[position]);
		dmli.setAirtistName(_artists[position]);
		
		dmli.save(mcontext, new SaveListener() { // 向服务器保存数据
			// 添加成功额
			@Override
			public void onSuccess() {
				// 保存注册数据
				if (ContantsRegister.iregister != null)
					ContantsRegister.iregister.onRegister();
				Toast.makeText(mcontext, "添加成功", Toast.LENGTH_SHORT)
						.show();
			}

			// 添加失败
			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(mcontext, "添加失败", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
	

}
