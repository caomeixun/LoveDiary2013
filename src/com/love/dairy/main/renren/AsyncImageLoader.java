package com.love.dairy.main.renren;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class AsyncImageLoader {
	//SoftReference是软引用，是为了更好的为了系统回收变�?
    private static Map<String, SoftReference<Drawable>> imageCache=new HashMap<String, SoftReference<Drawable>>();
    public AsyncImageLoader() {
    }
    
    public Drawable loadDrawable(final String userName,final String imageUrl,final ImageView imageView, final ImageCallback imageCallback){
    	if (imageCache.containsKey(userName)) {
            //从缓存中获取
            SoftReference<Drawable> softReference = imageCache.get(userName);
            Drawable drawable = softReference.get();
            if (drawable != null) {
            	imageView.setImageBitmap(((BitmapDrawable)drawable).getBitmap());
                return null;
            }
        }
	        final Handler handler = new Handler() {
	            public void handleMessage(Message message) {
	                imageCallback.imageLoaded((Drawable) message.obj, imageView,imageUrl);
	            }
	        };
	        //建立新一个新的线程下载图�?
	        new Thread() {
	            @Override
	            public void run() {
	                Drawable drawable = loadImageFromUrl(imageUrl);
	                imageCache.put(userName, new SoftReference<Drawable>(drawable));
	                Message message = handler.obtainMessage(0, drawable);
	                handler.sendMessage(message);
	            }
	        }.start();
        
        return null;
    }
    
    @SuppressWarnings("deprecation")
	public static Drawable loadImageFromUrl(String url){
        URL m;
        InputStream i = null;
        try {
            m = new URL(url);
            i = (InputStream) m.getContent();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Drawable d = Drawable.createFromStream(i, "src");
        return new BitmapDrawable(BitmapFactory.decodeStream(i));
    }
    
    //回调接口
    public interface ImageCallback {
        public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl);
    }
}