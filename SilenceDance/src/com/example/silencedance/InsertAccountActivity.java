package com.example.silencedance;

import java.util.Calendar;
import java.util.TimeZone;

import com.example.sqlite.BilldbHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class InsertAccountActivity extends Activity implements OnClickListener {

	private EditText edittext_acctitem, EditTextDESC, Fee;
	private TextView mDate, mTime;
	static final int RG_REQUEST = 0;

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	private String today;
	private Cursor cur;
	private SimpleCursorAdapter mAdapter;

	private Spinner s1;
	private Button BtnDate, BtnTime, BtnCancel, BtnSave;

	private BilldbHelper billdb;

	private int acctitemid = -1;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.activity_insert_account);

		// 初始化控件
		init();

		// 控件绑定点击
		bindClick();

		// 获取当前时间
		initTime();

		// 显示时间
		setDatetime();

		set();

	}

	// 初始化控件
	private void init() {
		edittext_acctitem = (EditText) findViewById(R.id.edittext_acctitem);
		EditTextDESC = (EditText) findViewById(R.id.EditTextDESC);
		Fee = (EditText) findViewById(R.id.Fee);
		BtnDate = (Button) findViewById(R.id.BtnDate);
		BtnTime = (Button) findViewById(R.id.BtnTime);
		BtnCancel = (Button) findViewById(R.id.BtnCancel);
		BtnSave = (Button) findViewById(R.id.BtnSave);
		mDate = (TextView) findViewById(R.id.vdate);
		mTime = (TextView) findViewById(R.id.vtime);
		s1 = (Spinner) findViewById(R.id.Spinner01);
	}

	// 控件绑定点击
	private void bindClick() {
		edittext_acctitem.setOnClickListener(this);
		BtnDate.setOnClickListener(this);
		BtnTime.setOnClickListener(this);
		BtnCancel.setOnClickListener(this);
		BtnSave.setOnClickListener(this);
	}

	// 点击事件
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.edittext_acctitem:
			Editor sharedata = getSharedPreferences("data", 0).edit();
			sharedata.putString("item", "hello getSharedPreferences");
			sharedata.commit();

			Intent intent = new Intent();
			intent.setClass(InsertAccountActivity.this,
					SelectAccountTypeActivity.class);
			startActivityForResult(intent, RG_REQUEST);
			break;
		case R.id.BtnTime:
			showDialog(1);
			break;
		case R.id.BtnDate:
			showDialog(2);
			break;
		case R.id.BtnCancel:
			cancel();
			break;
		case R.id.BtnSave:
			save();
			today = mYear + "-" + mMonth;

			cur = billdb.getBills(today);
			mAdapter.changeCursor(cur);
			((SimpleCursorAdapter) mAdapter).notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	private void set() {
		billdb = new BilldbHelper(this);
		String[] from = new String[] { "caption" };
		int[] to = new int[] { android.R.id.text1 };
		Cursor cur = billdb.getUserid();
		SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, cur, from, to);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s1.setAdapter(mAdapter);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RG_REQUEST) {
			if (resultCode == RESULT_CANCELED) {
			} else if (resultCode == RESULT_OK) {
				edittext_acctitem.setText((String) data
						.getCharSequenceExtra("name"));
				acctitemid = Integer.parseInt((String) data
						.getCharSequenceExtra("id"));

			}
		}
	}

	// 取消
	private void cancel() {
		edittext_acctitem.setText("");
		Fee.setText("");
		acctitemid = -1;
		initTime();
		setDatetime();
		EditTextDESC.setText("");
	}

	// 保存
	private void save() {
		if (acctitemid == -1) {
			new AlertDialog.Builder(this).setMessage("请首先选择账目.").show();
			return;
		}
		int fee = 0;
		String s = Fee.getText().toString();
		int pos = s.indexOf(".");
		if (pos > 0) {
			if (s.length() - pos < 3) {
				s = s + "0";
			}
			fee = Integer.parseInt(s.substring(0, pos)
					+ s.substring(pos + 1, pos + 3));
		} else {
			fee = Integer.parseInt(s) * 100;

		}
		if (billdb.Bills_save(acctitemid, fee, (int) s1.getSelectedItemId(),
				((TextView) mDate).getText().toString(), ((TextView) mTime)
						.getText().toString(), EditTextDESC.getText()
						.toString())) {
			Toast.makeText(this, "保存成功.", Toast.LENGTH_SHORT).show();
			cancel();
		} else {
			Toast.makeText(this, "保存失败,请检查数据.", Toast.LENGTH_SHORT).show();
		}
	}

	// 获取当前时间
	private void initTime() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH) + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
	}

	// 显示时间
	private void setDatetime() {
		mDate.setText(mYear + "-" + mMonth + "-" + mDay);
		mTime.setText(pad(mHour) + ":" + pad(mMinute));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);
		case 2:
			return new DatePickerDialog(this, mDateSetListener, mYear,
					mMonth - 1, mDay);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case 1:
			((TimePickerDialog) dialog).updateTime(mHour, mMinute);
			break;
		case 2:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth - 1, mDay);
			break;
		}
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear + 1;
			mDay = dayOfMonth;

			setDatetime();
		}
	};

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			setDatetime();
		}
	};

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}