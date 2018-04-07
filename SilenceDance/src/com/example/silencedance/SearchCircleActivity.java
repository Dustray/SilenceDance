package com.example.silencedance;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SearchCircleActivity extends Activity {
	private EditText circleID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_circle);

		circleID = (EditText) findViewById(R.id.circleID);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_circle, menu);
		return true;
	}

	public void searchCircle(View view) {
		
		String id = circleID.getText().toString();
		
		if (id.equals("")) {
			Toast.makeText(SearchCircleActivity.this, "«Î ‰»ÎŒË»¶∫≈",
					Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			Long.parseLong(id);
		} catch (Exception e) {
			Toast.makeText(SearchCircleActivity.this, "ŒË»¶∫≈Œ™¥ø ˝◊÷",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(SearchCircleActivity.this,
				JoinCircleActivity.class);
	    Bundle bundle=new Bundle();
	    bundle.putString("groupid", id);
	    intent.putExtras(bundle);
		startActivity(intent);
	}

	public void goBack(View view) {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
