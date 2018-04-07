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
			if (result == "��Ȧ����Ч����˶�") {
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
				// msg.obj = "��Ȧ����Ч";
				// } else {
				msg.obj = "��Ȧ��Ϣ��ȡʧ��";
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

					// Ⱥ�����˵��ô˷���
					// EMClient.getInstance().groupManager().addUsersToGroup(groupid,
					// ph);//���첽����
					// ˽��Ⱥ����������Ⱥ��Ա���룬Ⱥ��Ա����������淽��
					EMClient.getInstance().groupManager()
							.inviteUser(groupid, ph, null);// ���첽����
					msg.obj = "����ɹ�";
				} catch (HyphenateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					msg.obj = "����ʧ�ܣ���˶���Ȧ��";

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
		}// ��һ����������Ҫ���ɵ��ı����ڶ������������ɶ�ά����������Ŀ���

	}

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}