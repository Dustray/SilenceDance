package com.example.silencedance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class LiveServiceActivity extends Activity implements
		OnItemClickListener {

	private GridView gvShow;
	private List<Map<String, Object>> dataList; // 数据源集合
	private int[] icon = { R.drawable.gv_account, R.drawable.gv_clock,
			R.drawable.gv_information, R.drawable.gv_step,
			R.drawable.gv_weather, R.drawable.gv_weight }; // 将图片封装到数组
	private String[] iconName = { "记账", "闹钟", "资讯", "计步", "天气", "体重" }; // 将文字封装到数组
	private SimpleAdapter adapter;

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live_service);
		gvShow = (GridView) findViewById(R.id.gvShow);

		dataList = new ArrayList<Map<String, Object>>(); // 新建数据源

		/**
		 * 新建适配器 参数一是上下文 参数二是数据源 参数三是子布局页面 参数四是数据源集合的键名 参数五是子布局控件id
		 */
		adapter = new SimpleAdapter(this, getData(), R.layout.gv_item,
				new String[] { "image", "text" }, new int[] { R.id.ivPic,
						R.id.tvText });
		gvShow.setAdapter(adapter); // GridView加载适配器
		gvShow.setOnItemClickListener(this); // GridView加载点击事件

	}

	private List<Map<String, Object>> getData() {
		for (int i = 0; i < icon.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", icon[i]);
			map.put("text", iconName[i]);
			dataList.add(map); // 向集合中添加数据
		}
		return dataList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			intent = new Intent(this, AccountActivity.class);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(this, ClockActivity.class);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(this, HealthNewActivity.class);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(this, PedometerActivity.class);
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			break;
		case 5:
			intent = new Intent(this, WeightActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
