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

	private String cityName = "����"; // ���������ַ���

	private boolean oIsRain = false, oIsSnow = false, oIsHail = false,
			mIsRain = false, mIsSnow = false, mIsHail = false;// �ж��Ƿ�������ѩ���߱�������
	private int oW, oLow, oHigh, mLow, mHigh;

	private NotificationManager manager;// ֪ͨ������
	private int notification_id;// ����intification��id

	private LinearLayout llSelectTime; // ��ʾѡ��ʱ���һ��
	private Switch swOnOfOff; // ����
	private TextView tvSelectTime, tvFeedBack, tvUpdateData;

	private boolean pushSwitch = false;

	private Timer timer; // ��ʱ��
	private int savedHour = 00, savedMinute = 00; // ѡ�������������

	private SharedPreferences pref;
	private Editor editor;

	private Intent intent;

	private LocationClient mLocationClient;
	private String userName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // ����ҳ��ȥ��������
		setContentView(R.layout.activity_set);
		// text = (TextView) findViewById(R.id.text);

		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		pref = getSharedPreferences("userSetting", MODE_PRIVATE);// �����ݴ�ŵ���ΪuserInfo���ĵ���

		// ��ʼ���ؼ�������
		init();

		// �ؼ��󶨵���¼�
		bindClick();

		// ���ݳ�ʼ��
		getData();

		// ��ȡ��λ
		location();

	}

	// ��ʼ���ؼ�
	private void init() {
		// text = (TextView) findViewById(R.id.text);
		llSelectTime = (LinearLayout) findViewById(R.id.llSelectTime);
		tvSelectTime = (TextView) findViewById(R.id.tvSelectTime);
		tvFeedBack = (TextView) findViewById(R.id.tvFeedBack);
		tvUpdateData = (TextView) findViewById(R.id.tvUpdateData);
		// ���ص���¼�
		swOnOfOff = (Switch) findViewById(R.id.swOnOfOff);
		swOnOfOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton compoundButton,
					boolean checked) {
				if (checked) {
					llSelectTime.setVisibility(View.VISIBLE); // ���ÿɼ�
					pushSwitch = true;
					swOnOfOff.setTrackResource(R.drawable.track_switch1);
					saveSwitch();
					Log.i("wxy", "switch:" + pushSwitch);
				} else {

					llSelectTime.setVisibility(View.GONE); // ���ò��ɼ�
					pushSwitch = false;
					swOnOfOff.setTrackResource(R.drawable.track_switch2);
					saveSwitch();
					if (timer != null)
						timer.cancel();// �رն�ʱ��
				}
			}
		});

	}

	// �ؼ��󶨵���¼�
	private void bindClick() {
		tvSelectTime.setOnClickListener(this);
		tvFeedBack.setOnClickListener(this);
		tvUpdateData.setOnClickListener(this);
	}

	// �ؼ�����¼�
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tvSelectTime:
			// ��ʾѡ��ʱ��Ի���
			showSelectDailog();
			break;
		case R.id.tvFeedBack:
			// ���SharedPreferences����
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
			// ���SharedPreferences����
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

	// ���ݳ�ʼ��
	private void getData() {
		// �жϿ���
		pushSwitch = pref.getBoolean("pushSwitch", false);// ��ȡ�Ƿ�洢��������Ϣ��û��Ĭ��Ϊ�ر�״̬
		Log.i("wxy", "switch:" + pushSwitch);
		if (pushSwitch)
			swOnOfOff.setChecked(true);
		// ʱ����ʾ
		String showTime = "", h = "", m = "";
		getTime();
		if (savedHour < 10) { // ���Сʱ�ͷ���С��ʮ��Ҫ��ǰ�߼�0
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

	// ��ȡ��λ
	private void location() {
		mLocationClient = new LocationClient(this);

		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd0911");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);// ÿ�����������һ������
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

	// ���ݾ�γ�Ȼ�ȡ������
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
				cityName = city; // ����λ���ĳ�������ֵ��cityName�����ڻ�ȡ����
			}
		}
		return city;
	}

	// ��ʱ�����¼�
	private void timeTrigger(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		Date date = calendar.getTime(); // ��һ��ִ�������ʱ��
		if (date.before(new Date())) { //
			// �����һ��ִ�������ʱ��С�ڵ�ǰʱ�䣬��ôҪ��ִ�������ʱ���һ�죬���������ִ��
			date = this.addDay(date, 1);
		}
		timer = new Timer(true);
		timer.schedule(new Task(), date, 1000 * 60 * 60 * 24);//
		// �ڶ�������Ϊ��һ�ε��õ�ʱ�䣬����������Ϊ���ε��÷����ļ��������
	}

	// ���ڼ�һ��
	public Date addDay(Date date, int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, num);
		return calendar.getTime();
	}

	// ��ʾѡ��ʱ��Ի���
	private void showSelectDailog() {
		// ��ȡ��ǰ�洢��ʱ������
		getTime();
		OnTimeSetListener listener = new OnTimeSetListener() {
			// ѡ��󷵻ص�����
			@Override
			public void onTimeSet(TimePicker arg0, int hour, int minute) {
				String time = "";// ʱ���ַ���
				if (hour < 10) { // ���СʱС��10����ǰ�����0
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
				// ��ʱ�����¼�
				timeTrigger(hour, minute); // ���������ݻ�ȡ����ѡ���Сʱ�ͷ��ӣ���Ϊÿ�춨ʱ���õ�ʱ��

				// ����ѡ���ʱ������
				saveTime(hour, minute);
			}

		};
		TimePickerDialog dialog = new TimePickerDialog(this, listener,
				savedHour, savedMinute, true); // ����������ΪĬ��ʱ�䣬���һ������Ϊ�Ƿ�24Сʱ��ʽ
		dialog.show();

	}

	// ��ȡ��ǰ�洢��ʱ������
	private void getTime() {
		savedHour = pref.getInt("pushHour", 0);
		savedMinute = pref.getInt("pushMinute", 0);
	}

	// ���濪����Ϣ
	private void saveSwitch() {
		editor = pref.edit();
		editor.putBoolean("pushSwitch", pushSwitch);
		editor.commit();
	}

	// ����ѡ���ʱ������
	private void saveTime(int hour, int minute) {

		editor = pref.edit();
		editor.putInt("pushHour", hour);
		editor.putInt("pushMinute", minute);
		editor.commit();
	}

	// ��ȡ��������
	public void sendRequestWithHttpURLConnection() {
		Log.i("wxy", "123");
		// �����߳�
		new Thread() {
			public void run() {
				URL url;
				HttpURLConnection connection = null;
				try {
					Log.i("wxy", "cityName:" + cityName);
					// ��ȡ���������ַ���
					String cn = URLEncoder.encode(cityName, "utf-8");// ������ת��Ϊ����
					url = new URL(
							"http://v.juhe.cn/weather/index?format=2&cityname="
									+ cn
									+ "&key=63aa5542dfc7f0bdf194fe399fae64d4");
					connection = (HttpURLConnection) url.openConnection();// ʵ����connection
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();// ��ȡ������
					// ����Ի�ȡ�������������ж�ȡ
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

	// JSON��������
	private void parseWeatherWithJSON(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			String resultcode = jsonObject.getString("resultcode");
			if (resultcode.equals("200")) {
				JSONObject resultObject = jsonObject.getJSONObject("result");
				// ��������
				JSONObject todayObject = resultObject.getJSONObject("today");
				String oWeather = todayObject.getString("weather"); // ��ȡ�������
				if (oWeather.indexOf("��") != -1) // �ж��Ƿ����꣬ѩ���������Ӱ����е�����
					oIsRain = true;
				if (oWeather.indexOf("ѩ") != -1)
					oIsSnow = true;
				if (oWeather.indexOf("��") != -1)
					oIsHail = true;
				JSONObject skObject = resultObject.getJSONObject("sk");
				String oWind_strength = skObject.getString("wind_strength");// �����������硰2����
				String oWind = oWind_strength.substring(0,
						oWind_strength.indexOf("��")); // ȥ�������С������֣�ֻ������
				oW = Integer.parseInt(oWind); // ����������ת��Ϊ����

				String oTemperature = todayObject.getString("temperature");
				String[] oTemp = oTemperature.split("~"); // ͨ����~�����Ž��зָ�
				String oLowTemp = oTemp[0].substring(0, oTemp[0].indexOf("��"));
				oLow = Integer.parseInt(oLowTemp); // ���������¶�ת��Ϊ����
				String oHighTemp = oTemp[1].substring(0, oTemp[1].indexOf("��"));
				oHigh = Integer.parseInt(oHighTemp); // ���������¶�ת��Ϊ����
				Log.i("wxy", "oWeather=" + oWeather + "oIsRain=" + oIsRain
						+ ",oWind=" + oWind + ",oTemp=" + oTemperature
						+ ",oLowTemp=" + oLowTemp + "��oHighTemp=" + oHighTemp);
				// ��������
				JSONArray futureArray = resultObject.getJSONArray("future"); // ��ȡδ����������
				JSONObject tomorrowObject = futureArray.getJSONObject(1); //
				// �����±��ȡ�ڶ���Ԫ�أ����ڶ�����������
				String mWeather = tomorrowObject.getString("weather"); // ��ȡ�������
				if (mWeather.indexOf("��") != -1) // �ж��Ƿ����꣬ѩ���������Ӱ����е�����
					mIsRain = true;
				if (mWeather.indexOf("ѩ") != -1)
					mIsSnow = true;
				if (mWeather.indexOf("��") != -1)
					mIsHail = true;
				String mTemperature = tomorrowObject.getString("temperature");
				String[] mTemp = mTemperature.split("~"); // ͨ����~�����Ž��зָ�
				String mLowTemp = mTemp[0].substring(0, mTemp[0].indexOf("��"));
				mLow = Integer.parseInt(mLowTemp); // ���������¶�ת��Ϊ����
				String mHighTemp = mTemp[1].substring(0, mTemp[1].indexOf("��"));
				mHigh = Integer.parseInt(mHighTemp); // ���������¶�ת��Ϊ����
				Log.i("wxy", "mWeather=" + mWeather + "mIsRain=" + mIsRain
						+ ",mTemp=" + mTemperature + ",mLowTemp=" + mLowTemp
						+ "��mHighTemp=" + mHighTemp);

				if (oIsRain || oIsSnow || oIsHail || oW > 5 || oLow < -15
						|| oHigh > 35 || mIsRain || mIsSnow || mIsHail
						|| mLow < -15 || mHigh > 35) { // ����������ݴﵽ��ʾ��׼����������
					// ����
					sendNotification();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* ����notification�����͵�֪ͨ�� */
	private void sendNotification() {
		Intent intent = new Intent(this, MainActivity.class);// ����Activity
		PendingIntent pintent = PendingIntent.getActivity(this, 0, intent, 0);
		Builder builder = new Notification.Builder(this);// ʵ��Notification��������������Ϣ
		builder.setSmallIcon(R.drawable.ic_launcher);// ����ͼ��
		builder.setTicker("�����㳡��������ʾ");// �ֻ�״̬����ʾ
		builder.setWhen(System.currentTimeMillis());// ��ק����ʾ��ʱ�䣬Ϊ��ǰϵͳʱ��
		builder.setContentTitle("������ʾ");// ���ñ���
		String pushInfo = "";
		if (oIsRain || oIsSnow || oIsHail || oW > 5 || oLow < -15 || oHigh > 35) {
			pushInfo = "������";
			if (oIsRain)
				pushInfo += "�꣬";
			if (oIsSnow)
				pushInfo += "ѩ��";
			if (oIsHail)
				pushInfo += "������";
			if (oW > 5)
				pushInfo += "5�����ϴ�磬";
			if (oLow < -15)
				pushInfo += "-15�����µ��£�";
			if (oHigh > 35)
				pushInfo += "35�����ϸ��£�";
		}
		if (mIsRain || mIsSnow || mIsHail || mLow < -15 || mHigh > 35) {
			pushInfo += "������";
			if (mIsRain)
				pushInfo += "�꣬";
			if (mIsSnow)
				pushInfo += "ѩ��";
			if (mIsHail)
				pushInfo += "������";
			if (mLow < -15)
				pushInfo += "-15�����µ��£�";
			if (mHigh > 35)
				pushInfo += "35�����ϸ��£�";
		}
		pushInfo += "�뾡���������";
		builder.setContentText(pushInfo);// ���þ�������
		builder.setContentIntent(pintent);// ���õ�������ͼ
		builder.setDefaults(Notification.DEFAULT_ALL);// ����������ָʾ�ƣ���
		Notification notification = builder.build();// ��ȡNotification
		manager.notify(notification_id, notification);// ����֪ͨ��֪ͨ��
	}

	// ��ʱ���õķ���
	private class Task extends TimerTask {

		@Override
		public void run() {
			sendRequestWithHttpURLConnection();
		}

	}

	// ����
	public void goBack(View view) {
		finish();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!mLocationClient.isStarted()) {
			// ������λ
			mLocationClient.start();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		// ֹͣ��λ
		mLocationClient.start();
	}
}
