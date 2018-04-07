package com.example.silencedance;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.GuideAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class GuideActivity extends Activity {

	private List<View> guideViews;
	private ViewPager vpGuide;
	private int[] guide_dots={R.id.ivGuide_dot1,R.id.ivGuide_dot2,R.id.ivGuide_dot3,R.id.ivGuide_dot4};
	private ImageView[] dots;
	private TextView ivGo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initGuideViews();
		initDots();
		setListeners();
	}

	private void initGuideViews() {
		vpGuide=(ViewPager) findViewById(R.id.vpGuide);
		guideViews=new ArrayList<View>();
		LayoutInflater layoutInflater=LayoutInflater.from(this);//寻找Layout下边的xml文件
		guideViews.add(layoutInflater.inflate(R.layout.view1_of_pager,null));
		guideViews.add(layoutInflater.inflate(R.layout.view2_of_pager,null));
		guideViews.add(layoutInflater.inflate(R.layout.view3_of_pager,null));
		/*需要获取id为tvGo的控件，所以需要抽取出view4_of_pager.xml页面*/
		View view4=layoutInflater.inflate(R.layout.view4_of_pager,null);//将xml页面转换成view
		guideViews.add(view4);
		ivGo=(TextView)view4.findViewById(R.id.ivGo);
		GuideAdapter guideAdapter=new GuideAdapter(guideViews);//实例自定义适配器
		vpGuide.setAdapter(guideAdapter);//添加适配器
	}

	/*设置圆点小图标*/
	private void initDots() {
		dots=new ImageView[4];
		for(int i=0;i<dots.length;i++){
			dots[i]=(ImageView) findViewById(guide_dots[i]);
		}
	}

	/*导航滑动*/
	private void setListeners() {
		vpGuide.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				for(int i=0;i<guide_dots.length;i++){
					if(position==i){
						dots[i].setImageResource(R.drawable.bg_point_selected);//当前页面圆点图标为选中的
					}
					else{
						dots[i].setImageResource(R.drawable.bg_point);//不是当前页面的圆点图标为未选中的
					}
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		ivGo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(GuideActivity.this,StartActivity.class);
				startActivity(intent);
				finish();//跳转过后销毁原页面，使点击返回键时不回返回该页面
			}
		});
	}
}
