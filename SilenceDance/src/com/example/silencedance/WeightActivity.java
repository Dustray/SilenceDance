package com.example.silencedance;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class WeightActivity extends Activity implements OnClickListener {

	private RadioButton rbMan, rbWoman;
	private EditText etTall, etWeight;
	private Button btnWeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weight);

		// 初始化控件
		init();
		// 控件绑定点击
		bindClick();
	}

	// 初始化控件
	private void init() {
		rbMan = (RadioButton) findViewById(R.id.rbMan);
		rbWoman = (RadioButton) findViewById(R.id.rbWoman);
		etTall = (EditText) findViewById(R.id.etTall);
		etWeight = (EditText) findViewById(R.id.etWeight);
		btnWeight = (Button) findViewById(R.id.btnWeight);
	}

	// 控件绑定点击
	private void bindClick() {
		btnWeight.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnWeight:
			if (etTall == null || etTall.equals("") || etWeight == null
					|| etWeight.equals("")) {
				Toast.makeText(WeightActivity.this, "请输入身高和体重",
						Toast.LENGTH_SHORT).show();
			}
			String sex;
			if (rbMan.isChecked())
				sex = rbMan.getText().toString();
			else
				sex = rbWoman.getText().toString();
			double tall = Double.parseDouble(etTall.getText().toString());
			double weight = Double.parseDouble(etWeight.getText().toString());
			Intent intent = new Intent();
			intent.setClass(this, ShowWeightActivity.class);
			Bundle bundle = new Bundle(); // 传递身高体重值
			bundle.putString("sex", sex);
			bundle.putDouble("tall", tall);
			bundle.putDouble("weight", weight);
			intent.putExtras(bundle);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
