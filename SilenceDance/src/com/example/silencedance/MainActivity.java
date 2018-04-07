package com.example.silencedance;

import com.example.entity.NowPlayInfo;
import com.makeramen.roundedimageview.RoundedImageView;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends Activity implements OnClickListener {
	private Intent intent;
	private RoundedImageView rmvNowMisic;
	private ImageView ivMore, ivWebVideo, ivMusicDownload, tempImageView;

	private SharedPreferences pref;
	private ViewFlipper vfShowPicture; // 主页面上方闪屏
	private int[] resId = { R.drawable.cyclic_1, R.drawable.cyclic_2,
			R.drawable.cyclic_3, R.drawable.cyclic_4, R.drawable.cyclic_5 };
	private float X;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 初始化控件
		init();

		// 绑定点击事件
		bindClick();

		// 设置滑动
		setSlide();

	}

	// 初始化控件
	private void init() {
		vfShowPicture = (ViewFlipper) findViewById(R.id.vfShowPicture);
		rmvNowMisic = (RoundedImageView) findViewById(R.id.rmvNowMisic);
		ivMore = (ImageView) findViewById(R.id.ivMore);
		ivWebVideo = (ImageView) findViewById(R.id.ivWebVideo);
		ivMusicDownload = (ImageView) findViewById(R.id.ivMusicDownload);

		tempImageView = (ImageView) findViewById(R.id.tempImageView);
	}

	// 绑定点击事件
	private void bindClick() {

		rmvNowMisic.setOnClickListener(this);
		ivMore.setOnClickListener(this);
		ivWebVideo.setOnClickListener(this);
		vfShowPicture.setOnClickListener(this);
		ivMusicDownload.setOnClickListener(this);
		tempImageView.setOnClickListener(this);
	}

	// 设置滑动
	private void setSlide() {
		// 动态导入的方式为ViewFlipper加入自View
		for (int i = 0; i < resId.length; i++) {
			vfShowPicture.addView(getImageView(resId[i]));
		}

		/* 自动导航 */
		vfShowPicture.setInAnimation(this, R.anim.right_in);// 进入时图片效果
		vfShowPicture.setOutAnimation(this, R.anim.left_off);// 出去时图片效果
		vfShowPicture.setFlipInterval(3000);// 视图切换的时间间隔（移动时间+停留时间）
		vfShowPicture.startFlipping();// 开始播放

	}

	// 获取图片
	private ImageView getImageView(int resId) {
		ImageView image = new ImageView(this);
		image.setBackgroundResource(resId);// 全屏显示图片
		return image;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.rmvNowMisic:

			if (NowPlayInfo.getNOW_ids() == null) {
				intent = new Intent(MainActivity.this, MusicListActivity.class);

			} else {
				intent = new Intent(MainActivity.this, PlayMusicActivity.class);
				intent.putExtra("_ids", NowPlayInfo.getNOW_ids());
				Log.i("MainActivity", "" + NowPlayInfo.getNOW_ids());
				intent.putExtra("_titles", NowPlayInfo.getNOW_artists());
				intent.putExtra("_artists", NowPlayInfo.getNOW_titles());
				intent.putExtra("position", NowPlayInfo.getNOW_position());
			}
			startActivity(intent);
			break;

		case R.id.ivMore:
			intent = new Intent(MainActivity.this, MusicListActivity.class);
			startActivity(intent);
			break;

		case R.id.ivWebVideo:
			intent = new Intent(MainActivity.this, VideoLinkItemActivity.class);
			startActivity(intent);
			break;

		// ViewFlipper点击事件
		case R.id.vfShowPicture:
			if (vfShowPicture.getDisplayedChild() == 0) {
				intent = new Intent(MainActivity.this, WebActivity.class);
				intent.putExtra("_weblink",
						"http://v.youku.com/v_show/id_XMTUyNTkyNjA4MA==.html?from=s1.8-3-1.1");
				startActivity(intent);
			} else if (vfShowPicture.getDisplayedChild() == 1) {
				intent = new Intent(MainActivity.this, WebActivity.class);
				intent.putExtra("_weblink",
						"http://v.youku.com/v_show/id_XMTM5NDk3OTc5Mg==.html?from=s1.8-3-1.1");
				startActivity(intent);
			} else if (vfShowPicture.getDisplayedChild() == 2) {
				intent = new Intent(MainActivity.this, WebActivity.class);
				intent.putExtra("_weblink",
						"http://v.youku.com/v_show/id_XMTYxNTA2MjgyOA==.html?from=s1.8-3-1.1");
				startActivity(intent);
			} else {
				intent = new Intent(MainActivity.this, WebActivity.class);
				intent.putExtra("_weblink",
						"http://v.youku.com/v_show/id_XODgzMDYwMDMy.html?from=s1.8-3-1.1");
				startActivity(intent);
			}
			break;
		case R.id.ivMusicDownload:
			intent = new Intent(MainActivity.this, MusicSearchActivity.class);
			startActivity(intent);
			break;
		case R.id.tempImageView:
			pref = getSharedPreferences("userInfo", MODE_PRIVATE);
			String userName = pref.getString("userName", "");
			if (userName != "") {
				intent = new Intent(MainActivity.this,
						MyCircleListDialogActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(MainActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}
}
