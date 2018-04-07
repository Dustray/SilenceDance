package com.example.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MusicListTriangleView extends ImageView {

	public MusicListTriangleView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MusicListTriangleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MusicListTriangleView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 创建画笔
		Paint p = new Paint();
		p.setColor(Color.RED);// 设置红色
		super.onDraw(canvas);
		// canvas.drawText("画三角形：", 10, 200, p);
		// 绘制这个三角形,你可以绘制任意多边形
		Path path = new Path();
		path.moveTo(0, 0);// 此点为多边形的起点
		path.lineTo(200, 0);
		path.lineTo(200, 200);
		path.close(); // 使这些点构成封闭的多边形
		canvas.drawPath(path, p);
	}

}