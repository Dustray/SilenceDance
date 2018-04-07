package com.example.silencedance;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;

public class NewInfoActivity extends Activity {

//	private TextView tvInfo;
	private WebView wvInfo;
	private Long id; // 目录id，文章id
	private String APPKEY = "a0dcf0236afa6fe94f9169e19496ca28";
	private String message,title;

	
	Thread thread = new Thread() {
		public void run() {
			getNewInfo();
		};
	};
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//显示在webView上（默认获取到的为html数据，需要进行编码）
			wvInfo.loadDataWithBaseURL(null,msg.obj.toString(),"text/html","utf-8",null);
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_info);

		wvInfo = (WebView) findViewById(R.id.wvInfo);
		// 获取传递过来的id
		getId();

		// 查询具体新闻信息
		thread.start();

	}

	// 获取传递过来的id
	private void getId() {
		Bundle bundle = this.getIntent().getExtras();
		id = bundle.getLong("id");
		//tvInfo.setText(id.toString());
	}

	// 查询具体新闻信息
	private void getNewInfo() {
		try {
			URL newUrl = new URL(
					"http://japi.juhe.cn/health_knowledge/infoDetail?id="
							+ id + "&key="+APPKEY);

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

	//解析数据
	private void parseWeatherWithJSON(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			String error_code = jsonObject.getString("error_code");
			if (error_code.equals("0")) {
				JSONObject resultoJsonObject = jsonObject
						.getJSONObject("result");
				message=resultoJsonObject.getString("message");
				//title=resultoJsonObject.getString("title");
				Message msg = new Message();
				msg.obj = message;
				handler.sendMessage(msg);
			}
		}catch(Exception e){
			Log.e("wxy",e.toString());
		}
	}
}
