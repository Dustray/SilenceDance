package com.example.silencedance;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Window;
import android.widget.TabHost;

public class HealthNewActivity extends TabActivity {

	

	private TabHost tabHost;// 导航栏控件
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 取消标题栏
		setContentView(R.layout.activity_health_new);

		//实例化控件
		init();
		

	}

	//实例化控件
	private void init() {
		tabHost=getTabHost();
		addTab("饮食",R.string.food,R.drawable.transparent,FoodActivity.class);
		addTab("保养",R.string.keepFace,R.drawable.transparent,KeepFaceActivity.class);
		addTab("养生",R.string.keepBody,R.drawable.transparent,KeepBodyActivity.class);
		addTab("瘦身",R.string.thinBody,R.drawable.transparent,ThinBodyActivity.class);
		addTab("医护",R.string.medicalCare,R.drawable.transparent,MedicalCareActivity.class);
		addTab("情感",R.string.emotion,R.drawable.transparent,EmotionActivity.class);
	}
	
	/* 定义每个Tab的显示内容 */
	private void addTab(String tag, int title_introduction, int title_icon,
			Class ActivityClass) {
		tabHost.addTab(tabHost
				.newTabSpec(tag)
				.setIndicator(getString(title_introduction),
						getResources().getDrawable(title_icon))
				.setContent(new Intent(this, ActivityClass)));
	}
}
