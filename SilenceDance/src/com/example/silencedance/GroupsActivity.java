package com.example.silencedance;

import java.util.List;

import com.example.adapter.GroupAdapter;
import com.example.easemob.getMyCircle;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class GroupsActivity extends Activity {
	public static final String TAG = "GroupsActivity";
	private ListView groupListView;
	private Context context = this;
	protected List<EMGroup> grouplist;
	private GroupAdapter groupAdapter;
	private InputMethodManager inputMethodManager;
	public static GroupsActivity instance;
	private SwipeRefreshLayout swipeRefreshLayout;

	private Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			List<EMGroup> mygrouplist = (List<EMGroup>) msg.obj;

			if (grouplist.size() == 0) {
				Toast.makeText(context, "您没有建立或加入任何舞队", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			setAdapter(mygrouplist);

		}
	};
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			swipeRefreshLayout.setRefreshing(false);
			switch (msg.what) {
			case 0:
				refresh();
				break;
			case 1:
				Toast.makeText(GroupsActivity.this,
						R.string.Failed_to_get_group_chat_information,
						Toast.LENGTH_LONG).show();
				break;
			case 2:
				Toast.makeText(context, "获取网络舞队列表失败", Toast.LENGTH_SHORT)
						.show();

				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_fragment_groups);

		instance = this;
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		getMyCircle myCircle = new getMyCircle();
		if (myCircle.getMyCircleFromLocal().size() != 0) {
			grouplist = myCircle.getMyCircleFromLocal();
			setAdapter(grouplist);
		} else {
			new Thread() {
				getMyCircle myCircles = new getMyCircle();

				public void run() {
					if (myCircles.getMyCircleFromNet(context) != null) {
						grouplist = myCircles.getMyCircleFromNet(context);
						Message msg = new Message();

						msg.obj = grouplist;
						mhandler.sendMessage(msg);
					} else {
						handler.sendEmptyMessage(2);
					}
				}
			}.start();
		}

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
		swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright,
				R.color.holo_green_light, R.color.holo_orange_light,
				R.color.holo_red_light);
		// 下拉刷新
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				new Thread() {
					@Override
					public void run() {
						try {
							EMClient.getInstance().groupManager()
									.getJoinedGroupsFromServer();
							handler.sendEmptyMessage(0);
						} catch (HyphenateException e) {
							e.printStackTrace();
							handler.sendEmptyMessage(1);
						}
					}
				}.start();
			}
		});

	}

	public void setAdapter(List<EMGroup> mygrouplist) {
		groupListView = (ListView) findViewById(R.id.list);
		// show group list
		groupAdapter = new GroupAdapter(this, 1, mygrouplist);
		groupListView.setAdapter(groupAdapter);

		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// 进入群聊
				Intent intent = new Intent(GroupsActivity.this,
						ChatActivity.class);
				// it is group chat
				// intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
				intent.putExtra("userId", groupAdapter.getItem(position - 1)
						.getGroupId());
				startActivityForResult(intent, 0);

			}

		});
		groupListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(
								getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void refresh() {
		grouplist = EMClient.getInstance().groupManager().getAllGroups();
		groupAdapter = new GroupAdapter(this, 1, grouplist);
		groupListView.setAdapter(groupAdapter);
		groupAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	/**
	 * 杩斿洖
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}
}
