package com.example.silencedance;

import java.util.List;

import com.bumptech.glide.Glide;
import com.example.table.User;
import com.makeramen.roundedimageview.RoundedImageView;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class UserInfoActivity extends Activity implements OnClickListener {

	private TextView tvUserName, tvUserSex, tvUserAge, tvUserWork,
			tvUserBirthday, tvUserAddress;
	private Intent intent;
	private SharedPreferences pref;
	private Editor editor;
	private String userName, userSex, userWork, userBirthday, userAddress,
			userPhone, img_url;// 用户信息
	private int userAge;
	private Button btUpdateUserInfo;
	private RoundedImageView rivInfoHead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		Bmob.initialize(this, "e37ca30c22533836329999a7fcf077b8"); // 加载Bmob SDK

		// 初始化控件
		init();

		// 控件绑定点击事件
		bindClick();

		// 获取用户信息
		getUserInfo();

	}

	private void getUserInfo() {
		pref = getSharedPreferences("userInfo", MODE_PRIVATE);
		userPhone = pref.getString("userPhone", ""); // 取值，第二个参数为默认值
		img_url = pref.getString("userHead", "");
		if (img_url != "") {
			// 如果存了头像路径，显示头像
			Glide.with(UserInfoActivity.this).load(img_url).into(rivInfoHead);
		}

		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("userPhone", userPhone);
		query.findObjects(UserInfoActivity.this, new FindListener<User>() {

			@Override
			public void onSuccess(List<User> user) {
				if (user.size() > 0) {
					for (User u : user) {
						userName = u.getUserName();
						userSex = u.getUserSex();
						userWork = u.getUserWork();
						userAddress = u.getUserAddress();
						userAge = u.getUserAge();
						userBirthday = u.getUserBirthday();
						userSex = u.getUserSex();
					}
				}
				// 显示用户信息
				showUserInfo();
				// 保存用户信息
				saveUserInfo();
			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});
	}

	// 显示用户信息
	private void showUserInfo() {
		if (userName != "") {
			Log.i("wxy", "昵称" + userName + ",性别" + userSex + ",手机号" + userPhone);
			tvUserName.setText(userName);
			tvUserName.setTextColor(Color.rgb(0, 0, 0));
			tvUserSex.setText(userSex);
			tvUserSex.setTextColor(Color.rgb(0, 0, 0));
			tvUserAddress.setText(userAddress);
			tvUserAddress.setTextColor(Color.rgb(0, 0, 0));
			tvUserAge.setText("" + userAge);
			tvUserAge.setTextColor(Color.rgb(0, 0, 0));
			tvUserWork.setText(userWork);
			tvUserWork.setTextColor(Color.rgb(0, 0, 0));
			tvUserBirthday.setText(userBirthday);
			tvUserBirthday.setTextColor(Color.rgb(0, 0, 0));

		}
	}

	// 保存用户信息
	private void saveUserInfo() {
		editor = pref.edit();
		editor.putString("userName", userName);
		editor.putString("userPhone", userPhone);
		editor.putString("userWork", userWork);
		editor.putInt("userAge", userAge);
		editor.putString("userBirthday", userBirthday);
		editor.putString("userSex", userSex);
		editor.putString("userAddress", userAddress);
		editor.commit();
	}

	// 初始化控件
	private void init() {
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvUserAge = (TextView) findViewById(R.id.tvUserAge);
		tvUserSex = (TextView) findViewById(R.id.tvUserSex);
		tvUserWork = (TextView) findViewById(R.id.tvUserWork);
		tvUserBirthday = (TextView) findViewById(R.id.tvUserBirthday);
		tvUserAge = (TextView) findViewById(R.id.tvUserAge);
		tvUserAddress = (TextView) findViewById(R.id.tvUserAddress);
		btUpdateUserInfo = (Button) findViewById(R.id.btUpdateUserInfo);
		rivInfoHead = (RoundedImageView) findViewById(R.id.rivInfoHead);
	}

	// 控件绑定点击事件
	private void bindClick() {
		btUpdateUserInfo.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btUpdateUserInfo:
			intent = new Intent(UserInfoActivity.this, UserUpdateActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}

	// 返回上一个页面
	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
