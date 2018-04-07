package com.example.silencedance;

import java.io.IOException;

import com.bumptech.glide.Glide;
import com.example.entity.Music;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicDetailActivity extends Activity implements
		OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener {
	private Music music;
	public MediaPlayer mediaPlayer; // ý�岥����
	ImageView bigMusicImage = null;
	Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_detail);
		inti();
	}

	private void inti() {
		music = (Music) getIntent().getSerializableExtra("music");
		TextView tvMusicid = (TextView) findViewById(R.id.tv_id);
		tvMusicid.setText("����id:" + music.getMusicId());
		TextView tvMusicName = (TextView) findViewById(R.id.tv_mname);
		tvMusicName.setText("������:" + music.getMusciName());
		TextView tvAirtist = (TextView) findViewById(R.id.tv_aname);
		tvAirtist.setText("������:" + music.getAirtistName());
		TextView tvAlumbName = (TextView) findViewById(R.id.tv_alumb_name);
		tvAlumbName.setText("ר����:" + music.getAlbumName());
		TextView tvBigUrl = (TextView) findViewById(R.id.tv_big_url);
		tvBigUrl.setText("��ͼURL:" + music.getBigAlumUrl());
		bigMusicImage = (ImageView) findViewById(R.id.bigMusicImage);
		Glide.with(context).load(music.getBigAlumUrl()).into(bigMusicImage);
		TextView tvSmallUrl = (TextView) findViewById(R.id.tv_small_url);
		tvSmallUrl.setText("СͼURL:" + music.getSmallAlumUrl());
		TextView tvlrcUrl = (TextView) findViewById(R.id.tv_lrc_url);
		tvlrcUrl.setText("���URL:" + music.getLrcUrl());
		TextView tvMusicLocaiton = (TextView) findViewById(R.id.tv_music_url);
		tvMusicLocaiton.setText("������ַ:" + music.getPath());
		Button btn_play = (Button) findViewById(R.id.btn_play);
		Button btn_stop = (Button) findViewById(R.id.btn_stop);

		Button btn_download = (Button) findViewById(R.id.btn_download);
		// ��������
		btn_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				playUrl(music.getPath());
			}
		});
		// ��ͣ����
		btn_stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mediaPlayer.stop();
			}
		});
		btn_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = null;
				content_url = Uri.parse(music.getPath());
				intent.setData(content_url);
				startActivity(intent);
			}
		});
	}

	public void playUrl(String url) {
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// ����ý��������
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);

			mediaPlayer.reset();
			mediaPlayer.setDataSource(url); // ��������Դ
			mediaPlayer.prepare(); // prepare�Զ�����
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onViewClick(View v) {
		// ������в���ץȡ���
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = null;
		switch (v.getId()) {
		case R.id.tv_big_url:
			content_url = Uri.parse(music.getBigAlumUrl());
			break;
		case R.id.tv_small_url:
			content_url = Uri.parse(music.getSmallAlumUrl());
			break;
		case R.id.tv_lrc_url:
			content_url = Uri.parse(music.getLrcUrl());
			break;
		case R.id.tv_music_url:
			content_url = Uri.parse(music.getPath());
			break;
		}
		intent.setData(content_url);
		startActivity(intent);
	}

	// ����׼��
	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
	}

	// �������
	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	// �������
	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
