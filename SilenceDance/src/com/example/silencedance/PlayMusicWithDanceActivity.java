package com.example.silencedance;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.TreeMap;

import com.example.tools.AnalysisCodeFromWebServices;
import com.example.tools.FoundWebServices;
import com.example.tools.LRCbean;
import com.example.tools.pictureDim;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 播放界面。在前台PlayMusicActivity里注册一个BroadcastReceiver，
 * 然后在后台MusicService里使用handler消息机制，不停的向前台发送广播，广播里面的数据是当前MP播放的时间点，
 * 前台接收到广播后获得播放时间点来更新进度条，这样就能达到目的。
 */
public class PlayMusicWithDanceActivity extends Activity {
	private int[] _ids;// 存放临时ID的数组
	private String _titles[] = null;// 临时存放标题的数组
	private String _artists[] = null;// 临时存放艺术家的数组
	private String circleMusicName[] = null;// 临时存放舞队歌曲标题的数组
	private int position;// 手机中的所有歌曲列表中的真实位置
	private int circlePosition = 0;// 圈子列表中的音乐位置
	private ImageButton playbtn = null;// 播放按钮
	private ImageButton latestBtn = null;// 上一首
	private ImageButton nextBtn = null;// 下一首
	private TextView playtime = null;// 已播放时间
	private TextView durationTime = null;// 歌曲时间
	private SeekBar seekbar = null;// 歌曲进度
	private ImageView rootBackground = null;
	private int currentPosition;// 当前播放位置
	private boolean ifFirstPlay = true;
	private int duration;// 总时间
	private TextView name = null;// 歌名
	private TextView artist = null;// 歌手
	private static final String MUSIC_CURRENT = "com.music.currentTime";
	private static final String MUSIC_DURATION = "com.music.duration";
	private static final String MUSIC_NEXT = "com.music.next";
	private static final String MUSIC_UPDATE = "com.music.update";
	private static final int PLAY = 1;// 定义播放状态
	private static final int PAUSE = 2;// 暂停状态
	private static final int STOP = 3;// 停止
	private static final int PROGRESS_CHANGE = 4;// 进度条改变

	private static final int STATE_PLAY = 1;// 播放状态设为1,表示播放状态
	private static final int STATE_PAUSE = 2;// 播放状态设为2，表示暂停状态
	private int flag;// 标记
	private Cursor cursor;// 游标
	private TreeMap<Integer, LRCbean> lrc_map = new TreeMap<Integer, LRCbean>();

	private String get_method;
	private String circleID = "";

	public void runThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					Message msg = new Message();
					FoundWebServices fws = new FoundWebServices();
					msg.obj = fws.getRemoteInfo(circleID, name.getText()
							.toString(), get_method);
					handler.sendMessage(msg);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("main", e.toString());
				} finally {
				}
			}
		}).start();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int result = Integer.parseInt(msg.obj.toString());
			Log.i("mytag", "dance成功：" + result);
			if (result > 0) {// 成功获取
				seekbar_change(result);
			} else {
				AnalysisCodeFromWebServices acfws = new AnalysisCodeFromWebServices();
				Toast.makeText(PlayMusicWithDanceActivity.this,
						acfws.analysisCode(result), Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_play_music_with_dance);
		Intent intent = this.getIntent();// 获取列表的Intent对象
		Bundle bundle = intent.getExtras();// Bundle存取数据，那么在播放界面提取数据喽
		_ids = bundle.getIntArray("_ids");// 歌名数组的ID，用来临时保存音乐的ID
		circlePosition = bundle.getInt("position");// 音乐播放圈子位置
		Log.i("main", circlePosition + "sss");
		_titles = bundle.getStringArray("_titles");// 音乐播放标题
		_artists = bundle.getStringArray("_artists");// 传过来的艺术家，歌名一个都不允许遗漏，否则空指针是必须的
		circleID = bundle.getString("theCircleID");
		circleMusicName = bundle.getStringArray("circleMusicName");
		for (int i = 0; i < circleMusicName.length; i++) {
			Log.i("main", circleMusicName[i] + i);
		}
		name = (TextView) findViewById(R.id.name);// 歌名
		artist = (TextView) findViewById(R.id.singer);// 歌手，即艺术家
		playtime = (TextView) findViewById(R.id.playtime);// 左边正在播放时间
		durationTime = (TextView) findViewById(R.id.duration);// 声明右边总时间，要转换的
		rootBackground = (ImageView) findViewById(R.id.rootBackground);
		ShowPlayBtn();// 显示或者说监视播放按钮事件
		ShowLastBtn();// 上一首
		ShowNextBtn();// 下一首
		ShowSeekBar();// 进度条

	}

	// 显示各个按钮并做监视
	private void ShowPlayBtn() {
		playbtn = (ImageButton) findViewById(R.id.playBtn);
		playbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				switch (flag) {
				case STATE_PLAY:
					pause();
					break;

				case STATE_PAUSE:
					play();
					break;
				}

			}
		});

	}

	private void ShowSeekBar() {
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
				play();
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				pause();
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					seekbar_change(progress);
				}

			}
		});

	}

	private void ShowNextBtn() {
		nextBtn = (ImageButton) findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				nextOne();

			}
		});

	}

	private void ShowLastBtn() {
		latestBtn = (ImageButton) findViewById(R.id.lastBtn);
		latestBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				latestOne();
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i("main", "bofang" + circleMusicName[circlePosition]);
		if (judgeAndGetCircleMusicPosition(circleMusicName[circlePosition]) == -1) {
			Toast.makeText(this, "没有在您的设备中找到当前播放的歌曲，请下载", Toast.LENGTH_SHORT)
					.show();
		} else {
			position = judgeAndGetCircleMusicPosition(circleMusicName[circlePosition]);
			// setup();

			setup();// 初始化
			play();
			// get_method = "setSynTime";
			// runThread();
			// sendDanceOrder("*^&play&teamid?" + circleID + "&musicname?"
			// + _titles[position]);
		}
	}

	protected void onStop() {
		super.onStop();
		unregisterReceiver(musicreceiver);// 停止界面时，反注册广播接收器
	}

	private void sendDanceOrder(String actionOrder) {
		EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

		// 支持单聊和群聊，默认单聊，如果是群聊添加下面这行
		cmdMsg.setChatType(ChatType.GroupChat);
		String action = actionOrder;// action可以自定义
		EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
		String toUsername = circleID;// 发送给某个人
		cmdMsg.setReceipt(toUsername);
		cmdMsg.addBody(cmdBody);
		EMClient.getInstance().chatManager().sendMessage(cmdMsg);
	}

	// 获取手机中存储的音乐真实position
	public int judgeAndGetCircleMusicPosition(String musicName) {
		int musicPosition = -1;// 在本地列表中的位置
		for (int i = 0; i < _titles.length; i++) {
			Log.e("main", "this:" + musicName + _titles[i]);
				if (_titles[i].equals(musicName)) {
					musicPosition = i;
					Log.i("main", _titles[i] + i + musicName);
					break;
				}
		}
		Log.i("main", "musicPosition" + musicPosition);
		return musicPosition;
	}

	// 播放音乐
	protected void play() {

		if (ifFirstPlay) {

			get_method = "setSynTime";
		} else {

			get_method = "continueMusic";
		}
		runThread();

		sendDanceOrder("*^&play&teamid?" + circleID + "&musicname?"
				+ _titles[position]);
		flag = PLAY;
		playbtn.setImageResource(R.drawable.music_play);
		Intent intent = new Intent();
		intent.setAction("com.example.service.LocalMusicService");
		intent.putExtra("op", PLAY);
		startService(intent);

	}

	// 暂停
	protected void pause() {
		ifFirstPlay = false;
		get_method = "pauseMusic";
		runThread();
		sendDanceOrder("*^&pause&teamid?" + circleID + "&musicname?"
				+ _titles[position]);
		flag = PAUSE;
		playbtn.setImageResource(R.drawable.music_pause);
		Intent intent = new Intent();
		intent.setAction("com.example.service.LocalMusicService");
		intent.putExtra("op", PAUSE);
		startService(intent);

	}

	// 上一首
	protected void latestOne() {
		/*
		 * if (position == 0) { position = _ids.length - 1; } else if (position
		 * > 0) { position--; }
		 */

		ifFirstPlay = true;
		if (circlePosition - 1 < 0) {

			position = judgeAndGetCircleMusicPosition(circleMusicName[circleMusicName.length - 1]);
		} else {

			position = judgeAndGetCircleMusicPosition(circleMusicName[--circlePosition]);
		}

		sendDanceOrder("*^&change&teamid?" + circleID + "&musicname?"
				+ _titles[position]);
		stop();
		setup();
		play();

	}

	// 停止播放音乐
	private void stop() {
		Intent intent = new Intent();
		intent.setAction("com.example.service.LocalMusicService");
		intent.putExtra("op", STOP);
		startService(intent);
	}

	// 下一首播放音乐
	protected void nextOne() {
		/*
		 * if (position == _ids.length - 1) { position = 0; } else if (position
		 * < _ids.length - 1) { position++; }
		 */
		ifFirstPlay = true;
		if (circlePosition + 1 == circleMusicName.length) {
			position = judgeAndGetCircleMusicPosition(circleMusicName[0]);
		} else {
			position = judgeAndGetCircleMusicPosition(circleMusicName[++circlePosition]);
		}

		sendDanceOrder("*^&change&teamid?" + circleID + "&musicname?"
				+ _titles[position]);
		stop();
		setup();
		play();

	}

	// 进度条改变
	protected void seekbar_change(int progress) {
		Intent intent = new Intent();
		intent.setAction("com.example.service.LocalMusicService");
		intent.putExtra("op", PROGRESS_CHANGE);
		intent.putExtra("progress", progress);
		startService(intent);

	}

	// 同步按钮
	public void syn_m(View view) {
		get_method = "getSynTime";
		runThread();

		sendDanceOrder("*^&play&teamid?" + circleID + "&musicname?"
				+ _titles[position]);
	}

	// 准备
	private void setup() {
		loadclip();
		init();
		ReadSDLrc();
	}

	/**
	 * 读取SD卡的歌词
	 */
	private void ReadSDLrc() {
		cursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media.TITLE,
						MediaStore.Audio.Media.DURATION,
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media.ALBUM,
						MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.ALBUM_ID }, "_id=?",// 我们现在的歌词就是要String数组的第4个参数，显示文件名字
				new String[] { _ids[position] + "" }, null);
		cursor.moveToFirst();// 将游标移至第一位
		try {
			pictureDim pd = new pictureDim();

			rootBackground.setImageBitmap(pd.blurBitmap(
					getArtwork(this, _ids[position], cursor.getInt(5), true),
					this));
		} catch (Exception e) {

		}
		// String name = cursor.getString(4);// 游标定位到DISPLAY_NAME
		// read("/sdcard/" + name.substring(0, name.indexOf(".")) + ".lrc");//
		// sd卡的音乐名字截取字符窜并找到它的位置,这步重要，没有写一直表示歌词文件无法显示
		System.out.println(cursor.getString(4));// 调试时我先把音乐名字写死，在控制台打印能显示出音乐名字，那么由于判断音乐名字没问题.只是没有获取位置

	}

	// 初始化服务
	private void init() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(MUSIC_CURRENT);
		filter.addAction(MUSIC_DURATION);
		filter.addAction(MUSIC_NEXT);
		filter.addAction(MUSIC_UPDATE);
		registerReceiver(musicreceiver, filter);

	}

	// 截取标题，歌词，歌名
	private void loadclip() {
		seekbar.setProgress(0);
		int pos = _ids[position];
		name.setText(_titles[position]);
		artist.setText(_artists[position]);
		Intent intent = new Intent();
		intent.putExtra("_id", pos);
		intent.putExtra("_titles", _titles);
		intent.putExtra("position", position);
		intent.setAction("com.example.service.LocalMusicService");
		startService(intent);

	}

	// 然后在后台MusicService里使用handler消息机制，不停的向前台发送广播，广播里面的数据是当前mp播放的时间点，前台接收到广播后获得播放时间点来更新进度条
	private BroadcastReceiver musicreceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MUSIC_CURRENT)) {
				currentPosition = intent.getExtras().getInt("currentTime");// 获得当前播放位置
				playtime.setText(toTime(currentPosition));// 初始化播放时间
				seekbar.setProgress(currentPosition);// 初始化播放进度位置
				Iterator<Integer> iterator = lrc_map.keySet().iterator();
				while (iterator.hasNext()) {
					Object o = iterator.next();
					LRCbean val = lrc_map.get(o);
					if (val != null) {

						if (currentPosition > val.getBeginTime()
								&& currentPosition < val.getBeginTime()
										+ val.getLineTime()) {
							break;
						}
					}
				}

			} else if (action.equals(MUSIC_DURATION)) {
				duration = intent.getExtras().getInt("duration");// 获取总时间
				seekbar.setMax(duration);// 进度条设置最大值（传总时间）
				durationTime.setText(toTime(duration));// 总时间设置转换的函数
			} else if (action.equals(MUSIC_NEXT)) {
				System.out.println("音乐继续播放下一首");
				nextOne();
			} else if (action.equals(MUSIC_UPDATE)) {
				position = intent.getExtras().getInt("position");
				setup();
			}

		}
	};

	/**
	 * 时间的转换
	 * 
	 * @param time
	 * @return
	 */
	public String toTime(int time) {

		time /= 1000;
		int minute = time / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}

	/**
	 * 构造专辑图片，代码还是比较长的
	 * 
	 * @param context
	 * @param song_id
	 * @param album_id
	 * @param allowdefault
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static Bitmap getArtwork(Context context, long song_id,
			long album_id, boolean allowdefault) {
		if (album_id < 0) {

			if (song_id >= 0) {
				Bitmap bm = getArtworkFromFile(context, song_id, -1);
				if (bm != null) {
					return bm;
				}
			}
			if (allowdefault) {
				return getDefaultArtwork(context);
			}
			return null;
		}
		ContentResolver res = context.getContentResolver();
		Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
		if (uri != null) {
			InputStream in = null;
			try {
				in = res.openInputStream(uri);
				BitmapFactory.Options options = new BitmapFactory.Options();

				options.inSampleSize = 1;

				options.inJustDecodeBounds = true;

				BitmapFactory.decodeStream(in, null, options);

				options.inSampleSize = computeSampleSize(options, 200);

				options.inJustDecodeBounds = false;
				options.inDither = false;
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				in = res.openInputStream(uri);
				return BitmapFactory.decodeStream(in, null, sBitmapOptions);
			} catch (FileNotFoundException ex) {

				Bitmap bm = getArtworkFromFile(context, song_id, album_id);
				if (bm != null) {
					if (bm.getConfig() == null) {
						bm = bm.copy(Bitmap.Config.RGB_565, false);
						if (bm == null && allowdefault) {
							return getDefaultArtwork(context);
						}
					}
				} else if (allowdefault) {
					bm = getDefaultArtwork(context);
				}
				return bm;
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException ex) {
				}
			}
		}

		return null;
	}

	/**
	 * 对图片大小进行判断，并得到合适缩放比例
	 * 
	 * @param options
	 * @param target
	 * @return
	 */
	static int computeSampleSize(BitmapFactory.Options options, int target) {

		int w = options.outWidth;
		int h = options.outHeight;
		int candidateW = w / target;
		int candidateH = h / target;
		int candidate = Math.max(candidateW, candidateH);
		if (candidate == 0)
			return 1;
		if (candidate > 1) {
			if ((w > target) && (w / candidate) < target)
				candidate -= 1;
		}
		if (candidate > 1) {
			if ((h > target) && (h / candidate) < target)
				candidate -= 1;
		}
		Log.v("ADW", "candidate:" + candidate);
		return candidate;
	}

	private static Bitmap getArtworkFromFile(Context context, long songid,
			long albumid) {
		Bitmap bm = null;

		if (albumid < 0 && songid < 0) {
			throw new IllegalArgumentException(
					"Must specify an album or a song id");
		}
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			FileDescriptor fd = null;
			if (albumid < 0) {
				Uri uri = Uri.parse("content://media/external/audio/media/"
						+ songid + "/albumart");
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					fd = pfd.getFileDescriptor();

				}
			} else {
				Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					fd = pfd.getFileDescriptor();

				}
			}
			options.inSampleSize = 1;
			// 只进行大小判断
			options.inJustDecodeBounds = true;
			// 调用此方法得到options得到图片的大小
			BitmapFactory.decodeFileDescriptor(fd, null, options);
			// 我们的目标是在800pixel的画面上显示。
			// 所以需要调用computeSampleSize得到图片缩放的比例
			options.inSampleSize = 200;
			// OK,我们得到了缩放的比例，现在开始正式读入BitMap数据
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			// 根据options参数，减少所需要的内存
			bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
		} catch (FileNotFoundException ex) {

		}
		if (bm != null) {

		}
		return bm;
	}

	private static Bitmap getDefaultArtwork(Context context) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.RGB_565;

		return BitmapFactory.decodeStream(context.getResources()
				.openRawResource(R.drawable.music_head_icon), null, opts);
	}

	private static final Uri sArtworkUri = Uri
			.parse("content://media/external/audio/albumart");
	private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();

	public void goBack(View view) {
		finish();
	}

	public void closeMe(View view) {
		finish();
	}

}
