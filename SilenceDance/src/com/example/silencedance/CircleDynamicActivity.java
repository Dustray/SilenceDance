package com.example.silencedance;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.example.adapter.CircleDynamicAdapter;
import com.example.entity.CircleDynamicInfo;
import com.example.table.CircleDynamic;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class CircleDynamicActivity extends Activity implements OnClickListener {

	private Intent intent;
	private SharedPreferences pref;
	private ImageButton ibInsertDynamic;
	private ListView lvDynamic;
	private String userName;
	private List<CircleDynamicInfo> dynamicList = new ArrayList<CircleDynamicInfo>();// �������ϴ洢��Ϣ
	private CircleDynamicInfo info = null; // ��CircleDynamicת��ΪCircleDynamicInfo���м�����
	private String img_url = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_dynamic);

		Bmob.initialize(this, "e37ca30c22533836329999a7fcf077b8"); // ����Bmob SDK

		// ��ʼ���ؼ�
		init();

		// �󶨵��
		bindClick();

		// ��ȡ����
		getData();

	}

	// ��ʼ���ؼ�
	private void init() {
		lvDynamic = (ListView) findViewById(R.id.lvDynamic);
		ibInsertDynamic = (ImageButton) findViewById(R.id.ibInsertDynamic);
	}

	// �󶨵��
	private void bindClick() {
		ibInsertDynamic.setOnClickListener(this);
	}

	// ��ȡ����
	private void getData() {
		BmobQuery<CircleDynamic> query = new BmobQuery<CircleDynamic>();
		query.order("-updatedAt");
		query.findObjects(CircleDynamicActivity.this,
				new FindListener<CircleDynamic>() {

					@Override
					public void onSuccess(List<CircleDynamic> dynamics) {
						for (CircleDynamic dynamic : dynamics) {
							info = new CircleDynamicInfo();
							info.setDynamicUserName(dynamic
									.getDynamicUserName());
							info.setDynamicContent(dynamic.getDynamicContent());
							info.setDynamicDate(dynamic.getCreatedAt());
							info.setDynamicDiscuss(dynamic.getDynamicDiscuss());
							if (dynamic.getDynamicPicture() != null) {
								img_url = dynamic.getDynamicPicture().getUrl();
								info.setDynamicPictureUrl(img_url);
							}
							dynamicList.add(info);
							Log.i("wxy", "timeImgUrl=" + img_url);
							img_url = "";// �����ÿ�
						}
						CircleDynamicAdapter adapter = new CircleDynamicAdapter(
								CircleDynamicActivity.this,
								R.layout.circle_dynamic_item, dynamicList);
						lvDynamic.setAdapter(adapter);
					}

					@Override
					public void onError(int arg0, String arg1) {

					}
				});

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ibInsertDynamic:
			// ���SharedPreferences����
			pref = getSharedPreferences("userInfo", MODE_PRIVATE);
			userName = pref.getString("userName", "");
			if (userName != "") {
				intent = new Intent(CircleDynamicActivity.this,
						InsertDynamicActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(CircleDynamicActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}

}
