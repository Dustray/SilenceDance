package com.example.util;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;


public class ImageUtils {
	public static void disPlay(String uri, ImageView image, int default_pic) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(default_pic)
				.showImageForEmptyUri(default_pic)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new SimpleBitmapDisplayer()).build();

		getImageLoader().displayImage(uri, image, options);
	}

	private static ImageLoader imageLoader = ImageLoader.getInstance();

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	public static boolean checkImageLoader() {
		return imageLoader.isInited();
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				context).build();
		imageLoader.init(configuration);
	}
}
