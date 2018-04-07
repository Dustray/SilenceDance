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
			userSex;// 保存用户信息的字符串
	private int Nowyear,Nowmonth,Nowday;//保存当前日期
	private Calendar calendar;   //用于获取当前日期
	private int userAge;
	private EditText etUpdateName, etUpdateWork, etUpdateAddress;
	private RoundedImageView rivUpdateHead;
	private TextView tvAccomplish; // “完成”TextView
	private TextView tvUpdateSex, tvUpdateBirthday;
	private String objectId; // 存储用户objectId
	private String img_url;  //重新选择的头像url
	private String before_img_url;  //一开始头像url
	private User user;

	private static final int IMAGE_CODE = 0;// 打开相册
	private static final int RESIZE_CODE = 2;// 调整大小

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_update);

		Bmob.initialize(this, "e37ca30c22533836329999a7fcf077b8"); // 加载Bmob SDK

		// 初始化控件
		init();

		// 获取登录用户信息
		getUserInfo();

		// 绑定点击事件
		bindClick();
	}

	// 初始化控件
	private void init() {
		etUpdateName = (EditText) findViewById(R.id.etUpdateName);
		rivUpdateHead = (RoundedImageView) findViewById(R.id.rivUpdateHead);
		tvAccomplish = (TextView) findViewById(R.id.tvAccomplish);
		etUpdateWork = (EditText) findViewById(R.id.etUpdateWork);
		etUpdateAddress = (EditText) findViewById(R.id.etUpdateAddress);

		tvUpdateSex = (TextView) findViewById(R.id.tvUpdateSex);
		tvUpdateBirthday = (TextView) findViewById(R.id.tvUpdateBirthday);
	}

	// 绑定点击事件
	private void bindClick() {
		rivUpdateHead.setOnClickListener(this);
		tvAccomplish.setOnClickListener(this);
		tvUpdateSex.setOnClickListener(this);
		tvUpdateBirthday.setOnClickListener(this);
	}

	// 获取登录用户信息
	private void getUserInfo() {
		 // 获得SharedPreferences对象
		 pref = getSharedPreferences("userInfo", MODE_PRIVATE);
		 userName = pref.getString("userName", ""); //取值，第二个参数为默认值
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
			//显示头像
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
			//获取输入的值
			getText();
			break;
		case R.id.rivUpdateHead:
			Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
			galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
			galleryIntent.setType("image/*");// 图片
			startActivityForResult(galleryIntent, IMAGE_CODE); // 跳转，传递打开相册请求码
			break;
		case R.id.tvUpdateSex:
			//选择性别
			selectSex();
			break;
		case R.id.tvUpdateBirthday:
			//日期选择
			selectDate();
			break;
		default:
			break;
		}
	}

	//日期选择
	private void selectDate() {
		calendar=Calendar.getInstance();
		Nowyear=calendar.get(Calendar.YEAR);       //获取年月日时分秒  
		Nowmonth=calendar.get(Calendar.MONTH);   //获取到的月份是从0开始计数
		Nowday=calendar.get(Calendar.DAY_OF_MONTH);    
		
		OnDateSetListener listener=new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker arg0, int year, int month, int day) {
				userAge=Nowyear-year;
				tvUpdateBirthday.setText(year+"-"+(++month)+"-"+day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
				tvUpdateBirthday.setTextColor(Color.rgb(0, 0, 0));
			}
		};
		DatePickerDialog dialog=new DatePickerDialog(UserUpdateActivity.this, 0,listener,Nowyear,Nowmonth,Nowday);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
		dialog.show();
	}

	//选择性别
	private void selectSex() {
		String[] customItems = new String[]{"男", "女"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UserUpdateActivity.this);
        builder.setItems(customItems, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (0 == which) {
                	userSex="男";
                	tvUpdateSex.setTextAlignment(BIND_AUTO_CREATE);
                	tvUpdateSex.setText(userSex);
                } else if (1 == which) {
                	userSex="女";
                	tvUpdateSex.setText(userSex);
                	tvUpdateSex.setTextColor(Color.rgb(0, 0, 0));
                }
            }
        });
        builder.create().show();
	}

	// 获取传递回来的信息
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		} else {
			switch (requestCode) {
			case IMAGE_CODE:
				Uri uri = data.getData();
				resizeImage(uri);
				// 将获取到的uri转换为String型
				String[] images = { MediaStore.Images.Media.DATA };// 将图片URI转换成存储路径
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

	// 重塑图片大小
	public void resizeImage(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");// 可以裁剪
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESIZE_CODE);// 跳转，传递调整大小请求码
	}

	// 显示图片
	private void showResizeImage(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			rivUpdateHead.setImageDrawable(drawable);
		}
	}

	//获取输入的值
	private void getText() {
		// 获取输入值
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

		// 根据手机号查询objectId
		getObjectId();

	}

	// 根据手机号查询objectId
	private void getObjectId() {
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("userPhone", userPhone);
		query.findObjects(UserUpdateActivity.this, new FindListener<User>() {

			@Override
			public void onSuccess(List<User> user) {
				for (User u : user) {
					objectId = u.getObjectId();
				}
				//上传图片
				upload(img_url);
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});

	}
	//图片上传
	private void upload(String imgpath){
		final BmobFile icon=new BmobFile(new File(imgpath));
		icon.upload(this,new UploadFileListener() {
			
			@Override
			public void onSuccess() {
				Toast.makeText(UserUpdateActivity.this,"图片上传成功",Toast.LENGTH_SHORT).show();
				user.setUserHead(icon);
				updateUser();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(UserUpdateActivity.this,"图片上传失败",Toast.LENGTH_SHORT).show();
			}
		});
	}

	//修改用户信息
	private void updateUser() {
		// 根据objectId修改信息
		user.update(UserUpdateActivity.this, objectId, new UpdateListener() {

			@Override
			public void onSuccess() {
				//重新保存当前用户更改的信息
				saveUpdateUser();
				if (ContantsUpdateUserInfo.iupdateuserinfo != null)
					ContantsUpdateUserInfo.iupdateuserinfo.onUpdateUserInfo();
				Toast.makeText(UserUpdateActivity.this, "修改成功",
						Toast.LENGTH_SHORT).show();
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(UserUpdateActivity.this, "修改失败",
						Toast.LENGTH_SHORT).show();

			}
		});
	}

	//重新保存当前用户更改的信息
	private void saveUpdateUser() {
		editor = pref.edit();
		editor.putString("userHead",img_url);   //保存图片url
		editor.putString("userName",userName);
		editor.putString("userPhone",userPhone);
		Log.i("wxy","手机号"+userPhone);
		editor.putString("userWork",userWork);
		editor.putInt("userAge",userAge);
		editor.putString("userBirthday",userBirthday);
		editor.putString("userSex",userSex);
		editor.putString("userAddress",userAddress);
		editor.commit();
	}
}
