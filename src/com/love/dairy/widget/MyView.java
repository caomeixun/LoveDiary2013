package com.love.dairy.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.love.dairy.LoveApplication;
import com.love.dairy.cutimage.ImageFilterActivity;
import com.love.dairy.main.MainActivity;
import com.love.dairy.main.R;
import com.love.dairy.pojo.ImageInfo;
import com.love.dairy.sql.DataHelper;
import com.love.dairy.utils.BitmapWorkerTask;
import com.love.dairy.utils.LDLog;

public class MyView extends RelativeLayout{
	private ImageView iv = null;
	private TextView tvSub = null;
	private TextView tvTitle = null;
	private Context context = null;
	public Bitmap bitmap = null;
	public String imageName = null;
	public String imagePosition = null;
	private LinearLayout titleBg = null;
	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(getContext(), R.layout.myview, this);
		this.context = context;
		titleBg =  (LinearLayout) findViewById(R.id.textBg);
		iv = (ImageView) findViewById(R.id.ivImage);
		tvSub = (TextView) findViewById(R.id.tvSubTitle);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
	}
	public void setImage(Bitmap bitmap){
		iv.setImageBitmap(bitmap);
		setTitleBg(bitmap);
	}
	public void setImage(int rsId){
		Bitmap  bitmap = FlipCards.dateCache.get(rsId);
		if(bitmap!=null && !bitmap.isRecycled()){
			iv.setImageBitmap(bitmap);
			setTitleBg(bitmap);
		}
		else{
			LDLog.e("TAG", "去加载图片了");
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
		if(ImageFilterActivity.imageId != -1 && imagePosition != null){
			int rsId = Integer.parseInt(imagePosition);
			if(ImageFilterActivity.imageId == rsId )
			{
				setImage(rsId);
				ImageFilterActivity.imageId=-1;
			}
		}
		bitmap = GrabIt.takeScreenshot(this);
//		setImage(null);
	}
	public void bitmapRelases(){
		bitmap.recycle();
		bitmap = null;
	}
	public void loadInfo(int imagePosition){
		LoveApplication application = (LoveApplication) context.getApplicationContext();	
		imageName = MainActivity.path + application.photoIds.get(imagePosition);
		this.imagePosition = imagePosition + "";
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
	private void setTitleBg(Bitmap bitmap) {
			if(bitmap == null) return;
			int color = bitmap.getPixel(20, bitmap.getHeight()-20);
				if(color > 0x888888){
					titleBg.setBackgroundColor(Color.parseColor("#33FFFFFF"));
					
				}else{
					
					titleBg.setBackgroundColor(Color.parseColor("#33000000"));
				}
				
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
