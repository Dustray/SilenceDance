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

		option.maxUsers = 50;// ������Ȧ�������
		option.style = EMGroupStyle.EMGroupStylePublicOpenJoin;
		
		// ��һ�������һ�������б����list��������ӵ�����������б�Ĳ˵���
		list.add("��Ȧ���50��");
		list.add("��Ȧ���200��");
		list.add("��Ȧ���500��");
		list.add("��Ȧ���1000��");
		list.add("��Ȧ���2000��");

		// �ڶ�����Ϊ�����б���һ����������������õ���ǰ�涨���list��
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		// ��������Ϊ���������������б�����ʱ�Ĳ˵���ʽ��
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ���Ĳ�������������ӵ������б���
		circleType.setAdapter(adapter);
		// ���岽��Ϊ�����б����ø����¼�����Ӧ���������Ӧ�˵���ѡ��
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
		// ��һ�������һ�������б����list��������ӵ�����������б�Ĳ˵���
		list2.add("�����飩�����Ⱥ���Ա����");
		list2.add("ֻ�����������û�����");
		list2.add("ֻ�����Һ�Ⱥ��Ա�����û�����");
		list2.add("ֻ�����������û�����; ��Ⱥ��Ա�û��跢����Ⱥ����");

		// �ڶ�����Ϊ�����б���һ����������������õ���ǰ�涨���list��
		adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list2);
		// ��������Ϊ���������������б�����ʱ�Ĳ˵���ʽ��
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ���Ĳ�������������ӵ������б���
		maxMember.setAdapter(adapter2);
		// ���岽��Ϊ�����б����ø����¼�����Ӧ���������Ӧ�˵���ѡ��
		maxMember
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						switch (arg2) {
						case 0:
							option.maxUsers = 50;// ������Ȧ�������
							break;
						case 1:
							option.maxUsers = 200;// ������Ȧ�������
							break;
						case 2:
							option.maxUsers = 500;// ������Ȧ�������
							break;
						case 3:
							option.maxUsers = 1000;// ������Ȧ�������
							break;

						case 4:
							option.maxUsers = 2000;// ������Ȧ�������
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
		 * ����Ⱥ��
		 * 
		 * @param groupName
		 *            Ⱥ������
		 * @param desc
		 *            Ⱥ����
		 * @param allMembers
		 *            Ⱥ���ʼ��Ա�����ֻ���Լ��������鼴��
		 * @param reason
		 *            �����Ա�����reason
		 * @param option
		 *            Ⱥ������ѡ���������Ⱥ������û���(Ĭ��200)��Ⱥ������@see {@link EMGroupStyle}
		 * @return �����õ�group
		 * @throws HyphenateException
		 */

		String groupName = circleName.getText().toString();
		String desc = circleDescription.getText().toString();

		String[] allMembers = {};
		String reason = "";
		try {
			EMClient.getInstance().groupManager()
					.createGroup(groupName, desc, allMembers, reason, option);
			Toast.makeText(CreateCircleActivity.this, "��Ȧ�����ɹ�", Toast.LENGTH_SHORT).show();
		} catch (HyphenateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(CreateCircleActivity.this, "��Ȧ����ʧ��", Toast.LENGTH_SHORT).show();

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
