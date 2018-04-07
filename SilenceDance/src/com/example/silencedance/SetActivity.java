package com.example.silencedance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.Notification.Builder;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SetActivity extends Activity implements OnClickListener {

	// private TextView text;
	public static final int SHOW_RESPONSE = 0;

	private String cityName = "北京"; // 城市名称字符串

	private boolean oIsRain = false, oIsSnow = false, oIsHail = false,
			mIsRain = false, mIsSnow = false, mIsHail = false;// 判断是否下雨下雪或者冰雹天气
	private int oW, oLow, oHigh, mLow, mHigh;

	private NotificationManager manager;// 通知控制类
	private int notification_id;// 定义intification的id

	private LinearLayout llSelectTime; // 显示选择时间的一层
	private Switch swOnOfOff; // 开关
	private TextView tvSelectTime, tvFeedBack, tvUpdateData;

	private boolean pushSwitch = false;

	private Timer timer; // 定时器
	private int savedHour = 00, savedMinute = 00; // 选择器保存的数据

	private SharedPreferences pref;
	private Editor editor;

	private Intent intent;

	private LocationClient mLocationClient;
	private String userName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 单个页面去掉标题栏
		setContentView(R.layout.activity_set);
		// text = (TextView) findViewById(R.id.text);

		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		pref = getSharedPreferences("userSetting", MODE_PRIVATE);// 将内容存放到名为userInfo的文档内

		// 初始化控件及变量
		init();

		// 控件绑定点击事件
		bindClick();

		// 数据初始化
		getData();

		// 获取定位
		location();

	}

	// 初始化控件
	private void init() {
		// text = (TextView) findViewById(R.id.text);
		llSelectTime = (LinearLayout) findViewById(R.id.llSelectTime);
		tvSelectTime = (TextView) findViewById(R.id.tvSelectTime);
		tvFeedBack = (TextView) findViewById(R.id.tvFeedBack);
		tvUpdateData = (TextView) findViewById(R.id.tvUpdateData);
		// 开关点击事件
		swOnOfOff = (Switch) findViewById(R.id.swOnOfOff);
		swOnOfOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton compoundButton,
					boolean checked) {
				if (checked) {
					llSelectTime.setVisibility(View.VISIBLE); // 设置可见
					pushSwitch = true;
					swOnOfOff.setTrackResource(R.drawable.track_switch1);
					saveSwitch();
					Log.i("wxy", "switch:" + pushSwitch);
				} else {

					llSelectTime.setVisibility(View.GONE); // 设置不可见
					pushSwitch = false;
					swOnOfOff.setTrackResource(R.drawable.track_switch2);
					saveSwitch();
					if (timer != null)
						timer.cancel();// 关闭定时器
				}
			}
		});

	}

	// 控件绑定点击事件
	private void bindClick() {
		tvSelectTime.setOnClickListener(this);
		tvFeedBack.setOnClickListener(this);
		tvUpdateData.setOnClickListener(this);
	}

	// 控件点击事件
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tvSelectTime:
			// 显示选择时间对话框
			showSelectDailog();
			break;
		case R.id.tvFeedBack:
			// 获得SharedPreferences对象
			pref = getSharedPreferences("userInfo", MODE_PRIVATE);
			userName = pref.getString("userName", "");
			if (userName.isEmpty()) {
				// Toast.makeText(SetActivity.this, "name:" + userName,
				// Toast.LENGTH_SHORT).show();
				intent = new Intent(SetActivity.this, LoginActivity.class);
			} else {
				// Toast.makeText(SetActivity.this, "name:" + userName,
				// Toast.LENGTH_SHORT).show();
				intent = new Intent(SetActivity.this, FeedBackActivity.class);
			}
			startActivity(intent);
			break;

		case R.id.tvUpdateData:
			// 获得SharedPreferences对象
			pref = getSharedPreferences("userInfo", MODE_PRIVATE);
			userName = pref.getString("userName", "");
			if (userName.isEmpty()) {
				intent = new Intent(SetActivity.this, LoginActivity.class);
			} else {
				intent = new Intent(SetActivity.this, UserUpdateActivity.class);
			}
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	// 数据初始化
	private void getData() {
		// 判断开关
		pushSwitch = pref.getBoolean("pushSwitch", false);// 获取是否存储过开关信息，没有默认为关闭状态
		Log.i("wxy", "switch:" + pushSwitch);
		if (pushSwitch)
			swOnOfOff.setChecked(true);
		// 时间显示
		String showTime = "", h = "", m = "";
		getTime();
		if (savedHour < 10) { // 如果小时和分钟小于十，要在前边加0
			h += "0";
			h += savedHour;
		} else
			h += savedHour;
		if (savedMinute < 10) {
			m += "0";
			m += savedMinute;
		} else
			m += savedMinute;
		showTime = h + ":" + m;
		tvSelectTime.setText(showTime);
	}

	// 获取定位
	private void location() {
		mLocationClient = new LocationClient(this);

		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd0911");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);// 每个多少秒进行一次请求
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// text.setText(getaddress(location.getLatitude(),
				// location.getLongitude()));
				Toast.makeText(
						SetActivity.this,
						location.getLatitude() + "," + location.getLongitude()
								+ "", Toast.LENGTH_SHORT).show();
			}
		});
	}

	// 根据经纬度获取城市名
	public String getaddress(double latitude, double longitude) {
		String city = "";
		List<Address> addList = null;
		Geocoder ge = new Geocoder(SetActivity.this);
		try {
			addList = ge.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (addList != null && addList.size() > 0) {
			for (int i = 0; i < addList.size(); i++) {
				Address ad = addList.get(i);
				city += ad.getLocality();
				city = city.substring(0, 2);
				cityName = city; // 将定位到的城市名赋值给cityName，用于获取天气
			}
		}
		return city;
	}

	// 定时触发事件
	private void timeTrigger(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		Date date = calendar.getTime(); // 第一次执行任务的时间
		if (date.before(new Date())) { //
			// 如果第一次执行任务的时间小于当前时间，那么要在执行任务的时间加一天，否则会立即执行
			date = this.addDay(date, 1);
		}
		timer = new Timer(true);
		timer.schedule(new Task(), date, 1000 * 60 * 60 * 24);//
		// 第二个参数为第一次调用的时间，第三个参数为两次调用方法的间隔毫秒数
	}

	// 日期加一天
	public Date addDay(Date date, int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, num);
		return calendar.getTime();
	}

	// 显示选择时间对话框
	private void showSelectDailog() {
		// 获取以前存储的时间数据
		getTime();
		OnTimeSetListener listener = new OnTimeSetListener() {
			// 选择后返回的数据
			@Override
			public void onTimeSet(TimePicker arg0, int hour, int minute) {
				String time = "";// 时间字符串
				if (hour < 10) { // 如果小时小于10，在前边添加0
					String h = "0" + hour;
					time += h;
				} else
					time += hour;
				time += ":";
				if (minute < 10) {
					String m = "0" + minute;
					time += m;
				} else {
					time += minute;
				}
				tvSelectTime.setText(time);
				// 定时触发事件
				timeTrigger(hour, minute); // 触发并传递获取到的选择的小时和分钟，最为每天定时调用的时间

				// 保存选择的时间数据
				saveTime(hour, minute);
			}

		};
		TimePickerDialog dialog = new TimePickerDialog(this, listener,
				savedHour, savedMinute, true); // 第三个参数为默认时间，最后一个参数为是否24小时形式
		dialog.show();

	}

	// 获取以前存储的时间数据
	private void getTime() {
		savedHour = pref.getInt("pushHour", 0);
		savedMinute = pref.getInt("pushMinute", 0);
	}

	// 保存开关信息
	private void saveSwitch() {
		editor = pref.edit();
		editor.putBoolean("pushSwitch", pushSwitch);
		editor.commit();
	}

	// 保存选择的时间数据
	private void saveTime(int hour, int minute) {

		editor = pref.edit();
		editor.putInt("pushHour", hour);
		editor.putInt("pushMinute", minute);
		editor.commit();
	}

	// 获取天气数据
	public void sendRequestWithHttpURLConnection() {
		Log.i("wxy", "123");
		// 创建线程
		new Thread() {
			public void run() {
				URL url;
				HttpURLConnection connection = null;
				try {
					Log.i("wxy", "cityName:" + cityName);
					// 获取城市名称字符串
					String cn = URLEncoder.encode(cityName, "utf-8");// 将汉字转换为编码
					url = new URL(
							"http://v.juhe.cn/weather/index?format=2&cityname="
									+ cn
									+ "&key=63aa5542dfc7f0bdf194fe399fae64d4");
					connection = (HttpURLConnection) url.openConnection();// 实例化connection
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
					e.printStackTrace();
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			};

		}.start();
	}

	// JSON解析天气
	private void parseWeatherWithJSON(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			String resultcode = jsonObject.getString("resultcode");
			if (resultcode.equals("200")) {
				JSONObject resultObject = jsonObject.getJSONObject("result");
				// 今天天气
				JSONObject todayObject = resultObject.getJSONObject("today");
				String oWeather = todayObject.getString("weather"); // 获取天气情况
				if (oWeather.indexOf("雨") != -1) // 判断是否有雨，雪，或冰雹类影响出行的天气
					oIsRain = true;
				if (oWeather.indexOf("雪") != -1)
					oIsSnow = true;
				if (oWeather.indexOf("冰") != -1)
					oIsHail = true;
				JSONObject skObject = resultObject.getJSONObject("sk");
				String oWind_strength = skObject.getString("wind_strength");// 风力级数，如“2级”
				String oWind = oWind_strength.substring(0,
						oWind_strength.indexOf("级")); // 去掉级数中“级”字，只留数字
				oW = Integer.parseInt(oWind); // 将风力数据转换为整形

				String oTemperature = todayObject.getString("temperature");
				String[] oTemp = oTemperature.split("~"); // 通过“~”符号进行分割
				String oLowTemp = oTemp[0].substring(0, oTemp[0].indexOf("℃"));
				oLow = Integer.parseInt(oLowTemp); // 今天的最低温度转换为整形
				String oHighTemp = oTemp[1].substring(0, oTemp[1].indexOf("℃"));
				oHigh = Integer.parseInt(oHighTemp); // 今天的最高温度转换为整形
				Log.i("wxy", "oWeather=" + oWeather + "oIsRain=" + oIsRain
						+ ",oWind=" + oWind + ",oTemp=" + oTemperature
						+ ",oLowTemp=" + oLowTemp + "，oHighTemp=" + oHighTemp);
				// 明天天气
				JSONArray futureArray = resultObject.getJSONArray("future"); // 获取未来天气数组
				JSONObject tomorrowObject = futureArray.getJSONObject(1); //
				// 根据下标获取第二个元素，即第二天天气数据
				String mWeather = tomorrowObject.getString("weather"); // 获取天气情况
				if (mWeather.indexOf("雨") != -1) // 判断是否有雨，雪，或冰雹类影响出行的天气
					mIsRain = true;
				if (mWeather.indexOf("雪") != -1)
					mIsSnow = true;
				if (mWeather.indexOf("冰") != -1)
					mIsHail = true;
				String mTemperature = tomorrowObject.getString("temperature");
				String[] mTemp = mTemperature.split("~"); // 通过“~”符号进行分割
				String mLowTemp = mTemp[0].substring(0, mTemp[0].indexOf("℃"));
				mLow = Integer.parseInt(mLowTemp); // 明天的最低温度转换为整形
				String mHighTemp = mTemp[1].substring(0, mTemp[1].indexOf("℃"));
				mHigh = Integer.parseInt(mHighTemp); // 明天的最高温度转换为整形
				Log.i("wxy", "mWeather=" + mWeather + "mIsRain=" + mIsRain
						+ ",mTemp=" + mTemperature + ",mLowTemp=" + mLowTemp
						+ "，mHighTemp=" + mHighTemp);

				if (oIsRain || oIsSnow || oIsHail || oW > 5 || oLow < -15
						|| oHigh > 35 || mIsRain || mIsSnow || mIsHail
						|| mLow < -15 || mHigh > 35) { // 如果天气数据达到提示标准，调用推送
					// 推送
					sendNotification();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* 构造notification并发送到通知栏 */
	private void sendNotification() {
		Intent intent = new Intent(this, MainActivity.class);// 启动Activity
		PendingIntent pintent = PendingIntent.getActivity(this, 0, intent, 0);
		Builder builder = new Notification.Builder(this);// 实例Notification并传递上下文信息
		builder.setSmallIcon(R.drawable.ic_launcher);// 设置图标
		builder.setTicker("无声广场舞天气提示");// 手机状态栏提示
		builder.setWhen(System.currentTimeMillis());// 拖拽后显示的时间，为当前系统时间
		builder.setContentTitle("天气提示");// 设置标题
		String pushInfo = "";
		if (oIsRain || oIsSnow || oIsHail || oW > 5 || oLow < -15 || oHigh > 35) {
			pushInfo = "今天有";
			if (oIsRain)
				pushInfo += "雨，";
			if (oIsSnow)
				pushInfo += "雪，";
			if (oIsHail)
				pushInfo += "冰雹，";
			if (oW > 5)
				pushInfo += "5级以上大风，";
			if (oLow < -15)
				pushInfo += "-15℃以下低温，";
			if (oHigh > 35)
				pushInfo += "35℃以上高温，";
		}
		if (mIsRain || mIsSnow || mIsHail || mLow < -15 || mHigh > 35) {
			pushInfo += "明天有";
			if (mIsRain)
				pushInfo += "雨，";
			if (mIsSnow)
				pushInfo += "雪，";
			if (mIsHail)
				pushInfo += "冰雹，";
			if (mLow < -15)
				pushInfo += "-15℃以下低温，";
			if (mHigh > 35)
				pushInfo += "35℃以上高温，";
		}
		pushInfo += "请尽量减少外出";
		builder.setContentText(pushInfo);// 设置具体内容
		builder.setContentIntent(pintent);// 设置点击后的意图
		builder.setDefaults(Notification.DEFAULT_ALL);// 包括声音，指示灯，震动
		Notification notification = builder.build();// 获取Notification
		manager.notify(notification_id, notification);// 发送通知到通知栏
	}

	// 定时调用的方法
	private class Task extends TimerTask {

		@Override
		public void run() {
			sendRequestWithHttpURLConnection();
		}

	}

	// 返回
	public void goBack(View view) {
		finish();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!mLocationClient.isStarted()) {
			// 开启定位
			mLocationClient.start();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		// 停止定位
		mLocationClient.start();
	}
}
