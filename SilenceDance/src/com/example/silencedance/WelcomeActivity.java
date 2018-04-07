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
	private boolean isFirst=true;//�����ж��Ƿ��ǵ�һ�����У����к��Ϊfalse
	
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
				pref=getSharedPreferences("isFirst",MODE_PRIVATE);//����SharedPreferences����
				isFirst=pref.getBoolean("isFirstIn",true);//�����һ�����У���isFirstInֵ���Զ���ȡ�ڶ�������ΪĬ��ֵ
				if(isFirst){//���Ϊtrue������if���
					intent=new Intent(WelcomeActivity.this,GuideActivity.class);
					Editor editor=pref.edit();
					editor.putBoolean("isFirstIn",false);//����isFirstInֵΪfalse
					editor.commit();//�ύ����
				}else{
					intent=new Intent(WelcomeActivity.this,StartActivity.class);//���Ϊfalse��˵�������Ѿ����й���ֱ����ת����ҳ��
				}
				startActivity(intent);
				finish();
			}
			
		});
	}

}
