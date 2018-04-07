package com.example.silencedance;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.example.adapter.VideoInfoAdapter;
import com.example.adapter.VideoLinkAdapter;
import com.example.entity.VideoInfo;
import com.example.table.VideoLink;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class StartMenuActivity extends Activity {

	private ListView lvShowVideoInfo;
	private Intent intent;
	private List<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_menu);
		Bmob.initialize(this, "e37ca30c22533836329999a7fcf077b8"); // 加载Bmob SDK
		// 初始化
		init();
		// 获取数据
		getData();

	}

	private void item(final List<VideoInfo> vlinkList) {
		lvShowVideoInfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// Toast.makeText(VideoLinkItemActivity.this,"url:"+vlinkList.get(position).getWebVideoLink(),Toast.LENGTH_SHORT).show();
				jump(vlinkList.get(position).getWebVideoLink());
			}
		});
	}

	private void init() {
		lvShowVideoInfo = (ListView) findViewById(R.id.lvShowVideoInfo);
	}

	private void getData() {
		BmobQuery<VideoLink> query = new BmobQuery<VideoLink>();
		query.order("-updatedAt");
		query.findObjects(StartMenuActivity.this,
				new FindListener<VideoLink>() {

					@Override
					public void onSuccess(List<VideoLink> vlinkList) {
						for (VideoLink videoLink : vlinkList) {
							if (!TextUtils.isEmpty(videoLink
									.getWebVideoInfoName())) {
								VideoInfo videoInfo = new VideoInfo();
								videoInfo.setWebVideoInfoName(videoLink
										.getWebVideoInfoName());
								videoInfo.setWebVideoInfoTime(videoLink
										.getCreatedAt()); // 因为需要获取创建时间，所以重新写一个实体类进行获取保存
								videoInfo.setWebVideoLink(videoLink
										.getWebVideoLink());
								videoInfoList.add(videoInfo);
							}

						}
						VideoInfoAdapter adapter = new VideoInfoAdapter(
								StartMenuActivity.this,
								R.layout.show_video_info_item, videoInfoList);
						lvShowVideoInfo.setAdapter(adapter);
						item(videoInfoList);
					}

					@Override
					public void onError(int arg0, String arg1) {

					}
				});
	}

	public void jump(String link) {
		// TODO Auto-generated method stub

		intent = new Intent(StartMenuActivity.this, WebActivity.class);
		intent.putExtra("_weblink", link);
		startActivity(intent);
	}

	public void goBack(View view) {
		finish();
	}

	public void refresh(View view) {
		// 获取数据
		getData();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
