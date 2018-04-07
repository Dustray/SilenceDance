package com.example.adapter;

import java.util.List;

import com.example.entity.Music;
import com.example.silencedance.R;
import com.example.util.ImageUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class SearchResultListAdapter extends BaseAdapter {
	private List<Music> list;

	private Context context;

	public SearchResultListAdapter(List<Music> list, Context c) {
		super();
		this.list = list;
		this.context = c;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		holder = new ViewHolder();
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.item_online_search_list, null);
			holder.tvMusicName = (TextView) convertView
					.findViewById(R.id.tv_search_list_title);
			holder.tvMusicAritist = (TextView) convertView
					.findViewById(R.id.tv_search_list_airtist);
			holder.ivMusicImage = (ImageView) convertView
					.findViewById(R.id.iv_search_list);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Music music = list.get(position);
		holder.tvMusicName.setText(music.getMusciName());
		holder.tvMusicAritist.setText(music.getAirtistName());
		ImageUtils.disPlay(music.getSmallAlumUrl(), holder.ivMusicImage,
				R.drawable.gv_weather);
		return convertView;
	}

	class ViewHolder {
		TextView tvMusicName, tvMusicAritist;
		ImageView ivMusicImage;
	}

}
