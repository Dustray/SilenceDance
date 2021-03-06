package com.example.alert;


import com.example.silencedance.ClockAlertActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Toast.makeText(context, "你设置的闹铃时间到了", Toast.LENGTH_LONG).show();
		intent.setClass(context, ClockAlertActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   //必须增加，否则报错
		context.startActivity(intent);
	}
}