package com.love.dairy.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.love.dairy.LoveApplication;
import com.love.dairy.sql.DataHelper;
import com.love.dairy.utils.ImageUtil;
import com.love.dairy.widget.AutoFadeOutTextView;
import com.love.dairy.widget.FlipCards;
import com.love.dairy.widget.FlipViewGroup;
import com.love.dairy.widget.MyView;

/**
 * 仿flipboard特效
 * 
 * @author cqli qq:530746075
 * 
 */
public class MainActivity extends BaseActivity{
	private FlipViewGroup contentView;
	public static int screenHeight;
	public static int screenWidth;
	public static int menuWidth;
	public static int menuHeight;
	public static String path = null;
	private Dialog dialog = null;
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
		contentView = new FlipViewGroup(this);
		lastPath = path;
		LoveApplication application = (LoveApplication) this.getApplication();
		if(path != null){
			application.photoIds = new ArrayList<String>();
			File outFile  = new File(path);
			String[] files = outFile.list();
			if(files!=null){
				for(String s : files){
					if(s.indexOf("jpg")!=-1||s.indexOf("png")!=-1){
						application.photoIds.add(s);
					}
				}
			}
		}
	
		if(path==null|| application.photoIds.size()==0){
			choosePhotoPath();
			return;
		}
		FlipCards.dateCache = new SparseArray<Bitmap>();
		if(application.photoIds.size() < 3){
			List<String> addIds = application.photoIds;
			application.photoIds.addAll(addIds);
			application.photoIds.addAll(addIds);
		}
		for (int i=0;i<3;i++) {
			Bitmap bit = ImageUtil.decodeSampledBitmapFromResource(getResources(),path + application.photoIds.get(i), MainActivity.screenWidth, MainActivity.screenHeight);
			FlipCards.dateCache.put(i,bit);
		}
		
		for (int i=1;i>=0;i--) {
			MyView my = new MyView(getApplicationContext(),null);
			my.setImage(i);
			my.loadInfo(i);
			contentView.addFlipView(my);
		}
		RelativeLayout rel = new RelativeLayout(this);
		rel.addView(contentView);
		String recordTime = getSharedPreferencesData("recordTime");
		AutoFadeOutTextView textView = new AutoFadeOutTextView(this, contentView,recordTime);
		setContentView(rel);
		android.widget.RelativeLayout.LayoutParams layoutParams = new android.widget.RelativeLayout.LayoutParams(new LayoutParams(-2, -2));
		layoutParams.leftMargin = screenWidth - screenWidth/3;
		layoutParams.topMargin = screenHeight/4;
		rel.addView(textView,layoutParams);
		loadMenuSize();
	}
	/**
	 * 提示用户选择图片
	 */
	private void choosePhotoPath(){
		if(dialog == null){
			dialog = new AlertDialog.Builder(this).setTitle(R.string.love_dialog_tips).setMessage(getString(R.string.love_dialog_content)).setPositiveButton(getString(R.string.ok_button), new DialogInterface.OnClickListener()
			{
	
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					Intent intent = new Intent(MainActivity.this,LoginPage.class);
					intent.putExtra(LoginPage.OPEN_TYPE_PATH, 1);
					startActivity(intent);
					dialog.dismiss();
				}
	
			}).setNegativeButton(R.string.love_exit, new DialogInterface.OnClickListener()
			{
	
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					finish();
				}
	
			}).show();
		}
	}
	private void loadMenuSize() {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.menuhover, opts);
		MainActivity.menuHeight = opts.outHeight+ (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 25, getResources()
				.getDisplayMetrics());
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
	public void onConfigurationChanged(Configuration newConfig) {
    	screenHeight = getResources().getDisplayMetrics().heightPixels;
    	screenWidth = getResources().getDisplayMetrics().widthPixels;
    	FlipCards.dateCache.clear();
	    super.onConfigurationChanged(newConfig);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dataBackup();
	}
	public void login(int imageId){
		Intent intent = new Intent(MainActivity.this,LoginPage.class);
		intent.putExtra(LoginPage.IMAGE_ID, imageId);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.stack_push);
	}
	
}
