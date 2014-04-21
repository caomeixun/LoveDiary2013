package com.love.dairy.cutimage;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.love.dairy.LoveApplication;
import com.love.dairy.main.MainActivity;
import com.love.dairy.main.R;
import com.love.dairy.utils.ImageUtil;
import com.love.dairy.widget.FlipCards;


/**
 * 图片编辑类
 * 
 * @author cqli
 * 
 */
public class ImageFilterActivity extends KXActivity {
	private Button mCancel;
	private Button mFinish;
	private ImageButton mBack;
	private ImageButton mForward;
	private ImageView mDisplay;
	private Button mCut;
	private Button mEffect;
	private Button mFace;
	private Button mFrame;

	private String mOldPath;// 旧图片地址
	private Bitmap mOldBitmap;// 旧图片
	private String mNewPath;// 新图片地址
	private Bitmap mNewBitmap;// 新图片
	public static int imageId = -1;
	
	//缓存修改图片部分-----------------------------
	private int nowLocation = -1;
	private List<String> historyPicPaths = new ArrayList<String>();
	//缓存修改图片部分-----------------------------end
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagefilter_activity);
		findViewById();
		setListener();
		init();
	}

	private void findViewById() {
		mCancel = (Button) findViewById(R.id.imagefilter_cancel);
		mFinish = (Button) findViewById(R.id.imagefilter_finish);
		mBack = (ImageButton) findViewById(R.id.imagefilter_back);
		mForward = (ImageButton) findViewById(R.id.imagefilter_forward);
		mDisplay = (ImageView) findViewById(R.id.imagefilter_display);
		mCut = (Button) findViewById(R.id.imagefilter_cut);
		mEffect = (Button) findViewById(R.id.imagefilter_effect);
		mFace = (Button) findViewById(R.id.imagefilter_face);
		mFrame = (Button) findViewById(R.id.imagefilter_frame);
	}

	private void setListener() {
		mCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 关闭当前界面
				finish();
			}
		});
		mFinish.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

	
				// 根据是否选择旧图片返回图片地址
//				String mPath = PhotoUtil.saveToLocal(mNewBitmap,mOldPath);
				LoveApplication application = (LoveApplication) ImageFilterActivity.this.getApplication();	
				Bitmap bit = ImageUtil.decodeSampledBitmapFromResource(getResources(),MainActivity.path + application.photoIds[imageId], MainActivity.screenWidth, MainActivity.screenHeight);
				FlipCards.dateCache.put(imageId,bit);
				finish();
				
			}
		});
		mBack.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 选择旧图片
				nowLocation--;
				setBtnState();
				mDisplay.setImageBitmap(getPhoneAlbum(historyPicPaths.get(nowLocation)));
			}
		});
		mForward.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 选择新图片
				nowLocation++;
				setBtnState();
				mDisplay.setImageBitmap(getPhoneAlbum(historyPicPaths.get(nowLocation)));
			}
		});
		mCut.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 跳转到裁剪界面,并传递图片地址
				Intent intent = new Intent();
				intent.setClass(ImageFilterActivity.this,
						ImageFilterCropActivity.class);
				intent.putExtra("path", (historyPicPaths.get(nowLocation)));
				startActivityForResult(intent,
						ActivityForResultUtil.REQUESTCODE_IMAGEFILTER_CROP);
			}
		});
		mEffect.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 跳转到特效界面,并传递图片地址
				Intent intent = new Intent();
				intent.setClass(ImageFilterActivity.this,
						ImageFilterEffectActivity.class);
				intent.putExtra("path", (historyPicPaths.get(nowLocation)));
				startActivityForResult(intent,
						ActivityForResultUtil.REQUESTCODE_IMAGEFILTER_EFFECT);
			}
		});
		mFace.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 跳转到表情界面,并传递图片地址
				Intent intent = new Intent();
				intent.setClass(ImageFilterActivity.this,
						ImageFilterFaceActivity.class);
				intent.putExtra("path", (historyPicPaths.get(nowLocation)));
				startActivityForResult(intent,
						ActivityForResultUtil.REQUESTCODE_IMAGEFILTER_FACE);
			}
		});
		mFrame.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 跳转到边框界面,并传递图片地址
				Intent intent = new Intent();
				intent.setClass(ImageFilterActivity.this,
						ImageFilterFrameActivity.class);
				intent.putExtra("path", (historyPicPaths.get(nowLocation)));
				startActivityForResult(intent,
						ActivityForResultUtil.REQUESTCODE_IMAGEFILTER_FRAME);
			}
		});
	}

	private void init() {
		// 初始化界面按钮设为不可用
		imageId = getIntent().getIntExtra("path",-1);
		if(imageId  == -1) finish();
		mBack.setEnabled(false);
		mForward.setEnabled(false);
		// 接收传递的图片地址
		LoveApplication application = (LoveApplication) ImageFilterActivity.this.getApplication();	
		mOldPath = MainActivity.path + application.photoIds[imageId];
		mNewPath = MainActivity.path + application.photoIds[imageId];
		mOldBitmap = getPhoneAlbum(mOldPath);
		mNewBitmap = getPhoneAlbum(mNewPath);
		historyPicPaths.add(mOldPath);
		nowLocation = 0;
		// 显示图片
		mDisplay.setImageBitmap(mOldBitmap);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			// 接收修改后的图片地址,并更新
			mNewPath = data.getStringExtra("path");
			mNewBitmap = getPhoneAlbum(mNewPath);
			while(nowLocation != historyPicPaths.size() -1){
				historyPicPaths.remove(historyPicPaths.size()-1);
			}
			nowLocation = historyPicPaths.size() -1;
			historyPicPaths.add(mNewPath);
			nowLocation++;
			setBtnState();
			mDisplay.setImageBitmap(mNewBitmap);

		}
	}
	/**
	 * 设置返回按钮图片样式
	 */
	private void setBtnState(){
		if(nowLocation > 0) {
			mBack.setEnabled(true);
		}
		else  {
			mBack.setEnabled(false);
		}
		if(nowLocation < historyPicPaths.size()-1){
			mForward.setEnabled(true);
		}
		else {
			mForward.setEnabled(false);
		}
	}
}
