package com.example.silencedance;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.example.adapter.VideoLinkAdapter;
import com.example.silencedance.R;
import com.example.silencedance.VideoLinkItemActivity;
import com.example.silencedance.WebActivity;
import com.example.table.VideoLink;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class VideoLinkItemActivity extends Activity implements OnClickListener{

	private Intent intent;
	private SharedPreferences pref;
	private ListView lvVideoLink;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_link_item);
		Bmob.initialize(this, "e37ca30c22533836329999a7fcf077b8"); // 加载Bmob SDK
		// 初始化
		init();
		// 获取数据
		getData();

	}

	private void item(final List<VideoLink> vlinkList) {
		lvVideoLink.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// Toast.makeText(VideoLinkItemActivity.this,"url:"+vlinkList.get(position).getWebVideoLink(),Toast.LENGTH_SHORT).show();
				jump(vlinkList.get(position).getWebVideoLink());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video_link_item, menu);
		return true;
	}

	private void init() {
		lvVideoLink = (ListView) findViewById(R.id.lvVideoLink);
	}

	private void getData() {
		BmobQuery<VideoLink> query = new BmobQuery<VideoLink>();
		query.order("-updatedAt");
		query.findObjects(VideoLinkItemActivity.this,
				new FindListener<VideoLink>() {

					@Override
					public void onSuccess(List<VideoLink> vlinkList) {
						VideoLinkAdapter adapter = new VideoLinkAdapter(
								VideoLinkItemActivity.this,
								R.layout.item_videolink, vlinkList);
						lvVideoLink.setAdapter(adapter);
						item(vlinkList);
					}

					@Override
					public void onError(int arg0, String arg1) {

					}
				});
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

	}

	public void jump(String link) {
		// TODO Auto-generated method stub

		intent = new Intent(VideoLinkItemActivity.this, WebActivity.class);
		intent.putExtra("_weblink", link);
		startActivity(intent);
	}

	public void goBack(View view) {
		finish();
	}

	public void refresh(View view) {
		// 获取数据
		getData();
		// Toast.makeText(VideoLinkItemActivity.this,"刷新完成",Toast.LENGTH_SHORT).show();

		
	}
}
