package com.love.dairy.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;

import com.love.dairy.LoveApplication;
import com.love.dairy.sql.DataHelper;
import com.love.dairy.utils.ImageUtil;
import com.love.dairy.widget.FlipCards;
import com.love.dairy.widget.FlipViewGroup;
import com.love.dairy.widget.MyView;

/**
 * 仿flipboard特效
 * 
 * @author chengkai qq:1094226429
 * 
 */
public class MainActivity extends BaseActivity{
	private FlipViewGroup contentView;
	public static int screenHeight;
	public static int screenWidth;
	public static int menuWidth;
	public static int menuHeight;
	public static String path = null;
//	/**
//	 * 标题栏高度
//	 */
//	public static int contentTop = -1;
//	public static int[] photoIds = new int[]{R.drawable.zzz0,R.drawable.zzz1,R.drawable.zzz2,R.drawable.zzz3,R.drawable.zzz4
//			,R.drawable.zzz5,R.drawable.zzz6,R.drawable.zzz7,R.drawable.zzz8};
//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		super.onWindowFocusChanged(hasFocus);
//		Rect frame = new Rect();    
//	    
//		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);    
//		    
//		contentTop= frame.top;  
//	}
	private String lastPath = null;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataHelper da = new DataHelper(getApplicationContext());
		da.close();
		dataRecover();
		screenHeight = getResources().getDisplayMetrics().heightPixels;
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		init();
	}
	private void init(){
		path = getSharedPreferencesData(IMAGE_PATH);
		
		if(lastPath != null && path != null){
			if(path.equals(lastPath)){
				return;
			}
		}
		Log.e("TAG", "path---------------------");
		contentView = new FlipViewGroup(this);
		lastPath = path;
		List<String> strs = null;
		if(path != null){
			File outFile  = new File(path);
			strs =new ArrayList<String>();
			String[] files = outFile.list();
			if(files!=null){
				for(String s : files){
					if(s.indexOf("jpg")!=-1||s.indexOf("png")!=-1){
						strs.add(s);
					}
				}
			}
		}
		LoveApplication application = (LoveApplication) this.getApplication();
		if(strs!=null){
			application.photoIds = new String[]{};
			application.photoIds = strs.toArray(application.photoIds);
		}
		if(path==null|| application.photoIds.length==0){
			Intent intent = new Intent(MainActivity.this,LoginPage.class);
			intent.putExtra(LoginPage.OPEN_TYPE_PATH, 1);
			startActivity(intent);
			return;
		}
		FlipCards.dateCache = new SparseArray<Bitmap>();
		if(application.photoIds.length == 1){
			application.photoIds = new String[]{application.photoIds[0],application.photoIds[0],application.photoIds[0]};
		}else if(application.photoIds.length == 2){
			application.photoIds = new String[]{application.photoIds[0],application.photoIds[1],application.photoIds[0]};
		}
		for (int i=0;i<3;i++) {
			Bitmap bit = ImageUtil.decodeSampledBitmapFromResource(getResources(),path + application.photoIds[i], MainActivity.screenWidth, MainActivity.screenHeight);
			FlipCards.dateCache.put(i,bit);
		}
		MyView my = new MyView(getApplicationContext(),null);
		my.setImage(0);
		my.loadInfo(0);
		MyView my2 = new MyView(getApplicationContext(),null);
		my2.setImage(1);
		my2.loadInfo(1);
		contentView.addFlipView(my2);
		contentView.addFlipView(my);
			
		setContentView(contentView);
		contentView.startFlipping();
		loadMenuSize();
	}
	private void loadMenuSize() {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.menuhover, opts);
		MainActivity.menuHeight = opts.outHeight+ (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 25, getResources()
				.getDisplayMetrics());;
		MainActivity.menuWidth = opts.outWidth;
	}
	@Override
	protected void onResume() {
		super.onResume();
		System.gc();
		init();
		contentView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		contentView.onPause();
	}
	@Override
	public void openOptionsMenu() {
		super.openOptionsMenu();

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(keyCode == KeyEvent.KEYCODE_MENU){
//			startActivity(new Intent(MainActivity.this,LoginPage.class));
//			overridePendingTransition(R.anim.slide_in_from_right,
//					R.anim.stack_push);
//		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
    	screenHeight = getResources().getDisplayMetrics().heightPixels;
    	screenWidth = getResources().getDisplayMetrics().widthPixels;
    	FlipCards.dateCache.clear();
	    super.onConfigurationChanged(newConfig);
	}
	@Override
	protected void onStop() {
//		for (int i : FlipCards.dateCache.keySet()) {
//			if(FlipCards.dateCache.get(i)!=null)
//			FlipCards.dateCache.get(i).recycle();
//		}
//		FlipCards.dateCache = null;
//		finish();
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dataBackup();
	}
	public void login(int imageId){
		Log.e("TAg", "imageId-------"+imageId);
		Intent intent = new Intent(MainActivity.this,LoginPage.class);
		intent.putExtra(LoginPage.IMAGE_ID, imageId);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.stack_push);
	}
	
}
