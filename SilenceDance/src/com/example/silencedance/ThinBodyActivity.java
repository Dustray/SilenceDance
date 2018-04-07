package com.example.silencedance;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.news.adapter.NewAdapter;
import com.example.news.entity.NewEntity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ThinBodyActivity extends Activity {

	private String APPKEY = "a0dcf0236afa6fe94f9169e19496ca28";

	private List<NewEntity> newList = new ArrayList<NewEntity>();
	private ListView lvThinBodyNew;
	private Context context = this;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			List<NewEntity> newLists = (List<NewEntity>) msg.obj;
			NewAdapter adapter = new NewAdapter(context, R.layout.new_list_item,
					newLists); // 关联数据和子布局
			lvThinBodyNew.setAdapter(adapter);
		};
	};
	Thread thread = new Thread() {
		public void run() {
			getNews();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thin_body);

		// 初始化控件
		init();
		// 获取新闻列表
		thread.start();
		
		//列表每一项点击事件
		lvThinBodyNew.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				NewEntity one=newList.get(position);
				Intent intent=new Intent(ThinBodyActivity.this,NewInfoActivity.class);
				Bundle bundle=new Bundle();
				bundle.putLong("id", one.getId());  //传递id过去，用于查询文章信息
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

	}

	// 初始化控件
	private void init() {
		lvThinBodyNew = (ListView) findViewById(R.id.lvThinBodyNew);
	}

	// 获取饮食新闻列表
	private void getNews() {
		try {
			URL newUrl = new URL(
					"http://japi.juhe.cn/health_knowledge/infoList?key="
							+ APPKEY + "&id=11");

			HttpURLConnection connection = null;
			connection = (HttpURLConnection) newUrl.openConnection();// 实例化connection
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(8000);
			connection.setReadTimeout(8000);
			InputStream in = connection.getInputStream();// 获取输入流
			// 下面对获取到的输入流进行读取
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			parseWeatherWithJSON(response.toString());
		} catch (Exception e) {
			Log.e("wxy", e.toString());
		}

	}

	// 饮食新闻列表解析数据
	private void parseWeatherWithJSON(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			String error_code = jsonObject.getString("error_code");
			if (error_code.equals("0")) {
				JSONObject resultoJsonObject = jsonObject
						.getJSONObject("result");
				JSONArray dataArray = resultoJsonObject.getJSONArray("data");
				Log.i("wxy", dataArray + "");
				for (int i = 0; i < dataArray.length(); i++) {
					NewEntity oneNew = new NewEntity(); // 实例化entity
					JSONObject object = dataArray.getJSONObject(i); // 循环获取数组值
					Long id = object.getLong("id"); // 取出数组的每一个数据
					String keywords = object.getString("keywords");
					String title = object.getString("title");
					String description = object.getString("description");
					String img = object.getString("img");
					int loreclass = object.getInt("loreclass");
					int count = object.getInt("count");
					int rcount = object.getInt("rcount");
					int fcount = object.getInt("fcount");
					Long time = object.getLong("time");

					oneNew.setId(id);
					oneNew.setKeywords(keywords);
					oneNew.setTitle(title);
					oneNew.setDescription(description);
					oneNew.setImg(img);
					oneNew.setLoreclass(loreclass);
					oneNew.setCount(count);
					oneNew.setRcount(rcount);
					oneNew.setFcount(fcount);
					oneNew.setTime(time);

					newList.add(oneNew);

					Log.i("wxy", img);
				}
				Message msg = new Message();
				msg.obj = newList;
				handler.sendMessage(msg);
			}

		} catch (Exception e) {
			Log.e("wxy", e.toString());
		}
	}

}
