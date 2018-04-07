package com.example.silencedance;

import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseBaiduMapActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentHelper;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

public class ChatFragment extends EaseChatFragment implements
		EaseChatFragmentHelper {

	// constant start from 11 to avoid conflict with constant in base class
	static final int ITEM_TAKE_PICTURE = 1;
	static final int ITEM_PICTURE = 2;
	static final int ITEM_LOCATION = 3;

	private static final int ITEM_CONTROL = 11;
	private static final int ITEM_FILE = 12;
	private static final int ITEM_VOICE_CALL = 13;
	private static final int ITEM_VIDEO_CALL = 14;

	private static final int REQUEST_CODE_SELECT_VIDEO = 11;
	private static final int REQUEST_CODE_SELECT_FILE = 12;
	private static final int REQUEST_CODE_GROUP_DETAIL = 13;
	private static final int REQUEST_CODE_CONTEXT_MENU = 14;
	private static final int REQUEST_CODE_SELECT_AT_USER = 15;

	private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
	private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
	private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
	private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;

	private ConversationListFragment conversationListFragment;

	@Override
	protected void setUpView() {
		// TODO Auto-generated method stub
		super.setUpView();
		setChatFragmentListener(this);
	}

	@Override
	protected void registerExtendMenuItem() {
		super.registerExtendMenuItem();

		inputMenu.registerExtendMenuItem(R.string.attach_video,
				R.drawable.em_chat_control_selector, ITEM_CONTROL,
				extendMenuItemClickListener);

	}

	@Override
	public void onSetMessageAttributes(EMMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnterToChatDetails() {
		// TODO Auto-generated method stub
		EMGroup group = EMClient.getInstance().groupManager()
				.getGroup(toChatUsername);
		if (group == null) {
			Toast.makeText(getActivity(), R.string.gorup_not_found, 0).show();
			return;
		}
		startActivityForResult(
				(new Intent(getActivity(), CircleDetailActivity.class).putExtra(
						"groupid", toChatUsername)), REQUEST_CODE_GROUP_DETAIL);
	}

	@Override
	public void onAvatarClick(String username) {
		// TODO Auto-generated method stub
		// ”√ªßºÚΩÈ
		// Intent intent = new Intent(getActivity(), UserProfileActivity.class);
		// intent.putExtra("username", username);
		// startActivity(intent);
	}

	@Override
	public void onMessageReceived(List<EMMessage> messages) {
		// TODO Auto-generated method stub
		super.onMessageReceived(messages);
		conversationListFragment = new ConversationListFragment();
		conversationListFragment.refresh();
	}
 
	@Override
	public void onAvatarLongClick(String username) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMessageBubbleClick(EMMessage message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMessageBubbleLongClick(EMMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onExtendMenuItemClick(int itemId, View view) {

		switch (itemId) {
		case ITEM_TAKE_PICTURE:
			selectPicFromCamera();
			break;
		case ITEM_PICTURE:
			selectPicFromLocal();
			break;
		case ITEM_LOCATION:
			startActivityForResult(new Intent(getActivity(),
					EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
			break;
		case ITEM_CONTROL:
			Intent intent = new Intent(getActivity(),
					MusicListWithDanceActivity.class).putExtra("groupid",
					toChatUsername);
			startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
			break;

		default:
			break;
		}
		// keep exist extend menu
		return true;
	}

	@Override
	public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
		// TODO Auto-generated method stub
		return null;
	}

}
