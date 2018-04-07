package com.example.silencedance;

import com.example.easemob.JoinCircle;
import com.example.entity.NowPlayInfo;
import com.example.tools.StringParsing;
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
import android.widget.TextView;
import android.widget.Toast;

public class ScanResultActivity extends Activity {

	private TextView tvScanResult;
	String circleID;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String result = msg.obj.toString();
			Toast.makeText(ScanResultActivity.this, result, Toast.LENGTH_SHORT)
					.show();
			if (result == "����ʧ��") {
				finish();
			} else if (result == "����ɹ�" || result == "�����ڸ���Ȧ��") {
				// Intent intent = new Intent(ScanResultActivity.this,
				// PlayMusicActivity.class);
				// intent.putExtra("_ids", _ids);
				// intent.putExtra("_titles", _titles);
				// intent.putExtra("_artists", _artists);
				// intent.putExtra("position", position);
				//
				// NowPlayInfo.setNOW_ids(_ids); // ���浱ǰ���ŵ�������Ϣ
				// NowPlayInfo.setNOW_titles(_titles);
				// NowPlayInfo.setNOW_artists(_artists);
				// NowPlayInfo.setNOW_position(position);
				//
				// startActivity(intent);
				finish();
			}
		}
	};

	public void joinCircle() {
		new Thread() {
			public void run() {
				Message msg = new Message();
				JoinCircle jc = new JoinCircle();
				try {
					jc.joinCircle(circleID, "��Һ�");
					msg.obj = "����ɹ�";
				} catch (HyphenateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (e.toString().indexOf("joined") >= 0) {
						msg.obj = "�����ڸ���Ȧ��";
					} else {
						msg.obj = "����ʧ��";
					}
				} finally {
					handler.sendMessage(msg);

				}

			}
		}.start();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_result);

		init();

		// ��ʾɨ����
		showScanResult();
	}

	private void init() {
		tvScanResult = (TextView) findViewById(R.id.tvScanResult);
	}

	// ��ʾɨ����
	private void showScanResult() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String result = bundle.getString("result");
		tvScanResult.setText(result);
		deal(result);
	}

	public void deal(String result) {

		// "*^&join&teamid?146025545484&musicname?Сƻ��";
		StringParsing sp = new StringParsing();
		String orderType = sp.getTypeByOrder(result);

		circleID = sp.getTeamIDByOrder(result);
		String circleMusicName = sp.getMusicNameByOrder(result);
		if (orderType.equals("join")) {
			Intent intent = new Intent(ScanResultActivity.this,
					JoinCircleActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("groupid", circleID);
			intent.putExtras(bundle);
			startActivity(intent);
		} else if (orderType.equals("joinandstart")) {
			joinCircle();
		} else {
			Toast.makeText(this, "��ά����Ч", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
}
