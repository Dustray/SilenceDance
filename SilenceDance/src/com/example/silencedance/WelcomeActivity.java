package com.example.silencedance;

import com.hyphenate.chat.EMClient;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

public class WelcomeActivity extends Activity {

	private Intent intent;
	private SharedPreferences pref;
	private boolean isFirst=true;//用于判断是否是第一次运行，运行后变为false
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		
		RelativeLayout layoutWelcome=(RelativeLayout) findViewById(R.id.layoutwelcome);
		AlphaAnimation alphaAnimation=new AlphaAnimation(0.1f,1.0f);
		alphaAnimation.setDuration(3000);
		layoutWelcome.startAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				judgeIntent();
			}
			private void judgeIntent() {
				pref=getSharedPreferences("isFirst",MODE_PRIVATE);//创建SharedPreferences对象
				isFirst=pref.getBoolean("isFirstIn",true);//如果第一次运行，无isFirstIn值，自动获取第二个参数为默认值
				if(isFirst){//如果为true，进入if语句
					intent=new Intent(WelcomeActivity.this,GuideActivity.class);
					Editor editor=pref.edit();
					editor.putBoolean("isFirstIn",false);//保存isFirstIn值为false
					editor.commit();//提交数据
				}else{
					intent=new Intent(WelcomeActivity.this,StartActivity.class);//如果为false，说明程序已经运行过，直接跳转到主页面
				}
				startActivity(intent);
				finish();
			}
			
		});
	}

}
