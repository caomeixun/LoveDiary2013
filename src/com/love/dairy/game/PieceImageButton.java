package com.love.dairy.game;

import android.content.Context;
import android.graphics.Point;
import android.widget.ImageButton;

import com.love.dairy.utils.LDLog;

public class PieceImageButton extends ImageButton {
	//private Point key;
	//private int lineWidth;   //碎片的内部宽度
	//private int rowHeight;   //碎片的内部高度
	
	private Point minp;
	public int pieceWidth;   //整个碎片的宽度
	public int pieceHeight;  //整个碎片的高度
	
	private Point location;   //保存碎片当前位置
	
	private boolean hasTop = false;
	private boolean hasRight = false;
	private boolean hasFeet = false;
	private boolean hasLeft = false;
	
	private boolean traverse = false;

	
	public PieceImageButton(Context context) {
		super(context);
	}
	public Point getMinp() {
		return minp;
	}

	public void setMinp(Point minp) {
		this.minp = minp;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public boolean isHasTop() {
		return hasTop;
	}

	public void setHasTop(boolean hasTop) {
		this.hasTop = hasTop;
	}

	public boolean isHasRight() {
		return hasRight;
	}

	public void setHasRight(boolean hasRight) {
		this.hasRight = hasRight;
	}

	public boolean isHasFeet() {
		return hasFeet;
	}

	public void setHasFeet(boolean hasFeet) {
		this.hasFeet = hasFeet;
	}

	public boolean isHasLeft() {
		return hasLeft;
	}

	public void setHasLeft(boolean hasLeft) {
		this.hasLeft = hasLeft;
	}

	public boolean isTraverse() {
		return traverse;
	}

	public void setTraverse(boolean traverse) {
		this.traverse = traverse;
	}
	
	public int getSumPath(){
		LDLog.e("getSumPath");
		int sum = 0;
		if(isHasFeet()){
			sum++;
		}
		if(isHasTop()){
			sum++;
		}
		if(isHasRight()){
			sum++;
		}
		if(isHasLeft()){
			sum++;
		}
		return sum;
	}

}
