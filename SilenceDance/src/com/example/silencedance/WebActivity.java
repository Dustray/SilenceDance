package com.example.silencedance;

import com.example.silencedance.R;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends Activity {
	private Intent intent;
	private WebView webView;
	private String nowUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		webView = (WebView) findViewById(R.id.web_view);
		//֧��javascript
		webView.getSettings().setJavaScriptEnabled(true); 
		// ���ÿ���֧������ 
		webView.getSettings().setSupportZoom(true); 
		// ���ó������Ź��� 
		webView.getSettings().setBuiltInZoomControls(true);
		//�������������
		webView.getSettings().setUseWideViewPort(true);
		//����Ӧ��Ļ
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(true);
		
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		intent = this.getIntent();// ��ȡIntent����
		Bundle bundle = intent.getExtras();// Bundle��ȡ����
		String url = bundle.getString("_weblink");
		
		String temp = url.substring(0, 4);
		// Toast.makeText(WebActivity.this, "url:" + temp,
		// Toast.LENGTH_SHORT).show();
		if (!temp.equals("http")) {
			url = "http://" + url;
		}
		webView.loadUrl(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_video, menu);
		return true;
	}

	public void closeThis(View view) {
		finish();
	}

	public void goBack(View view) {
		webView.goBack();// ����ǰһ��ҳ��
	}

	public void goForward(View view) {
		webView.goForward();// ����ǰһ��ҳ��
	}

	public void refresh(View view) {
		webView.reload();
	}

}
