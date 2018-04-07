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
 * ��Ҫ���Զ��岼�֣���ô����һ���������� Ŀ�����ڰѿؼ���ŵ�ָ����λ�á� �����������α���������������Լ������ĵ�λ��
 */
public class MusicListAdapter extends BaseAdapter {


	private Context mcontext;// ������
	private Cursor mcursor;// �α�

	private String[] _artists;
	private String[] _titles;
	
	private String mCircleID;

	/**
	 * ���췽��
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
		return mcursor.getCount();// �����α�����
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	// ��ȡ��ͼ

	public View getView(int position, View convertView, ViewGroup parent) {
		
		convertView = LayoutInflater.from(mcontext).inflate(
				R.layout.musiclist_item, null);// ��LayoutInflaterװ�ز��֡�
		mcursor.moveToPosition(position);// ���α��ƶ���ĳλ��
		ImageView images = (ImageView) convertView.findViewById(R.id.listitem);// ͨ��convertView��ID��

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

	public void submit(int position) {
		DanceMusicListInfo dmli = new DanceMusicListInfo();// ʵ����ʵ����
		
		if(mCircleID == ""){
			Toast.makeText(mcontext, "���ʧ�ܣ�δѡ���κ����", Toast.LENGTH_SHORT)
			.show();
			return;
		}
		dmli.setTeamName(mCircleID); // ��װ����
		dmli.setMusicName(_titles[position]);
		dmli.setAirtistName(_artists[position]);
		
		dmli.save(mcontext, new SaveListener() { // ���������������
			// ��ӳɹ���
			@Override
			public void onSuccess() {
				// ����ע������
				if (ContantsRegister.iregister != null)
					ContantsRegister.iregister.onRegister();
				Toast.makeText(mcontext, "��ӳɹ�", Toast.LENGTH_SHORT)
						.show();
			}

			// ���ʧ��
			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(mcontext, "���ʧ��", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
	

}
