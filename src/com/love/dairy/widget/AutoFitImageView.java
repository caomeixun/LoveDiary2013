package com.love.dairy.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.love.dairy.main.MainActivity;

public class AutoFitImageView extends ImageView{
	
	public AutoFitImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Matrix m = new Matrix();
		Bitmap  bm = ((BitmapDrawable)getDrawable()).getBitmap();
		if(bm!=null && !bm.isRecycled()){
			if(bm.getWidth()  <  MainActivity.screenWidth){
				m.postScale(2.0f, 2.0f);
				canvas.drawBitmap(bm , m, null);
			}
			else{
				m.postScale(1.0f, 1.0f);
				canvas.drawBitmap(bm , m, null);
			}
		}
	}
}
