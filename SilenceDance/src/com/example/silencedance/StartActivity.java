package com.example.silencedance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.contants.ContantsDynamic;
import com.example.contants.ContantsLogin;
import com.example.contants.ContantsOutLogin;
import com.example.contants.ContantsRegister;
import com.example.contants.ContantsUpdateUserInfo;
import com.example.ginterface.whenIDynamic;
import com.example.ginterface.whenILogin;
import com.example.ginterface.whenIOutLogin;
import com.example.ginterface.whenIRegister;
import com.example.ginterface.whenIUpdateUserInfo;
import com.example.adapter.ActivityAdapter;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity {
	public static StartActivity instance = null;

	private Intent intent;
	private Intent intentMain, intentCircle, intentMy;
	private LinearLayout id_ViewPagerhome;
	private ViewPager id_ViewPager;
	private List<View> mViews = null;
	private ActivityAdapter adapter = null;
	private LocalActivityManager manager;
	// TextView
	private TextView tvFirst, tvCircle, tvMy;

	//��������ʱ��
	private SharedPreferences pref;
	private Editor editor;
	
	protected Context appContext = null, applicationContext = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		instance = this;
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);

		// �û���¼��ת����
		ContantsLogin.ilogin = new whenILogin() {// �ӿ���һ�� ���� ��һ�� Contants ��
			@Override
			public void onLogin() {
				refresh();
			}
		};

		// ע���û���ת����
		ContantsRegister.iregister = new whenIRegister() {
			@Override
			public void onRegister() {
				refresh();
			}
		};

		// ����̬��ת����
		ContantsDynamic.idynamic = new whenIDynamic() {
			@Override
			public void onDynamic() {
				refreshCircle();
			}
		};

		// �˳���¼��ת����
		ContantsOutLogin.ioutlogin = new whenIOutLogin() {
			@Override
			public void onOutLogin() {
				refresh();
			}
		};

		// �˳���¼��ת����
		ContantsUpdateUserInfo.iupdateuserinfo = new whenIUpdateUserInfo() {
			@Override
			public void onUpdateUserInfo() {
				refresh();
			}
		};

		findViewById();
		/* ���ų�ʼ��SDK��ʼ */
		EMOptions options = new EMOptions();
		// Ĭ����Ӻ���ʱ���ǲ���Ҫ��֤�ģ��ĳ���Ҫ��֤
		options.setAcceptInvitationAlways(false);
		appContext = this;
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// ���APP������Զ�̵�service����application:onCreate�ᱻ����2��
		// Ϊ�˷�ֹ����SDK����ʼ��2�Σ��Ӵ��жϻᱣ֤SDK����ʼ��1��
		// Ĭ�ϵ�APP�����԰���ΪĬ�ϵ�process name�����У�����鵽��process name����APP��process
		// name����������

		if (processAppName == null
				|| !processAppName
						.equalsIgnoreCase(appContext.getPackageName())) {
			// Log.e(TAG, "enter the service process!");

			// ���application::onCreate �Ǳ�service ���õģ�ֱ�ӷ���
			return;
		}
		// ��ʼ��


		EaseUI.getInstance().init(applicationContext, options);
		// �����������ʱ���ر�debugģʽ���������Ĳ���Ҫ����Դ
		EMClient.getInstance().setDebugMode(true);
		/* ���ų�ʼ��SDK���� */
		/* ����ע������״̬����1��ʼ */
		// ע��һ����������״̬��listener
		EMClient.getInstance()
				.addConnectionListener(new MyConnectionListener());
		/* ����ע������״̬����1���� */
		
		/* ������Ϣ���ռ���3��ʼ */
		new Thread(){
			public void run(){
				try {
					List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
				} catch (HyphenateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
		initView();// ʵ�����ؼ�
	}

	/* ����ע������״̬����2��ʼ */
	// ʵ��ConnectionListener�ӿ�
	private class MyConnectionListener implements EMConnectionListener {
		@Override
		public void onConnected() {
		}
		@Override
		public void onDisconnected(final int error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (error == EMError.USER_REMOVED) {
						// ��ʾ�ʺ��Ѿ����Ƴ�
						Toast.makeText(StartActivity.this, "�ʺ��ѱ��Ƴ�,����ϵ��������ߡ�", Toast.LENGTH_SHORT).show();
					} else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
						// ��ʾ�ʺ��������豸��¼
						pref = getSharedPreferences("userInfo", MODE_PRIVATE);
						// ���SharedPreferences.Editor����
						editor = pref.edit();
						editor.putString("userPhone", null); // ��ձ�����û�����
						editor.putString("userName", null);
						editor.putString("userPass", null);
						editor.putString("userWork", null);
						editor.putInt("userAge",0);
						editor.putString("userAddress", null);
						editor.putString("userSex", null);
						editor.putString("userHead", null);
						editor.commit(); // �ύ����
						if(ContantsOutLogin.ioutlogin!=null)
							ContantsOutLogin.ioutlogin.onOutLogin();
						Toast.makeText(StartActivity.this, "�ʺ��������豸��¼", Toast.LENGTH_SHORT).show();

					} else {
						if (NetUtils.hasNetwork(StartActivity.this)) {
							// ���Ӳ������������
							//Toast.makeText(StartActivity.this, "���Ӳ������������"+EMClient.getInstance().isLoggedInBefore(), Toast.LENGTH_SHORT).show();

							
						} else {
							// ��ǰ���粻���ã�������������
							Toast.makeText(StartActivity.this, "��ǰ���粻���ã�������������", Toast.LENGTH_SHORT).show();

						}
					}
				}
			});
		}
	}

	/* ����ע������״̬����2���� */

	// �û�ע���¼���˳���¼���ã���ʾViewPager�е�MyActivityҳ��
	private void refresh() {
		manager.removeAllActivities(); // ���֮ǰ���ɵ�Activity
		id_ViewPagerhome.removeAllViews(); // ��� ������� Linearlayout
		initView(); // �ٻ�һ���µ�ViewPager����ʼ��
		id_ViewPager.setCurrentItem(2); // ��ת��ָ����Ŀ
		tvMy.setTextColor(Color.rgb(255, 255, 255));
	}

	// �û�����Ȧ�Ӷ�̬���ã���ʾViewPager�е�CircleActivityҳ��
	private void refreshCircle() {
		manager.removeAllActivities(); // ���֮ǰ���ɵ�Activity
		id_ViewPagerhome.removeAllViews(); // ��� ������� Linearlayout
		initView(); // �ٻ�һ���µ�ViewPager����ʼ��
		id_ViewPager.setCurrentItem(1); // ��ת��ָ����Ŀ
		tvCircle.setTextColor(Color.rgb(255, 255, 255));
	}

	private void findViewById() {
		id_ViewPagerhome = (LinearLayout) findViewById(R.id.id_viewPager);

		tvFirst = (TextView) findViewById(R.id.tvFirst);
		tvCircle = (TextView) findViewById(R.id.tvCircle);
		tvMy = (TextView) findViewById(R.id.tvMy);
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		mViews = new ArrayList<View>();// ÿNewһ����������ڴ�����һ�� �̶��� ��ַ������������֮ǰ�� ֱ��New
										// �µ�
		adapter = new ActivityAdapter(mViews);// ͬ��
		id_ViewPager = new ViewPager(StartActivity.this);// ��ʱ��ViewPager

		intentMain = new Intent(StartActivity.this, MainActivity.class);
		View tab01 = manager.startActivity("viewMain", intentMain)
				.getDecorView();

		intentCircle = new Intent(StartActivity.this, CircleActivity.class);
		View tab02 = manager.startActivity("viewCircle", intentCircle)
				.getDecorView();

		intentMy = new Intent(StartActivity.this, MyActivity.class);
		View tab03 = manager.startActivity("viewMy", intentMy).getDecorView();

		mViews.add(tab01);
		mViews.add(tab02);
		mViews.add(tab03);

		id_ViewPager.setAdapter(adapter);// ����������
		id_ViewPager.setOffscreenPageLimit(2); // ���û���ҳ
		id_ViewPagerhome.addView(id_ViewPager);
		initEvents();// ��Ϊ ÿ�ν����ﶼ������һ����ͬ��viewPager ���� Ҫ���µ� ViewPager���ü���
	}

	// �����¼�
	private void initEvents() {
		id_ViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				resetTextView();
				int currentItem = id_ViewPager.getCurrentItem();
				switch (currentItem) {
				case 0:
					tvFirst.setTextColor(Color.rgb(255, 255, 255));
					break;
				case 1:
					tvCircle.setTextColor(Color.rgb(255, 255, 255));
					break;
				case 2:
					tvMy.setTextColor(Color.rgb(255, 255, 255));
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	// TextView����¼�
	public void switchover(View view) {
		resetTextView();
		switch (view.getId()) {
		case R.id.tvFirst:
			id_ViewPager.setCurrentItem(0);
			tvFirst.setTextColor(Color.rgb(255, 255, 255));
			break;
		case R.id.tvCircle:
			id_ViewPager.setCurrentItem(1);
			tvCircle.setTextColor(Color.rgb(255, 255, 255));
			break;
		case R.id.tvMy:
			id_ViewPager.setCurrentItem(2);
			tvMy.setTextColor(Color.rgb(255, 255, 255));
			break;
		default:
			break;
		}
	}

	private void resetTextView() {
		tvFirst.setTextColor(Color.rgb(0, 0, 0));
		tvCircle.setTextColor(Color.rgb(0, 0, 0));
		tvMy.setTextColor(Color.rgb(0, 0, 0));
	}

	public void cornerClick(View view) {
		switch (view.getId()) {
		case R.id.ibMenu:
			intent = new Intent(StartActivity.this, StartMenuActivity.class);
			startActivity(intent);
			break;
		case R.id.ibMore:
			intent = new Intent(StartActivity.this, DialogActivity.class);
			startActivity(intent);
		default:
			break;
		}
	}

	// ����
	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}
	//����Ԥ����Ⱥ��ͻỰ
		@Override
		protected void onStart() {
			super.onStart();
			
			//pref = getSharedPreferences("userInfo", MODE_PRIVATE);
			
			new Thread(new Runnable() {
				public void run() {
					if (EMClient.getInstance().isLoggedInBefore()) {
						// ** ���½��� �������б���Ⱥ�ͻỰ
						//���Ǳ���ģ�����sdkҲ���Զ��첽ȥ����(�����ظ�����)��
						//���ϵĻ���֤������ҳ��Ự��Ⱥ�鶼�Ѿ�load���
						long start = System.currentTimeMillis();
						EMClient.getInstance().groupManager().loadAllGroups();
						EMClient.getInstance().chatManager().loadAllConversations();

						
						long costTime = System.currentTimeMillis() - start;
						//�ȴ�sleeptimeʱ��
						if (20000 - costTime > 0) {
							try {
								Thread.sleep(20000 - costTime);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						//������ҳ��
						//startActivity(new Intent(StartActivity.this, MainActivity.class));
						//finish();
					}else {
						//try {
							//Thread.sleep(20000);
						//} catch (InterruptedException e) {
						//}
						//startActivity(new Intent(StartActivity.this, LoginActivity.class));
						//finish();
					}
				}
			}).start();

		}
}
