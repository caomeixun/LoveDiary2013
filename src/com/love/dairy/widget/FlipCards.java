package com.love.dairy.widget;

 import static javax.microedition.khronos.opengles.GL10.GL_RGBA;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_2D;
import static javax.microedition.khronos.opengles.GL10.GL_UNSIGNED_BYTE;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.MotionEvent;

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

	private static final int STATE_TIP = 0;
	private static final int STATE_TOUCH = 1;

	private Texture[] textures = new Texture[2];
	public static HashMap<Integer, Bitmap> dateCache = new HashMap<Integer, Bitmap>();
	int photoPostion = 0;


	private Card[] topCards = new Card[]{new Card(),new Card()};
	
	

	private float cardAngle = 0f;
	private float vposAngle = 0f;
	MyView flipViews = null;
	MyView flipViewsVpos = null;
	/**
	 * 触屏状态
	 */
	private int state = STATE_TIP;
	Context context = null;
	public FlipCards(FlipViewGroup flipViewGroup,Context context) {
		this.flipViewGroup = flipViewGroup;
		this.context = context;
	}

	public void reloadTexture(MyView flipViews , MyView flipViewsVpos) {
			this.flipViews =flipViews;
			this.flipViewsVpos = flipViewsVpos;
	}
	private boolean isLoading = false;
	/**
	 * 重载当前页面
	 */
	public void reloadNowImage(){
		Log.e("TAG", "reloadNowImage");
		if(!isLoading){
			if(flipViews!=null){

					flipViews.setViewToBitmap();
				isLoading = false;
			}
		}
	}

	public void draw(GL10 gl) {

		applyTexture(gl);
		if (textures == null)
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
		default:
			break;
		}
		lastPage(gl);
	}

	private void lastPage(GL10 gl) {
				move(gl);
	}
	
	private boolean isReplay = false;
	public void move(GL10 gl){
//		topBudong.setAngle(angle);
//		topBudong.draw(gl);
		if(topCards[1]!=null){
			cardAngle++;
			if(cardAngle>900){
				cardAngle=0;
			}
		topCards[1].setAngle(cardAngle);
		topCards[1].draw(gl);
		topCards[1].setMoveType();
		}
		if(topCards[0]!=null){
			if(!isReplay){
				vposAngle++;
				if(vposAngle>vposMoveHeight*0.32){
					isReplay = true;
				}
			}else{
				vposAngle-=5;
				if(vposAngle<=0){
					isReplay = false;
				}
			}
		topCards[0].setAngle(vposAngle);
		topCards[0].draw(gl);
		}
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

	private int vposMoveHeight = -1;
	private void applyTexture(GL10 gl) {
		if(flipViews!=null){
			Bitmap bm = flipViews.bitmap;
			if (bm != null) {
				if(bm.isRecycled()){
					Log.e("TAG", "takeScreenshot--isRecycled");
				}
				//Log.e("TAG", flipViews.get(i).imagePosition+"----applyTexture--第"+i);
				if (textures[0] != null){
					textures[0].destroy(gl);
				}
				textures[0] = Texture.createTexture(bm, gl);
				topCards[0].setTexture(textures[0]);
				//bottomCard.setTexture(textures);
				if(topCards[0].getCardVertices() == null){
					Log.e("TAG", "-----------applyTexture--------------");
					topCards[0].setCardVertices(new float[] { 0f,
							bm .getHeight() * 1.5f, 0f, // top left
							0f, bm .getHeight() / 2.0f, 0f, // bottom left
							bm .getWidth(), bm .getHeight() / 2f, 0f, // bottom
																					// right
							bm .getWidth(), bm .getHeight() * 1.5f, 0f // top
																				// right
							});
					topCards[0]
							.setTextureCoordinates(new float[] {
									0f,
									0f,
									0f,
									bm .getHeight() / 1f
											/ (float) textures[0].getHeight(),
											bm .getWidth()
											/ (float) textures[0].getWidth(),
											bm .getHeight() / 1f
											/ (float) textures[0].getHeight(),
											bm .getWidth()
											/ (float) textures[0].getWidth(), 0f });
					

				}
				checkError(gl);
	
				flipViews.bitmapRelases();
				//System.gc();
		}
			
		}
		
		
		if(flipViewsVpos!=null){
			Bitmap bm = flipViewsVpos.bitmap;
			
			if (bm != null) {
				if(bm.isRecycled()){
					Log.e("TAG", "takeScreenshot--isRecycled");
				}
				//Log.e("TAG", flipViews.get(i).imagePosition+"----applyTexture--第"+i);
				if (textures[1] != null){
					textures[1].destroy(gl);
				}
				textures[1] = Texture.createTexture(bm, gl);
				topCards[1].setTexture(textures[1]);
				//bottomCard.setTexture(textures);
				if(topCards[1].getCardVertices() == null){
					vposMoveHeight = bm.getHeight();
					Log.e("TAG", "-----------applyTexture--------------");
					topCards[1].setCardVertices(new float[] { 0f,
							bm .getHeight() * 1.5f, 0f, // top left
							0f, bm .getHeight() / 2.0f, 0f, // bottom left
							bm .getWidth(), bm .getHeight() / 2f, 0f, // bottom
							// right
							bm .getWidth(), bm .getHeight() * 1.5f, 0f // top
							// right
					});
					topCards[1]
					.setTextureCoordinates(new float[] {
							0f,
							0f,
							0f,
							bm .getHeight() / 1f
							/ (float) textures[1].getHeight(),
							bm .getWidth()
							/ (float) textures[1].getWidth(),
							bm .getHeight() / 1f
							/ (float) textures[1].getHeight(),
							bm .getWidth()
							/ (float) textures[1].getWidth(), 0f });
					
					
				}
				checkError(gl);
				
				flipViewsVpos.bitmapRelases();
				//System.gc();
			}
		}
		
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
			if(textures!=null)
				textures = new Texture[2];
	}

	private float lastY = -1;
	private long double_tap_time = 0;
	private float lastRotate =0;
	private float lastUpRotate =0;
	private boolean isCanDo = false;
	public boolean handleTouchEvent(MotionEvent event) {

		if (textures == null){
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
			isCanDo = true;
			return true;
		case MotionEvent.ACTION_MOVE:
			delta = lastY - event.getY();

//			Log.e("TAG", 180 * delta / frontTexture.getContentHeight()
//					* MOVEMENT_RATE+"[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[");
			lastY = event.getY();

			return true;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			xxx = !xxx;
			
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
	
	public static boolean xxx = true;
}
