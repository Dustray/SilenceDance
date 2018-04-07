package com.example.silencedance;

import com.example.table.UserFeedBack;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedBackActivity extends Activity implements OnClickListener {

	private EditText etFeedBack;
	private Button btnSendFeedBack;
	private String userName, userFBInfo;
	private SharedPreferences pref;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);

		Bmob.initialize(this, "e37ca30c22533836329999a7fcf077b8"); // 加载Bmob SDK
		init();

		bindClick();
	}

	private void init() {
		etFeedBack = (EditText) findViewById(R.id.etFeedBack);
		btnSendFeedBack = (Button) findViewById(R.id.btnSendFeedBack);
	}

	private void bindClick() {
		btnSendFeedBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnSendFeedBack:
			send();
			break;

		default:
			break;
		}
	}

	private void send() {
		// 获得SharedPreferences对象
		pref = getSharedPreferences("userInfo", MODE_PRIVATE);
		userName = pref.getString("userName", "");
		userFBInfo = etFeedBack.getText().toString();
		UserFeedBack userFeedBack = new UserFeedBack();
		userFeedBack.setUserName(userName);
		userFeedBack.setUserFeedBack(userFBInfo);
		userFeedBack.save(FeedBackActivity.this, new SaveListener() {

			@Override
			public void onSuccess() {
				Toast.makeText(FeedBackActivity.this, "反馈成功",
						Toast.LENGTH_SHORT).show();
				// intent=new Intent(FeedBackActivity.this,SetActivity.class);
				// startActivity(intent);
				finish();

			}

			@Override
			public void onFailure(int arg0, String arg1) {

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
