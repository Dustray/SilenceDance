package com.example.silencedance;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Window;
import android.widget.TabHost;

public class HealthNewActivity extends TabActivity {

	

	private TabHost tabHost;// �������ؼ�
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȡ��������
		setContentView(R.layout.activity_health_new);

		//ʵ�����ؼ�
		init();
		

	}

	//ʵ�����ؼ�
	private void init() {
		tabHost=getTabHost();
		addTab("��ʳ",R.string.food,R.drawable.transparent,FoodActivity.class);
		addTab("����",R.string.keepFace,R.drawable.transparent,KeepFaceActivity.class);
		addTab("����",R.string.keepBody,R.drawable.transparent,KeepBodyActivity.class);
		addTab("����",R.string.thinBody,R.drawable.transparent,ThinBodyActivity.class);
		addTab("ҽ��",R.string.medicalCare,R.drawable.transparent,MedicalCareActivity.class);
		addTab("���",R.string.emotion,R.drawable.transparent,EmotionActivity.class);
	}
	
	/* ����ÿ��Tab����ʾ���� */
	private void addTab(String tag, int title_introduction, int title_icon,
			Class ActivityClass) {
		tabHost.addTab(tabHost
				.newTabSpec(tag)
				.setIndicator(getString(title_introduction),
						getResources().getDrawable(title_icon))
				.setContent(new Intent(this, ActivityClass)));
	}
}
