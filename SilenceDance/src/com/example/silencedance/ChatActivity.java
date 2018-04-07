package com.example.silencedance;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.example.silencedance.R;

public class ChatActivity extends EaseBaseActivity{
    public static ChatActivity activityInstance;
    private ChatFragment chatFragment;
    String toChatUsername;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_chat);
        activityInstance = this;
        //user or group id
        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        chatFragment = new ChatFragment();
        //set arguments
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

//
//		EMMessageListener msgListener = new EMMessageListener() {
//
//			@Override
//			public void onMessageReceived(List<EMMessage> messages) {
//				
//				// 收到消息
//				Log.e("mytag","dswa");
//
//				
//
//			}
//
//			@Override
//			public void onCmdMessageReceived(List<EMMessage> messages) {
//				// 收到透传消息
//				Log.e("mytag", "b");
//			}
//
//			@Override
//			public void onMessageReadAckReceived(List<EMMessage> messages) {
//				// 收到已读回执
//				Log.e("mytag", "c");
//			}
//
//			@Override
//			public void onMessageDeliveryAckReceived(List<EMMessage> message) {
//				// 收到已送达回执
//				Log.e("mytag", "d");
//			}
//
//			@Override
//			public void onMessageChanged(EMMessage message, Object change) {
//				// 消息状态变动
//				Log.e("mytag", "e");
//			}
//		};
//		EMClient.getInstance().chatManager().addMessageListener(msgListener);

    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        // enter to chat activity when click notification bar, here make sure only one chat activiy
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }
    
    public String getToChatUsername(){
        return toChatUsername;
    }
}
