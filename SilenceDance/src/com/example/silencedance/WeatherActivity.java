package com.example.silencedance;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.WeatherAdapter;
import com.example.entity.Weather;
import com.example.get.HttpUtil;
import com.example.util.HttpCallbackListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class WeatherActivity extends Activity {
	private EditText etCity;
	private ImageButton btnQuery;
	private ListView lvFutureWeather;
	public static final int SHOW_RESPONSE = 1;
	private List<Weather> data;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHOW_RESPONSE:
				String response = (String) msg.obj;
				if (response != null) {
					parseWithJSON(response);
					WeatherAdapter weatherAdapter = new WeatherAdapter(
							WeatherActivity.this, R.layout.weather_item, data);
					lvFutureWeather.setAdapter(weatherAdapter);
					ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0,
							1);
					scaleAnimation.setDuration(1000);
					LayoutAnimationController animationController = new LayoutAnimationController(
							scaleAnimation, 0.6f);
					lvFutureWeather.setLayoutAnimation(animationController);
				}
			default:
				break;
			}
		}

		private void parseWithJSON(String response) {
			data = new ArrayList<Weather>();
			JsonParser parser = new JsonParser();// json 解析器
			JsonObject obj = (JsonObject) parser.parse(response); /* 获取返回状态码 */
			String resultcode = obj.get("resultcode").getAsString(); /* 如果状态码是200说明返回数据成功 */
			if (resultcode != null && resultcode.equals("200")) {
				JsonObject resultObj = obj.get("result").getAsJsonObject();
				JsonArray futureWeatherArray = resultObj.get("future")
						.getAsJsonArray();
				for (int i = 0; i < futureWeatherArray.size(); i++) {
					Weather weather = new Weather();
					JsonObject weatherObject = futureWeatherArray.get(i)
							.getAsJsonObject();
					weather.setDayOfWeek(weatherObject.get("week")
							.getAsString());
					weather.setDate(weatherObject.get("date").getAsString());
					weather.setTemperature(weatherObject.get("temperature")
							.getAsString());
					weather.setWeather(weatherObject.get("weather")
							.getAsString());
					data.add(weather);
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		initViews();
		setListeners();
	}

	private void initViews() {
		etCity = (EditText) findViewById(R.id.etCity);
		btnQuery = (ImageButton) findViewById(R.id.btnQuery);
		lvFutureWeather = (ListView) findViewById(R.id.lvFutureWeather);
	}

	private void setListeners() {
		btnQuery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(WeatherActivity.this, "成功", 1).show();
				String city = etCity.getText().toString();
				String weatherUrl = "http://v.juhe.cn/weather/index?format=2&cityname="
						+ city + "&key=63aa5542dfc7f0bdf194fe399fae64d4";
				HttpUtil.sendHttpRequest(weatherUrl,
						new HttpCallbackListener() {
							@Override
							public void onFinish(String response) {
								Log.i("MainActivity", "success");
								Message message = new Message();
								message.what = SHOW_RESPONSE; // 将服务器返回的结果存放到
																// Message 中
								message.obj = response.toString();
								handler.sendMessage(message);
								Log.i("Main", "message:" + message);
							}

							@Override
							public void onError(Exception e) {
								Log.i("main", "fail");
							}
						});
			}
		});
	}

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}