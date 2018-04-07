package com.example.silencedance;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.bumptech.glide.Glide;
import com.example.contants.ContantsLogin;
import com.example.contants.ContantsUpdateUserInfo;
import com.example.table.User;
import com.makeramen.roundedimageview.RoundedImageView;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserUpdateActivity extends Activity implements OnClickListener {
	private SharedPreferences pref;
	private Editor editor;
	private String userName, userPhone, userWork, userAddress, userBirthday,
			userSex;// �����û���Ϣ���ַ���
	private int Nowyear,Nowmonth,Nowday;//���浱ǰ����
	private Calendar calendar;   //���ڻ�ȡ��ǰ����
	private int userAge;
	private EditText etUpdateName, etUpdateWork, etUpdateAddress;
	private RoundedImageView rivUpdateHead;
	private TextView tvAccomplish; // ����ɡ�TextView
	private TextView tvUpdateSex, tvUpdateBirthday;
	private String objectId; // �洢�û�objectId
	private String img_url;  //����ѡ���ͷ��url
	private String before_img_url;  //һ��ʼͷ��url
	private User user;

	private static final int IMAGE_CODE = 0;// �����
	private static final int RESIZE_CODE = 2;// ������С

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_update);

		Bmob.initialize(this, "e37ca30c22533836329999a7fcf077b8"); // ����Bmob SDK

		// ��ʼ���ؼ�
		init();

		// ��ȡ��¼�û���Ϣ
		getUserInfo();

		// �󶨵���¼�
		bindClick();
	}

	// ��ʼ���ؼ�
	private void init() {
		etUpdateName = (EditText) findViewById(R.id.etUpdateName);
		rivUpdateHead = (RoundedImageView) findViewById(R.id.rivUpdateHead);
		tvAccomplish = (TextView) findViewById(R.id.tvAccomplish);
		etUpdateWork = (EditText) findViewById(R.id.etUpdateWork);
		etUpdateAddress = (EditText) findViewById(R.id.etUpdateAddress);

		tvUpdateSex = (TextView) findViewById(R.id.tvUpdateSex);
		tvUpdateBirthday = (TextView) findViewById(R.id.tvUpdateBirthday);
	}

	// �󶨵���¼�
	private void bindClick() {
		rivUpdateHead.setOnClickListener(this);
		tvAccomplish.setOnClickListener(this);
		tvUpdateSex.setOnClickListener(this);
		tvUpdateBirthday.setOnClickListener(this);
	}

	// ��ȡ��¼�û���Ϣ
	private void getUserInfo() {
		 // ���SharedPreferences����
		 pref = getSharedPreferences("userInfo", MODE_PRIVATE);
		 userName = pref.getString("userName", ""); //ȡֵ���ڶ�������ΪĬ��ֵ
		 userSex=pref.getString("userSex","");
		 userWork=pref.getString("userWork","");
		 userBirthday=pref.getString("userBirthday","");
		 userAddress=pref.getString("userAddress","");
		 userPhone=pref.getString("userPhone","");
		 userAge=pref.getInt("userAge",0);
		 before_img_url=pref.getString("userHead","");
		 img_url=before_img_url;

		
			etUpdateName.setText(userName);
			etUpdateWork.setText(userWork);
			etUpdateAddress.setText(userAddress);
			tvUpdateSex.setText(userSex);
			tvUpdateSex.setTextColor(Color.rgb(0, 0, 0));
			tvUpdateBirthday.setText(userBirthday);
			tvUpdateBirthday.setTextColor(Color.rgb(0, 0, 0));
		if(before_img_url!=""){
			//��ʾͷ��
			Glide.with(UserUpdateActivity.this).load(before_img_url).into(rivUpdateHead);
		}
	}

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tvAccomplish:
			//��ȡ�����ֵ
			getText();
			break;
		case R.id.rivUpdateHead:
			Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
			galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
			galleryIntent.setType("image/*");// ͼƬ
			startActivityForResult(galleryIntent, IMAGE_CODE); // ��ת�����ݴ����������
			break;
		case R.id.tvUpdateSex:
			//ѡ���Ա�
			selectSex();
			break;
		case R.id.tvUpdateBirthday:
			//����ѡ��
			selectDate();
			break;
		default:
			break;
		}
	}

	//����ѡ��
	private void selectDate() {
		calendar=Calendar.getInstance();
		Nowyear=calendar.get(Calendar.YEAR);       //��ȡ������ʱ����  
		Nowmonth=calendar.get(Calendar.MONTH);   //��ȡ�����·��Ǵ�0��ʼ����
		Nowday=calendar.get(Calendar.DAY_OF_MONTH);    
		
		OnDateSetListener listener=new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker arg0, int year, int month, int day) {
				userAge=Nowyear-year;
				tvUpdateBirthday.setText(year+"-"+(++month)+"-"+day);      //��ѡ���������ʾ��TextView��,��Ϊ֮ǰ��ȡmonthֱ��ʹ�ã����Բ���Ҫ+1������ط���Ҫ��ʾ������+1
				tvUpdateBirthday.setTextColor(Color.rgb(0, 0, 0));
			}
		};
		DatePickerDialog dialog=new DatePickerDialog(UserUpdateActivity.this, 0,listener,Nowyear,Nowmonth,Nowday);//�����������Ϊ��ʾdialogʱĬ�ϵ����ڣ��·ݴ�0��ʼ��0-11��Ӧ1-12����
		dialog.show();
	}

	//ѡ���Ա�
	private void selectSex() {
		String[] customItems = new String[]{"��", "Ů"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UserUpdateActivity.this);
        builder.setItems(customItems, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (0 == which) {
                	userSex="��";
                	tvUpdateSex.setTextAlignment(BIND_AUTO_CREATE);
                	tvUpdateSex.setText(userSex);
                } else if (1 == which) {
                	userSex="Ů";
                	tvUpdateSex.setText(userSex);
                	tvUpdateSex.setTextColor(Color.rgb(0, 0, 0));
                }
            }
        });
        builder.create().show();
	}

	// ��ȡ���ݻ�������Ϣ
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		} else {
			switch (requestCode) {
			case IMAGE_CODE:
				Uri uri = data.getData();
				resizeImage(uri);
				// ����ȡ����uriת��ΪString��
				String[] images = { MediaStore.Images.Media.DATA };// ��ͼƬURIת���ɴ洢·��
				Cursor cursor = this
						.managedQuery(uri, images, null, null, null);
				int index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				img_url = cursor.getString(index);
				break;
			case RESIZE_CODE:
				if (data != null) {
					showResizeImage(data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// ����ͼƬ��С
	public void resizeImage(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");// ���Բü�
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESIZE_CODE);// ��ת�����ݵ�����С������
	}

	// ��ʾͼƬ
	private void showResizeImage(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			rivUpdateHead.setImageDrawable(drawable);
		}
	}

	//��ȡ�����ֵ
	private void getText() {
		// ��ȡ����ֵ
		userName = etUpdateName.getText().toString().trim();
		userWork = etUpdateWork.getText().toString().trim();
		userAddress = etUpdateAddress.getText().toString().trim();
		userBirthday = tvUpdateBirthday.getText().toString().trim();
		userSex = tvUpdateSex.getText().toString().trim();

		user = new User();
		user.setUserName(userName);
		user.setUserWork(userWork);
		user.setUserAddress(userAddress);
		user.setUserBirthday(userBirthday);
		user.setUserSex(userSex);
		user.setUserAge(userAge);

		// �����ֻ��Ų�ѯobjectId
		getObjectId();

	}

	// �����ֻ��Ų�ѯobjectId
	private void getObjectId() {
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("userPhone", userPhone);
		query.findObjects(UserUpdateActivity.this, new FindListener<User>() {

			@Override
			public void onSuccess(List<User> user) {
				for (User u : user) {
					objectId = u.getObjectId();
				}
				//�ϴ�ͼƬ
				upload(img_url);
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});

	}
	//ͼƬ�ϴ�
	private void upload(String imgpath){
		final BmobFile icon=new BmobFile(new File(imgpath));
		icon.upload(this,new UploadFileListener() {
			
			@Override
			public void onSuccess() {
				Toast.makeText(UserUpdateActivity.this,"ͼƬ�ϴ��ɹ�",Toast.LENGTH_SHORT).show();
				user.setUserHead(icon);
				updateUser();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(UserUpdateActivity.this,"ͼƬ�ϴ�ʧ��",Toast.LENGTH_SHORT).show();
			}
		});
	}

	//�޸��û���Ϣ
	private void updateUser() {
		// ����objectId�޸���Ϣ
		user.update(UserUpdateActivity.this, objectId, new UpdateListener() {

			@Override
			public void onSuccess() {
				//���±��浱ǰ�û����ĵ���Ϣ
				saveUpdateUser();
				if (ContantsUpdateUserInfo.iupdateuserinfo != null)
					ContantsUpdateUserInfo.iupdateuserinfo.onUpdateUserInfo();
				Toast.makeText(UserUpdateActivity.this, "�޸ĳɹ�",
						Toast.LENGTH_SHORT).show();
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(UserUpdateActivity.this, "�޸�ʧ��",
						Toast.LENGTH_SHORT).show();

			}
		});
	}

	//���±��浱ǰ�û����ĵ���Ϣ
	private void saveUpdateUser() {
		editor = pref.edit();
		editor.putString("userHead",img_url);   //����ͼƬurl
		editor.putString("userName",userName);
		editor.putString("userPhone",userPhone);
		Log.i("wxy","�ֻ���"+userPhone);
		editor.putString("userWork",userWork);
		editor.putInt("userAge",userAge);
		editor.putString("userBirthday",userBirthday);
		editor.putString("userSex",userSex);
		editor.putString("userAddress",userAddress);
		editor.commit();
	}
}
