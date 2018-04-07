package com.example.easemob;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

public class JoinCircle {
	public void joinCircle(String groupid, String reason)
			throws HyphenateException {
		// 如果群开群是自由加入的，即group.isMembersOnly()为false，直接join
 
		EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(groupid);
		if (group.isMembersOnly() == false)
			EMClient.getInstance().groupManager().joinGroup(groupid);// 需异步处理
		// 需要申请和验证才能加入的，即group.isMembersOnly()为true，调用下面方法
		else
			EMClient.getInstance().groupManager()
					.applyJoinToGroup(groupid, reason);// 需异步处理
	}
}
