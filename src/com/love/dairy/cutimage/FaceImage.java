package com.love.dairy.cutimage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class FaceImage {
	private Bitmap mFace;
	private RectF mRectF;
	private float scale=1f;
	private float lastScale = 1f;
	public FaceImage(Bitmap face, RectF rectF) {
		this.mFace = face;
		this.mRectF = rectF;
	}
	public void setMatrix(float scale){
		this.scale = scale;
	}
	public void setLastSacle(){
		this.lastScale *= scale;
	}
	public void draw(Canvas canvas) {
		canvas.save();
		/*src原始图片绘画部分*/
		/*dst画多大和坐标部分*/ 
		canvas.drawBitmap(mFace, 
				new Rect(0,0,mFace.getWidth(),mFace.getHeight()),
				new RectF(mRectF.left,mRectF.top,mRectF.left + mFace.getWidth() * scale * lastScale , mRectF.top + mFace.getHeight() * scale * lastScale), null);
		canvas.restore();
	}

	public void move(int x, int y) {
		mRectF.left = x - mFace.getWidth() * scale * lastScale/ 2;
		mRectF.top = y - mFace.getHeight() * scale * lastScale/ 2;
		mRectF.right = x + mFace.getWidth() * scale * lastScale/ 2;
		mRectF.bottom = y + mFace.getHeight() * scale * lastScale/ 2;
	}

	public Bitmap getmFace() {
		return mFace;
	}

	public void setmFace(Bitmap mFace) {
		this.mFace = mFace;
	}

	public RectF getmRectF() {
		return mRectF;
	}

	public void setmRectF(RectF mRectF) {
		this.mRectF = mRectF;
	}
}
