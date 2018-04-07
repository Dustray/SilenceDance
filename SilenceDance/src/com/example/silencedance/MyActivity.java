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
			/* 环信退出登陆开始 */
			EMClient.getInstance().logout(true);// 此方法为同步方法，里面的参数 true 表示退出登录时解绑
												// GCM 或者小米推送的 token

			/* 环信退出登陆结束 */
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		Bmob.initialize(this, "8ef8a4752f48682eadb32d3c8c8e398f");// 初始化Bmob
		// 初始化控件
		init();

		// 判断是否登录并显示
		judgeLogin();

		// 绑定点击事件
		bindClick();

	}

	// 初始化控件
	private void init() {
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvMySet = (TextView) findViewById(R.id.tvMySet);
		tvCreateMyCircle = (TextView) findViewById(R.id.tvCreateMyCircle);
		tvMyDownLoad = (TextView) findViewById(R.id.tvMyDownLoad);
		tvLiveService = (TextView) findViewById(R.id.tvLiveService);
		btDropLogin = (Button) findViewById(R.id.btDropLogin);
		rivUserHead = (RoundedImageView) findViewById(R.id.rivUserHead);
	}

	// 绑定点击事件
	private void bindClick() {
		tvUserName.setOnClickListener(this);
		tvMySet.setOnClickListener(this);
		tvCreateMyCircle.setOnClickListener(this);
		tvMyDownLoad.setOnClickListener(this);
		tvLiveService.setOnClickListener(this);
		btDropLogin.setOnClickListener(this);
	}

	// 判断是否登录并显示
	private void judgeLogin() {
		// 获得SharedPreferences对象
		pref = getSharedPreferences("userInfo", MODE_PRIVATE);
		userName = pref.getString("userName", "");
		userPhone = pref.getString("userPhone", userPhone);
		if (userName != "") {

			// 根据手机号查询objectId
			selectObjectId();

			tvUserName.setText(userName);
			btDropLogin.setVisibility(View.VISIBLE); // 设置可见
		}
	}

	// 根据手机号查询objectId
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
					// 获取头像url
					getUrl();
				}
			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});

	}

	// 根据id查询用户
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

	// 获取头像url并显示
	private void getImageUrl(User user) {
		String url = "";
		if (user.getUserHead() != null) {
			if (!TextUtils.isEmpty(user.getUserHead().getUrl())) {
				url = user.getUserHead().getUrl(); // 获取url

				saveUrl(url);
				Glide.with(MyActivity.this).load(url).into(rivUserHead);
			}
		}
	}

	// 保存头像url
	private void saveUrl(String url) {
		pref = getSharedPreferences("userInfo", MODE_PRIVATE);
		// 获得SharedPreferences.Editor对象
		editor = pref.edit();
		editor.putString("userHead", url);
		editor.commit();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		// 用户昵称
		case R.id.tvUserName:
			if (userName == "") {

				intent = new Intent(MyActivity.this, LoginActivity.class);
				startActivity(intent);

			} else {
				intent = new Intent(MyActivity.this, UserInfoActivity.class);
				startActivity(intent);
			}
			break;

		// 退出登录
		case R.id.btDropLogin:
			pref = getSharedPreferences("userInfo", MODE_PRIVATE);
			// 获得SharedPreferences.Editor对象
			editor = pref.edit();
			editor.putString("userPhone", null); // 清空保存的用户数据
			editor.putString("userName", null);
			editor.putString("userPass", null);
			editor.putString("userWork", null);
			editor.putInt("userAge", 0);
			editor.putString("userAddress", null);
			editor.putString("userSex", null);
			editor.putString("userHead", null);
			editor.commit(); // 提交数据
			if (ContantsOutLogin.ioutlogin != null)
				ContantsOutLogin.ioutlogin.onOutLogin();
			/* 环信退出登陆开始 */
			thread.start();
			// 此方法为异步方法
			EMClient.getInstance().logout(true, new EMCallBack() {
				@Override
				public void onSuccess() {
				}

				@Override
				public void onProgress(int progress, String status) {
				}

				@Override
				public void onError(int code, String message) {
					Toast.makeText(MyActivity.this, "退出聊天服务器失败！",
							Toast.LENGTH_SHORT).show();
				}
			});
			/* 环信退出登陆结束 */
			Toast.makeText(MyActivity.this, "退出登录成功", Toast.LENGTH_SHORT)
					.show();
			break;
		// 设置
		case R.id.tvMySet:
			intent = new Intent(MyActivity.this, SetActivity.class);
			startActivity(intent);
			break;
		// 创建舞队
		case R.id.tvCreateMyCircle:
			intent = new Intent(MyActivity.this, CreateCircleActivity.class);
			startActivity(intent);
			break;

		// 我的下载
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
