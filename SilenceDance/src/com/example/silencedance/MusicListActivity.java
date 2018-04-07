package com.example.silencedance;

import java.util.List;

import com.example.adapter.MusicListAdapter;
import com.example.entity.NowPlayInfo;
import com.example.tools.ScanSDCardReceiver;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * ֮ǰ�����Զ����������б������������ǾͿ�����Activity��ContentResolver��query��������/����ý����Ϣ�ˡ�
 * ������ContentResolver�Ĳ��ҷ���: URI��ָ��Ҫ��ѯ�����ݿ����Ƽ��ϱ������
 * Projection��ָ����ѯ���ݿ���е��ļ��У����ص��α��н�������Ӧ����Ϣ��Null�򷵻�������Ϣ�� selection: ָ����ѯ����
 * selectionArgs
 * ������selection���У���������ǣ����������ʵ��ֵ��������ʺš����selection���û�У��Ļ�����ô���String�������Ϊnull
 * SortOrder��ָ����ѯ���������˳�� ����ʾ��һ���б��� ���Կؼ���ListView��ʵ����Ҫ�ġ�
 * 
 */
public class MusicListActivity extends Activity {
	private int[] _ids;// ��������ID��ʱ����
	private String[] _artists;// ����������
	private String[] _titles;// ������ʱ����
	private String circleID = "";//��ȦID
	private ListView listview;// �б����
	private Button choose_my_circle_btn;//ѡ���ҵ���Ȧ��ť
	private ScanSDCardReceiver receiver = null;// ɨ��SD����ʵ��
	private static final int SCAN = Menu.FIRST;// ��д�˵��ĳ���
	private static final int ABOUT = Menu.FIRST + 1;
	
	Cursor cursor ;
	
	/**
	 * �������������Ϣ���飬1.���⣬2����ʱ��,3.������,4.����id��5.��ʾ����,6.���ݡ�
	 */
	String[] media_info = new String[] { MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME,
			MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_music_list);
		choose_my_circle_btn =  (Button)findViewById(R.id.choose_my_circle_btn);
		listview = (ListView) findViewById(R.id.music_list);// ��ListView��ID
		listview.setOnItemClickListener(new MusicListOnClickListener());// ����һ��ListView����������
		ShowMp3List();// ��ʾ����
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, SCAN, 0, "ɨ��SD��");
		menu.add(1, ABOUT, 1, "����");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == SCAN) {
			ScanSDCard();
		}
		return true;
	}

	/**
	 * ��ʾ�����б�
	 */
	private void ShowMp3List() {
		cursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, media_info, null,
				null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();// ���α��ƶ�����ʼλ��
		_ids = new int[cursor.getCount()];// ����INT��һ����
		_artists = new String[cursor.getCount()];// ����String��һ����
		_titles = new String[cursor.getCount()];// ����String��һ����
		for (int i = 0; i < cursor.getCount(); i++) {
			_ids[i] = cursor.getInt(3);
			_titles[i] = cursor.getString(0);
			_artists[i] = cursor.getString(2);
			cursor.moveToNext();// ���α��Ƶ���һ��
		}
		Log.i("mytag","2"+circleID);
		listview.setAdapter(new MusicListAdapter(this, cursor,_artists,_titles, circleID));// ��setAdapterװ������
	}

	// ����б��¼�
	public class MusicListOnClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
				playMusic(position);// ���ݵ��λ������������
		}

	}

	// �������ַ���
	public void playMusic(int position) {
		Intent intent = new Intent(MusicListActivity.this,
				PlayMusicActivity.class);
		intent.putExtra("_ids", _ids);
		intent.putExtra("_titles", _titles);
		intent.putExtra("_artists", _artists);
		intent.putExtra("position", position);

		NowPlayInfo.setNOW_ids(_ids); // ���浱ǰ���ŵ�������Ϣ
		NowPlayInfo.setNOW_titles(_titles);
		NowPlayInfo.setNOW_artists(_artists);
		NowPlayInfo.setNOW_position(position);

		startActivity(intent);
		// finish();

	}

	// ɨ��SD��
	private void ScanSDCard() {
		IntentFilter filter = new IntentFilter(
				Intent.ACTION_MEDIA_SCANNER_STARTED);
		filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		receiver = new ScanSDCardReceiver();
		filter.addDataScheme("file");
		registerReceiver(receiver, filter);
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
				Uri.parse("file://"
						+ Environment.getExternalStorageDirectory()
								.getAbsolutePath())));
	}

	public void chooseMyCircle(View view) {

        Intent intent=new Intent(this,MyCircleListDialogActivity.class);  
        startActivityForResult(intent,1);   //��MainActivityҳ��������붨λ1  
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){  
        case 1:         //�ж��ǲ�����MainActivityҳ���������ģ��������ִ��case���  
            if(resultCode==RESULT_OK){        //�ж��ǲ�����NewActivityҳ�������ת�����ģ�����������if���  
                if(data!=null){  
                    String returnCircleName=data.getStringExtra("return_data_circle_name");
                    String returnCircleID=data.getStringExtra("return_data_circle_id");
                    //Log.i("mytag",returnCircleName+returnCircleID);
        	        choose_my_circle_btn.setText(returnCircleName);
        	        circleID = returnCircleID;
        			Log.i("mytag","1"+circleID);
        			listview.setAdapter(new MusicListAdapter(this, cursor,_artists,_titles, circleID));// ��setAdapterװ������
                }else{
                	
                }
            }  
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