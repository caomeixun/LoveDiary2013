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
	private ImageView iv = null;
	private TextView tvSub = null;
	private TextView tvTitle = null;
	private Context context = null;
	public Bitmap bitmap = null;
	public String imageName = null;
	public String imagePosition = null;
	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.inflate(getContext(), R.layout.myview, this);
		this.context = context;
		iv = (ImageView) findViewById(R.id.ivImage);
		tvSub = (TextView) findViewById(R.id.tvSubTitle);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
	}
	public void setImage(Bitmap bitmap){
		iv.setImageBitmap(bitmap);
	}
	public void setImage(int rsId){
		Bitmap  bitmap = FlipCards.dateCache.get(rsId);
		if(bitmap!=null && !bitmap.isRecycled()){
			iv.setImageBitmap(bitmap);
		}
		else{
			Log.e("TAG", "去加载图片了");
			BitmapWorkerTask task = new BitmapWorkerTask(context,BitmapWorkerTask.FULL_TYPE,this);
			task.execute(rsId);
		}

	}
	private void setSubText(String str){
		tvSub.setText(str);
	}
	private void setTitle(String str){
		tvTitle.setText(str);
	}
	public void setViewToBitmap(){
		Log.e("TAF","----------------setViewToBitmap");
		bitmap = GrabIt.takeScreenshot(this);
	}
	public void bitmapRelases(){
		bitmap.recycle();
		bitmap = null;
	}
	public void loadInfo(int imagePosition){
		imageName = MainActivity.path+MainActivity.photoIds[imagePosition];
		this.imagePosition = imagePosition+"";
		DataHelper da = new DataHelper(context);
		ImageInfo  info = da.getImageInfo(imageName);
		if(info!=null){
			setTitle(info.title);
			setSubText(info.content);
		}else{
			setTitle(imagePosition + "");
			setSubText(imagePosition + "");
		}
		da.close();
	}
	public void reloadInfo(){
		DataHelper da = new DataHelper(context);
		ImageInfo  info = da.getImageInfo(imageName);
		if(info!=null){
			setTitle(info.title);
			setSubText(info.content);
		}else{
			setTitle(imagePosition + "");
			setSubText(imagePosition + "");
		}
		da.close();
	}
}
