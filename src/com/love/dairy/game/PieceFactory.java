package com.love.dairy.game;

import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * 1������͸��ͼƬ����Ϊ�ɰ�
 * 2�����ɰ��п飬�����Ƴ�һ���п�ı߿�
 * 3�����ݱ߿����պ���������
 * 4�������ɫ�����滻������ָ��ͼƬָ���������ɫ
 * 5��ѭ������3��4����¼������ɵ��п�
 * @author pcuser
 *
 */
public class PieceFactory {
	private Bitmap mBitPic;   //ʵ��ͼƬ
	private Bitmap mCutBit;   //ʵ��ͼƬ
	
	private Canvas canvas = new Canvas();
	
	private Path dotPath = new Path();
	private Paint noPicPaint = new Paint();
	private Paint edgePaint = new Paint();
//	private Paint addPaint = new Paint();
	
	//��������
	private Vector<Piece> allPiece = new Vector<Piece>();
	
	/////����ͼƬ����
	private int _imageW;
	private int _imageH;
	
	//ͼƬ�з־������У�����
	private int _row;
	private int _line;
	
	//��Ƭ�Ŀ��
	private int _pieceW;
	private int _pieceH;
	private int _pieceMinWH;
	private int _pieceD;
	
	//���о��ο��
	private int _pieceOW;
	private int _pieceOH;
	
	//����ϵ��
	private int _pieceD_k = 10;  //�߽����Ҹ���ϵ��
	private int _pieceO_k = 4;   //��Ƭ��͹ϵ����ռ�߽糤�ȵİٷֱ�
	
	//����
	private int RIGHT = 1;
	private int FEET = 2;
	
	private Context mContext = null;
	
	public PieceFactory(Context context) {
		mContext = context;
		// TODO Auto-generated constructor stub
		PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);  
		canvas.setDrawFilter(pfd); 
	}
	
	public void setImage(Bitmap bitmap){
		mBitPic = bitmap;
		_imageW = mBitPic.getWidth();
		_imageH = mBitPic.getHeight();
	}

	public void setImage(int imageid){
		mBitPic = BitmapFactory.decodeResource(mContext.getResources(), imageid);
		_imageW = mBitPic.getWidth();
		_imageH = mBitPic.getHeight();
		
	}
	
	public void setRowAndLine(int row, int line){
		_row = row;
		_line = line;
		
		bitmapCut();
	}
	private int bgColor = Color.parseColor("#FFFFFF");
	///////˽�з���
	private void pieceSet(){
		_pieceW = _imageW / _line;
		_pieceH = _imageH / _row;
		_pieceMinWH = Math.min(_pieceW, _pieceH);
		_pieceD = _pieceMinWH / _pieceD_k;   ///10
		_pieceOW = _pieceMinWH / _pieceO_k;  ///4
		_pieceOH = _pieceMinWH / _pieceO_k;
		
		
		noPicPaint.setColor(bgColor);
		noPicPaint.setStyle(Paint.Style.FILL);  //ʵ�����
		noPicPaint.setStrokeWidth(1); //�����
		noPicPaint.setAntiAlias(true);  //�����
//		edgePaint.setColor(Color.GRAY);
//		edgePaint.setStyle(Paint.Style.STROKE);
//		edgePaint.setStrokeWidth(3);
//		edgePaint.setAntiAlias(true);
		
		////////////////
		edgePaint.setColor(Color.parseColor("#8C8C9F"));
//		edgePaint.setAlpha(108);
		edgePaint.setStyle(Paint.Style.STROKE);
		edgePaint.setStrokeWidth(1);
		edgePaint.setAntiAlias(true);
		//////////ͼƬЧ��
//		//���ù�Դ�ķ���
//		float[] direction = new float[]{ 1, 1, 1 };
//		//���û���������
//		float light = 10.6f;
//		//ѡ��ҪӦ�õķ���ȼ�
//		float specular = 0;
//		//��maskӦ��һ�������ģ��
//		float blur = 0f;
//		EmbossMaskFilter emboss = new EmbossMaskFilter(direction, light, specular, blur);
//		//Ӧ��mask
//		edgePaint.setMaskFilter(emboss);
		
		
	}
	
	private int getRndD(){
		//������߽���ĸ߶�
		return _pieceD - (int)Math.random() * 2 * _pieceD;
	}
	
	//˳ʱ��ȡ��Բ��λ���ұ߽���±߽�
	private ArrayList<Point> getOvalDotArray(Piece piece, int position){
		int rnd = ((int)(Math.random()*10)%2 ==0) ? 1 : -1;
		ArrayList<Point> circleDotArray = new ArrayList<Point>();
		Point key = piece.getKey();
		
		switch(position){
		case 1:   //RIGHT
			Point r0 = new Point(key.x + _pieceW, key.y);
			Point r1 = new Point(r0.x + getRndD(), r0.y + (_pieceH - _pieceOW)/2);
			Point r2 = new Point(r1.x + rnd * _pieceOH/2, r1.y - _pieceOW/4);
			Point r3 = new Point(r1.x + rnd * _pieceOH, r1.y);
			Point r4 = new Point(r3.x + rnd * _pieceOH/4, r3.y + _pieceOW/2);
			Point r5 = new Point(r3.x, r3.y + _pieceOW);
			Point r6 = new Point(r2.x, r5.y + _pieceOW/4);
			Point r7 = new Point(r1.x, r5.y);
			Point r8 = new Point(r0.x, r0.y + _pieceH);
			
			circleDotArray.add(r0);
			circleDotArray.add(r1);
			circleDotArray.add(r2);
			circleDotArray.add(r3);
			circleDotArray.add(r4);
			circleDotArray.add(r5);
			circleDotArray.add(r6);
			circleDotArray.add(r7);
			circleDotArray.add(r8);
			
			break;
	
		case 2:  //FEET
			Point f0 = new Point(key.x + _pieceW, key.y + _pieceH);
			Point f1 = new Point(f0.x - (_pieceW - _pieceOW)/2, f0.y + getRndD());
			Point f2 = new Point(f1.x + _pieceOW/4, f1.y + rnd * _pieceOH/2);
			Point f3 = new Point(f1.x, f1.y +  rnd * _pieceOH);
			Point f4 = new Point(f3.x - _pieceOW/2, f3.y + rnd * _pieceOH/4);
			Point f5 = new Point(f3.x - _pieceOW, f3.y);
			Point f6 = new Point(f5.x - _pieceOW/4, f2.y);
			Point f7 = new Point(f5.x, f1.y);
			Point f8 = new Point(f0.x - _pieceW, f0.y);
			
			circleDotArray.add(f0);
			circleDotArray.add(f1);
			circleDotArray.add(f2);
			circleDotArray.add(f3);
			circleDotArray.add(f4);
			circleDotArray.add(f5);
			circleDotArray.add(f6);
			circleDotArray.add(f7);
			circleDotArray.add(f8);
			break;
		}
		
		return circleDotArray;
	}
	
	private void getAllDotArray(Piece piece){
		//top,right,feet,left����
		ArrayList<Point> top = new ArrayList<Point>();
		ArrayList<Point> right = new ArrayList<Point>();
		ArrayList<Point> feet = new ArrayList<Point>();
		ArrayList<Point> left = new ArrayList<Point>();
		
		Point id = piece.getId();
		Point key = piece.getKey();
		if(id.x == 0){
			//top�߽�Ϊֱ��
			Point tp1 = new Point(key.x, key.y);
			Point tp2 = new Point(key.x + _pieceW, key.y);
			top.add(tp1);
			top.add(tp2);
		}else{  //top�߽�Ϊ���ߣ������ߵ�Ϊ��һ����Ƭ��feet�߽�
			Piece tmpPiece = (Piece) allPiece.get(_line * (id.x - 1) + id.y);
			//Log.i("top", "top bian " + id.y + " " + id.y + " " + (_line * (id.x - 1) + id.y));
			ArrayList<Point> tmpFeet = tmpPiece.getApFeet();
			for(int i=tmpFeet.size()-1; i>=0; i--){
				top.add(tmpFeet.get(i));
			}
		}
		
		if(id.y == 0){
			//left�߽�Ϊֱ��
			Point lp1 = new Point(key.x, key.y + _pieceH);
			Point lp2 = new Point(key.x, key.y);
			left.add(lp1);
			left.add(lp2);
		}else{  //left�߽�Ϊ���ߣ������ߵ�Ϊ���һ����Ƭ��right�߽�
			Piece tmpPiece = (Piece) allPiece.get(_line * id.x + id.y - 1);
			//Log.i("left", "left bian " + id.y + " " + id.y + " " + (_line * id.x + id.y - 1));
			ArrayList<Point> tmpRight = tmpPiece.getApRight();
			for(int i=tmpRight.size()-1; i>=0; i--){
				left.add(tmpRight.get(i));
			}
		}
		
		if(id.x == _row-1){
			//feet�߽�Ϊֱ��
			Point fp1 = new Point(key.x + _pieceW, key.y + _pieceH);
			Point fp2 = new Point(key.x, key.y + _pieceH);
			feet.add(fp1);
			feet.add(fp2);
		}else{
			feet = getOvalDotArray(piece, FEET);
		}
		
		if(id.y == _line-1){
			//right�߽�Ϊֱ��
			Point rp1 = new Point(key.x + _pieceW, key.y);
			Point rp2 = new Point(key.x + _pieceW, key.y + _pieceH);
			right.add(rp1);
			right.add(rp2);
		}else{
			right = getOvalDotArray(piece, RIGHT);
		}
		piece.setApTop(top);
		piece.setApRight(right);
		piece.setApFeet(feet);
		piece.setApLeft(left);
		allPiece.add(piece);

	}
	
	//�ó���Ƭ�����ϽǺ����½������λ
	private void getMinAndMaxPoint(Piece piece){
		int minx = _imageW;
		int miny = _imageH;
		int maxx = 0;
		int maxy = 0;
		
		ArrayList<Point> left = piece.getApLeft();
		for(int i=0; i<left.size(); i++){
			Point lp = (Point) left.get(i);
			if(lp.x < minx){
				minx = lp.x;
			}
		}
		
		ArrayList<Point> top = piece.getApTop();
		for(int i=0; i<top.size(); i++){
			Point tp = (Point) top.get(i);
			if(tp.y < miny){
				miny = tp.y;
			}
		}
		//Log.i("getMinAndMaxPoint", "min point: (" + minx + ", " + miny + ")");
		piece.setMinp(new Point(minx, miny));   // ���Ͻǵ�λ
		
		ArrayList<Point> right = piece.getApRight();
		for(int i=0; i<right.size(); i++){
			Point rp = (Point) right.get(i);
			if(rp.x > maxx){
				maxx = rp.x;
			}
		}
		
		ArrayList<Point> feet = piece.getApFeet();
		for(int i=0; i<feet.size(); i++){
			Point fp = (Point) feet.get(i);
			if(fp.y > maxy){
				maxy = fp.y;
			}
		}
		//Log.i("getMinAndMaxPoint", "max point: (" + maxx + ", " + maxy + ")");
		piece.setMaxp(new Point(maxx, maxy));   // ���½ǵ�λ
		piece.setPieceWidth(maxx-minx);
		piece.setPieceHeight(maxy-miny);
		
	}
	
	private void bitmapCut(){
		pieceSet();
		
		//͸��ģ��
		//Bitmap copyBitPic = Bitmap.createBitmap(_imageW, _imageH, Config.ARGB_8888);
		
		for(int i=0; i<_row; i++){
			for(int j=0; j<_line; j++){
				Piece piece = new Piece();
				
				Point id = new Point(i, j);  //������Ƭ�ľ����
				piece.setId(id);
				
				Point key = new Point(j*_pieceW, i*_pieceH);  ///
				piece.setKey(key);
				piece.setLineWidth(_pieceW);
				piece.setRowHeight(_pieceH);

				//�ó���Ƭ�Ĺؼ��߽��
				getAllDotArray(piece);
				
				//�ó����Ͻǵ�λ�����½ǵ�λ��������ȡС����ƬͼƬ
				getMinAndMaxPoint(piece);
				int width = piece.getMaxp().x- piece.getMinp().x;
				int height = piece.getMaxp().y- piece.getMinp().y;
				
				width = width> mBitPic.getWidth() ?mBitPic.getWidth():width;
				height = height> mBitPic.getHeight() ?mBitPic.getHeight():height;
				mCutBit = Bitmap.createBitmap(mBitPic, piece.getMinp().x, piece.getMinp().y, width, height);
				getPieceBitmap(piece);
				fillPieceWithBitmap(piece);
				
				//addEdge(piece);
			}
		}
				
	}
	
//	/**
//	 * ���߿�ͼƬ�ϳɵ���Ƭ��
//	 * @param piece
//	 */
//	private void addEdge(Piece piece){
//		Bitmap temppiece = piece.getBmPiece();
//		canvas.setBitmap(temppiece);
//		canvas.drawBitmap(piece.getBmEdge(), 0, 0, addPaint);
//		canvas.save(Canvas.ALL_SAVE_FLAG);
//		canvas.restore();
//		
//		piece.setBmPiece(temppiece);
//		piece.getBmEdge().recycle();  //����ͼƬ
//	}
//	
//	private int changeColorToLight(int color, double contrast, int light){
//		int red = Color.red(color);
//		int green = Color.green(color);
//		int blue = Color.blue(color);
//		
//		int r = (int) (red * contrast + light);
//		int g = (int) (green * contrast + light);
//		int b = (int) (blue * contrast + light);
//		
//		if(r > 255){
//			r = 255;
//		}else if(r < 0){
//			r = 0;
//		}
//		
//		if(g > 255){
//			g = 255;
//		}else if(g < 0){
//			g = 0;
//		}
//		
//		if(b > 255){
//			b = 255;
//		}else if(b < 0){
//			b = 0;
//		}
//		
//		return Color.rgb(r, g, b);
//	}
//	
	/**
	 * ��ÿ��piece�ɰ�������أ��õ�ƴͼ��Ƭpiece
	 */
	private void fillPieceWithBitmap(Piece piece){
//		Bitmap pieceEdge = piece.getBmEdge();
		Bitmap pieceBit = piece.getBmPiece();
//		Point minp = piece.getMinp();
		
		//ƴͼ��Ƭ�Ŀ��
//		int tpieceW = pieceBit.getWidth();
//		int tpieceH = pieceBit.getHeight();
		
//		for(int i=0; i<tpieceW; i++){
//			for(int j=0; j<tpieceH; j++){
//				if(pieceBit.getPixel(i, j) == bgColor){
//					int mBitPicPixelColor = mBitPic.getPixel(minp.x+i, minp.y+j);
//					if(pieceEdge.getPixel(i, j) == bgColor){
//						mBitPicPixelColor = Color.BLACK;
//						pieceBit.setPixel(i, j, mBitPicPixelColor);
//					}
//				}else{
//				}
//				
//			}
//		}
        
		piece.setBmPiece(pieceBit);
	}
	
	//��ȡÿ����Ƭ��ͼ��
	private Point getPieceBitmap(Piece piece){
		dotPath.reset();
		
		Point minp = piece.getMinp();
		Point maxp = piece.getMaxp();
		
		Point diff = minp;
		int w = maxp.x - minp.x;
		int h = maxp.y - minp.y;
		
		Point key = (Point) piece.getKey();
		dotPath.moveTo(key.x-diff.x, key.y-diff.y);
		
		ArrayList<Point> top = piece.getApTop();
		changeDotPath(top, dotPath, diff);
		
		ArrayList<Point> right = piece.getApRight();
		changeDotPath(right, dotPath, diff);
		
		ArrayList<Point> feet = piece.getApFeet();
		changeDotPath(feet, dotPath, diff);
		
		ArrayList<Point> left = piece.getApLeft();
		changeDotPath(left, dotPath, diff);
	 
		/////������Ƭ�Ĵ�С������͸��ͼƬ���ڻ�����ÿ�λ���һ����Ƭ��Ȼ�󱣴�
		Bitmap pieceBit = Bitmap.createBitmap(w, h, Config.ARGB_8888);

		canvas.setBitmap(pieceBit);
		canvas.drawPath(dotPath, noPicPaint);

		noPicPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(mCutBit,0,0,noPicPaint);
		if(mCutBit!=null && !mCutBit.isRecycled()){
			mCutBit.recycle();
			mCutBit=null;
		}
		noPicPaint.setXfermode(null);
		//
		canvas.drawPath(dotPath, edgePaint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
 		piece.setBmPiece(pieceBit);
//		BlurMaskFilter blurFilter = new BlurMaskFilter(3, BlurMaskFilter.Blur.OUTER);
// 		Paint shadowPaint = new Paint();		
// 		shadowPaint.setMaskFilter(blurFilter);
// 		int[] offsetXY =new int[2];
// 		Bitmap shadowBitmap = pieceBit.extractAlpha(shadowPaint,offsetXY);
// 		Bitmap shadowImage32 = shadowBitmap.copy(Bitmap.Config.ARGB_8888, true);
// 		canvas.
		
		//������Ƭ�ı�Ե
		Bitmap edge = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		canvas.setBitmap(edge);
		canvas.drawPath(dotPath, edgePaint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		piece.setBmEdge(edge);
		return diff;   //���ص�λ������
		
	}
	
	//����minp�㣬�����Ե�λת��Ϊ��Ե�λ
	private void changeDotPath(ArrayList<Point> dotList, Path dotPath, Point diff){
		int len = dotList.size();
		ArrayList<Point> tempDot = dotList;
		
		for(int i=0; i<len; i++){
			if(tempDot.size() == 2){
				Point p0 = (Point)tempDot.get(i);
				dotPath.lineTo(p0.x-diff.x, p0.y-diff.y);
			}else if(i + 1 < tempDot.size()){
				Point p0 = (Point) tempDot.get(i);
				Point p1 = (Point) tempDot.get(i+1);
				dotPath.quadTo(p0.x-diff.x, p0.y-diff.y, p1.x-diff.x, p1.y-diff.y);
			}
		}
		
	}

	public Vector<Piece> getAllPiece() {
		return allPiece;
	}

	public void setAllPiece(Vector<Piece> allPiece) {
		this.allPiece = allPiece;
	}

}
