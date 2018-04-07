package com.example.silencedance;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.example.tools.FileDownloadThread;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadActivity extends Activity implements OnClickListener {

	private static final String TAG = DownloadActivity.class.getSimpleName();
	/** 显示下载进度TextView */
	private TextView mMessageView;
	/** 显示下载进度ProgressBar */
	private ProgressBar mProgressbar;
	/** 输入的下载链接 **/
	private EditText downloadLink;
	
	int keys = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		findViewById(R.id.download_btn).setOnClickListener(this);
		mMessageView = (TextView) findViewById(R.id.download_message);
		mProgressbar = (ProgressBar) findViewById(R.id.download_progress);
		downloadLink = (EditText) findViewById(R.id.download_link);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.download, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.download_btn) {
			if (!downloadLink.getText().toString().equals("")) {
				keys = 0;
				doDownload(downloadLink.getText().toString());
			} else {
				Toast.makeText(DownloadActivity.this, "地址不能为空", 1).show();
				return;
			}

		}
	}

	/**
	 * 使用Handler更新UI界面信息
	 */

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			mProgressbar.setProgress(msg.getData().getInt("size"));

			float temp = (float) mProgressbar.getProgress()
					/ (float) mProgressbar.getMax();

			int progress = (int) (temp * 100);
			if (progress == 100&&keys ==0) {
				Toast.makeText(DownloadActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
				keys = 1;
			}
			mMessageView.setText("下载进度:" + progress + " %");

		}
	};

	/**
	 * 下载准备工作，获取SD卡路径、开启线程
	 */
	private void doDownload(String link) {
		// 获取SD卡路径
		String path = Environment.getExternalStorageDirectory()
				+ "/amosdownload/";
		File file = new File(path);
		// 如果SD卡目录不存在创建
		if (!file.exists()) {
			file.mkdir();
		}
		// 设置progressBar初始化
		mProgressbar.setProgress(0);

		// URL和文件名称写死，其实这些都可以通过HttpHeader获取到
		String downloadUrl = link;

		String[] sourceStrArray = link.split("/");
		String fileNameTemp = sourceStrArray[sourceStrArray.length - 1];// 取得文件名
		String fileName = fileNameTemp;

		int threadNum = 5;
		String filepath = path + fileName;
		Log.d(TAG, "download file  path:" + filepath);
		downloadTask task = new downloadTask(downloadUrl, threadNum, filepath);
		task.start();
	}

	/**
	 * 多线程文件下载
	 * 
	 * @author yangxiaolong
	 * @2014-8-7
	 */
	class downloadTask extends Thread {
		private String downloadUrl;// 下载链接地址
		private int threadNum;// 开启的线程数
		private String filePath;// 保存文件路径地址
		private int blockSize;// 每一个线程的下载量

		public downloadTask(String downloadUrl, int threadNum, String fileptah) {
			this.downloadUrl = downloadUrl;
			this.threadNum = threadNum;
			this.filePath = fileptah;
		}

		@Override
		public void run() {

			FileDownloadThread[] threads = new FileDownloadThread[threadNum];
			try {
				URL url = new URL(downloadUrl);
				Log.d(TAG, "download file http path:" + downloadUrl);
				URLConnection conn = url.openConnection();
				// 读取下载文件总大小
				int fileSize = conn.getContentLength();
				if (fileSize <= 0) {
					System.out.println("读取文件失败");
					Toast.makeText(DownloadActivity.this, "读取文件失败", 1).show();

					return;
				}
				// 设置ProgressBar最大的长度为文件Size
				mProgressbar.setMax(fileSize);

				// 计算每条线程下载的数据长度
				blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum
						: fileSize / threadNum + 1;

				Log.d(TAG, "fileSize:" + fileSize + "  blockSize:");

				File file = new File(filePath);
				for (int i = 0; i < threads.length; i++) {
					// 启动线程，分别下载每个线程需要下载的部分
					threads[i] = new FileDownloadThread(url, file, blockSize,
							(i + 1));
					threads[i].setName("Thread:" + i);
					threads[i].start();
				}

				boolean isfinished = false;
				int downloadedAllSize = 0;
				while (!isfinished) {
					isfinished = true;
					// 当前所有线程下载总量
					downloadedAllSize = 0;
					for (int i = 0; i < threads.length; i++) {
						downloadedAllSize += threads[i].getDownloadLength();
						if (!threads[i].isCompleted()) {
							isfinished = false;
						}
					}
					// 通知handler去更新视图组件
					Message msg = new Message();
					msg.getData().putInt("size", downloadedAllSize);
					mHandler.sendMessage(msg);

					Thread.sleep(1000);// 休息1秒后再读取下载进度
				}
				Log.d(TAG, " all of downloadSize:" + downloadedAllSize);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
