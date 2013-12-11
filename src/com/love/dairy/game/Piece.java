package com.love.dairy.game;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Point;

public class Piece {
	private Point id;  //记录剪切拼图时的点位
	private Point key; //用于记录每块拼图的中心点，吸附判断使用
	private Point minp;  //左上角的点位
	private Point maxp;  //右下角的点位
	
	private int lineWidth;  //不包含凹凸的宽度
	private int rowHeight;  //不包含凹凸的高度
	
	private int pieceWidth;  //包含凹凸的宽度
	private int pieceHeight;  //包含凹凸的高度
	
	private ArrayList apTop = new ArrayList(4);
	private ArrayList apRight = new ArrayList(4);
	private ArrayList apFeet = new ArrayList(4);
	private ArrayList apLeft = new ArrayList(4);

	private Bitmap bmPiece;
	private Bitmap bmEdge;

	public Bitmap getBmEdge() {
		return bmEdge;
	}

	public void setBmEdge(Bitmap bmEdge) {
		this.bmEdge = bmEdge;
	}

	public Point getId() {
		return id;
	}

	public void setId(Point id) {
		this.id = id;
	}

	public Point getKey() {
		return key;
	}

	public void setKey(Point key) {
		this.key = key;
	}

	public ArrayList getApTop() {
		return apTop;
	}

	public void setApTop(ArrayList apTop) {
		this.apTop = apTop;
	}

	public ArrayList getApRight() {
		return apRight;
	}

	public void setApRight(ArrayList apRight) {
		this.apRight = apRight;
	}

	public ArrayList getApFeet() {
		return apFeet;
	}

	public void setApFeet(ArrayList apFeet) {
		this.apFeet = apFeet;
	}

	public ArrayList getApLeft() {
		return apLeft;
	}

	public void setApLeft(ArrayList apLeft) {
		this.apLeft = apLeft;
	}

	public Bitmap getBmPiece() {
		return bmPiece;
	}

	public void setBmPiece(Bitmap bmPiece) {
		this.bmPiece = bmPiece;
	}

	public Point getMinp() {
		return minp;
	}

	public void setMinp(Point minp) {
		this.minp = minp;
	}

	public Point getMaxp() {
		return maxp;
	}

	public void setMaxp(Point maxp) {
		this.maxp = maxp;
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public int getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(int rowHeight) {
		this.rowHeight = rowHeight;
	}

	public int getPieceWidth() {
		return pieceWidth;
	}

	public void setPieceWidth(int pieceWidth) {
		this.pieceWidth = pieceWidth;
	}

	public int getPieceHeight() {
		return pieceHeight;
	}

	public void setPieceHeight(int pieceHeight) {
		this.pieceHeight = pieceHeight;
	}
	
}
