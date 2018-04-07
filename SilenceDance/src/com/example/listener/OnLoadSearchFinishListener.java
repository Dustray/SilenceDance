package com.example.listener;

import java.util.List;

import com.example.entity.Music;

public interface OnLoadSearchFinishListener {
	void onLoadSucess(List<Music> musicList);

	void onLoadFiler();
}
