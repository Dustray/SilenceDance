package com.example.easemob;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

public class JoinCircle {
	public void joinCircle(String groupid, String reason)
			throws HyphenateException {
		// ���Ⱥ��Ⱥ�����ɼ���ģ���group.isMembersOnly()Ϊfalse��ֱ��join
 
		EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(groupid);
		if (group.isMembersOnly() == false)
			EMClient.getInstance().groupManager().joinGroup(groupid);// ���첽����
		// ��Ҫ�������֤���ܼ���ģ���group.isMembersOnly()Ϊtrue���������淽��
		else
			EMClient.getInstance().groupManager()
					.applyJoinToGroup(groupid, reason);// ���첽����
	}
}
