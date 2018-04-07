package com.example.silencedance;

import com.example.easemob.JoinCircle;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class JoinCircleActivity extends Activity {
	private EditText joinReason;
	private TextView circleID, circleName, circleDescription, circleOwner,
			circleMembers;
	private String groupid;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String result = msg.obj.toString();
			Toast.makeText(JoinCircleActivity.this, result, Toast.LENGTH_SHORT)
					.show();
			if (result == "舞圈号无效，请核对") {
				finish();
			}
		}
	};
	private Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			EMGroup group = (EMGroup) msg.obj;

			circleName.setText(group.getGroupName());
			circleDescription.setText(group.getDescription());
			circleOwner.setText(group.getOwner());
			circleMembers.setText("" + group.getAffiliationsCount());
			joinReason.setText("大家好，我是" + "/*我的名字*/");
		}
	};
	Thread mthread = new Thread() {
		public void run() {

			Message msg = new Message();
			try {
				EMGroup group = EMClient.getInstance().groupManager()
						.getGroupFromServer(groupid);
				msg.obj = group;
				mhandler.sendMessage(msg);
			} catch (HyphenateException e) {
				e.printStackTrace();
				if (e.toString().indexOf("invalid") >= 0) {
					msg.obj = "舞圈号无效，请核对";
				} else {
					msg.obj = "舞圈信息获取失败";
				}
				handler.sendMessage(msg);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_circle);

		circleID = (TextView) findViewById(R.id.circleID);
		circleName = (TextView) findViewById(R.id.circleName);
		circleDescription = (TextView) findViewById(R.id.circleDescription);
		circleOwner = (TextView) findViewById(R.id.circleOwner);
		circleMembers = (TextView) findViewById(R.id.circleMembers);
		joinReason = (EditText) findViewById(R.id.joinReason);

		Bundle bundle = this.getIntent().getExtras();
		groupid = bundle.getString("groupid");
		circleID.setText(groupid);
		mthread.start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.join_circle, menu);
		return true;
	}

	public void joinCircle(View view) {
		new Thread() {
			public void run() {
				Message msg = new Message();
				JoinCircle jc = new JoinCircle();
				try {
					jc.joinCircle(circleID.getText().toString(), joinReason
							.getText().toString());
					msg.obj = "加入成功";
				} catch (HyphenateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (e.toString().indexOf("joined") >= 0) {
						msg.obj = "您已在该舞圈中";
					} else {
						msg.obj = "加入失败，请核对舞圈号"+e;
					}
				} finally {
					handler.sendMessage(msg);

				}

			}
		}.start();

	}

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
