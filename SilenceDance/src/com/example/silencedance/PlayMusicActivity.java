package com.example.silencedance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import com.example.weight.CircleImageView;
import com.example.tools.FoundWebServices;
import com.example.tools.LRCbean;
import com.example.tools.StringParsing;
import com.example.tools.pictureDim;
import com.example.tools.AnalysisCodeFromWebServices;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import android.R.integer;
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
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ���Ž��档��ǰ̨PlayMusicActivity��ע��һ��BroadcastReceiver��
 * Ȼ���ں�̨MusicService��ʹ��handler��Ϣ���ƣ���ͣ����ǰ̨���͹㲥���㲥����������ǵ�ǰMP���ŵ�ʱ��㣬
 * ǰ̨���յ��㲥���ò���ʱ��������½��������������ܴﵽĿ�ġ�
 */

public class PlayMusicActivity extends Activity {
	private int[] _ids;// �����ʱID������
	private String _titles[] = null;// ��ʱ��ű��������
	private String _artists[] = null;// ��ʱ��������ҵ�����
	private int position;// λ��
	private int currentPosition;// ��ǰseekbar����λ��
	private ImageButton playbtn = null;// ���Ű�ť
	private ImageButton latestBtn = null;// ��һ��
	private ImageButton nextBtn = null;// ��һ��
	private TextView playtime = null;// �Ѳ���ʱ��
	private TextView durationTime = null;// ����ʱ��
	private SeekBar seekbar = null;// ��������
	private ImageView rootBackground = null;
	private CircleImageView civTest;
	private ImageView civ_test_center;
	private int duration;// ��ʱ��
	private TextView name = null;// ����
	private TextView artist = null;// ����
	private TextView lrcText = null;// ���
	private static final String MUSIC_CURRENT = "com.music.currentTime";
	private static final String MUSIC_DURATION = "com.music.duration";
	private static final String MUSIC_NEXT = "com.music.next";
	private static final String MUSIC_UPDATE = "com.music.update";
	private static final int PLAY = 1;// ���岥��״̬
	private static final int PAUSE = 2;// ��ͣ״̬
	private static final int STOP = 3;// ֹͣ
	private static final int PROGRESS_CHANGE = 4;// �������ı�

	private static final int STATE_PLAY = 1;// ����״̬��Ϊ1,��ʾ����״̬
	private static final int STATE_PAUSE = 2;// ����״̬��Ϊ2����ʾ��ͣ״̬
	private int flag;// ���
	private Cursor cursor;// �α�
	private TreeMap<Integer, LRCbean> lrc_map = new TreeMap<Integer, LRCbean>();

	private ConversationListFragment conversationListFragment;
	private String get_method;
	EMMessageListener msgListener;
	private String circleID = "", circleMusicName = "";

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int result = Integer.parseInt(msg.obj.toString());
			Log.i("mytag", "��ӡWebServices��־result��" + result);
			if (result > 0) {// �ɹ���ȡ
				seekbar_change(result);
			} else {
				AnalysisCodeFromWebServices acfws = new AnalysisCodeFromWebServices();
				Toast.makeText(PlayMusicActivity.this,
						acfws.analysisCode(result), Toast.LENGTH_SHORT).show();
			}
		}
	};
	private Handler handler2 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.obj.toString().equals("pause")) {
				playbtn.setImageResource(R.drawable.music_pause);
			} else  if(msg.obj.toString().equals("play")){
				playbtn.setImageResource(R.drawable.music_play);
			}else if (msg.obj.toString().equals("change")) {
			
				stop();
				setup();
				play();
			}
		}
	};

	public void runThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					Message msg = new Message();
					FoundWebServices fws = new FoundWebServices();
					msg.obj = fws.getRemoteInfo(circleID, circleMusicName,
							get_method);
					handler.sendMessage(msg);

					Log.d("main", "��ȡ��");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("main", "�д���" + e.toString());
				} finally {
				}
			}
		}).start();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_play_music);
		Intent intent = this.getIntent();// ��ȡ�б��Intent����
		Bundle bundle = intent.getExtras();// Bundle��ȡ���ݣ���ô�ڲ��Ž�����ȡ�����
		_ids = bundle.getIntArray("_ids");// ���������ID��������ʱ�������ֵ�ID
		position = bundle.getInt("position");// ���ֲ���λ��
		_titles = bundle.getStringArray("_titles");// ���ֲ��ű���
		_artists = bundle.getStringArray("_artists");// �������������ң�����һ������������©�������ָ���Ǳ����
		name = (TextView) findViewById(R.id.name);// ����
		artist = (TextView) findViewById(R.id.singer);// ���֣���������
		lrcText = (TextView) findViewById(R.id.lrc);// ���
		playtime = (TextView) findViewById(R.id.playtime);// ������ڲ���ʱ��
		durationTime = (TextView) findViewById(R.id.duration);// �����ұ���ʱ�䣬Ҫת����
		rootBackground = (ImageView) findViewById(R.id.rootBackground);
		playbtn = (ImageButton) findViewById(R.id.playBtn);
		civ_test_center = (ImageView) findViewById(R.id.civ_test_center);
		civTest = (CircleImageView) findViewById(R.id.civ_test);
		civTest.setBorderColor(Color.GRAY);
		civTest.setBorderWidth(8);
		// civTest.roatateStart();
		// civ_test = (ImageView) findViewById(R.id.civ_test);
		LayoutParams params = civTest.getLayoutParams();
		LayoutParams params2 = civ_test_center.getLayoutParams();
		WindowManager wm1 = this.getWindowManager();
		params2.width = params.width = wm1.getDefaultDisplay().getWidth() - 200;
		params2.height = params.height = wm1.getDefaultDisplay().getWidth() - 200;
		civTest.setLayoutParams(params);
		ShowPlayBtn();// ��ʾ����˵���Ӳ��Ű�ť�¼�
		ShowLastBtn();// ��һ��
		ShowNextBtn();// ��һ��
		ShowSeekBar();// ������

		CircleTalkActivity c = new CircleTalkActivity();
		// EMClient.getInstance().chatManager().removeMessageListener(c.msgListener);
		msgListener = new EMMessageListener() {

			@Override
			public void onMessageReceived(List<EMMessage> messages) {

				conversationListFragment = new ConversationListFragment();
				conversationListFragment.refresh();
			}

			@Override
			public void onCmdMessageReceived(List<EMMessage> messages) {

				// �յ�͸����Ϣ
				for (EMMessage message : messages) {
					Log.i("main", "��ʼ");
					Log.i("main", messages.get(0).getBody().toString()
							+ "from:" + messages.get(0).getFrom() + "to"
							+ messages.get(0).getTo()
							+ message.getBody().toString().substring(5, 7));
					if (message.getBody().toString().substring(5, 7)
							.equals("*^")) {
						String str = message.getBody().toString();
						// "*^&join&teamid?146025545484&musicname?Сƻ��";
						StringParsing sp = new StringParsing();
						// System.out.println(sp.getTypeByOrder(demoStr));
						// System.out.println(sp.getTeamIDByOrder(demoStr));
						// System.out.println(sp.getMusicNameByOrder(demoStr));
						String orderType = sp.getTypeByOrder(str);

						circleID = sp.getTeamIDByOrder(str);
						circleMusicName = sp.getMusicNameByOrder(str);
						// Log.i("mytag", orderType);
						// Log.i("mytag", "1");
						if (orderType.equals("play")) {
							/**
							 * �������ظ���������ƥ��
							 */

							// Log.d("main", circleMusicName);
							researchMusicAndPlay(circleMusicName);
							// get_method = "getSynTime";
							// runThread();
							// flag = PLAY;
							// play();
						} else if (orderType.equals("pause")) {

							// flag = PAUSE;
							// pause();
							// flag = PAUSE;
							try {
								civTest.roatatePause();
								Message msg = new Message();
								msg.obj = "pause";
								handler2.sendMessage(msg);
								Intent intent = new Intent();
								intent.setAction("com.example.service.LocalMusicService");
								intent.putExtra("op", PAUSE);
								startService(intent);

							} catch (Exception e) {
								Log.i("main", "����+" + e);
							}
						} else if (orderType.equals("change")) {
							Log.i("main", "this" + circleMusicName);
							position = judgeAndGetCircleMusicPosition(circleMusicName);

							Log.i("main", "this2" + position);
							if (position == -1) {
								Toast.makeText(PlayMusicActivity.this,
										"���ֻ��в��������׸�", Toast.LENGTH_SHORT)
										.show();
								return;
							}
							Message msg = new Message();
							msg.obj = "change";
							handler2.sendMessage(msg);

						}
					}
				}
			}

			@Override
			public void onMessageReadAckReceived(List<EMMessage> messages) {
				// �յ��Ѷ���ִ
			}

			@Override
			public void onMessageDeliveryAckReceived(List<EMMessage> message) {
				// �յ����ʹ��ִ
			}

			@Override
			public void onMessageChanged(EMMessage message, Object change) {
				// ��Ϣ״̬�䶯
			}

		};
		EMClient.getInstance().chatManager().addMessageListener(msgListener);

	}

	public void researchMusicAndPlay(String musicName) {
		// Log.d("main", "2222" +
		// judgeAndGetCircleMusicPosition(musicName)+"position");
		if (judgeAndGetCircleMusicPosition(musicName) == -1) {
			Toast.makeText(this, "û���������豸���ҵ���ǰ���ŵĸ�����������", Toast.LENGTH_SHORT)
					.show();
			// Log.d("main", "4444" +
			// judgeAndGetCircleMusicPosition(musicName)+"position4");
		} else {
			// Log.d("main", "3333"+"position3");
			position = judgeAndGetCircleMusicPosition(musicName);
			// setup();
			if (!musicName.equals(name.getText())) {
				stop();
				setup();
				play();
			} else {
				get_method = "getSynTime";
				runThread();
				flag = PLAY;
				play();
			}
		}

	}

	public int judgeAndGetCircleMusicPosition(String musicName) {
		int musicPosition = -1;// �ڱ����б��е�λ��
		for (int i = 0; i < _titles.length; i++) {
			// Log.i("main", _titles[i]);
			if (_titles[i].equals(musicName)) {
				musicPosition = i;
				break;
			}
		}
		return musicPosition;
	}

	// ��ʾ������ť��������
	private void ShowPlayBtn() {
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
		setup();// ��ʼ��
		play();
	}

	protected void onStop() {
		super.onStop();
		unregisterReceiver(musicreceiver);// ֹͣ����ʱ����ע��㲥������
	}

	// ��������
	protected void play() {
		flag = PLAY;
		civTest.roatateStart();
		Message msg = new Message();
		msg.obj = "play";
		handler2.sendMessage(msg);
		//playbtn.setImageResource(R.drawable.music_play);
		Intent intent = new Intent();
		intent.setAction("com.example.service.LocalMusicService");
		intent.putExtra("op", PLAY);
		startService(intent);

	}

	// ��ͣ
	protected void pause() {
		flag = PAUSE;
		civTest.roatatePause();

		playbtn.setImageResource(R.drawable.music_pause);
		Intent intent = new Intent();
		intent.setAction("com.example.service.LocalMusicService");
		intent.putExtra("op", PAUSE);
		startService(intent);

	}

	// ��һ��
	protected void latestOne() {
		if (position == 0) {
			position = _ids.length - 1;
		} else if (position > 0) {
			position--;
		}
		stop();
		setup();
		play();

	}

	// ֹͣ��������
	private void stop() {
		civTest.roatatePause();
		Intent intent = new Intent();
		intent.setAction("com.example.service.LocalMusicService");
		intent.putExtra("op", STOP);
		startService(intent);
	}

	// ��һ�ײ�������
	protected void nextOne() {
		if (position == _ids.length - 1) {
			position = 0;
		} else if (position < _ids.length - 1) {
			position++;
		}
		stop();
		setup();
		play();

	}

	// �������ı�
	protected void seekbar_change(int progress) {
		Intent intent = new Intent();
		intent.setAction("com.example.service.LocalMusicService");
		intent.putExtra("op", PROGRESS_CHANGE);
		intent.putExtra("progress", progress);
		startService(intent);
		// Log.e("mytag", "" + progress);

	}

	// ͬ����ť
	public void syn_m(View view) {
		get_method = "getSynTime";
		runThread();
	}

	// ׼��
	private void setup() {
		loadclip();
		init();
		ReadSDLrc();

	}

	/**
	 * ��ȡSD���ĸ��
	 */
	private void ReadSDLrc() {
		cursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media.TITLE,
						MediaStore.Audio.Media.DURATION,
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media.ALBUM,
						MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.ALBUM_ID }, "_id=?",// �������ڵĸ�ʾ���ҪString����ĵ�4����������ʾ�ļ�����
				new String[] { _ids[position] + "" }, null);
		cursor.moveToFirst();// ���α�������һλ
		try {
			pictureDim pd = new pictureDim();

			rootBackground.setImageBitmap(pd.blurBitmap(
					getArtwork(this, _ids[position], cursor.getInt(5), true),
					this));
		} catch (Exception e) {

		}
		civTest.setImageBitmap(getArtwork(this, _ids[position],
				cursor.getInt(5), true));
		String name = cursor.getString(4);// �α궨λ��DISPLAY_NAME
		// read("/sdcard/" + name.substring(0, name.indexOf(".")) + ".lrc");//
		// sd�����������ֽ�ȡ�ַ��ܲ��ҵ�����λ��,�ⲽ��Ҫ��û��дһֱ��ʾ����ļ��޷���ʾ
		System.out.println(cursor.getString(4));// ����ʱ���Ȱ���������д�����ڿ���̨��ӡ����ʾ���������֣���ô�����ж���������û����.ֻ��û�л�ȡλ��

	}

	// ��ʼ������
	private void init() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(MUSIC_CURRENT);
		filter.addAction(MUSIC_DURATION);
		filter.addAction(MUSIC_NEXT);
		filter.addAction(MUSIC_UPDATE);
		registerReceiver(musicreceiver, filter);

	}

	// ��ȡ���⣬��ʣ�����
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

	// Ȼ���ں�̨MusicService��ʹ��handler��Ϣ���ƣ���ͣ����ǰ̨���͹㲥���㲥����������ǵ�ǰmp���ŵ�ʱ��㣬ǰ̨���յ��㲥���ò���ʱ��������½�����
	private BroadcastReceiver musicreceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MUSIC_CURRENT)) {
				currentPosition = intent.getExtras().getInt("currentTime");// ��õ�ǰ����λ��
				playtime.setText(toTime(currentPosition));// ��ʼ������ʱ��
				seekbar.setProgress(currentPosition);// ��ʼ�����Ž���λ��
				Iterator<Integer> iterator = lrc_map.keySet().iterator();
				while (iterator.hasNext()) {
					Object o = iterator.next();
					LRCbean val = lrc_map.get(o);
					if (val != null) {

						if (currentPosition > val.getBeginTime()
								&& currentPosition < val.getBeginTime()
										+ val.getLineTime()) {
							lrcText.setText(val.getLrcBody());
							break;
						}
					}
				}

			} else if (action.equals(MUSIC_DURATION)) {
				duration = intent.getExtras().getInt("duration");// ��ȡ��ʱ��
				seekbar.setMax(duration);// �������������ֵ������ʱ�䣩
				durationTime.setText(toTime(duration));// ��ʱ������ת���ĺ���
			} else if (action.equals(MUSIC_NEXT)) {
				System.out.println("���ּ���������һ��");
				nextOne();
			} else if (action.equals(MUSIC_UPDATE)) {
				position = intent.getExtras().getInt("position");
				setup();
			}

		}
	};

	/**
	 * ʱ���ת��
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
	 * �������
	 * 
	 * @param path
	 */
	private void read(String path) {
		lrc_map.clear();
		TreeMap<Integer, LRCbean> lrc_read = new TreeMap<Integer, LRCbean>();
		String data = "";
		BufferedReader br = null;
		File file = new File(path);
		System.out.println(path);
		if (!file.exists()) {
			lrcText.setText("����ļ�������...");
			return;
		}
		FileInputStream stream = null;
		try {

			stream = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			while ((data = br.readLine()) != null) {
				if (data.length() > 6) {
					if (data.charAt(3) == ':' && data.charAt(6) == '.') {// �Ӹ�����Ŀ�ʼ
						data = data.replace("[", "");
						data = data.replace("]", "@");
						data = data.replace(".", ":");
						String lrc[] = data.split("@");
						String lrcContent = null;
						if (lrc.length == 2) {
							lrcContent = lrc[lrc.length - 1];// ���
						} else {
							lrcContent = "";
						}

						for (int i = 0; i < lrc.length - 1; i++) {
							String lrcTime[] = lrc[0].split(":");

							int m = Integer.parseInt(lrcTime[0]);// ��
							int s = Integer.parseInt(lrcTime[1]);// ��
							int ms = Integer.parseInt(lrcTime[2]);// ����

							int begintime = (m * 60 + s) * 1000 + ms;// ת���ɺ���
							LRCbean lrcbean = new LRCbean();
							lrcbean.setBeginTime(begintime);// ���ø�ʿ�ʼʱ��
							lrcbean.setLrcBody(lrcContent);// ���ø�ʵ�����
							lrc_read.put(begintime, lrcbean);

						}

					}
				}
			}
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// ����ÿ������Ҫ��ʱ��
		lrc_map.clear();
		data = "";
		Iterator<Integer> iterator = lrc_read.keySet().iterator();
		LRCbean oldval = null;
		int i = 0;

		while (iterator.hasNext()) {
			Object ob = iterator.next();
			LRCbean val = lrc_read.get(ob);
			if (oldval == null) {
				oldval = val;
			} else {
				LRCbean item1 = new LRCbean();
				item1 = oldval;
				item1.setLineTime(val.getBeginTime() - oldval.getBeginTime());
				lrc_map.put(new Integer(i), item1);
				i++;
				oldval = val;
			}
		}
	}

	/**
	 * ����ר��ͼƬ�����뻹�ǱȽϳ���
	 * 
	 * @param context
	 * @param song_id
	 * @param album_id
	 * @param allowdefault
	 * @return
	 */
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
	 * ��ͼƬ��С�����жϣ����õ��������ű���
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
			// ֻ���д�С�ж�
			options.inJustDecodeBounds = true;
			// ���ô˷����õ�options�õ�ͼƬ�Ĵ�С
			BitmapFactory.decodeFileDescriptor(fd, null, options);
			// ���ǵ�Ŀ������800pixel�Ļ�������ʾ��
			// ������Ҫ����computeSampleSize�õ�ͼƬ���ŵı���
			options.inSampleSize = 200;
			// OK,���ǵõ������ŵı��������ڿ�ʼ��ʽ����BitMap����
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			// ����options��������������Ҫ���ڴ�
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
		EMClient.getInstance().chatManager().removeMessageListener(msgListener);

		finish();
	}

}
