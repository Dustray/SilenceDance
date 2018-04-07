package com.example.silencedance;

import java.util.List;

import com.example.contants.ContantsLogin;
import com.example.table.User;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	private Intent intent;
	private TextView tvToegister;
	private EditText etPhone, etPass;
	private Button btLogin;

	private SharedPreferences pref;
	private Editor editor;

	private String phone, pass;// 保存转换成字符串的手机号和密码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// StartActivity.instance.finish();// 关闭StartActivity
		Bmob.initialize(this, "e37ca30c22533836329999a7fcf077b8"); // 加载Bmob SDK

		// 初始化控件
		init();

		// 绑定点击事件
		bindClick();

	}

	// 初始化控件
	private void init() {
		etPhone = (EditText) findViewById(R.id.etPhone);
		etPass = (EditText) findViewById(R.id.etPass);
		tvToegister = (TextView) findViewById(R.id.tvToegister);
		btLogin = (Button) findViewById(R.id.btLogin);
	}

	// 绑定点击事件
	private void bindClick() {
		tvToegister.setOnClickListener(this);
		btLogin.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tvToegister:
			intent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			finish();

			break;

		case R.id.btLogin:
			submit();
		default:
			break;
		}
	}

	// 点击登录提交数据
	private void submit() {
		phone = etPhone.getText().toString();
		pass = etPass.getText().toString();
		if ((TextUtils.isEmpty(phone) && phone.equals(""))
				|| (TextUtils.isEmpty(pass) && equals(""))) {
			Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("userPhone", phone);
		query.addWhereEqualTo("userPass", pass);
		query.findObjects(LoginActivity.this, new FindListener<User>() {

			@Override
			public void onSuccess(List<User> user) {
				if (user.size() > 0) {
					// 获得SharedPreferences对象
					pref = getSharedPreferences("userInfo", MODE_PRIVATE);// 将内容存放到名为userInfo的文档内

					for (User u : user) { // 查询出来只有一条数据，但还是使用List封装，需要遍历出来
						// 获得SharedPreferences.Editor对象
						editor = pref.edit();
						editor.putString("userName", u.getUserName());
						editor.putString("userPhone", phone);
						editor.putString("userPass", pass);
					}
					editor.commit();
					if (ContantsLogin.ilogin != null)
						ContantsLogin.ilogin.onLogin();// 调用接口前 先判断 是否为空 这里就会执行
														// new 接口时候的代码
					/* 环信用户登陆开始 */
					LoginHuanxin();
					/* 环信用户登陆结束 */

					Toast.makeText(LoginActivity.this, "登录成功",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(LoginActivity.this, "用户名或密码不正确",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	/* 环信用户登陆开始 */
	private void LoginHuanxin() {
		EMClient.getInstance().login(phone, pass, new EMCallBack() {// 回调
					@Override
					public void onSuccess() {
						EMClient.getInstance().groupManager().loadAllGroups();
						EMClient.getInstance().chatManager()
								.loadAllConversations();
						// Log.d("main", "登录聊天服务器成功！");
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						Toast.makeText(LoginActivity.this, "登录聊天服务器失败！",
								Toast.LENGTH_SHORT).show();
						// Log.d("main", "登录聊天服务器失败！");
					}
				});
	}

	/* 环信用户登陆结束 */

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
