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
		LayoutInflater layoutInflater=LayoutInflater.from(this);//Ѱ��Layout�±ߵ�xml�ļ�
		guideViews.add(layoutInflater.inflate(R.layout.view1_of_pager,null));
		guideViews.add(layoutInflater.inflate(R.layout.view2_of_pager,null));
		guideViews.add(layoutInflater.inflate(R.layout.view3_of_pager,null));
		/*��Ҫ��ȡidΪtvGo�Ŀؼ���������Ҫ��ȡ��view4_of_pager.xmlҳ��*/
		View view4=layoutInflater.inflate(R.layout.view4_of_pager,null);//��xmlҳ��ת����view
		guideViews.add(view4);
		ivGo=(TextView)view4.findViewById(R.id.ivGo);
		GuideAdapter guideAdapter=new GuideAdapter(guideViews);//ʵ���Զ���������
		vpGuide.setAdapter(guideAdapter);//���������
	}

	/*����Բ��Сͼ��*/
	private void initDots() {
		dots=new ImageView[4];
		for(int i=0;i<dots.length;i++){
			dots[i]=(ImageView) findViewById(guide_dots[i]);
		}
	}

	/*��������*/
	private void setListeners() {
		vpGuide.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				for(int i=0;i<guide_dots.length;i++){
					if(position==i){
						dots[i].setImageResource(R.drawable.bg_point_selected);//��ǰҳ��Բ��ͼ��Ϊѡ�е�
					}
					else{
						dots[i].setImageResource(R.drawable.bg_point);//���ǵ�ǰҳ���Բ��ͼ��Ϊδѡ�е�
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
				finish();//��ת��������ԭҳ�棬ʹ������ؼ�ʱ���ط��ظ�ҳ��
			}
		});
	}
}
