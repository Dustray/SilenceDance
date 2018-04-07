package com.example.silencedance;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShowWeightActivity extends Activity {
	private TextView tvShowTall;
	String sex, weightInfo; // ��һ��Ϊ�û�ѡ����Ա𣬵ڶ���Ϊ�û����ؾ�����Ϣ
	double tall, weight, normalWeight, TW;// �û���ߣ����أ��û���߶�Ӧ�ı�׼���أ��õ��Լ���ߺ����صı���ֵ

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_weight);
		// ��ȡ����
		getDate();
		// ��ʼ���ؼ�
		init();
		// ��ʾ����
		showData();
	}

	private void getDate() {
		Bundle bundle = getIntent().getExtras();
		sex = bundle.getString("sex");
		tall = bundle.getDouble("tall");
		weight = bundle.getDouble("weight");
		if (sex.equals("��")) {
			normalWeight = (tall - 100) * 0.9; // ����߶�Ӧ����������

			// �������ؾ�����Ϣ
			calWeight();
		} else {
			normalWeight = (tall - 100) * 0.9 - 2.5;

			// �������ؾ�����Ϣ
			calWeight();
		}
	}

	// �������ؾ�����Ϣ
	private void calWeight() {
		if (weight >= normalWeight) {
			TW = (weight - normalWeight) / normalWeight;
			if (TW <= 0.1)
				weightInfo = "��������";
			else if (TW > 0.1 && TW < 0.2)
				weightInfo = "΢��";
			else if (TW > 0.2 && TW < 0.3)
				weightInfo = "��ȷ���";
			else if (TW > 0.3 && TW < 0.5)
				weightInfo = "�жȷ���";
			else
				weightInfo = "�ضȷ���";
		} else {
			TW = (normalWeight - weight) / normalWeight;
			if (TW <= 0.1)
				weightInfo = "��������";
			else if (TW > 0.1 && TW < 0.2)
				weightInfo = "΢��";
			else if (TW > 0.2 && TW < 0.3)
				weightInfo = "���ƫ��";
			else if (TW > 0.3 && TW < 0.5)
				weightInfo = "����ƫ��";
			else
				weightInfo = "Ӫ������";
		}
	}

	private void init() {
		tvShowTall = (TextView) findViewById(R.id.tvShowTall);
	}

	private void showData() {
		tvShowTall.setText("����һλ" + sex + "��" + "\n��������" + tall + "��������"
				+ weight + "\n����Ϊ" + weightInfo + "\n��ı�׼������" + normalWeight);
	}

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
