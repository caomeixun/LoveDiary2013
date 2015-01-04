package com.love.dairy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import android.app.Application;
import android.graphics.Bitmap;

public class LoveApplication extends Application{
	//照片集合
	public List<String> photoIds = new ArrayList<String>();
	//连连看图片集合
	public WeakHashMap<String, Bitmap> matchBitmapCache = new WeakHashMap<String, Bitmap>();
	/**
	 * 清除图片缓存
	 */
	public void releaseMatchBitmapCache(){
		Set<String> strs = matchBitmapCache.keySet();
		for(String s : strs){
			Bitmap bitmap = matchBitmapCache.get(s);
			if(bitmap != null && !bitmap.isRecycled()){
				bitmap.recycle();
				bitmap = null;
			}
		}
		matchBitmapCache.clear();
	}
	
}
