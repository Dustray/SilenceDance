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

	// ������֤��SDK
	private static String APPKEY = "14046040526d1";
	private static String APPSECRET = "1546c633049d41b038c0bfcfde28881b";

	private MyCountDownTime myCountDownTime;// ������֤�뵹��ʱ
	private Handler mHandler;// ����ִ�к�ʱ����
	private Thread thread = new Thread() {
		public void run() {
			/* ����ע�Ὺʼ */
			// ע��ʧ�ܻ��׳�HyphenateException
			try {
				EMClient.getInstance().createAccount(userPhone, userPass);
				LoginHuanxin();

			} catch (HyphenateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("mytag", e.toString());
			}// ͬ������
			/* ����ע����� */
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		SMSSDK.initSDK(this, APPKEY, APPSECRET, false);// ���ض�����֤��SDK
		Bmob.initialize(this, "e37ca30c22533836329999a7fcf077b8"); // ����Bmob SDK

		// ��ʼ���ؼ�
		init();

		// ���Żص�
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
			SMSSDK.registerEventHandler(eventHandler); // ע����Żص�
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ��ʼ���ؼ�
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

	// �����ת����¼ҳ��
	public void toLogin(View view) {
		intent = new Intent(RegisterActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	public void submit() {
		userPhone = etPhone.getText().toString().trim(); // ��ȡ�ֻ���
		userName = etNickname.getText().toString().trim(); // ��ȡ�ǳ�
		userPass = etPass.getText().toString().trim(); // ��ȡ����

		if (userName.equals("") || userPass.equals("")) {
			Toast.makeText(RegisterActivity.this, "�û��������벻��Ϊ��",
					Toast.LENGTH_SHORT).show();
			return;
		}

		User userobj = new User();// ʵ����ʵ����
		userobj.setUserPhone(userPhone); // ��װ����
		userobj.setUserName(userName);
		userobj.setUserPass(userPass);
		userobj.save(RegisterActivity.this, new SaveListener() { // ���������������
					// ��ӳɹ���
					@Override
					public void onSuccess() {
						// ����ע������
						saveRegister();
						if (ContantsRegister.iregister != null)
							ContantsRegister.iregister.onRegister();
						/* ����ע�Ὺʼ */
						thread.start();
						/* ����ע����� */
						Toast.makeText(RegisterActivity.this, "ע��ɹ�",
								Toast.LENGTH_SHORT).show();
						finish();
					}

					// ���ʧ��
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(RegisterActivity.this, "ע��ʧ��",
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
			// �ص����
			if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
				// �ύ��֤��ɹ�
				submit();// ���÷�����bmob�ύ
			} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
				// ��ȡ��֤��ɹ�
				Toast.makeText(RegisterActivity.this, "��ȡ��֤��ɹ�",
						Toast.LENGTH_SHORT).show();
			} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
				// ����֧�ַ�����֤��Ĺ����б�
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
				Toast.makeText(RegisterActivity.this, "�绰���벻��Ϊ��",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btRegister:
			String number = etNumber.getText().toString().trim();
			if (!TextUtils.isEmpty(number)) {
				SMSSDK.submitVerificationCode("86", phone, number);// ��֤����
			} else {
				Toast.makeText(RegisterActivity.this, "�绰���벻��Ϊ��",
						Toast.LENGTH_SHORT).show();
				return;
			}
			break;
		default:
			break;
		}
	}

	// �жϵ绰�����Ƿ�ע��
	private void judgeExit(String tell) {
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("userPhone", tell);
		query.findObjects(RegisterActivity.this, new FindListener<User>() {

			@Override
			public void onSuccess(List<User> user) {
				if(user.size()==0){
					SMSSDK.getVerificationCode("86", phone);// ��ȡ����
					btGetNumber.setBackgroundResource(R.drawable.btn_long_grey);
					btGetNumber.setTextColor(Color.GRAY);
					getCode();
				} else {
					Toast.makeText(RegisterActivity.this, "�ֻ����ѱ�ע��",
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
	 * @Description: TODO ���ͳɹ��󣬿�ʼ����ʱ
	 * 
	 */
	private void startTimer() {
		if (null == myCountDownTime) {
			myCountDownTime = new MyCountDownTime(60000, 1000, btGetNumber,
					"���·���");

		}
		myCountDownTime.start();
	}

	/**
	 * 
	 * @Description: TODO һ�㷢��ʧ��ʱ����Ҫ����Button״̬
	 */
	private void cancelTimer() {
		if (null != myCountDownTime) {
			myCountDownTime.cancel();
			myCountDownTime.onFinish();
		}
	}

	protected void onDestroy() {
		// ���ٻص������ӿ�
		SMSSDK.unregisterAllEventHandler();
		super.onDestroy();
	}

	// ����ע������
	private void saveRegister() {

		// ���SharedPreferences����
		pref = getSharedPreferences("userInfo", MODE_PRIVATE);// �����ݴ�ŵ���ΪuserInfo���ĵ���

		// ���SharedPreferences.Editor����
		editor = pref.edit();
		editor.putString("userPhone", userPhone);
		editor.putString("userName", userName);
		editor.putString("userPass", userPass);
		editor.commit();
	}

	public void goBack(View view) {
		finish();
	}

	/* �����û���½��ʼ */
	private void LoginHuanxin() {
		EMClient.getInstance().login(userPhone, userPass, new EMCallBack() {// �ص�
					@Override
					public void onSuccess() {
						EMClient.getInstance().groupManager().loadAllGroups();
						EMClient.getInstance().chatManager()
								.loadAllConversations();
						Log.d("main", "��¼����������ɹ���");
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						Toast.makeText(RegisterActivity.this, "��¼���������ʧ�ܣ�",
								Toast.LENGTH_SHORT).show();
						Log.d("main", "��¼���������ʧ�ܣ�");
					}
				});
	}

	/* �����û���½���� */

}
