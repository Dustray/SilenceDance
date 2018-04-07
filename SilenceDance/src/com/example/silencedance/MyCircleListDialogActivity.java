package com.example.silencedance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.example.adapter.MyCreatedCircleListAdapter;
import com.example.easemob.getMyCircle;
import com.example.silencedance.MusicListActivity.MusicListOnClickListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyCircleListDialogActivity extends Activity {
	private ListView my_created_circle_list;
	private Context context = this;
	private List<EMGroup> myGrouplist;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			List<EMGroup> grouplist = (List<EMGroup>) msg.obj;

			if (grouplist.size() == 0) {
				Toast.makeText(context, "��û�н����κ����", Toast.LENGTH_SHORT).show();
				return;
			}
			setAdapter(grouplist);
			
		}
	};

	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_circle_list_dialog);
		my_created_circle_list = (ListView) findViewById(R.id.my_created_circle_listview);
		getMyCircleList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_circle_list_dialog, menu);
		return true;
	}

	public void getMyCircleList() {

		SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
		String myPhone = pref.getString("userPhone", "");

		final getMyCircle myCircle = new getMyCircle();
		
		// �ӱ��ؼ���Ⱥ���б�
		 //grouplist = new ArrayList<EMGroup>();
		if (myCircle.getMyCircleFromLocal().size() != 0) {
			List<EMGroup> grouplist = myCircle.getMyCircleFromLocal();
			Log.i("mytag","1��ȡ�����б�"+grouplist.get(0).getGroupId());
			setAdapter(grouplist);
			
		} else {
			new Thread() {
				public void run() {
					List<EMGroup> grouplists = myCircle.getMyCircleFromNet(context);
					Message msg = new Message();
					
					msg.obj = grouplists;
					handler.sendMessage(msg);
				}
			}.start();
			
		}
		//int i = 0;
		//ArrayList<EMGroup> newlist = (ArrayList<EMGroup>) grouplist;
		//List<EMGroup> newlist = ArrayList<EMGroup>(grouplist);
		
		/*Iterator<EMGroup> iter = grouplist.iterator();
		 while(iter.hasNext()){
			 EMGroup group = null;
				// if(EMClient.getInstance().groupManager().getGroup(item.getGroupId())
				// != null)
				try {
					group = EMClient.getInstance().groupManager()
							.getGroupFromServer(grouplist.get(i).getGroupId());
				} catch (HyphenateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.i("mytag","2"+e.toString());

				}

				// group.getMembers();//��ȡȺ��Ա �Ժ�������ȡ����
				if (group.getOwner() != myPhone) {// �Ա����
					//grouplist.remove(i);
				}
				i++;
		 }*/
		 
		 /*for(int i=0;i<newlist.size();i++){
			 EMGroup group = null;
				// if(EMClient.getInstance().groupManager().getGroup(item.getGroupId())
				// != null)
				try {
					group = EMClient.getInstance().groupManager()
							.getGroupFromServer(newlist.get(i).getGroupId());
				} catch (HyphenateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.i("mytag","2"+e.toString());

				}

				// group.getMembers();//��ȡȺ��Ա �Ժ�������ȡ����
				if (group.getOwner() != myPhone) {// �Ա����
					grouplist.remove(i);
				}
         }*/
		/*for (EMGroup item : grouplist) {
			EMGroup group = null;
			// if(EMClient.getInstance().groupManager().getGroup(item.getGroupId())
			// != null)
			try {
				group = EMClient.getInstance().groupManager()
						.getGroupFromServer(item.getGroupId());
			} catch (HyphenateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i("mytag","2"+e.toString());

			}

			// group.getMembers();//��ȡȺ��Ա �Ժ�������ȡ����
			if (group.getOwner() != myPhone) {// �Ա����
				grouplist.remove(i);
			}
			i++;
		}*/
	}
	public void setAdapter(List<EMGroup> grouplist){
		MyCreatedCircleListAdapter mccla = new MyCreatedCircleListAdapter(
				context, R.layout.mycirclelistdialog_item, grouplist);
		my_created_circle_list.setAdapter(mccla);
		myGrouplist = grouplist;
		my_created_circle_list.setOnItemClickListener(new MyCircleOnClickListener());// ����һ��ListView����������

	}
	// ����б��¼�
	public class MyCircleOnClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			Intent intent=new Intent();
	        intent.putExtra("return_data_circle_name",myGrouplist.get(position).getGroupName());  
	        intent.putExtra("return_data_circle_id",myGrouplist.get(position).getGroupId());  
	        setResult(RESULT_OK,intent);   //����ҳ���Ŷ���ΪRESULT_OK,Ҳ����-1  
	        finish();     
			
		}

	}
}
