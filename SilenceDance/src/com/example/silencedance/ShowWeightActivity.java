package com.example.silencedance;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShowWeightActivity extends Activity {
	private TextView tvShowTall;
	String sex, weightInfo; // 第一个为用户选择的性别，第二个为用户体重具体信息
	double tall, weight, normalWeight, TW;// 用户身高，体重，用户身高对应的标准体重，用的自己身高和体重的比例值

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_weight);
		// 获取数据
		getDate();
		// 初始化控件
		init();
		// 显示数据
		showData();
	}

	private void getDate() {
		Bundle bundle = getIntent().getExtras();
		sex = bundle.getString("sex");
		tall = bundle.getDouble("tall");
		weight = bundle.getDouble("weight");
		if (sex.equals("男")) {
			normalWeight = (tall - 100) * 0.9; // 该身高对应的正常体重

			// 计算体重具体信息
			calWeight();
		} else {
			normalWeight = (tall - 100) * 0.9 - 2.5;

			// 计算体重具体信息
			calWeight();
		}
	}

	// 计算体重具体信息
	private void calWeight() {
		if (weight >= normalWeight) {
			TW = (weight - normalWeight) / normalWeight;
			if (TW <= 0.1)
				weightInfo = "正常体重";
			else if (TW > 0.1 && TW < 0.2)
				weightInfo = "微胖";
			else if (TW > 0.2 && TW < 0.3)
				weightInfo = "轻度肥胖";
			else if (TW > 0.3 && TW < 0.5)
				weightInfo = "中度肥胖";
			else
				weightInfo = "重度肥胖";
		} else {
			TW = (normalWeight - weight) / normalWeight;
			if (TW <= 0.1)
				weightInfo = "正常体重";
			else if (TW > 0.1 && TW < 0.2)
				weightInfo = "微轻";
			else if (TW > 0.2 && TW < 0.3)
				weightInfo = "轻度偏瘦";
			else if (TW > 0.3 && TW < 0.5)
				weightInfo = "明显偏瘦";
			else
				weightInfo = "营养不良";
		}
	}

	private void init() {
		tvShowTall = (TextView) findViewById(R.id.tvShowTall);
	}

	private void showData() {
		tvShowTall.setText("你是一位" + sex + "性" + "\n你的身高是" + tall + "，体重是"
				+ weight + "\n体重为" + weightInfo + "\n你的标准体重是" + normalWeight);
	}

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
