package com.love.dairy.game;

import android.content.Context;
import android.graphics.Point;
import android.widget.ImageButton;

public class PieceImageButton extends ImageButton {
	//private Point key;
	//private int lineWidth;   //��Ƭ���ڲ����
	//private int rowHeight;   //��Ƭ���ڲ��߶�
	
	private Point minp;
	public int pieceWidth;   //������Ƭ�Ŀ��
	public int pieceHeight;  //������Ƭ�ĸ߶�
	
	private Point location;   //������Ƭ��ǰλ��
	
	private boolean hasTop = false;
	private boolean hasRight = false;
	private boolean hasFeet = false;
	private boolean hasLeft = false;
	
	private boolean traverse = false;
	public boolean isAbsort = false;
	
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
	


}
