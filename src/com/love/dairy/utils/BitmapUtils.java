package com.love.dairy.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;

public class BitmapUtils {
    
    public static Bitmap createReflectionImageWithOrigin(Bitmap originalImage) {
    	  // The gap we want between the reflection and the original image
    	  final int reflectionGap = 4;
    	 
    	  // Get you bit map from drawable folder
    	  // Bitmap originalImage = BitmapFactory.decodeResource(getResources(),
    	  // R.drawable.twitter_icon);
    	 
    	  int width = originalImage.getWidth();
    	  int height = originalImage.getHeight();
    	 
    	  // This will not scale but will flip on the Y axis
    	  Matrix matrix = new Matrix();
    	  matrix.preScale(1, -1);
    	 
    	  // Create a Bitmap with the flip matix applied to it.
    	  // We only want the bottom half of the image
    	  Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
    	    height / 2, width, height / 2, matrix, false);
    	 
    	  // Create a new bitmap with same width but taller to fit reflection
    	  Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
    	    (height + height / 2), Config.ARGB_8888);
    	 
    	  // Create a new Canvas with the bitmap that's big enough for
    	  // the image plus gap plus reflection
    	  Canvas canvas = new Canvas(bitmapWithReflection);
    	  // Draw in the original image
    	  canvas.drawBitmap(originalImage, 0, 0, null);
    	  // Draw in the gap
    	  Paint deafaultPaint = new Paint();
    	  canvas
    	    .drawRect(0, height, width, height + reflectionGap,
    	      deafaultPaint);
    	  // Draw in the reflection
    	  canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
    	 
    	  // Create a shader that is a linear gradient that covers the reflection
    	  Paint paint = new Paint();
    	  LinearGradient shader = new LinearGradient(0,
    	    originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
    	      + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
    	  // Set the paint to use this shader (linear gradient)
    	  paint.setShader(shader);
    	  // Set the Transfer mode to be porter duff and destination in
    	  paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
    	  // Draw a rectangle using the paint with our linear gradient
    	  canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
    	    + reflectionGap, paint);
    	 
    	  return bitmapWithReflection;
    	 }
    	 
    	 public static Bitmap createReflectionImageWithNoOrigin(Bitmap originalImage) {
       	  // The gap we want between the reflection and the original image
       	  final int reflectionGap = 2;
       	 
       	  // Get you bit map from drawable folder
       	  // Bitmap originalImage = BitmapFactory.decodeResource(getResources(),
       	  // R.drawable.twitter_icon);
       	 
       	  int width = originalImage.getWidth();
       	  int height = originalImage.getHeight();
       	 
       	  // This will not scale but will flip on the Y axis
       	  Matrix matrix = new Matrix();
       	  matrix.preScale(1, -1);
       	 
       	  // Create a Bitmap with the flip matix applied to it.
       	  // We only want the bottom half of the image
       	  Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
       	    height / 2, width, height / 2, matrix, false);
       	 
       	  // Create a new bitmap with same width but taller to fit reflection
       	  Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
       	    (height / 2), Config.ARGB_8888);
       	 
       	  // Create a new Canvas with the bitmap that's big enough for
       	  // the image plus gap plus reflection
       	  Canvas canvas = new Canvas(bitmapWithReflection);
       	  // Draw in the original image
       	  //canvas.drawBitmap(originalImage, 0, 0, null);
       	  height=8;
       	  // Draw in the gap
       	  Paint deafaultPaint = new Paint();
       	  canvas
       	    .drawRect(0, height, width, height + reflectionGap,
       	      deafaultPaint);
       	  // Draw in the reflection
       	  canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
       	 
       	  // Create a shader that is a linear gradient that covers the reflection
       	  Paint paint = new Paint();
       	  LinearGradient shader = new LinearGradient(0,
       			height, 0, bitmapWithReflection.getHeight()
       	      + reflectionGap, 0x90ffffff, 0x00ffffff, TileMode.CLAMP);
       	  // Set the paint to use this shader (linear gradient)
       	  paint.setShader(shader);
       	  // Set the Transfer mode to be porter duff and destination in
       	  paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
       	  // Draw a rectangle using the paint with our linear gradient
       	  canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
       	    + reflectionGap, paint);
       	 
       	  return bitmapWithReflection;
    	 }
    	 
    	 public static Bitmap setAlpha(Bitmap sourceImg, int number) {
    	  int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
    	  sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg
    	    .getWidth(), sourceImg.getHeight());//Get the ARGB Value of the image
    	  number = number * 255 / 100;
    	  for (int i = 0; i < argb.length; i++) {
    	   //argb = (number << 24) | (argb & 0x00ffffff);// modify the top two bit value
    	  }
    	  sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg
    	    .getHeight(), Config.ARGB_8888);
    	 
    	  return sourceImg;
    	 }
    	 
    	 /**
    	  * get reflection bitmap of the original bitmap.
    	  * 
    	  * @param srcBitmap
    	  * @return
    	  */
    	 public static Bitmap makeReflectionBitmap(Bitmap originalImage) {
    	 
    	  int width = originalImage.getWidth();
    	  int height = originalImage.getHeight();
    	 
    	  // This will not scale but will flip on the Y axis
    	  Matrix matrix = new Matrix();
    	  matrix.preScale(1, -1);
    	 
    	  // Create a Bitmap with the flip matix applied to it.
    	  // We only want the bottom half of the image
    	  int divider = 8;
    	 
    	  Bitmap srcBitmap = Bitmap.createBitmap(originalImage, 0, height
    	    / divider * (divider - 1), width, height / divider, matrix,
    	    false);
    	 
    	  //srcBitmap=originalImage;
    	  int bmpWidth = srcBitmap.getWidth();
    	  int bmpHeight = srcBitmap.getHeight();
    	  int[] pixels = new int[bmpWidth * bmpHeight * 4];
    	  srcBitmap.getPixels(pixels, 0, bmpWidth, 0, 0, bmpWidth, bmpHeight);
    	 
    	  // get reversed bitmap
    	  Bitmap reverseBitmap = Bitmap.createBitmap(bmpWidth, bmpHeight,
    	    Bitmap.Config.ARGB_8888);
    	  for (int y = 0; y < bmpHeight; y++) {
    	   reverseBitmap.setPixels(pixels, y * bmpWidth, bmpWidth, 0,
    	     bmpHeight - y - 1, bmpWidth, 1);
    	  }
    	 
    	  // get reflection bitmap based on the reversed one
    	  reverseBitmap.getPixels(pixels, 0, bmpWidth, 0, 0, bmpWidth, bmpHeight);
    	  Bitmap reflectionBitmap = Bitmap.createBitmap(bmpWidth, bmpHeight,
    	    Bitmap.Config.ARGB_8888);
    	  int alpha = 0x00000000;
    	  for (int y = 0; y < bmpHeight; y++) {
    	   for (int x = 0; x < bmpWidth; x++) {
    	    int index = y * bmpWidth + x;
    	    int r = (pixels[index] >> 16) & 0xff;
    	    int g = (pixels[index] >> 8) & 0xff;
    	    int b = pixels[index] & 0xff;
    	 
    	    pixels[index] = alpha | (r << 16) | (g << 8) | b;
    	 
    	    reflectionBitmap.setPixel(x, y, pixels[index]);
    	   }
    	   alpha = alpha + 0x01000000;
    	  }
    	  return reflectionBitmap;
    	 }
    	 
    	 
    	 /**
    	  * Set the Bitmap alpha
    	  * @param srcBitmap
    	  * @return
    	  */
    	 public static Bitmap setAlpha(Bitmap srcBitmap){
    	  int bmpWidth = srcBitmap.getWidth();
    	  int bmpHeight = srcBitmap.getHeight();
    	  int[] pixels = new int[bmpWidth * bmpHeight * 4];
    	  srcBitmap.getPixels(pixels, 0, bmpWidth, 0, 0, bmpWidth, bmpHeight);
    	  // get reflection bitmap based on the reversed one
    	  // Bitmap reverseBitmap = Bitmap.createBitmap(bmpWidth, bmpHeight,
    	  // Bitmap.Config.ARGB_8888);
    	  // srcBitmap.getPixels(pixels, 0, bmpWidth, 0, 0, bmpWidth, bmpHeight);
    	  Bitmap reflectionBitmap = Bitmap.createBitmap(bmpWidth, bmpHeight,
    	    Bitmap.Config.ARGB_8888);
    	  //int alpha = 0x88ffffff/10;
    	  int alpha = 0x01000000*100;
    	  for (int y = 0; y < bmpHeight; y++) {
    	   for (int x = 0; x < bmpWidth; x++) {
    	    int index = y * bmpWidth + x;
    	    int r = (pixels[index] >> 16) & 0xff;
    	    int g = (pixels[index] >> 8) & 0xff;
    	    int b = pixels[index] & 0xff;
    	 
    	    pixels[index] = alpha | (r << 16) | (g << 8) | b;
    	 
    	    reflectionBitmap.setPixel(x, y, pixels[index]);
    	   }
    	 
    	   alpha = alpha - 0x01000000*10;
    	 
    	  }
    	  return reflectionBitmap;
    	 }
    	 
    	 
    	 public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
    		 if(bitmap!=null)
    		 {
		    	  Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
		    	    .getHeight(), Config.ARGB_8888);
		    	  Canvas canvas = new Canvas(output);
		    	 
		    	  final int color = 0xff424242;
		    	  final Paint paint = new Paint();
		    	  final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		    	  final RectF rectF = new RectF(rect);
		    	  final float roundPx = 10;
		    	 
		    	  paint.setAntiAlias(true);
		    	  canvas.drawARGB(0, 0, 0, 0);
		    	  paint.setColor(color);
		    	  canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		    	 
		    	  paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		    	  canvas.drawBitmap(bitmap, rect, rect, paint);
		    	 
		    	  return output;
    		 }
    		 return null;
    	 }

    	 /** 
    	     * create the bitmap from a byte array 
    	     *���ˮӡͼ�?
    	     * @param src the bitmap object you want proecss 
    	     * @param watermark the water mark above the src 
    	     * @return return a bitmap object ,if paramter's length is 0,return null 
    	     */  
    	    public static Bitmap createSignBitmap( Bitmap src, Bitmap watermark )  
    	    {  
    	        if( src == null )  
    	        {  
    	            return null;  
    	        }  
    	   
    	        int srcWidth = src.getWidth();  
    	        int srcHeight = src.getHeight();  
    	        int waterWidth = watermark.getWidth();  
    	        int waterHeight = watermark.getHeight();  
    	        //create the new blank bitmap  
    	        Bitmap newb = Bitmap.createBitmap( srcWidth, srcHeight, Config.ARGB_8888 );//����һ���µĺ�SRC���ȿ��һ���λͼ  
    	        Canvas cv = new Canvas( newb );  
    	        //draw src into  
    	        cv.drawBitmap( src, 0, 0, null );//�� 0��0��꿪ʼ����src  
    	        //draw watermark into  
    	        cv.drawBitmap( watermark, (srcWidth - waterWidth)/2, (srcHeight- waterHeight)/2, null );//��src�����½ǻ���ˮӡ  
    	        //save all clip  
    	        cv.save( Canvas.ALL_SAVE_FLAG );//����  
    	        //store  
    	        cv.restore();//�洢  
    	        return newb;  
    	    }  
    	    
    	    /*** 
    	    * ͼƬ�����ŷ��� 
    	    * 
    	    * @param bgimage 
    	    * ��ԴͼƬ��Դ 
    	    * @param newWidth 
    	    * �����ź��� 
    	    * @param newHeight 
    	    * �����ź�߶�?
    	    * @return 
    	    */  
    	    public static Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {  
    	    // ��ȡ���ͼƬ�Ŀ�͸�  
    	    int width = bgimage.getWidth();  
    	    int height = bgimage.getHeight();  
    	    // ��������ͼƬ�õ�matrix����  
    	    Matrix matrix = new Matrix();  
    	    // ���������ʣ��³ߴ��ԭʼ�ߴ�? 
    	    float scaleWidth = ((float) newWidth) / width;  
    	    float scaleHeight = ((float) newHeight) / height;  
    	    // ����ͼƬ����  
    	    matrix.postScale(scaleWidth, scaleHeight);  
    	    Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,  
    	    matrix, true);  
    	    return bitmap;  
    	    }  
    	    
    	    public static byte[] changeBitmapToByte(Bitmap bm)
    	    {
    	    	ByteArrayOutputStream baOut=new ByteArrayOutputStream();
    	    	bm.compress(CompressFormat.PNG, 100, baOut);
    	    	return baOut.toByteArray();
    	    }
}
