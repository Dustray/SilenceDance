package com.example.silencedance;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.SearchResultListAdapter;
import com.example.entity.Music;
import com.example.listener.OnLoadSearchFinishListener;
import com.example.util.ImageUtils;
import com.example.util.SearchUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MusicSearchActivity extends Activity {
	private ListView lvSearchReasult;

	private List<Music> listSearchResult;

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_search);
		ImageUtils.initImageLoader(this);// ImageLoader初始化
		init();
	}

	private void init() {
		listSearchResult = new ArrayList<Music>();
		dialog = new ProgressDialog(this);
		dialog.setTitle("加载中。。。");
		lvSearchReasult = (ListView) findViewById(R.id.lv_search_list);
		Button btSearch = (Button) findViewById(R.id.bt_online_search);
		final EditText edtKey = (EditText) findViewById(R.id.edt_search);
		btSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.show();// 进入加载状态，显示进度条
				new Thread(new Runnable() {

					@Override
					public void run() {
						SearchUtils.getIds(edtKey.getText().toString(),
								new OnLoadSearchFinishListener() {

									@Override
									public void onLoadSucess(
											List<Music> musicList) {
										dialog.dismiss();// 加载完成，取消进度条
										Message msg = new Message();
										msg.what = 0;
										mHandler.sendMessage(msg);
										listSearchResult = musicList;
									}

									@Override
									public void onLoadFiler() {
										dialog.dismiss();// 加载失败，取消进度条
									}
								});

					}
				}).start();
			}
		});
		lvSearchReasult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Music music = listSearchResult.get(arg2);
				Intent i = new Intent(MusicSearchActivity.this,
						MusicDetailActivity.class);
				i.putExtra("music", music);
				startActivity(i);
			}
		});
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				SearchResultListAdapter adapter = new SearchResultListAdapter(
						listSearchResult, MusicSearchActivity.this);
				lvSearchReasult.setAdapter(adapter);
				break;
			}
		};
	};

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
