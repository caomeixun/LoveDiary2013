package com.love.dairy.game;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.love.dairy.main.R;
import com.love.dairy.widget.FlipCards;


@SuppressWarnings("deprecation")
public class Game extends Activity {
	private int screenWidth;
	private int screenHeight;
	
	private int INACCURACY = 12;
		
	//private Vector<PieceImageButton> allImagePieces = new Vector<PieceImageButton>();
	private ArrayList<PieceImageButton> allImagePieces = new ArrayList<PieceImageButton>();
	
	//private Vector<PieceImageButton> movePieces = new Vector<PieceImageButton>();
	private ArrayList<PieceImageButton> movePieces = new ArrayList<PieceImageButton>();
	
	private AbsoluteLayout puzzle = null;
	
	private MyDBAdapter db = new MyDBAdapter(this);
	
	private int _id;
	private int imageId;
	
	private int levelId;
	private int row;
	private int line;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.gc();  //回收空间
		
		Intent i = this.getIntent();
		String action = i.getAction();
		Log.i("Game", "action = " + action);
		
		Bundle bundle = i.getExtras();
		imageId = bundle.getInt("imageId");
		Log.i("Game", "imageId = " + imageId);
		
		db.open();
		Cursor cursor = db.getLastEntry("pt_game", "_id");
		_id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
		levelId = cursor.getInt(cursor.getColumnIndexOrThrow("level_id"));
		cursor.close();
		
		cursor = db.getEntry("pt_level", "level_id", levelId);
		cursor.moveToFirst();
		row = cursor.getInt(cursor.getColumnIndexOrThrow("piece_row"));
		line = cursor.getInt(cursor.getColumnIndexOrThrow("piece_line"));
		Log.i("Game", "row = " + row + ", line = " + line);
		cursor.close();
		
		String where = "image_id=" + imageId + " and level_id=" + levelId + " ";
		int count = db.getEntryCount("pt_piece", where);
		
		db.close();
		/////////////////////////////
		
		//获得屏幕的宽和高
		DisplayMetrics dm = getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		puzzle = (AbsoluteLayout) inflater.inflate(R.layout.game, null);
		setContentView(puzzle);
		initBtnReview();

		Bitmap dg = FlipCards.dateCache.get(imageId);
		puzzle.setBackgroundDrawable(new BitmapDrawable(dg));
		puzzle.getBackground().setAlpha(120);
		
		
		//////////////////////

		if("RETURN_GAME_ACTION".equals(action) && count>0){
			
			//setContentView(getOldPuzzle(imageId, levelId));
			getOldPuzzle(imageId, levelId);
			
			
		}else if("NEW_GAME_ACTION".equals(action)){
			
			recordGame(_id, imageId);
			
//			if(count == (row * line)){
//				//setContentView(getReadyPuzzle(imageId, levelId));
//				getReadyPuzzle(imageId, levelId);
//				
//			}else{
				//将图片根据row行line列切开，并返回切块数组
		        //row = 5;
		        //line = 3;
		        
		        getNewPuzzle(imageId, row, line);
//			}
	        
		}else{
			//default
		}
		
	}
	
	
	@Override
	protected void onPause() {
//		new MyLoading().execute(100);
		super.onPause();
	}


	private void nextGame(){
		db.open();
		
		//如果已经是最大难度，则没有下一关
		int max_level = db.getMaxValue("pt_level", "level_id", "level_id");
		int next_level = levelId + 1;
		if(next_level > max_level){
			next_level = max_level;
		}
		
		ContentValues values = new ContentValues();
		values.put("level_id", next_level);
		db.insertEntry("pt_game", values);
		
		db.close();
	}
	
	private void recordGame(int game_id, int game_imageid){
		db.open();
		
		db.updateEntry("pt_game", game_id, "image_id", game_imageid);
		
		db.close();
		
	}
	
	private void initBtnReview(){
		
		/////////////
		progressBar = (ProgressBar) findViewById(R.id.ProgressBar01);
		progressBar.setProgress(0);
		Button btnBack = (Button) findViewById(R.id.btnReview);
		int size = getResources().getDimensionPixelSize(R.dimen.game_btn_size);
		AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(size, size, 5, screenHeight-size-5);
		btnBack.setLayoutParams(lp);
		btnBack.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				new MyLoading().execute(100);
				Intent i = new Intent(Game.this, PicView.class);
				i.setAction("GAME_REVIEW_ACTION");
				Bundle bundle = new Bundle();
				bundle.putInt("imageId", imageId);
				i.putExtras(bundle);
				startActivity(i);
	    		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
	    		finish();
			}
			
		});
		
	}
	
//	private View getReadyPuzzle(int resid, int levelid){
////		//获得屏幕的宽和高
////		DisplayMetrics dm = getResources().getDisplayMetrics();
////		screenWidth = dm.widthPixels;
////		screenHeight = dm.heightPixels;
////		
////		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
////		puzzle = (AbsoluteLayout) inflater.inflate(R.layout.game, null);
////
////		Drawable dg = MyBitmapFactory.readDrawable(this, resid, screenWidth, screenHeight);
////		puzzle.setBackgroundDrawable(dg);
////		puzzle.getBackground().setAlpha(120);
//		
//		db.open();
//		allImagePieces = db.getPieces(resid, levelid);
//		db.close();
//		
//		int piececount = allImagePieces.size();
//		Log.i("Game", "getOldPuzzle piececount = " + piececount);
//		for(int i=0; i<piececount; i++){
//			PieceImageButton pib = (PieceImageButton) allImagePieces.get(i);
//			pib.setOnTouchListener(onTouchListener);
//			
//			int autoX = (int) (Math.random() * (screenWidth - screenWidth/line));
//			int autoY = (int) (Math.random() * (screenHeight-screenHeight/row));
//			Point loc = new Point(autoX, autoY);
//			pib.setLocation(loc);
//			
//			AbsoluteLayout.LayoutParams autoParams = new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, autoX, autoY);
//			pib.setLayoutParams(autoParams);
//			
//			puzzle.addView(pib);
//		}
//		
//		return puzzle;
//	}
	
	/**
	 * 
	 * @param resid
	 * @return
	 */
	private View getOldPuzzle(int resid, int levelid){
	
//		//获得屏幕的宽和高
//		DisplayMetrics dm = getResources().getDisplayMetrics();
//		screenWidth = dm.widthPixels;
//		screenHeight = dm.heightPixels;
//		
//		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
//		puzzle = (AbsoluteLayout) inflater.inflate(R.layout.game, null);
//
//		Drawable dg = MyBitmapFactory.readDrawable(this, resid, screenWidth, screenHeight);
//		puzzle.setBackgroundDrawable(dg);
//		puzzle.getBackground().setAlpha(120);
		
		db.open();
		allImagePieces = db.getPieces(resid, levelid);
		db.close();
		
		int piececount = allImagePieces.size();
		Log.i("Game", "getOldPuzzle piececount = " + piececount);
		for(int i=0; i<piececount; i++){
			PieceImageButton pib = (PieceImageButton) allImagePieces.get(i);
			pib.setOnTouchListener(onTouchListener);
			Point loc = pib.getLocation();
			
			AbsoluteLayout.LayoutParams autoParams = new AbsoluteLayout.LayoutParams(pib.pieceWidth, pib.pieceHeight, loc.x, loc.y);
			pib.setLayoutParams(autoParams);
			
			puzzle.addView(pib);
		}
		
		return puzzle;
	}
	
	private void getNewPuzzle(int resid, int row, int line){
		Bitmap newWallpPaper = scalePic(FlipCards.dateCache.get(imageId));

        Vector<Piece> allPieces = createAllPieces(newWallpPaper, row, line);
        
        //对各个切块图片包装成imagebutton待用
        createAllPieceImageButton(allPieces);
        
        //将包装好的imageview随机绘制到拼图板上
        //setContentView(createPuzzle(resid));
        createPuzzle(resid);
        
	}
	
	private View createPuzzle(int resid){        
//		puzzle = new AbsoluteLayout(this);
//		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
//		puzzle.setLayoutParams(params);
		
//		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
//		puzzle = (AbsoluteLayout) inflater.inflate(R.layout.game, null);
//
//		Drawable dg = MyBitmapFactory.readDrawable(this, resid, screenWidth, screenHeight);
//		puzzle.setBackgroundDrawable(dg);
//		puzzle.getBackground().setAlpha(120);
		
		int piececount = allImagePieces.size();
		for(int i=0; i<piececount; i++){
			PieceImageButton pib = (PieceImageButton) allImagePieces.get(i);
			pib.setOnTouchListener(onTouchListener);
			int autoX = (int) (Math.random() * (screenWidth - screenWidth/line));
			int autoY = (int) (Math.random() * (screenHeight-screenHeight/row));
			Point loc = new Point(autoX, autoY);
			pib.setLocation(loc);
			AbsoluteLayout.LayoutParams autoParams = new AbsoluteLayout.LayoutParams(pib.pieceWidth, pib.pieceHeight, autoX, autoY);
			pib.setLayoutParams(autoParams);
			
			puzzle.addView(pib);
			
		}
		
		return puzzle;
	}
	
	/**
	 * 缩放图片
	 * @param resid
	 * @return
	 */
	private Bitmap scalePic(Bitmap wallpaper){
		//获得屏幕的宽和高
		
		
		//获得图片的宽和高
        int width = wallpaper.getWidth();
        int height = wallpaper.getHeight();
        
        //计算缩放率,使图片和手机屏幕一样大小
        float scaleWidth = (float)screenWidth / width;
        float scaleHeight = (float)screenHeight / height;
        
        //创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        
        //缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        
        //创建新的图片
        Bitmap newWallpPaper = Bitmap.createBitmap(wallpaper, 0, 0, width, height, matrix, true);

        return newWallpPaper;
		
	}
	
	private void createAllPieceImageButton(Vector<Piece> allPieces){
		for(int i=0; i<allPieces.size(); i++){
			Piece piece = (Piece) allPieces.get(i);			
			PieceImageButton pieceImageButton = new PieceImageButton(this);
			
			pieceImageButton.setId(i);  //碎片的唯一ID
			pieceImageButton.setMinp(piece.getMinp());     //整个碎片的外部开始点,切图前的点位
			pieceImageButton.setLocation(new Point(0,0));
			
			//图片占满整个ImageButton,方法1
			/*
			pieceImageButton.setPadding(0, 0, 0, 0);
			pieceImageButton.setScaleType(ScaleType.FIT_XY);
			pieceImageButton.setImageBitmap(piece.getBmPiece());
			pieceImageButton.getBackground().setAlpha(0);
			*/
			
			pieceImageButton.pieceHeight = piece.getPieceHeight();
			pieceImageButton.pieceWidth = piece.getPieceWidth();
			Log.e("TAG", "pib.pieceWidth"+piece.getPieceWidth());
			Log.e("TAG", piece.getMaxp().x- piece.getMinp().x+"pib.pieceWidth");
			//背景图片占满整个ImageButton，方法2
			BitmapDrawable bd = new BitmapDrawable(piece.getBmPiece());
			pieceImageButton.setBackgroundDrawable(bd);			
			
			allImagePieces.add(pieceImageButton);
		}
		
	}
	
	//通过bitmap创建
	private Vector<Piece> createAllPieces(Bitmap bitmap, int row, int line){
		PieceFactory pu = new PieceFactory(this);
    	pu.setImage(bitmap);
    	pu.setRowAndLine(row, line);
    	
    	return pu.getAllPiece();
	}
	
//	//通过资源创建
//    private Vector<Piece> createAllPieces(int imageid, int row, int line){
//    	PieceFactory pu = new PieceFactory(this);
//    	pu.setImage(imageid);
//    	pu.setRowAndLine(row, line);
//    	
//    	return pu.getAllPiece();
//    }
    
    private OnTouchListener onTouchListener = new OnTouchListener(){
    	int lastX;
    	int lastY;
    	
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				
				//puzzle.bringChildToFront(v);  //把该视图置于其他所有子视图之上
				displayFront((PieceImageButton)v);
				
				break;
				/**
				 * layout(l,t,r,b)
				 * l  Left position, relative to parent 
                t  Top position, relative to parent 
                r  Right position, relative to parent 
                b  Bottom position, relative to parent  
				 * */
			case MotionEvent.ACTION_MOVE:
				int dx =(int)event.getRawX() - lastX;
				int dy =(int)event.getRawY() - lastY;
				
				//存在延迟
				checkMove((PieceImageButton)v, dx, dy, movePieces);
				moveSomePieces(movePieces);
				movePieces.clear();   //重置移动的标志，清空可移动记录
				cleanPath();
				
				
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				
				break;
			case MotionEvent.ACTION_UP:
				
				//先取得碎片吸附的路径，然后移动碎片
				cleanPath();
				PieceImageButton firstPiece = checkAbsorb((PieceImageButton)v);
				
				cleanPath();
				absorb(firstPiece);
				
				//吸附后，显示到前端
				displayFront(firstPiece);
				
				//判断是否完成
				hasComplete();
				
				break;        		
			}

			return false;
		}
    };
    
    private void displayFront(PieceImageButton curPiece){
    	puzzle.bringChildToFront(curPiece);   //把该视图置于其他所有子视图之上
    	curPiece.postInvalidate();
    	
    	int id = curPiece.getId();
    	int curRow = id / line;
    	int curLine = id % line;
    	
    	//top
    	if(curPiece.isHasTop()){
    		PieceImageButton topPiece = (PieceImageButton) allImagePieces.get((curRow - 1) * line + curLine);
    		if(!topPiece.isTraverse()){
    			topPiece.setTraverse(true);
    			displayFront(topPiece);
    		}
    		
    	}
    	
    	//right
    	if(curPiece.isHasRight()){
    		PieceImageButton rightPiece = (PieceImageButton) allImagePieces.get(id + 1);
    		if(!rightPiece.isTraverse()){
    			rightPiece.setTraverse(true);
        		displayFront(rightPiece);
    		}
    		
    	}
    	
    	//feet
    	if(curPiece.isHasFeet()){
    		PieceImageButton feetPiece = (PieceImageButton) allImagePieces.get((curRow + 1) * line + curLine);
    		if(!feetPiece.isTraverse()){
    			feetPiece.setTraverse(true);
        		displayFront(feetPiece);
    		}
    		
    	}
    	
    	//left
    	if(curPiece.isHasLeft()){
    		PieceImageButton leftPiece = (PieceImageButton) allImagePieces.get(id - 1);
    		if(!leftPiece.isTraverse()){
    			leftPiece.setTraverse(true);
        		displayFront(leftPiece);
    		}
    		
    	}
    	
    }
    
    private void checkMove(PieceImageButton curPIB, int dx, int dy, ArrayList<PieceImageButton> movePieces){
    	int l = curPIB.getLeft() + dx;
    	int t = curPIB.getTop() + dy;
    	curPIB.setLocation(new Point(l, t));

    	/*
		if(l < 0){
			l = 0;
			r = l + curPIB.getWidth();
		}
		if(r > screenWidth){
			r = screenWidth;
			l = r - curPIB.getWidth();
		}
		if(t < 0){
			t = 0;
			b = t + curPIB.getHeight();
		}
		if(b > screenHeight){
			b = screenHeight;
			t = b - curPIB.getHeight();
		}
		*/
    	
		//curPIB.layout(l, t, r, b);
    	movePieces.add(curPIB);
    	
		int id = curPIB.getId();
    	int curRow = id / line;
    	int curLine = id % line;
    	
    	//top
    	if(curPIB.isHasTop()){
    		PieceImageButton topPIB = (PieceImageButton) allImagePieces.get((curRow - 1) * line + curLine);
    		if(!topPIB.isTraverse()){
    			topPIB.setTraverse(true);
    			checkMove(topPIB, dx, dy, movePieces);
    		}
    		
    	}
    	
    	//right
    	if(curPIB.isHasRight()){
    		PieceImageButton rightPIB = (PieceImageButton) allImagePieces.get(id + 1);
        	if(!rightPIB.isTraverse()){
        		rightPIB.setTraverse(true);
        		checkMove(rightPIB, dx, dy, movePieces);
        	}
    		
    	}
    	
    	//feet
    	if(curPIB.isHasFeet()){
    		PieceImageButton feetPIB = (PieceImageButton) allImagePieces.get((curRow + 1) * line + curLine);
        	if(!feetPIB.isTraverse()){
        		feetPIB.setTraverse(true);
        		checkMove(feetPIB, dx, dy, movePieces);
        	}
    		
    	}
    	
    	//left
    	if(curPIB.isHasLeft()){
    		PieceImageButton leftPIB = (PieceImageButton) allImagePieces.get(id - 1);
        	if(!leftPIB.isTraverse()){
        		leftPIB.setTraverse(true);
        		checkMove(leftPIB, dx, dy, movePieces);
        	}
    		
    	}
    	
    }
    
    private void moveSomePieces(ArrayList<PieceImageButton> absorbPieces){
    	for(int i=0; i<absorbPieces.size(); i++){
    		PieceImageButton piece = (PieceImageButton) absorbPieces.get(i);
    		Point loc = piece.getLocation();
    		piece.layout(loc.x, loc.y, loc.x + piece.getWidth(), loc.y + piece.getHeight());
    	}
    }
    
    private void absorb(PieceImageButton curPiece){
    	Point curMinp = curPiece.getMinp();
    	Point curLoc = curPiece.getLocation();
    	curPiece.layout(curLoc.x, curLoc.y, curLoc.x + curPiece.getWidth(), curLoc.y + curPiece.getHeight());
    	
    	int id = curPiece.getId();
    	int curRow = id / line;
    	int curLine = id % line;
    	
    	//top
    	if(curPiece.isHasTop()){
    		PieceImageButton topPiece = (PieceImageButton) allImagePieces.get((curRow - 1) * line + curLine);
    		if(!topPiece.isTraverse()){
    			Point topMinp = topPiece.getMinp();
        		topPiece.setLocation(new Point(curLoc.x + (topMinp.x - curMinp.x), curLoc.y + (topMinp.y - curMinp.y)));
        		topPiece.setTraverse(true);
        		absorb(topPiece);
    		}
    		
    	}
    	
    	//right
    	if(curPiece.isHasRight()){
    		PieceImageButton rightPiece = (PieceImageButton) allImagePieces.get(id + 1);
    		if(!rightPiece.isTraverse()){
    			Point rightMinp = rightPiece.getMinp();
        		rightPiece.setLocation(new Point(curLoc.x + (rightMinp.x - curMinp.x), curLoc.y + (rightMinp.y - curMinp.y)));
        		rightPiece.setTraverse(true);
        		absorb(rightPiece);
    		}
    		
    	}
    	
    	//feet
    	if(curPiece.isHasFeet()){
    		PieceImageButton feetPiece = (PieceImageButton) allImagePieces.get((curRow + 1) * line + curLine);
    		if(!feetPiece.isTraverse()){
    			Point feetMinp = feetPiece.getMinp();
        		feetPiece.setLocation(new Point(curLoc.x + (feetMinp.x - curMinp.x), curLoc.y + (feetMinp.y - curMinp.y)));
        		feetPiece.setTraverse(true);
        		absorb(feetPiece);
    		}
    		
    	}
    	
    	//left
    	if(curPiece.isHasLeft()){
    		PieceImageButton leftPiece = (PieceImageButton) allImagePieces.get(id - 1);
    		if(!leftPiece.isTraverse()){
    			Point leftMinp = leftPiece.getMinp();
        		leftPiece.setLocation(new Point(curLoc.x + (leftMinp.x - curMinp.x), curLoc.y + (leftMinp.y - curMinp.y)));
        		leftPiece.setTraverse(true);
        		absorb(leftPiece);
    		}
    		
    	}
    	
    }
    
    //从top，right，feet，left开始遍历，设置吸附标志
    private PieceImageButton checkAbsorb(PieceImageButton v){
    	PieceImageButton firstPiece = null;
    	
    	PieceImageButton curPiece = (PieceImageButton) v;
    	curPiece.setTraverse(true);
    	
    	int curId = curPiece.getId();
    	int curRow = curId / line;
    	int curLine = curId % line;
       	Point curMinp = curPiece.getMinp();
    	Point curLoc = curPiece.getLocation();
 
    	//top
    	if(curRow > 0){   //当前碎片存在上面的碎片
    		int topPieceId = (curRow - 1) * line + curLine;
    		if(!curPiece.isHasTop()){  //如果上面的碎片还未吸附
    			//如果存在上面的碎片，还没有碰撞，则得到上面碎片的位置判断是否吸附
    			PieceImageButton topPiece = (PieceImageButton) allImagePieces.get(topPieceId);
	    		Point topLoc = topPiece.getLocation();
	    		Point topMinp = topPiece.getMinp();
	    		
	    		//如果吸附条件成立，则吸附
	    		if(distance(curMinp, topMinp, curLoc, topLoc, INACCURACY)){
	    			curPiece.setHasTop(true);
	    			topPiece.setHasFeet(true);
	    			if(firstPiece == null){
	    				firstPiece = topPiece;
	    			}

	    		}
    		}else{  //如果上面的碎片已经吸附,且不是搜索的来源（避免死循环）,则继续上面的碎片查找
    			PieceImageButton topPiece = (PieceImageButton) allImagePieces.get(topPieceId);
    			if(!topPiece.isTraverse()){
    				checkAbsorb(topPiece);
    			}

    		}
    	}
    	
    	//right
    	if(curLine < (line -1)){  //当前碎片存在右面的碎片
    		int rightPieceId = curId + 1;
    		if(!curPiece.isHasRight()){  //如果右面的碎片还为吸附
    			//如果存在右面的碎片，还没有碰撞，则得到右面碎片的位置判断是否吸附
    			PieceImageButton rightPiece = (PieceImageButton) allImagePieces.get(rightPieceId);
	    		Point rightLoc = rightPiece.getLocation();
	    		Point rightMinp = rightPiece.getMinp();

	    		//如果吸附条件成立，则吸附
	    		if(distance(curMinp, rightMinp, curLoc, rightLoc, INACCURACY)){
	    			curPiece.setHasRight(true);
	    			rightPiece.setHasLeft(true);
	    			if(firstPiece == null){
	    				firstPiece = rightPiece;
	    			}
	    			
	    		}
    		}else{
    			PieceImageButton rightPiece = (PieceImageButton) allImagePieces.get(rightPieceId);
    			if(!rightPiece.isTraverse()){
    				checkAbsorb(rightPiece);
    			}

    		}
    	}
    	
    	//feet
    	if(curRow < (row - 1)){
    		int feetPieceId = (curRow + 1) * line + curLine;
    		if(!curPiece.isHasFeet()){
    			//如果存在右面的碎片，还没有碰撞，则得到右面碎片的位置判断是否吸附
    			PieceImageButton feetPiece = (PieceImageButton) allImagePieces.get(feetPieceId);
	    		Point feetLoc = feetPiece.getLocation();
	    		Point feetMinp = feetPiece.getMinp();
	    		
	    		//如果吸附条件成立，则吸附
	    		if(distance(curMinp, feetMinp, curLoc, feetLoc, INACCURACY)){
	    			curPiece.setHasFeet(true);
	    			feetPiece.setHasTop(true);
	    			if(firstPiece == null){
	    				firstPiece = feetPiece;
	    			}
	    			
	    		}
    		}else{
    			PieceImageButton feetPiece = (PieceImageButton) allImagePieces.get(feetPieceId);
    			if(!feetPiece.isTraverse()){
    				checkAbsorb(feetPiece);
    			}
    		}

    	}

    	//left
    	if(curLine > 0){
    		int leftPieceId = curId - 1;
    		if(!curPiece.isHasLeft()){
    			//如果存在右面的碎片，还没有碰撞，则得到右面碎片的位置判断是否吸附
    			PieceImageButton leftPiece = (PieceImageButton) allImagePieces.get(leftPieceId);
	    		Point leftLoc = leftPiece.getLocation();
	    		Point leftMinp = leftPiece.getMinp();
	    		
	    		//如果吸附条件成立，则吸附
	    		if(distance(curMinp, leftMinp, curLoc, leftLoc, INACCURACY)){
	    			curPiece.setHasLeft(true);
	    			leftPiece.setHasRight(true);
	    			if(firstPiece == null){
	    				firstPiece = leftPiece;
	    			}
	    			
	    		}
    		}else{
    			PieceImageButton leftPiece = (PieceImageButton) allImagePieces.get(leftPieceId);
    			if(!leftPiece.isTraverse()){
    				checkAbsorb(leftPiece);
    			}
    		}

    	}
    	if(firstPiece == null){
    		firstPiece = v;
    	}
    	return firstPiece;
    	
    }
    
    
    private boolean distance(Point srckey, Point destkey, Point srcloc, Point destloc, int inaccuracy){
    	//当前X坐标的差值，与原来的虚坐标的差值接近时
		if(Math.abs((srckey.x - destkey.x) - (srcloc.x - destloc.x)) <= inaccuracy){
			if(Math.abs((srckey.y - destkey.y) - (srcloc.y - destloc.y)) <= inaccuracy){
				return true;
			}
		}
    	return false;
    }
    
    
    private void cleanPath(){
    	for(int i=0; i<allImagePieces.size(); i++){
    		PieceImageButton piece = (PieceImageButton) allImagePieces.get(i);
    		piece.setTraverse(false);
        	
    	}
    	
    }

    private void hasComplete(){
    	int finish = 0;
    	for(int i=0; i<allImagePieces.size(); i++){
    		PieceImageButton piece = (PieceImageButton) allImagePieces.get(i);
    		if(piece.isTraverse()){
    			finish ++;
    		}
        	
    	}
    	if(finish == row * line){
    		openFinishDialog();
    	}
    }
	
    private void openFinishDialog(){
    	Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("拼图完成");
		builder.setCancelable(false);
		builder.setView(new EditText(this));
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialogInterface, int which) {
				// TODO Auto-generated method stub
				nextGame();   //完成后，新增pt_game中的记录
				
			}
			
		}).show();
    }
    
	private ProgressBar progressBar;
	protected static final int GUI_STOP_NOTIFIER = 0x108;
	protected static final int GUI_THREADING_NOTIFIER = 0x109;
	protected int intCounter = 0;
	
	class MyLoading extends AsyncTask<Integer, Integer, String>{
		@Override
		protected String doInBackground(Integer... params) {
			saveGame();
			return null;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		private void saveGame(){
			Log.e("TAG","saveGame-start");
			
			long time = System.currentTimeMillis();
			db.open();
			int n = db.getEntryCount("pt_piece", "image_id", imageId);
			if(n > 0){
				db.removeEntry("pt_piece", "image_id", "" + imageId);
			}
			int count = allImagePieces.size();
			for(int i=0; i<count; i++){
				PieceImageButton pib = (PieceImageButton) allImagePieces.get(i);
				db.insertPiece("pt_piece", pib, imageId, levelId);
				pib = null;
				publishProgress(i);
			}
			
			Log.e("TAG", "-------" + db.str);
			db.close();
			Log.e("TAG",System.currentTimeMillis() - time + "：saveGame-end");
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			allImagePieces = null;
			System.gc();
		}
	}
	
}
