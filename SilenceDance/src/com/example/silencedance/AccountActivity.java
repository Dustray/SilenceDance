package com.example.silencedance;

import java.util.Calendar;
import java.util.TimeZone;

import com.example.sqlite.BilldbHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

public class AccountActivity extends Activity implements
		OnItemLongClickListener, OnClickListener {
	private BilldbHelper billdb;
	private View sv;
	private EditText edit;
	private AbsoluteLayout alayout;
	private int a = 10, b = 10;
	private GridView grd;

	private TextView total;

	private DatePicker dp;
	private Button okbtn, btnInserAccount;
	private ListView lv;

	private int mYear, mMonth, mDay;

	private String today;
	private String[] from;
	private int[] to;

	private SimpleCursorAdapter mAdapter;
	private Cursor cur;
	private int _id;

	protected GridView listHands = null;
	

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.activity_account);

		// 创建数据库
		creatData();

		// 初始化控件
		init();

		// 控件绑定点击
		bindClick();

		// 获取时间
		getDate();

		set();

	}

	// 初始化控件
	private void init() {
		lv = (ListView) findViewById(R.id.listview);
		btnInserAccount = (Button) findViewById(R.id.btnInserAccount);
		total = (TextView) findViewById(R.id.totalitem);
	}

	// 控件绑定点击
	private void bindClick() {
		btnInserAccount.setOnClickListener(this);

	}

	// 创建数据库
	private void creatData() {
		BilldbHelper billdb = new BilldbHelper(this);
		billdb.FirstStart();
		billdb.close();
	}

	// 获取时间
	private void getDate() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH) + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		today = mYear + "-" + mMonth;
	}

	private void set() {
		billdb = new BilldbHelper(this);
		cur = billdb.getBills(today);
		from = new String[] { "rowid", "name", "fee", "sdate", "desc" };
		to = new int[] { R.id.item1, R.id.item2, R.id.item3, R.id.item4,
				R.id.item5 };
		mAdapter = new SimpleCursorAdapter(this, R.layout.account_list_item,
				cur, from, to);
		lv.setAdapter(mAdapter);
		total.setText(billdb.getBillsTotal(today));
		lv.setOnItemLongClickListener(this);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, "选择月份");

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			showDialog("请选择年月:", "");
			return true;
		}
		return false;
	}

	private void showDialog(String title, String text) {
		final DatePickerDialog dia = new DatePickerDialog(this,
				mDateSetListener, mYear, mMonth - 1, mDay);

		dia.show();
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear + 1;
			mDay = dayOfMonth;
			today = mYear + "-" + mMonth;

			cur = billdb.getBills(today);
			mAdapter.changeCursor(cur);
			((SimpleCursorAdapter) mAdapter).notifyDataSetChanged();
		}
	};

	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		_id = (int) id;
		new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage("确定删除该明细?")
				.setIcon(R.drawable.quit)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						billdb.delBills(_id);
						mAdapter.changeCursor(cur);
						
						//删除后重新刷新数据
						today = mYear + "-" + mMonth;
						cur = billdb.getBills(today);
						mAdapter.changeCursor(cur);
						total.setText(billdb.getBillsTotal(today));
						((SimpleCursorAdapter) mAdapter).notifyDataSetChanged();
						
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				}).show();

		return true;

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnInserAccount:
			Intent intent = new Intent(AccountActivity.this,
					InsertAccountActivity.class);
			startActivity(intent);
			break;

		default:
			break;
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