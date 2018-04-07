package com.example.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class noSlideViewPager extends ViewPager {
	private boolean noSlide = false;

	public noSlideViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public noSlideViewPager(Context context) {
		super(context);
	}

	public void setNoSlide(boolean noSlide) {
		this.noSlide = noSlide;
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (noSlide)
			return super.onTouchEvent(arg0);
		else
			return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (noSlide)
			return false;
		else
			return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item);
	}

	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		super.setCurrentItem(item, smoothScroll);
	}
}
