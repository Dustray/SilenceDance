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

	private String phone, pass;// ����ת�����ַ������ֻ��ź�����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// StartActivity.instance.finish();// �ر�StartActivity
		Bmob.initialize(this, "e37ca30c22533836329999a7fcf077b8"); // ����Bmob SDK

		// ��ʼ���ؼ�
		init();

		// �󶨵���¼�
		bindClick();

	}

	// ��ʼ���ؼ�
	private void init() {
		etPhone = (EditText) findViewById(R.id.etPhone);
		etPass = (EditText) findViewById(R.id.etPass);
		tvToegister = (TextView) findViewById(R.id.tvToegister);
		btLogin = (Button) findViewById(R.id.btLogin);
	}

	// �󶨵���¼�
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

	// �����¼�ύ����
	private void submit() {
		phone = etPhone.getText().toString();
		pass = etPass.getText().toString();
		if ((TextUtils.isEmpty(phone) && phone.equals(""))
				|| (TextUtils.isEmpty(pass) && equals(""))) {
			Toast.makeText(LoginActivity.this, "�û��������벻��Ϊ��", Toast.LENGTH_SHORT)
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
					// ���SharedPreferences����
					pref = getSharedPreferences("userInfo", MODE_PRIVATE);// �����ݴ�ŵ���ΪuserInfo���ĵ���

					for (User u : user) { // ��ѯ����ֻ��һ�����ݣ�������ʹ��List��װ����Ҫ��������
						// ���SharedPreferences.Editor����
						editor = pref.edit();
						editor.putString("userName", u.getUserName());
						editor.putString("userPhone", phone);
						editor.putString("userPass", pass);
					}
					editor.commit();
					if (ContantsLogin.ilogin != null)
						ContantsLogin.ilogin.onLogin();// ���ýӿ�ǰ ���ж� �Ƿ�Ϊ�� ����ͻ�ִ��
														// new �ӿ�ʱ��Ĵ���
					/* �����û���½��ʼ */
					LoginHuanxin();
					/* �����û���½���� */

					Toast.makeText(LoginActivity.this, "��¼�ɹ�",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(LoginActivity.this, "�û��������벻��ȷ",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(LoginActivity.this, "��¼ʧ��", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	/* �����û���½��ʼ */
	private void LoginHuanxin() {
		EMClient.getInstance().login(phone, pass, new EMCallBack() {// �ص�
					@Override
					public void onSuccess() {
						EMClient.getInstance().groupManager().loadAllGroups();
						EMClient.getInstance().chatManager()
								.loadAllConversations();
						// Log.d("main", "��¼����������ɹ���");
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						Toast.makeText(LoginActivity.this, "��¼���������ʧ�ܣ�",
								Toast.LENGTH_SHORT).show();
						// Log.d("main", "��¼���������ʧ�ܣ�");
					}
				});
	}

	/* �����û���½���� */

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
