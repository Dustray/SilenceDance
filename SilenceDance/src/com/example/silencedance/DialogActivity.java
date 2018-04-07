package com.example.silencedance;


import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * @author yangyu
 *	��������������Activity����
 */
public class DialogActivity extends Activity implements OnClickListener{
	private LinearLayout layout01,llScan;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog);

		initView();
	}

	/**
	 * ��ʼ�����
	 */
	private void initView(){
		//�õ���������������ü����¼�
		layout01 = (LinearLayout)findViewById(R.id.llSearch);
		llScan = (LinearLayout)findViewById(R.id.llScan);

		layout01.setOnClickListener(this);
		llScan.setOnClickListener(this);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.llScan:
			intent= new Intent(DialogActivity.this,
					CaptureActivity.class);
			startActivityForResult(intent, 0); // �ܻ�ȡ�ش����ݵ���ת��ʽ
			break;
		case R.id.llSearch:
			intent= new Intent(DialogActivity.this,
					SearchCircleActivity.class);
			 startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			String result = data.getExtras().getString("result");
			Intent intent=new Intent( DialogActivity.this,ScanResultActivity.class);
			intent.putExtra("result",result);
			startActivity(intent);
			finish();
		}
	}
	
}
