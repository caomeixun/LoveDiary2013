package com.love.dairy.main;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.love.dairy.sql.DataHelper;
import com.love.dairy.utils.FileDownload;
import com.love.dairy.utils.ImageUtil;
import com.love.dairy.widget.FlipCards;
import com.love.dairy.widget.FlipViewGroup;
import com.love.dairy.widget.MyView;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
	public static String path = null;
	/**
	 * 标题栏高度
	 */
	public static int contentTop = -1;
//	public static int[] photoIds = new int[]{R.drawable.zzz0,R.drawable.zzz1,R.drawable.zzz2,R.drawable.zzz3,R.drawable.zzz4
//			,R.drawable.zzz5,R.drawable.zzz6,R.drawable.zzz7,R.drawable.zzz8};
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		Rect frame = new Rect();    
	    
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);    
		
		contentTop= frame.top;  
	}
	public static String[] photoIds = new String[]{};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataHelper da = new DataHelper(getApplicationContext());
		da.close();
		dataRecover();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		screenHeight = getResources().getDisplayMetrics().heightPixels;
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		
		Log.e("TAG", "options.inSampleSize"+dm.heightPixels+"--"+dm.widthPixels);
		Log.e("TAG", "options.inSampleSize"+screenHeight+"--"+screenWidth);
		RelativeLayout layout = new RelativeLayout(this);
		ImageView im = new ImageView(this);
		Bitmap iphone = BitmapFactory.decodeResource(getResources(), R.drawable.iphone);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(iphone.getWidth(),iphone.getHeight());
		params.topMargin=screenHeight/2;
		params.leftMargin = (screenWidth-iphone.getWidth())/2;
		im.setLayoutParams(params);
		im.setBackgroundDrawable(new BitmapDrawable(iphone));
		contentView = new FlipViewGroup(this);
		layout.addView(contentView);

		path = getSharedPreferencesData(IMAGE_PATH);
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
		if(strs!=null){
			photoIds = strs.toArray(photoIds);
		}
		if(path==null || photoIds.length==0){
			Intent intent = new Intent(MainActivity.this,LoginPage.class);
			intent.putExtra(LoginPage.OPEN_TYPE_PATH, 1);
			startActivity(intent);
			return;
		}
		FlipCards.dateCache = new HashMap<Integer, Bitmap>();
			
		Bitmap bit  = BitmapFactory.decodeResource(getResources(), R.drawable.vpos);
		FlipCards.dateCache.put(1,bit);
		
		Bitmap bit2 = BitmapFactory.decodeResource(getResources(), R.drawable.sk);
		FlipCards.dateCache.put(0,bit2);
		MyView my = new MyView(getApplicationContext(),null);
		my.setImage(0);
		MyView my2 = new MyView(getApplicationContext(),null);
		my2.setImage(1);
		contentView.addFlipView(my2);
		contentView.addFlipView(my);
			
		System.gc();
		setContentView(layout);
		contentView.startFlipping();
		layout.addView(im);
		im.bringToFront();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
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
