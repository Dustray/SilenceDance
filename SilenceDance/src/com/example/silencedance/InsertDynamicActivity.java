package com.example.silencedance;

import java.io.File;

import com.example.contants.ContantsDynamic;
import com.example.table.CircleDynamic;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InsertDynamicActivity extends Activity implements OnClickListener {

	private Button btDynamicPublic;
	private EditText etDynamicContent;
	private SharedPreferences pref;
	private Intent intent;
	private ImageView ivDynamicPicture;
	private CircleDynamic dynamicobj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_dynamic);

		Bmob.initialize(this, "e37ca30c22533836329999a7fcf077b8"); // ����Bmob SDK

		// ��ʼ���ؼ�
		init();

		// �󶨵���¼�
		bindClick();
	}

	// ��ʼ���ؼ�
	private void init() {
		btDynamicPublic =  (Button) findViewById(R.id.btDynamicPublic);
		etDynamicContent = (EditText) findViewById(R.id.etDynamicContent);
		ivDynamicPicture=(ImageView) findViewById(R.id.ivDynamicPicture);
	}

	// �󶨵���¼�
	private void bindClick() {
		btDynamicPublic.setOnClickListener(this);
		ivDynamicPicture.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btDynamicPublic:
			submit();
			break;
		case R.id.ivDynamicPicture:
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");// ͼƬ
			startActivityForResult(intent, 1); // ��ת�����ݴ����������
			break;
		default:
			break;
		}

	}

	// ���������������
	private void submit() {
		String dynamicContent = etDynamicContent.getText().toString();
		// ���SharedPreferences����
		pref = getSharedPreferences("userInfo", MODE_PRIVATE);
		String dynamicUserName = pref.getString("userName", "");
		if (dynamicContent.equals("")) {
			Toast.makeText(InsertDynamicActivity.this, "�������ݲ���Ϊ��",
					Toast.LENGTH_SHORT).show();
			return;
		}
		dynamicobj.setDynamicUserName(dynamicUserName);
		dynamicobj.setDynamicContent(dynamicContent);
		dynamicobj.save(InsertDynamicActivity.this, new SaveListener() {

			@Override
			public void onSuccess() {
				if (ContantsDynamic.idynamic != null)
					ContantsDynamic.idynamic.onDynamic();
				Toast.makeText(InsertDynamicActivity.this, "����ɹ�",
						Toast.LENGTH_SHORT).show();
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		} else {
			switch (requestCode) {
			case 1:
				btDynamicPublic.setClickable(false);
				Uri uri = data.getData();
				// ����ȡ����uriת��ΪString��
				String[] images = { MediaStore.Images.Media.DATA };// ��ͼƬURIת���ɴ洢·��
				Cursor cursor = this
						.managedQuery(uri, images, null, null, null);
				int index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				String img_url = cursor.getString(index);
				//��ʾͼƬ
				showPicture(img_url);
				//ͼƬ�ϴ�
				upload(img_url);
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	//��ʾͼƬ
	private void showPicture(String imgpath) {
		ivDynamicPicture.setImageBitmap(BitmapFactory.decodeFile(imgpath));
	}

	//ͼƬ�ϴ�
		private void upload(String imgpath){
			final BmobFile icon=new BmobFile(new File(imgpath));
			icon.upload(this,new UploadFileListener() {
				
				@Override
				public void onSuccess() {
					dynamicobj = new CircleDynamic();
					Toast.makeText(InsertDynamicActivity.this,"ͼƬ�ϴ��ɹ�",Toast.LENGTH_SHORT).show();
					dynamicobj.setDynamicPicture(icon);
					btDynamicPublic.setClickable(true);
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					Toast.makeText(InsertDynamicActivity.this,"ͼƬ�ϴ�ʧ��",Toast.LENGTH_SHORT).show();
				}
			});
		}
	
	public void goBack(View view) {
		finish();
	}
}
