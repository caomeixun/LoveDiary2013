package com.love.dairy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.love.dairy.main.MainActivity;
import com.love.dairy.widget.FlipCards;
import com.love.dairy.widget.MyView;

public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
	public static int HALF_TYPE = 444;
	public static int FULL_TYPE = 555;
    private int data = 0;
    private Context context = null;
    private int type = -1;
    private MyView myView =null;
    public BitmapWorkerTask(Context context,int type) {
    	this.context =context;
    	this.type = type;
    }
    public BitmapWorkerTask(Context context,int type ,MyView myView) {
    	this.context =context;
    	this.type = type;
    	this.myView=myView;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Integer... params) {
        data = params[0];
        if(type == FULL_TYPE)
        	return ImageUtil.decodeSampledBitmapFromResource(context.getResources(),MainActivity.path+MainActivity.photoIds[data], MainActivity.screenWidth, MainActivity.screenHeight);
        else{
        	return ImageUtil.decodeSampledBitmapFromResource(context.getResources(),MainActivity.path+MainActivity.photoIds[data], MainActivity.screenWidth/2, MainActivity.screenHeight/2);
        }
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
    	Log.e("BitmapWorkerTask", "加载完成");
    	if(myView!=null){
    		
    		myView.setImage(bitmap);
    		myView.setViewToBitmap();
    	}
        if (FlipCards.dateCache != null && bitmap != null) {
        	if(FlipCards.dateCache.get(data) == null){
        			FlipCards.dateCache.put(data, bitmap);
        	}else{
        		Bitmap rBimtmap = FlipCards.dateCache.get(data);
        		rBimtmap.recycle();
        		rBimtmap = null;
        		FlipCards.dateCache.put(data, bitmap);
        		Log.e("BitmapWorkerTask", "替换图片完成");
        	}
        }
    }
}
