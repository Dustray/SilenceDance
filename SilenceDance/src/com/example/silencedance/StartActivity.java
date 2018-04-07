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

	//被迫下线时用
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

		// 用户登录跳转回来
		ContantsLogin.ilogin = new whenILogin() {// 接口是一个 常量 有一个 Contants 类
			@Override
			public void onLogin() {
				refresh();
			}
		};

		// 注册用户跳转回来
		ContantsRegister.iregister = new whenIRegister() {
			@Override
			public void onRegister() {
				refresh();
			}
		};

		// 发表动态跳转回来
		ContantsDynamic.idynamic = new whenIDynamic() {
			@Override
			public void onDynamic() {
				refreshCircle();
			}
		};

		// 退出登录跳转回来
		ContantsOutLogin.ioutlogin = new whenIOutLogin() {
			@Override
			public void onOutLogin() {
				refresh();
			}
		};

		// 退出登录跳转回来
		ContantsUpdateUserInfo.iupdateuserinfo = new whenIUpdateUserInfo() {
			@Override
			public void onUpdateUserInfo() {
				refresh();
			}
		};

		findViewById();
		/* 环信初始化SDK开始 */
		EMOptions options = new EMOptions();
		// 默认添加好友时，是不需要验证的，改成需要验证
		options.setAcceptInvitationAlways(false);
		appContext = this;
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果APP启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process
		// name就立即返回

		if (processAppName == null
				|| !processAppName
						.equalsIgnoreCase(appContext.getPackageName())) {
			// Log.e(TAG, "enter the service process!");

			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}
		// 初始化


		EaseUI.getInstance().init(applicationContext, options);
		// 在做打包混淆时，关闭debug模式，避免消耗不必要的资源
		EMClient.getInstance().setDebugMode(true);
		/* 环信初始化SDK结束 */
		/* 环信注册连接状态监听1开始 */
		// 注册一个监听连接状态的listener
		EMClient.getInstance()
				.addConnectionListener(new MyConnectionListener());
		/* 环信注册连接状态监听1结束 */
		
		/* 环信消息接收监听3开始 */
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
		
		initView();// 实例化控件
	}

	/* 环信注册连接状态监听2开始 */
	// 实现ConnectionListener接口
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
						// 显示帐号已经被移除
						Toast.makeText(StartActivity.this, "帐号已被移除,请联系软件开发者。", Toast.LENGTH_SHORT).show();
					} else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
						// 显示帐号在其他设备登录
						pref = getSharedPreferences("userInfo", MODE_PRIVATE);
						// 获得SharedPreferences.Editor对象
						editor = pref.edit();
						editor.putString("userPhone", null); // 清空保存的用户数据
						editor.putString("userName", null);
						editor.putString("userPass", null);
						editor.putString("userWork", null);
						editor.putInt("userAge",0);
						editor.putString("userAddress", null);
						editor.putString("userSex", null);
						editor.putString("userHead", null);
						editor.commit(); // 提交数据
						if(ContantsOutLogin.ioutlogin!=null)
							ContantsOutLogin.ioutlogin.onOutLogin();
						Toast.makeText(StartActivity.this, "帐号在其他设备登录", Toast.LENGTH_SHORT).show();

					} else {
						if (NetUtils.hasNetwork(StartActivity.this)) {
							// 连接不到聊天服务器
							//Toast.makeText(StartActivity.this, "连接不到聊天服务器"+EMClient.getInstance().isLoggedInBefore(), Toast.LENGTH_SHORT).show();

							
						} else {
							// 当前网络不可用，请检查网络设置
							Toast.makeText(StartActivity.this, "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();

						}
					}
				}
			});
		}
	}

	/* 环信注册连接状态监听2结束 */

	// 用户注册登录和退出登录调用，显示ViewPager中的MyActivity页面
	private void refresh() {
		manager.removeAllActivities(); // 清空之前生成的Activity
		id_ViewPagerhome.removeAllViews(); // 清空 界面里的 Linearlayout
		initView(); // 再画一个新的ViewPager并初始化
		id_ViewPager.setCurrentItem(2); // 跳转到指定条目
		tvMy.setTextColor(Color.rgb(255, 255, 255));
	}

	// 用户发表圈子动态调用，显示ViewPager中的CircleActivity页面
	private void refreshCircle() {
		manager.removeAllActivities(); // 清空之前生成的Activity
		id_ViewPagerhome.removeAllViews(); // 清空 界面里的 Linearlayout
		initView(); // 再画一个新的ViewPager并初始化
		id_ViewPager.setCurrentItem(1); // 跳转到指定条目
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
		mViews = new ArrayList<View>();// 每New一个对象会在内存中有一个 固定的 地址，所以懒得清之前的 直接New
										// 新的
		adapter = new ActivityAdapter(mViews);// 同上
		id_ViewPager = new ViewPager(StartActivity.this);// 临时的ViewPager

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

		id_ViewPager.setAdapter(adapter);// 配置适配器
		id_ViewPager.setOffscreenPageLimit(2); // 设置缓存页
		id_ViewPagerhome.addView(id_ViewPager);
		initEvents();// 因为 每次进这里都会生成一个不同的viewPager 所以 要对新的 ViewPager设置监听
	}

	// 滑动事件
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

	// TextView点击事件
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

	// 环信
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
	//环信预加载群组和会话
		@Override
		protected void onStart() {
			super.onStart();
			
			//pref = getSharedPreferences("userInfo", MODE_PRIVATE);
			
			new Thread(new Runnable() {
				public void run() {
					if (EMClient.getInstance().isLoggedInBefore()) {
						// ** 免登陆情况 加载所有本地群和会话
						//不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
						//加上的话保证进了主页面会话和群组都已经load完毕
						long start = System.currentTimeMillis();
						EMClient.getInstance().groupManager().loadAllGroups();
						EMClient.getInstance().chatManager().loadAllConversations();

						
						long costTime = System.currentTimeMillis() - start;
						//等待sleeptime时长
						if (20000 - costTime > 0) {
							try {
								Thread.sleep(20000 - costTime);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						//进入主页面
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
