package com.love.dairy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.love.dairy.LoveApplication;
import com.love.dairy.main.MainActivity;
import com.love.dairy.widget.FlipCards;
import com.love.dairy.widget.MyView;

public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
	public static int HALF_TYPE = 444;
	public static int FULL_TYPE = 555;
    private int index = 0;
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
        index = params[0];
		LoveApplication application = (LoveApplication) context.getApplicationContext();
        if(type == FULL_TYPE)
        	return ImageUtil.decodeSampledBitmapFromResource(context.getResources(),MainActivity.path+application.photoIds.get(index), MainActivity.screenWidth, MainActivity.screenHeight);
        else{
        	return ImageUtil.decodeSampledBitmapFromResource(context.getResources(),MainActivity.path+application.photoIds.get(index), MainActivity.screenWidth/2, MainActivity.screenHeight/2);
        }
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
    	if(myView!=null){
    		myView.setImage(bitmap);
    		myView.setViewToBitmap();
    	}
        if (FlipCards.dateCache != null && bitmap != null) {
        	if(FlipCards.dateCache.get(index) == null){
        			FlipCards.dateCache.put(index, bitmap);
        	}else{
        		Bitmap rBimtmap = FlipCards.dateCache.get(index);
        		rBimtmap.recycle();
        		rBimtmap = null;
        		FlipCards.dateCache.put(index, bitmap);
        	}
        }
    }
}
