package com.example.silencedance;

import java.util.List;

import org.json.JSONObject;

import com.example.contants.ContantsRegister;
import com.example.tools.FoundWebServices;
import com.example.tools.MyCountDownTime;
import com.example.table.User;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener,
		Callback {

	private EditText etPhone, etNumber, etNickname, etPass;
	private Button btGetNumber, btRegister;

	private String userPhone, userName, userPass;

	private String phone;
	private Intent intent;

	private SharedPreferences pref;
	private Editor editor;

	// 短信验证码SDK
	private static String APPKEY = "14046040526d1";
	private static String APPSECRET = "1546c633049d41b038c0bfcfde28881b";

	private MyCountDownTime myCountDownTime;// 用于验证码倒计时
	private Handler mHandler;// 用于执行耗时操作
	private Thread thread = new Thread() {
		public void run() {
			/* 环信注册开始 */
			// 注册失败会抛出HyphenateException
			try {
				EMClient.getInstance().createAccount(userPhone, userPass);
				LoginHuanxin();

			} catch (HyphenateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("mytag", e.toString());
			}// 同步方法
			/* 环信注册结束 */
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		SMSSDK.initSDK(this, APPKEY, APPSECRET, false);// 加载短信验证码SDK
		Bmob.initialize(this, "e37ca30c22533836329999a7fcf077b8"); // 加载Bmob SDK

		// 初始化控件
		init();

		// 短信回调
		initSDK();
	}

	private void initSDK() {
		try {
			final Handler handler = new Handler(this);
			EventHandler eventHandler = new EventHandler() {
				public void afterEvent(int event, int result, Object data) {
					Message msg = new Message();
					msg.arg1 = event;
					msg.arg2 = result;
					msg.obj = data;
					handler.sendMessage(msg);
				}
			};
			SMSSDK.registerEventHandler(eventHandler); // 注册短信回调
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 初始化控件
	private void init() {
		etPhone = (EditText) findViewById(R.id.etPhone);
		etNumber = (EditText) findViewById(R.id.etNumber);
		etNickname = (EditText) findViewById(R.id.etNickname);
		etPass = (EditText) findViewById(R.id.etPass);

		btGetNumber = (Button) findViewById(R.id.btGetNumber);
		btRegister = (Button) findViewById(R.id.btRegister);
		btGetNumber.setOnClickListener(this);
		btRegister.setOnClickListener(this);
	}

	// 点击跳转到登录页面
	public void toLogin(View view) {
		intent = new Intent(RegisterActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	public void submit() {
		userPhone = etPhone.getText().toString().trim(); // 获取手机号
		userName = etNickname.getText().toString().trim(); // 获取昵称
		userPass = etPass.getText().toString().trim(); // 获取密码

		if (userName.equals("") || userPass.equals("")) {
			Toast.makeText(RegisterActivity.this, "用户名或密码不能为空",
					Toast.LENGTH_SHORT).show();
			return;
		}

		User userobj = new User();// 实例化实体类
		userobj.setUserPhone(userPhone); // 封装数据
		userobj.setUserName(userName);
		userobj.setUserPass(userPass);
		userobj.save(RegisterActivity.this, new SaveListener() { // 向服务器保存数据
					// 添加成功额
					@Override
					public void onSuccess() {
						// 保存注册数据
						saveRegister();
						if (ContantsRegister.iregister != null)
							ContantsRegister.iregister.onRegister();
						/* 环信注册开始 */
						thread.start();
						/* 环信注册结束 */
						Toast.makeText(RegisterActivity.this, "注册成功",
								Toast.LENGTH_SHORT).show();
						finish();
					}

					// 添加失败
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(RegisterActivity.this, "注册失败",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	@Override
	public boolean handleMessage(Message msg) {
		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;
		if (result == SMSSDK.RESULT_COMPLETE) {
			// 回调完成
			if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
				// 提交验证码成功
				submit();// 调用方法向bmob提交
			} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
				// 获取验证码成功
				Toast.makeText(RegisterActivity.this, "获取验证码成功",
						Toast.LENGTH_SHORT).show();
			} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
				// 返回支持发送验证码的国家列表
			}
		} else {
			int status = 0;
			try {
				((Throwable) data).printStackTrace();
				Throwable throwable = (Throwable) data;

				JSONObject object = new JSONObject(throwable.getMessage());
				String des = object.optString("detail");
				status = object.optInt("status");
				if (!TextUtils.isEmpty(des)) {
					Toast.makeText(RegisterActivity.this, des,
							Toast.LENGTH_SHORT).show();
					return false;
				}
			} catch (Exception e) {
				SMSLog.getInstance().w(e);
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btGetNumber:
			phone = etPhone.getText().toString().trim();
			if (!TextUtils.isEmpty(phone)) {
				judgeExit(phone);
			} else {
				Toast.makeText(RegisterActivity.this, "电话号码不能为空",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btRegister:
			String number = etNumber.getText().toString().trim();
			if (!TextUtils.isEmpty(number)) {
				SMSSDK.submitVerificationCode("86", phone, number);// 验证短信
			} else {
				Toast.makeText(RegisterActivity.this, "电话号码不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			}
			break;
		default:
			break;
		}
	}

	// 判断电话号码是否被注册
	private void judgeExit(String tell) {
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("userPhone", tell);
		query.findObjects(RegisterActivity.this, new FindListener<User>() {

			@Override
			public void onSuccess(List<User> user) {
				if(user.size()==0){
					SMSSDK.getVerificationCode("86", phone);// 获取短信
					btGetNumber.setBackgroundResource(R.drawable.btn_long_grey);
					btGetNumber.setTextColor(Color.GRAY);
					getCode();
				} else {
					Toast.makeText(RegisterActivity.this, "手机号已被注册",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
			}
		});

	}

	private void getCode() {
		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startTimer();
			}
		}, 0);
	}

	/**
	 * 
	 * @Description: TODO 发送成功后，开始倒计时
	 * 
	 */
	private void startTimer() {
		if (null == myCountDownTime) {
			myCountDownTime = new MyCountDownTime(60000, 1000, btGetNumber,
					"重新发送");

		}
		myCountDownTime.start();
	}

	/**
	 * 
	 * @Description: TODO 一般发送失败时，需要重置Button状态
	 */
	private void cancelTimer() {
		if (null != myCountDownTime) {
			myCountDownTime.cancel();
			myCountDownTime.onFinish();
		}
	}

	protected void onDestroy() {
		// 销毁回调监听接口
		SMSSDK.unregisterAllEventHandler();
		super.onDestroy();
	}

	// 保存注册数据
	private void saveRegister() {

		// 获得SharedPreferences对象
		pref = getSharedPreferences("userInfo", MODE_PRIVATE);// 将内容存放到名为userInfo的文档内

		// 获得SharedPreferences.Editor对象
		editor = pref.edit();
		editor.putString("userPhone", userPhone);
		editor.putString("userName", userName);
		editor.putString("userPass", userPass);
		editor.commit();
	}

	public void goBack(View view) {
		finish();
	}

	/* 环信用户登陆开始 */
	private void LoginHuanxin() {
		EMClient.getInstance().login(userPhone, userPass, new EMCallBack() {// 回调
					@Override
					public void onSuccess() {
						EMClient.getInstance().groupManager().loadAllGroups();
						EMClient.getInstance().chatManager()
								.loadAllConversations();
						Log.d("main", "登录聊天服务器成功！");
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						Toast.makeText(RegisterActivity.this, "登录聊天服务器失败！",
								Toast.LENGTH_SHORT).show();
						Log.d("main", "登录聊天服务器失败！");
					}
				});
	}

	/* 环信用户登陆结束 */

}
