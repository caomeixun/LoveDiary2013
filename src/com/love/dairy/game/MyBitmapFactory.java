package com.love.dairy.game;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class MyBitmapFactory {
	
	/**
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap DrawableToBitmap(Drawable drawable){
		BitmapDrawable bd = (BitmapDrawable)drawable;
		return bd.getBitmap();
	}
	public static Bitmap cutBitmap(Bitmap bitmap){
		
		return null;
	}
	
	/**
	 * 
	 * @param bitmap
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Drawable BitmapToDrawable(Bitmap bitmap){
		if(null == bitmap){
			return null;
		}
		
		return new BitmapDrawable(bitmap);
	}

	public static Bitmap readBitMap(Context context, int resId, int width, int height){
		Bitmap bitmap = readBitMap(context, resId);
		return Bitmap.createScaledBitmap(bitmap, width, height, true);
	}
	
	public static Drawable readDrawable(Context context, int resId, int width, int height){
		Bitmap bitmap = readBitMap(context, resId);
		bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
		return BitmapToDrawable(bitmap);
	}
	
	/**
	 * ����ʡ�ڴ�ķ�ʽ��ȡ������Դ��ͼƬ
	 * @param context
	 * @param resId
	 * @return bitmap
	 */
	public static Bitmap readBitMap(Context context, int resId){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
		
	}
	
	public static Bitmap createReflectedImage(Context context, int resId){
		int reflectionGap = 4;
		
		Bitmap originalImage = readBitMap(context, resId);
		int width = originalImage.getWidth();   //ԭͼ��
		int height = originalImage.getHeight();   //ԭͼ��
		
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		
		//��ȡԭͼ�°���ͼƬ
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false);

		//����1.5���ߵ�͸��ͼƬ
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);
		
		Canvas canvas = new Canvas(bitmapWithReflection);
		//����ԭͼ
		canvas.drawBitmap(originalImage, 0, 0, null);
		
		Paint paint = new Paint();
		//����ԭͼ��ˮӡ�ļ�϶
		canvas.drawRect(0, height, width, height + reflectionGap, paint);

		//�����°���ͼƬ��Ӱ
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		
		LinearGradient shader = new LinearGradient(0, height, 0, (height + height/2) + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, height, width, (height + height/2) + reflectionGap, paint);
		
		return bitmapWithReflection;
	}
	
}
