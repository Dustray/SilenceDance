package com.example.account;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Dialog_edit extends AlertDialog implements OnClickListener {
	private String text = "";
	private EditText edit;
	private OnDateSetListener mCallback;

	private LinearLayout layout;

	public interface OnDateSetListener {

		void onDateSet(String text);
	}

	public Dialog_edit(Context context, String title, String value,
			OnDateSetListener Callback) {
		super(context);
		mCallback = Callback;
		TextView label = new TextView(context);
		label.setText("hint");
		// setView(label);
		edit = new EditText(context);
		edit.setText(value);
		layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		// LinearLayout.LayoutParams param =
		// new LinearLayout.LayoutParams(100, 40);
		// layout.addView(label, param);
		LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(200,
				50);
		layout.addView(edit, param2);
		setView(layout);
		setTitle(title);
		setButton("确定", this);
		setButton2("取消", (OnClickListener) null);

	}

	public void onClick(DialogInterface dialog, int which) {
		// Log.v("cola","U click which="+which);
		text = edit.getText().toString();
		Log.v("cola", "U click text=" + text);
		if (mCallback != null)
			mCallback.onDateSet(text);

	}

}
