package com.love.dairy.cutimage;

import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

public class FaceImageView extends ImageView {

	private Bitmap mSrc;
	private LinkedList<FaceImage> mFaceImages = new LinkedList<FaceImage>();
	private int mFaceImagePosition = -1;
	private FaceImage mFaceImage;
	public FaceImageView(Context context) {
		super(context);
	}

	public FaceImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FaceImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void addFace(Bitmap face) {
		if (mSrc.getWidth() > 100 && mSrc.getHeight() > 100) {
//			face = Bitmap.createScaledBitmap(face, 100, 100, true);
		} else if (mSrc.getWidth() > 100 && mSrc.getHeight() < 100) {
			face = Bitmap.createScaledBitmap(face, mSrc.getHeight(),
					mSrc.getHeight(), true);
		} else if (mSrc.getHeight() > 100 && mSrc.getWidth() < 100) {
			face = Bitmap.createScaledBitmap(face, mSrc.getWidth(),
					mSrc.getWidth(), true);
		} else {
			face = Bitmap.createScaledBitmap(face, mSrc.getWidth(),
					mSrc.getHeight(), true);
		}

		float left = mSrc.getWidth() / 2 - face.getWidth() / 2;
		float top = mSrc.getHeight() / 2 - face.getHeight() / 2;
		float right = left + face.getWidth();
		float bottom = top + face.getHeight();
		RectF rectF = new RectF(left, top, right, bottom);
		FaceImage faceImage = new FaceImage(face, rectF);
		mFaceImages.addFirst(faceImage);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		for (int i = mFaceImages.size(); i > 0; i--) {
			mFaceImages.get(i - 1).draw(canvas);
		}
		canvas.restore();
	}

	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		mSrc = bm;
	}
	private float oldDist;
	     private PointF midPoint = new PointF();
	     private boolean isZoom = false;
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mFaceImagePosition = getPosition(x, y);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
            oldDist = spacing(event);
            midPoint(midPoint, event);
            isZoom = true;
			break;
		case MotionEvent.ACTION_MOVE:
				if (mFaceImagePosition != -1 && mFaceImage == null) {
					mFaceImage = mFaceImages.get(mFaceImagePosition);
					mFaceImages.remove(mFaceImagePosition);
					mFaceImages.addFirst(mFaceImage);
				}
				if(!isZoom){
					if (mFaceImage != null) {
						mFaceImage.move(x, y);
						invalidate();
					}
				}
				else if(event.getPointerCount()>=2){
				    claMartix(event);
				}
			break;

		case MotionEvent.ACTION_UP:
			mFaceImagePosition = -1;
			if(mFaceImage != null){
				mFaceImage.setLastSacle();
				mFaceImage.setMatrix(1f);
			}
			mFaceImage = null;
			isZoom = false;
			break;
		}
		return true;
	}

	public int getPosition(int x, int y) {
		int position = -1;
		for (int i = 0; i < mFaceImages.size(); i++) {
			if (mFaceImages.get(i).getmRectF().contains(x, y)) {
				position = i;
				break;
			}
		}
		return position;
	}

	public LinkedList<FaceImage> getFaceImages() {
		return mFaceImages;
	}
	/** 计算两个手指间的距离 */
	 @SuppressLint("FloatMath")
	private float spacing(MotionEvent event) {
		          float x = event.getX(0) - event.getX(1);
		          float y = event.getY(0) - event.getY(1);
		          /** 使用勾股定理返回两点之间的距离 */
		          return FloatMath.sqrt(x * x + y * y);
		     }
	 /** 计算两个手指间的中间点 */
		     private void midPoint(PointF point, MotionEvent event) {
		         float x = event.getX(0) + event.getX(1);
		         float y = event.getY(0) + event.getY(1);
		         point.set(x / 2, y / 2);
		     }

	private void claMartix(MotionEvent event) {
		if (isZoom) {
			float newDist = spacing(event);
			/**
			 * 表示新的距离比两个手指刚触碰的距离大 ( +10个像素用来延迟一下放大，不然稍微动一点像素,也放大，感觉也太快了。)
			 */
			if (newDist + 10 > oldDist || newDist + 10 < oldDist) {
				if (mFaceImage != null)
				mFaceImage.setMatrix(newDist/oldDist);
				invalidate();
			}
			
		}
	}
}
