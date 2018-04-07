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
		//支持javascript
		webView.getSettings().setJavaScriptEnabled(true); 
		// 设置可以支持缩放 
		webView.getSettings().setSupportZoom(true); 
		// 设置出现缩放工具 
		webView.getSettings().setBuiltInZoomControls(true);
		//扩大比例的缩放
		webView.getSettings().setUseWideViewPort(true);
		//自适应屏幕
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(true);
		
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		intent = this.getIntent();// 获取Intent对象
		Bundle bundle = intent.getExtras();// Bundle存取数据
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
		webView.goBack();// 返回前一个页面
	}

	public void goForward(View view) {
		webView.goForward();// 返回前一个页面
	}

	public void refresh(View view) {
		webView.reload();
	}

}
