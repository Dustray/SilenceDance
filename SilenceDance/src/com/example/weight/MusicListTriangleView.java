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
		// ��������
		Paint p = new Paint();
		p.setColor(Color.RED);// ���ú�ɫ
		super.onDraw(canvas);
		// canvas.drawText("�������Σ�", 10, 200, p);
		// �������������,����Ի�����������
		Path path = new Path();
		path.moveTo(0, 0);// �˵�Ϊ����ε����
		path.lineTo(200, 0);
		path.lineTo(200, 200);
		path.close(); // ʹ��Щ�㹹�ɷ�յĶ����
		canvas.drawPath(path, p);
	}

}