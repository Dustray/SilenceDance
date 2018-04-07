package com.example.silencedance;

import java.util.ArrayList;
import java.util.List;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager.EMGroupOptions;
import com.hyphenate.chat.EMGroupManager.EMGroupStyle;
import com.hyphenate.exceptions.HyphenateException;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateCircleActivity extends Activity {

	private List<String> list = new ArrayList<String>();
	private List<String> list2 = new ArrayList<String>();

	private ArrayAdapter<String> adapter, adapter2;

	private EditText circleName;
	private EditText circleDescription;
	private Spinner circleType, maxMember;

	EMGroupOptions option = new EMGroupOptions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_circle);

		circleName = (EditText) findViewById(R.id.circleName);
		circleDescription = (EditText) findViewById(R.id.circleDescription);
		circleType = (Spinner) findViewById(R.id.circleType);
		maxMember = (Spinner) findViewById(R.id.maxMember);

		option.maxUsers = 50;// 设置舞圈最大人数
		option.style = EMGroupStyle.EMGroupStylePublicOpenJoin;
		
		// 第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项
		list.add("舞圈最大50人");
		list.add("舞圈最大200人");
		list.add("舞圈最大500人");
		list.add("舞圈最大1000人");
		list.add("舞圈最大2000人");

		// 第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		// 第三步：为适配器设置下拉列表下拉时的菜单样式。
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 第四步：将适配器添加到下拉列表上
		circleType.setAdapter(adapter);
		// 第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
		circleType
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						switch (arg2) {
						case 0:
							option.style = EMGroupStyle.EMGroupStylePublicOpenJoin;
							break;
						case 1:
							option.style = EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
							break;
						case 2:
							option.style = EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
							break;
						case 3:
							option.style = EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
							break;
						}
						arg0.setVisibility(View.VISIBLE);
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
		// 第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项
		list2.add("（建议）允许非群组成员加入");
		list2.add("只允许我邀请用户加入");
		list2.add("只允许我和群成员邀请用户加入");
		list2.add("只允许我邀请用户加入; 非群成员用户需发送入群申请");

		// 第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
		adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list2);
		// 第三步：为适配器设置下拉列表下拉时的菜单样式。
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 第四步：将适配器添加到下拉列表上
		maxMember.setAdapter(adapter2);
		// 第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
		maxMember
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						switch (arg2) {
						case 0:
							option.maxUsers = 50;// 设置舞圈最大人数
							break;
						case 1:
							option.maxUsers = 200;// 设置舞圈最大人数
							break;
						case 2:
							option.maxUsers = 500;// 设置舞圈最大人数
							break;
						case 3:
							option.maxUsers = 1000;// 设置舞圈最大人数
							break;

						case 4:
							option.maxUsers = 2000;// 设置舞圈最大人数
							break;
						}
						arg0.setVisibility(View.VISIBLE);
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_circle, menu);
		return true;
	}

	public void createCircle(View view) {
		/**
		 * 创建群组
		 * 
		 * @param groupName
		 *            群组名称
		 * @param desc
		 *            群组简介
		 * @param allMembers
		 *            群组初始成员，如果只有自己传空数组即可
		 * @param reason
		 *            邀请成员加入的reason
		 * @param option
		 *            群组类型选项，可以设置群组最大用户数(默认200)及群组类型@see {@link EMGroupStyle}
		 * @return 创建好的group
		 * @throws HyphenateException
		 */

		String groupName = circleName.getText().toString();
		String desc = circleDescription.getText().toString();

		String[] allMembers = {};
		String reason = "";
		try {
			EMClient.getInstance().groupManager()
					.createGroup(groupName, desc, allMembers, reason, option);
			Toast.makeText(CreateCircleActivity.this, "舞圈创建成功", Toast.LENGTH_SHORT).show();
		} catch (HyphenateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(CreateCircleActivity.this, "舞圈创建失败", Toast.LENGTH_SHORT).show();

		}
	}

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
