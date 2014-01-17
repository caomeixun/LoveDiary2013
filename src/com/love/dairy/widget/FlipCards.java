package com.love.dairy.widget;

 import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLU;
import android.util.Log;
import android.view.MotionEvent;

import com.love.dairy.main.MainActivity;
import com.love.dairy.utils.BitmapWorkerTask;

/*
 Copyright 2012 Aphid Mobile

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */

public class FlipCards {
	private FlipViewGroup flipViewGroup;
	private static final float ACCELERATION = 0.618f;
	private static final float TIP_SPEED = 1f;
	private static final float MOVEMENT_RATE = 1.5f;
	private static final int MAX_TIP_ANGLE = 60;

	private static final int STATE_TIP = 0;
	private static final int STATE_TOUCH = 1;
	private static final int STATE_AUTO_ROTATE = 2;
	private static final int STATE_STOP = 3;

	private Texture[] textures = new Texture[2];
	int photoPostion = 0;
	private boolean isChange = false;


	private Card[] topCards = new Card[]{new Card(),new Card()};
	private Card[] bottomCard = new Card[]{new Card(),new Card()};
	private String TAG = "imageUtil";
	
	
	public static HashMap<Integer, Bitmap> dateCache = null;
	//public static HashMap<Integer, SoftReference<Bitmap>> dateCache = null;
	private void loadBitmap(boolean isNext,int position,int maxSize){
		Log.e("TAG", "loadBitmap--------------"+position);
		int loadId = 0;
		if(!isNext){
			loadId =position-2;
		}else{
			loadId =position+2;
		}
			
		if(loadId < 0){
			loadId = Math.abs(maxSize)+loadId;
		}
		if(loadId >= maxSize){
			loadId = Math.abs(loadId-maxSize);
		}
		if(!dateCache.containsKey(loadId)){
			final int postion =  loadId;
			flipViewGroup.context.runOnUiThread( new Runnable() {
				
				@Override
				public void run() {
					BitmapWorkerTask task = new BitmapWorkerTask(FlipCards.this.context,BitmapWorkerTask.HALF_TYPE);
					task.execute(postion);
					Log.e("TAG", "cacheId--------------"+postion);
				}
			});

		}
	}
	private void releaseBitmap(boolean isNext,int loadId,int maxSize){
		if(isNext){
			loadId =loadId-2;
		}else{
			loadId =loadId+2;
		}
		if(loadId < 0){
			loadId = Math.abs(maxSize)+loadId;
		}
		if(loadId >= maxSize){
			loadId = Math.abs(loadId-maxSize);
		}

		//即dataCache中始终只缓存了（M＝6＋Gallery当前可见view的个数）M个bitmap
		freeBitmapFromIndex(loadId);

		}
		private Set claPosition(int postion,int maxSize){
			Set list = new HashSet();
			for(int i=postion;i <= postion+2;i++){
				int index = i;
				if(i >= maxSize){
					index = Math.abs(i-maxSize);
				}
				list.add(index);
			}
			for(int i=postion;i > postion-2;i--){
				int index = i;
				if(i < 0){
					index = Math.abs(maxSize)+i;
				}
				list.add(index);
			}
			list.add(postion);
			return list;
		}


		/**

		* 从某一位置开始释放bitmap资源

		* @param index

		*/



		private void freeBitmapFromIndex(int del) {
			Bitmap delBitmap;
			if(dateCache.get(del)!=null){
				delBitmap = dateCache.get(del);
	
				if(delBitmap != null){
			
					//如果非空则表示有缓存的bitmap，需要清理
			
					Log.v(TAG, "release position:"+ del);
			
					//从缓存中移除该del->bitmap的映射
			
					dateCache.remove(del);
			
					delBitmap.recycle();
					System.gc();
					delBitmap= null;
				}
			}
		}

	private float angle = 0f;
	private boolean forward = true;
	private GL10 gl = null;
	LinkedList<MyView> flipViews = null;
	// private boolean animating = false;
	private int animatedFrame = 0;
	/**
	 * 触屏状态
	 */
	private int state = STATE_TIP;
	Context context = null;
	public FlipCards(FlipViewGroup flipViewGroup,Context context) {
		this.flipViewGroup = flipViewGroup;
		this.context = context;
		bottomCard[0].setAxis(Card.AXIS_TOP);
		topCards[0].setAxis(Card.AXIS_BOTTOM);
		topCards[1].setAxis(Card.AXIS_BOTTOM);
	}

	public void reloadTexture(MyView firstView,MyView secondView) {
		//Log.e("TAG", "reloadTexture");
		if(this.flipViews == null){
			secondView.setViewToBitmap();
			firstView.setViewToBitmap();
			this.flipViews = new LinkedList<MyView>();
			this.flipViews.add(secondView);
			this.flipViews.add(firstView);
		}else{
			//Log.e("TAG", "---------reloadNowImage----------");
			reloadNowImage();
		}
	}
	private boolean isLoading = false;
	/**
	 * 重载当前页面
	 */
	public void reloadNowImage(){
		Log.e("TAG", "reloadNowImage");
		if(!isLoading){
			if(flipViews!=null){

				for(int i=1;i>=0;i--){
					flipViews.get(i).reloadInfo();
					flipViews.get(i).setViewToBitmap();
				}
				isLoading = false;
			}
		}
	}
	public void rotateBy(float delta) {
		angle += delta;
		if (angle > 180)
			angle = 180;
		else if (angle < -180)
			angle = -180;
	}

	public void setState(int state) {
		if (this.state != state) {
			this.state = state;
			animatedFrame = 0;
		}
	}
	private boolean isSetPosition = false;
	public void draw(GL10 gl) {
		applyTexture(gl);
		this.gl = gl;
		if (textures[0] == null)
			return;
		switch (state) {
		case STATE_TIP: {
//			if (angle >= 180)
//				forward = false;
//			else if (angle <= 0)
//				forward = true;
//
//			rotateBy((forward ? TIP_SPEED : -TIP_SPEED));
//			if (angle > 90 && angle <= 180 - MAX_TIP_ANGLE) {
//				forward = true;
//			} else if (angle < 90 && angle >= MAX_TIP_ANGLE) {
//				forward = false;
//			}
		}
			break;
		case STATE_TOUCH:
			break;
		case STATE_STOP:
			if(isSetPosition){
				if(angle>90)
					setPosition(getPosition()+1);
				else if(angle<-90)
					setPosition(getPosition()-1);
				angle=0;
			}
			isSetPosition=  false;
			break;
		case STATE_AUTO_ROTATE: {
			animatedFrame++;
			if(angle>0){
				rotateBy((forward ? ACCELERATION : -ACCELERATION) * animatedFrame);
			}else{
				rotateBy((forward ? -ACCELERATION : ACCELERATION) * animatedFrame);
				if (angle >= 0){
					setState(STATE_STOP);
				}
			}
			if (angle >= 180 || angle <= -180||angle - 0 == 0){
				setState(STATE_STOP);
			}
		}
			break;
		default:
			break;
		}
		//Log.e("TAG",getPosition()+ "angle-------------="+angle);
		if(angle>=0)
			nextPage(gl);
		else
			lastPage(gl);
	}

	private void lastPage(GL10 gl) {

		if (angle >-90) {

				//wu
				move(gl,topCards[claPostion(getPosition(), -1, flipViews.size())],topCards[claPostion(getPosition(), 0, flipViews.size())],bottomCard[claPostion(getPosition(), 0, flipViews.size())],Math.abs(angle));
		} else {
				//tiaozhuan

				move(gl,topCards[claPostion(getPosition(), -1, flipViews.size())],bottomCard[claPostion(getPosition(), -1, flipViews.size())],bottomCard[claPostion(getPosition(), 0, flipViews.size())],180-Math.abs(angle));
		}
//		if(angle+180==0 && isDo){
//			Log.e("TAG", lastUpRotate+"lastUpRotate-lastRotate"+lastRotate);
//			setPosition(getPosition()-1);
//			angle=0;
//		}
	}
	private void nextPage(GL10 gl) {

		if (angle < 90) {
				//无跳转
				move(gl,topCards[claPostion(getPosition(), 0, flipViews.size())],bottomCard[claPostion(getPosition(), 0, flipViews.size())],bottomCard[claPostion(getPosition(), 1, flipViews.size())],angle);
		} else {
				move(gl,topCards[claPostion(getPosition(), 0, flipViews.size())],topCards[claPostion(getPosition(), 1, flipViews.size())],bottomCard[claPostion(getPosition(), 1, flipViews.size())],180-angle);
		}

	}
	public void move(GL10 gl,Card topBudong,Card dongTu,Card ditu,float angle){
		topBudong.setAngle(0);
		topBudong.draw(gl);
		ditu.setAngle(0);
		ditu.draw(gl);
		dongTu.setAngle(angle);
		dongTu.draw(gl);
	}
//	public void nextyuan(GL10 gl,Card buttomBudong,Card ruyuan,Card ditu,float angle){
//		buttomBudong.setAngle(0);
//		buttomBudong.draw(gl);
//		ditu.setAngle(0);
//		ditu.draw(gl);
//		ruyuan.setAngle(angle);
//		ruyuan.draw(gl);
//	}
	int position = 0;
	
	
	public int getPosition() {
		if(position>1){
			position = 0;
		}
		if(position<0)
			position=1;
		return position;
	}
	public void setPosition(int position) {
		Log.e("TAG", "setPosition"+position);
		int lastPostion = getPosition();
		this.position = position;
//		if(position==1)
//		{
//			isChange = true;
//		}
		isChange = true;
		boolean isNextPage = true;
		if(position>lastPostion){
			photoPostion++;
			if(photoPostion >= MainActivity.photoIds.length)
				photoPostion = 0;
			isNextPage= true;
		}else{
			photoPostion--;
			isNextPage = false;
			if(photoPostion < 0)
				photoPostion =MainActivity. photoIds.length-1;
		}
		releaseBitmap(isNextPage,photoPostion, MainActivity.photoIds.length);
		loadBitmap(isNextPage,photoPostion, MainActivity.photoIds.length);
		if(isChange)
		{
			 changeLastTwoView(gl,isNextPage);
		}
	}
	
	private void applyTexture(GL10 gl) {
		if(flipViews!=null)
		for(int i=0;i<flipViews.size();i++){
			if (flipViews.get(i).bitmap != null) {
				int height = flipViews.get(i).bitmap .getHeight();
				int width  = flipViews.get(i).bitmap .getWidth();
				if(width < MainActivity.screenWidth){
					height*=2;
					width*=2;
				}
				//Log.e("TAG", flipViews.get(i).imagePosition+"----applyTexture--第"+i);
				if (textures[i] != null)
					textures[i] .destroy(gl);
				textures[i] = Texture.createTexture(flipViews.get(i).bitmap, gl);
				topCards[i].setTexture(textures[i]);
				bottomCard[i].setTexture(textures[i]);
				if(topCards[i].getCardVertices() == null){
					topCards[i].setCardVertices(new float[] { 0f,
							height, 0f, // top left
							0f, height / 2.0f, 0f, // bottom left
							width, height / 2f, 0f, // bottom
																					// right
							width, height, 0f // top
																				// right
							});
					topCards[i]
							.setTextureCoordinates(new float[] {
									0f,
									0f,
									0f,
									height / 2f
											/ (float) textures[i].getHeight(),
											width
											/ (float) textures[i].getWidth(),
											height / 2f
											/ (float) textures[i].getHeight(),
											width
											/ (float) textures[i].getWidth(), 0f });
					bottomCard[i].setCardVertices(new float[] { 0f,
							height / 2f, 0f, // top left
							0f, 0f, 0f, // bottom left
							width, 0f, 0f, // bottom right
							width, height / 2f, 0f // top
																					// right
							});
					bottomCard[i].setTextureCoordinates(new float[] {
							0f,
							height / 2f
									/ (float) textures[i].getHeight(),
							0f,
							height / (float) textures[i].getHeight(),
							width / (float) textures[i].getWidth(),
							height / (float) textures[i].getHeight(),
							width / (float) textures[i].getWidth(),
							height / 2f
									/ (float) textures[i].getHeight() });
				}
				checkError(gl);
	
				flipViews.get(i).bitmapRelases();
				//Log.e("TAG", "----applyTexture------end"+i);
				System.gc();
			}
		}

	}
	long time =0;
	/**
	 * 更新下一张图片
	 * @param gl
	 */
	private void changeLastTwoView(GL10 gl,final boolean isNextPage) {
		
//		ImageView image = new ImageView(context);
//		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//		bitmapOptions.inSampleSize = 4;
//		image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.kk, bitmapOptions));
//		image.setScaleType(ScaleType.CENTER_CROP);
//		frontBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.kk, bitmapOptions);
//		frontBitmap = BitmapUtils.zoomImage(frontBitmap, 800, 1280);
//		Drawable d = new BitmapDrawable(frontBitmap);
		flipViewGroup.context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				int i = isNextPage ?1:-1;
				MyView my = flipViews.get(claPostion(getPosition(), i, flipViews.size()));
				my.setImage(claPostion(photoPostion, i, MainActivity.photoIds.length));
				my.loadInfo(claPostion(photoPostion, i, MainActivity.photoIds.length));
				Log.e("TAG",claPostion(getPosition(), i, flipViews.size())+ "page："+claPostion(photoPostion, i, MainActivity.photoIds.length));
				my.setViewToBitmap();
			}
		});

		
	}
	/**
	 * 计算索引，首位循环用
	 * @param photoPostion
	 * @param i
	 * @param maxPosition
	 * @return
	 */
	public int claPostion(int photoPostion,int i,int maxPosition){
		int result = photoPostion+i;
		if(result> maxPosition-1){
			return 0;
		}
		if(result<0)
			return maxPosition-1;
		return result;
	}
	public void invalidateTexture() {
		// Texture is vanished when the gl context is gone, no need to delete it
		// explicitly
		for(int i=0;i<textures.length;i++){
			if(textures[i]!=null)
				textures[i] = null;
		}
	}

	private float lastY = -1;
	private long double_tap_time = 0;
	private float lastRotate =0;
	private float lastUpRotate =0;
	private boolean isCanDo = false;
	public boolean handleTouchEvent(MotionEvent event) {
		if (textures[0] == null){
			return false;
		}
		float delta;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastY = event.getY();
//			if(lastRotate-lastUpRotate<-90){
//				setPosition(1);
//			}
//			else if(lastRotate-lastUpRotate>90){
//				setPosition(0);
//			}
			Log.e("TAG", System.currentTimeMillis()- double_tap_time+"MotionEvent");
			if(System.currentTimeMillis()- double_tap_time < 200){
				flipViewGroup.context.login(photoPostion);
			}
			double_tap_time = System.currentTimeMillis();
			lastRotate = 0;
			setState(STATE_TOUCH);
			isCanDo = true;
			return true;
		case MotionEvent.ACTION_MOVE:
			delta = lastY - event.getY();

			rotateBy(180 * delta / textures[0].getContentHeight()
					* MOVEMENT_RATE);
//			Log.e("TAG", 180 * delta / frontTexture.getContentHeight()
//					* MOVEMENT_RATE+"[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[");
			lastY = event.getY();

			return true;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			delta = lastY - event.getY();
	
			rotateBy(180 * delta / textures[0].getContentHeight()
					* MOVEMENT_RATE);
			lastUpRotate = angle;
			if (angle > 90 || (angle < -90)){
				isSetPosition=  true;	
				forward = true;
			}
			else{
				isSetPosition=  false;	
				forward = false;
			}
			setState(STATE_AUTO_ROTATE);
			return true;
		}

		return false;
	}

	public static void checkError(GL10 gl) {
		int error = gl.glGetError();
		if (error != 0) {
			throw new RuntimeException(GLU.gluErrorString(error));
		}
	}
}
