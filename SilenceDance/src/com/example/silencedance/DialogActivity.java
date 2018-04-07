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
 *	功能描述：弹出Activity界面
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
	 * 初始化组件
	 */
	private void initView(){
		//得到布局组件对象并设置监听事件
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
			startActivityForResult(intent, 0); // 能获取回传数据的跳转方式
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
