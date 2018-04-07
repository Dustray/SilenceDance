package com.example.silencedance;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.ActivityAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CircleActivity extends Activity implements OnClickListener {

	private ViewPager nsViewPager;
	private List<View> mViews = new ArrayList<View>();
	private ActivityAdapter adapter = new ActivityAdapter(mViews);
	private TextView tvCircleTalk, tvCircleDynamic;
	private ImageButton ibCircleTalk, ibCircleDynamic;
	private LinearLayout tab_circleTalk, tab_circleDynamic;
	private LocalActivityManager manager;
	private Intent intentTalk, intentDynamic;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle);

		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		initViews();
		initEvents();
		

	}

	private void initViews() {
		nsViewPager = (ViewPager) findViewById(R.id.nsViewPager);

		tvCircleTalk = (TextView) findViewById(R.id.tvCircleTalk);
		tvCircleDynamic = (TextView) findViewById(R.id.tvCircleDynamic);

		ibCircleTalk = (ImageButton) findViewById(R.id.ibCircleTalk);
		ibCircleDynamic = (ImageButton) findViewById(R.id.ibCircleDynamic);

		tab_circleTalk = (LinearLayout) findViewById(R.id.tab_circleTalk);
		tab_circleDynamic = (LinearLayout) findViewById(R.id.tab_circleDynamic);

		tab_circleTalk.setOnClickListener(this);
		tab_circleDynamic.setOnClickListener(this);

		LayoutInflater mInflater = LayoutInflater.from(this);

		intentTalk = new Intent(CircleActivity.this, CircleTalkActivity.class);
		View tab01 = manager.startActivity("viewTalk", intentTalk)
				.getDecorView();

		intentDynamic = new Intent(CircleActivity.this,
				CircleDynamicActivity.class);
		View tab02 = manager.startActivity("viewDynamic", intentDynamic)
				.getDecorView();

		mViews.add(tab01);
		mViews.add(tab02);

		nsViewPager.setAdapter(adapter);// ≈‰÷√  ≈‰∆˜
	}

	private void resetTextView() {
		tvCircleTalk.setTextColor(Color.rgb(0, 0, 0));
		tvCircleDynamic.setTextColor(Color.rgb(0, 0, 0));

		ibCircleTalk.setImageResource(R.drawable.talked);
		ibCircleDynamic.setImageResource(R.drawable.dynamiced);
	}

	@Override
	public void onClick(View view) {
		resetTextView();
		switch (view.getId()) {
		case R.id.tab_circleTalk:
			nsViewPager.setCurrentItem(0);
			tvCircleTalk.setTextColor(Color.rgb(255, 255, 255));
			ibCircleTalk.setImageResource(R.drawable.talking);
			break;
		case R.id.tab_circleDynamic:
			nsViewPager.setCurrentItem(1);
			tvCircleDynamic.setTextColor(Color.rgb(255, 255, 255));
			ibCircleDynamic.setImageResource(R.drawable.dynamicing);
			break;
		default:
			break;
		}
	}

	// ª¨∂Ø ¬º˛
	private void initEvents() {
		nsViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				resetTextView();
				int currentItem = nsViewPager.getCurrentItem();
				switch (currentItem) {
				case 0:
					tvCircleTalk.setTextColor(Color.rgb(255, 255, 255));
					break;
				case 1:
					tvCircleDynamic.setTextColor(Color.rgb(255, 255, 255));
					break;

				default:
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
}
