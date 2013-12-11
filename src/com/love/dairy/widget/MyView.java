package com.love.dairy.widget;

import java.util.Random;

import com.love.dairy.main.MainActivity;
import com.love.dairy.main.R;
import com.love.dairy.pojo.ImageInfo;
import com.love.dairy.sql.DataHelper;
import com.love.dairy.utils.BitmapUtils;
import com.love.dairy.utils.BitmapWorkerTask;
import com.love.dairy.utils.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class MyView extends RelativeLayout{
	public ImageView iv = null;
	private TextView tvSub = null;
	private TextView tvTitle = null;
	private Context context = null;
	public Bitmap bitmap = null;
	public String imageName = null;
	public String imagePosition = null;
	private LinearLayout titleBg = null;
	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	public void setImage(Bitmap bitmap){
		this.bitmap = bitmap;
		iv.setImageBitmap(bitmap);
	}
	public boolean setImage(int rsId){
		Bitmap  bm = FlipCards.dateCache.get(rsId);
		if(bm!=null && !bm.isRecycled()){
			Log.e("TAG", "ͼƬ�ڻ�����");
			this.bitmap = bm;
			return false;
		}
		return true;

	}
	/**
	 * ���õײ���ɫɫֵ
	 * @param bitmap
	 */
	private void setTitleBg(Bitmap bitmap) {
		//TODO
		int color = bitmap.getPixel(20, bitmap.getHeight()-20);
		Log.e("TAG", 0x888888+"setTitleBg"+color);
		if(color > 0x888888){
			titleBg.setBackgroundColor(Color.parseColor("#33FFFFFF"));
			
		}else{
			
			titleBg.setBackgroundColor(Color.parseColor("#33000000"));
		}
		
	}
	private void setSubText(String str){
		tvSub.setText(str);
	}
	private void setTitle(String str){
		tvTitle.setText(str);
	}
	public Bitmap setViewToBitmap(){
		Log.e("TAF","----------------setViewToBitmap");
//		bitmap = GrabIt.takeScreenshot(this);
		return null;
	}
	public void bitmapRelases(){
		if(bitmap!=null && !bitmap.isRecycled()){
			Log.e("TAF","----------------bitmapRelases");
		bitmap.recycle();
		bitmap = null;
		}
	}
	
}

