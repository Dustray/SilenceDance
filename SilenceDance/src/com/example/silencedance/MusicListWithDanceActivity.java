package com.example.silencedance;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.example.adapter.MusicListWithDanceAdapter;
import com.example.entity.DanceMusicListInfo;
import com.example.entity.NowPlayInfo;
import com.example.tools.ScanSDCardReceiver;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MusicListWithDanceActivity extends Activity {
	private int[] _ids;// 保存音乐ID临时数组
	private String[] _artists;// 保存艺术家
	private String[] _titles;// 标题临时数组
	private String circleID;// 舞圈ID
	private String circleMusicName[] = null;// 临时存放舞队歌曲标题的数组
	private Button choose_my_circle_btn;// 选择我的舞圈按钮
	private List<DanceMusicListInfo> danceMusicList = null;
	// private ListView listview;// 列表对象
	private ListView lvDanceMusicList;
	Cursor cursor;
	private ScanSDCardReceiver receiver = null;// 扫描SD卡的实例
	private static final int SCAN = Menu.FIRST;// 重写菜单的常量
	private static final int ABOUT = Menu.FIRST + 1;
	/**
	 * 定义查找音乐信息数组，1.标题，2音乐时间,3.艺术家,4.音乐id，5.显示名字,6.数据。
	 */
	String[] media_info = new String[] { MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME,
			MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_music_list_with_dance);
		// listview = (ListView) findViewById(R.id.music_list);// 找ListView的ID
		// listview.setOnItemClickListener(new MusicListOnClickListener());//
		// 创建一个ListView监听器对象

		Bmob.initialize(this, "e37ca30c22533836329999a7fcf077b8"); // 加载Bmob SDK
		// 初始化控件
		choose_my_circle_btn = (Button) findViewById(R.id.choose_my_circle2_btn);

		lvDanceMusicList = (ListView) findViewById(R.id.lvDanceMusicList);
		lvDanceMusicList.setOnItemClickListener(new MusicListOnClickListener());// 创建一个ListView监听器对象
		// 查询音乐数据
		selectMusicData();

		// ShowMp3List();// 显示音乐

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, SCAN, 0, "扫描SD卡");
		menu.add(1, ABOUT, 1, "关于");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == SCAN) {
			ScanSDCard();
		}
		return true;
	}

	/**
	 * 显示音乐列表
	 */
	private void ShowMp3List(int position) {
		cursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, media_info, null,
				null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();// 将游标移动到初始位置
		_ids = new int[cursor.getCount()];// 返回INT的一个列
		_artists = new String[cursor.getCount()];// 返回String的一个列
		_titles = new String[cursor.getCount()];// 返回String的一个列
		for (int i = 0; i < cursor.getCount(); i++) {
			_ids[i] = cursor.getInt(3);
			_titles[i] = cursor.getString(0);
			_artists[i] = cursor.getString(2);
			cursor.moveToNext();// 将游标移到下一行
		}
		cursor.moveToFirst();// 将游标移动到初始位置
		for (int i = 0; i < cursor.getCount(); i++) {
			DanceMusicListInfo music = danceMusicList.get(position);
			if (cursor.getString(0).equals(music.getMusicName())) {
				playMusic(position);// 根据点击位置来播放音乐
				return;
			}
			cursor.moveToNext();// 将游标移到下一行
		}

		// listview.setAdapter(new MusicListAdapter(this, cursor,
		// _artists,_titles, circleID));// 用setAdapter装载数据
	}

	// 点击列表事件
	public class MusicListOnClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {
			ShowMp3List(position);
		}

	}

	// 播放音乐方法
	public void playMusic(int position) {
		Intent intent = new Intent(MusicListWithDanceActivity.this,
				PlayMusicWithDanceActivity.class);
		intent.putExtra("_ids", _ids);
		intent.putExtra("_titles", _titles);
		intent.putExtra("_artists", _artists);
		intent.putExtra("position", position);
		intent.putExtra("theCircleID", circleID);

		for (int i = 0; i < danceMusicList.size(); i++) {
			circleMusicName[i] = danceMusicList.get(i).getMusicName();
		}
		intent.putExtra("circleMusicName", circleMusicName);
		NowPlayInfo.setNOW_ids(_ids); // 保存当前播放的音乐信息
		NowPlayInfo.setNOW_titles(_titles);
		NowPlayInfo.setNOW_artists(_artists);
		NowPlayInfo.setNOW_position(position);

		startActivity(intent);
		// finish();

	}

	// 扫描SD卡
	private void ScanSDCard() {
		IntentFilter filter = new IntentFilter(
				Intent.ACTION_MEDIA_SCANNER_STARTED);
		filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		receiver = new ScanSDCardReceiver();
		filter.addDataScheme("file");
		registerReceiver(receiver, filter);
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
				Uri.parse("file://"
						+ Environment.getExternalStorageDirectory()
								.getAbsolutePath())));
	}

	// 查询音乐数据
	private void selectMusicData() {
		BmobQuery<DanceMusicListInfo> query = new BmobQuery<DanceMusicListInfo>();
		query.addWhereEqualTo("teamName", circleID); // 舞圈名称
		query.findObjects(this, new FindListener<DanceMusicListInfo>() {

			@Override
			public void onSuccess(List<DanceMusicListInfo> musicList) {
				if (musicList.size() > 0) {

					danceMusicList = musicList;
					circleMusicName = new String[danceMusicList.size()];
					lvDanceMusicList.setAdapter(new MusicListWithDanceAdapter(
							MusicListWithDanceActivity.this,
							R.layout.musiclistwithdance_item, musicList));
				}
			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1: // 判断是不是由MainActivity页面进行请求的，如果是则执行case语句
			if (resultCode == RESULT_OK) { // 判断是不是由NewActivity页面进行跳转回来的，如果是则进入if语句
				if (data != null) {
					String returnCircleName = data
							.getStringExtra("return_data_circle_name");
					String returnCircleID = data
							.getStringExtra("return_data_circle_id");
					choose_my_circle_btn.setText(returnCircleName);
					circleID = returnCircleID;
					selectMusicData();
				} else {

				}
			}
			break;
		default:
			break;
		}
	}

	public void chooseMyCircle(View view) {

		Intent intent = new Intent(this, MyCircleListDialogActivity.class);
		startActivityForResult(intent, 1); // 将MainActivity页面的请求码定位1
	}

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}