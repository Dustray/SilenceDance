package com.example.silencedance;

import com.example.easemob.JoinCircle;
import com.google.zxing.WriterException;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.zxing.encoding.EncodingHandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CircleDetailActivity extends Activity {
	private EditText invitedPhone;
	private TextView circleID, circleName, circleDescription, circleOwner,
			circleMembers;
	private ImageView ivCode;
	private String groupid, groupname;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String result = msg.obj.toString();
			Toast.makeText(CircleDetailActivity.this, result,
					Toast.LENGTH_SHORT).show();
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

			// circleName.setText(group.getGroupName());
			circleDescription.setText(group.getDescription());
			circleOwner.setText(group.getOwner());
			circleMembers.setText("" + group.getAffiliationsCount());
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
				// if (e.toString().indexOf("invalid") >= 0) {
				// msg.obj = "舞圈号无效";
				// } else {
				msg.obj = "舞圈信息获取失败";
				// }
				Log.i("mytag", e.toString());
				handler.sendMessage(msg);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_detail);

		circleID = (TextView) findViewById(R.id.circleID);
		circleName = (TextView) findViewById(R.id.circleName);
		circleDescription = (TextView) findViewById(R.id.circleDescription);
		circleOwner = (TextView) findViewById(R.id.circleOwner);
		circleMembers = (TextView) findViewById(R.id.circleMembers);
		invitedPhone = (EditText) findViewById(R.id.invitedPhone);
		ivCode = (ImageView) findViewById(R.id.ivCode);

		Bundle bundle = this.getIntent().getExtras();
		groupid = bundle.getString("groupid");
		groupname = bundle.getString("groupname");
		circleID.setText(groupid);
		circleName.setText(groupname);
		mthread.start();

		generateQr();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.circle_detail, menu);
		return true;
	}

	public void inviteJoinCircle(View view) {
		new Thread() {
			public void run() {
				Message msg = new Message();
				JoinCircle jc = new JoinCircle();
				try {
					String[] ph = { invitedPhone.getText().toString() };

					// 群主加人调用此方法
					// EMClient.getInstance().groupManager().addUsersToGroup(groupid,
					// ph);//需异步处理
					// 私有群里，如果开放了群成员邀请，群成员邀请调用下面方法
					EMClient.getInstance().groupManager()
							.inviteUser(groupid, ph, null);// 需异步处理
					msg.obj = "邀请成功";
				} catch (HyphenateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					msg.obj = "加入失败，请核对舞圈号";

				} finally {
					handler.sendMessage(msg);

				}

			}
		}.start();

	}

	private void generateQr() {
		//
		String in = "*^&join&teamid?" + groupid + "&musicname?null";
		try {

			Bitmap qrcode = EncodingHandler.createQRCode(in, 400);
			ivCode.setImageBitmap(qrcode);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 第一个参数是想要生成的文本，第二个参数是生成二维码横向和纵向的宽度

	}

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
