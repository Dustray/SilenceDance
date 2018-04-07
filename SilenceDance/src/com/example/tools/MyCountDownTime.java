package com.example.tools;

import com.example.silencedance.R;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;
/**
 * 用于验证码倒计时
 * @author Sunday
 *
 */
public class MyCountDownTime extends CountDownTimer {

	private Button btn;
	private String message;

	public MyCountDownTime(long millisInFuture, long countDownInterval,
			Button btn, String message) {
		super(millisInFuture, countDownInterval);
		this.btn=btn;
		this.message =message;
	}

	@Override
	public void onFinish() {
		btn.setEnabled(true);
		btn.setText(message);
		btn.setBackgroundResource(R.drawable.btn_long_white);
		btn.setTextColor(Color.RED);

	}

	@Override
	public void onTick(long arg0) {
		btn.setEnabled(false);
		btn.setText("倒计时(" + arg0 / 1000 + ")");
	}

}
