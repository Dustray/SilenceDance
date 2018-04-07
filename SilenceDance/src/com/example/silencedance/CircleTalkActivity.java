package com.example.silencedance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.NowPlayInfo;
import com.example.tools.StringParsing;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.ui.EaseContactListFragment.EaseContactListItemClickListener;
import com.hyphenate.easeui.ui.EaseConversationListFragment.EaseConversationListItemClickListener;
import com.hyphenate.exceptions.HyphenateException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CircleTalkActivity extends EaseBaseActivity {

	Context context = this;

	private TextView unreadLabel;
	private Button[] mTabs;
	private ConversationListFragment conversationListFragment;
	private EaseContactListFragment contactListFragment;
	private Fragment[] fragments;
	private int index;
	private int currentTabIndex;
	private SharedPreferences pref;
	private Editor editor;

	// EMMessageListener msgListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_talk);

		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
		mTabs = new Button[3];
		mTabs[0] = (Button) findViewById(R.id.btn_conversation);
		mTabs[1] = (Button) findViewById(R.id.btn_address_list);
		// set first tab as selected
		mTabs[0].setSelected(true);

		conversationListFragment = new ConversationListFragment();
		contactListFragment = new EaseContactListFragment();
		contactListFragment.setContactsMap(getContacts());
		conversationListFragment
				.setConversationListItemClickListener(new EaseConversationListItemClickListener() {

					@Override
					public void onListItemClicked(EMConversation conversation) {
						startActivity(new Intent(CircleTalkActivity.this,
								ChatActivity.class).putExtra(
								EaseConstant.EXTRA_USER_ID,
								conversation.getUserName()).putExtra(
								EaseConstant.EXTRA_CHAT_TYPE, 2));
					}
				});
		contactListFragment
				.setContactListItemClickListener(new EaseContactListItemClickListener() {

					@Override
					public void onListItemClicked(EaseUser user) {
						startActivity(new Intent(CircleTalkActivity.this,
								ChatActivity.class).putExtra(
								EaseConstant.EXTRA_USER_ID, user.getUsername())
								.putExtra(EaseConstant.EXTRA_CHAT_TYPE, 2));
					}
				});

		fragments = new Fragment[] { conversationListFragment };
		// add and show first fragment

		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, conversationListFragment)
				.add(R.id.fragment_container, contactListFragment)
				.hide(contactListFragment).show(conversationListFragment)
				.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.circle_talk, menu);
		return true;
	}

	/**
	 * onTabClicked
	 * 
	 * @param view
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.btn_conversation:
			index = 0;
			break;
		case R.id.btn_address_list:
			index = 1;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager()
					.beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// set current tab as selected.
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	/**
	 * prepared users, password is "123456" you can use these user to test
	 * 
	 * @return
	 */
	private Map<String, EaseUser> getContacts() {
		Map<String, EaseUser> contacts = new HashMap<String, EaseUser>();
		List<EMGroup> grouplist = null;
		try {
			grouplist = EMClient.getInstance().groupManager().getAllGroups();
			for (int i = 1; i <= grouplist.size(); i++) {
				EaseUser user = new EaseUser(grouplist.get(i).getGroupId());
				contacts.put(grouplist.get(i).getGroupId(), user);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return contacts;
	}

	public void openGroupsActivity(View view) {
		// 获得SharedPreferences对象
		Intent intent;
		pref = getSharedPreferences("userInfo", MODE_PRIVATE);
		String userName = pref.getString("userName", "");
		if (userName.isEmpty()) {
			// Toast.makeText(SetActivity.this, "name:" + userName,
			// Toast.LENGTH_SHORT).show();
			intent = new Intent(CircleTalkActivity.this, LoginActivity.class);
		} else {
			// Toast.makeText(SetActivity.this, "name:" + userName,
			// Toast.LENGTH_SHORT).show();
			intent = new Intent(CircleTalkActivity.this, GroupsActivity.class);
		}
		startActivity(intent);
	}
}
