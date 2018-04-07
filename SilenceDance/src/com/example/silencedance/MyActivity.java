package com.example.silencedance;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

import com.bumptech.glide.Glide;
import com.example.contants.ContantsOutLogin;
import com.example.table.User;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.makeramen.roundedimageview.RoundedImageView;

import a.breaking;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends Activity implements OnClickListener {
	private Intent intent;
	private TextView tvUserName, tvMySet, tvCreateMyCircle, tvMyDownLoad,
			tvLiveService;
	private SharedPreferences pref;
	private Editor editor;
	private String userName, userPhone, objectId;
	private Button btDropLogin;
	private RoundedImageView rivUserHead;

	private Thread thread = new Thread() {
		public void run() {
			/* �����˳���½��ʼ */
			EMClient.getInstance().logout(true);// �˷���Ϊͬ������������Ĳ��� true ��ʾ�˳���¼ʱ���
												// GCM ����С�����͵� token

			/* �����˳���½���� */
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		Bmob.initialize(this, "8ef8a4752f48682eadb32d3c8c8e398f");// ��ʼ��Bmob
		// ��ʼ���ؼ�
		init();

		// �ж��Ƿ��¼����ʾ
		judgeLogin();

		// �󶨵���¼�
		bindClick();

	}

	// ��ʼ���ؼ�
	private void init() {
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvMySet = (TextView) findViewById(R.id.tvMySet);
		tvCreateMyCircle = (TextView) findViewById(R.id.tvCreateMyCircle);
		tvMyDownLoad = (TextView) findViewById(R.id.tvMyDownLoad);
		tvLiveService = (TextView) findViewById(R.id.tvLiveService);
		btDropLogin = (Button) findViewById(R.id.btDropLogin);
		rivUserHead = (RoundedImageView) findViewById(R.id.rivUserHead);
	}

	// �󶨵���¼�
	private void bindClick() {
		tvUserName.setOnClickListener(this);
		tvMySet.setOnClickListener(this);
		tvCreateMyCircle.setOnClickListener(this);
		tvMyDownLoad.setOnClickListener(this);
		tvLiveService.setOnClickListener(this);
		btDropLogin.setOnClickListener(this);
	}

	// �ж��Ƿ��¼����ʾ
	private void judgeLogin() {
		// ���SharedPreferences����
		pref = getSharedPreferences("userInfo", MODE_PRIVATE);
		userName = pref.getString("userName", "");
		userPhone = pref.getString("userPhone", userPhone);
		if (userName != "") {

			// �����ֻ��Ų�ѯobjectId
			selectObjectId();

			tvUserName.setText(userName);
			btDropLogin.setVisibility(View.VISIBLE); // ���ÿɼ�
		}
	}

	// �����ֻ��Ų�ѯobjectId
	private void selectObjectId() {
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("userPhone", userPhone);
		query.findObjects(this, new FindListener<User>() {

			@Override
			public void onSuccess(List<User> user) {
				if (user.size() > 0) {
					for (User u : user) {
						objectId = u.getObjectId();
					}
					// ��ȡͷ��url
					getUrl();
				}
			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});

	}

	// ����id��ѯ�û�
	private void getUrl() {
		BmobQuery<User> query = new BmobQuery<User>();
		query.getObject(this, objectId, new GetListener<User>() {

			@Override
			public void onSuccess(User user) {
				getImageUrl(user);
			}

			@Override
			public void onFailure(int arg0, String arg1) {

			}
		});
	}

	// ��ȡͷ��url����ʾ
	private void getImageUrl(User user) {
		String url = "";
		if (user.getUserHead() != null) {
			if (!TextUtils.isEmpty(user.getUserHead().getUrl())) {
				url = user.getUserHead().getUrl(); // ��ȡurl

				saveUrl(url);
				Glide.with(MyActivity.this).load(url).into(rivUserHead);
			}
		}
	}

	// ����ͷ��url
	private void saveUrl(String url) {
		pref = getSharedPreferences("userInfo", MODE_PRIVATE);
		// ���SharedPreferences.Editor����
		editor = pref.edit();
		editor.putString("userHead", url);
		editor.commit();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		// �û��ǳ�
		case R.id.tvUserName:
			if (userName == "") {

				intent = new Intent(MyActivity.this, LoginActivity.class);
				startActivity(intent);

			} else {
				intent = new Intent(MyActivity.this, UserInfoActivity.class);
				startActivity(intent);
			}
			break;

		// �˳���¼
		case R.id.btDropLogin:
			pref = getSharedPreferences("userInfo", MODE_PRIVATE);
			// ���SharedPreferences.Editor����
			editor = pref.edit();
			editor.putString("userPhone", null); // ��ձ�����û�����
			editor.putString("userName", null);
			editor.putString("userPass", null);
			editor.putString("userWork", null);
			editor.putInt("userAge", 0);
			editor.putString("userAddress", null);
			editor.putString("userSex", null);
			editor.putString("userHead", null);
			editor.commit(); // �ύ����
			if (ContantsOutLogin.ioutlogin != null)
				ContantsOutLogin.ioutlogin.onOutLogin();
			/* �����˳���½��ʼ */
			thread.start();
			// �˷���Ϊ�첽����
			EMClient.getInstance().logout(true, new EMCallBack() {
				@Override
				public void onSuccess() {
				}

				@Override
				public void onProgress(int progress, String status) {
				}

				@Override
				public void onError(int code, String message) {
					Toast.makeText(MyActivity.this, "�˳����������ʧ�ܣ�",
							Toast.LENGTH_SHORT).show();
				}
			});
			/* �����˳���½���� */
			Toast.makeText(MyActivity.this, "�˳���¼�ɹ�", Toast.LENGTH_SHORT)
					.show();
			break;
		// ����
		case R.id.tvMySet:
			intent = new Intent(MyActivity.this, SetActivity.class);
			startActivity(intent);
			break;
		// �������
		case R.id.tvCreateMyCircle:
			intent = new Intent(MyActivity.this, CreateCircleActivity.class);
			startActivity(intent);
			break;

		// �ҵ�����
		case R.id.tvMyDownLoad:
			intent = new Intent(MyActivity.this, DownloadActivity.class);
			startActivity(intent);
			break;
		case R.id.tvLiveService:
			intent = new Intent(MyActivity.this, LiveServiceActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
