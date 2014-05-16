package com.love.dairy.game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
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
import android.util.Base64;
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
import android.widget.TextView;

import com.love.dairy.LoveApplication;
import com.love.dairy.main.MainActivity;
import com.love.dairy.main.R;
import com.love.dairy.utils.LDLog;
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
	private String imageName;
	
	private int levelId;
	private int row;
	private int line;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.gc();  //���տռ�
		
		Intent i = this.getIntent();
		String action = i.getAction();
		LDLog.i("Game", "action = " + action);
		
		Bundle bundle = i.getExtras();
		imageId = bundle.getInt("imageId");
		LoveApplication application = (LoveApplication) this.getApplication();
		imageName = Base64.encodeToString(application.photoIds.get(imageId).getBytes(),Base64.DEFAULT).replace("\n", "");
		LDLog.i("Game", "imageId = " + imageName);
		
		db.open();
		Cursor cursor = db.getLastEntry("pt_game", "_id");
		_id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
		levelId = cursor.getInt(cursor.getColumnIndexOrThrow("level_id"));
		cursor.close();
		
		cursor = db.getEntry("pt_level", "level_id", levelId);
		cursor.moveToFirst();
		row = cursor.getInt(cursor.getColumnIndexOrThrow("piece_row"));
		line = cursor.getInt(cursor.getColumnIndexOrThrow("piece_line"));
		LDLog.i("Game", "row = " + row + ", line = " + line);
		cursor.close();
		
		String where = "image_id=" + imageId + " and level_id=" + levelId + " ";
		int count = db.getEntryCount("pt_piece", where);
		
		db.close();
		/////////////////////////////
		
		//�����Ļ�Ŀ�͸�
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
				//��ͼƬ����row��line���п����������п�����
		        //row = 5;
		        //line = 3;
		        
		        getNewPuzzle(imageId, row, line);
//			}
	        
		}else{
			//default
		}
		
//		refreshJindu();
		
	}
	
	private void refreshJindu(){
		if(tfJindu != null){
			tfJindu.setText((countTaverse++) + "/" + "" + allImagePieces.size());
			progressBar.setMax(allImagePieces.size());
			progressBar.setProgress(countTaverse++);
		}
	}
	@Override
	protected void onPause() {
//		new MyLoading().execute(100);
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(timer != null){
			timer.cancel();
			timer = null;
		}
	}
	
	private void nextGame(){
		db.open();
		
		//����Ѿ�������Ѷȣ���û����һ��
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
		tfTime = (TextView) findViewById(R.id.tfTime);
		tfJindu = (TextView) findViewById(R.id.tfJindu);
		Button btnBack = (Button) findViewById(R.id.btnReview);
		int size = getResources().getDimensionPixelSize(R.dimen.game_btn_size);
		int progressBarHeight = getResources().getDimensionPixelSize(R.dimen.item_space_extra);
		AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(size, size, 5, screenHeight-size-5);
		btnBack.setLayoutParams(lp);
		tfTime.setLayoutParams(new AbsoluteLayout.LayoutParams(MainActivity.screenWidth / 2, progressBarHeight, 5, 0));
		tfJindu.setLayoutParams(new AbsoluteLayout.LayoutParams(MainActivity.screenWidth / 2, progressBarHeight, MainActivity.screenWidth / 2, 0));
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
		
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				Game.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						tfTime.setText(String.format("����ʱ %d ��", use_time));
						use_time++;
					}
				});

			}
		};
		timer = new Timer("Update_time");
		timer.schedule(timerTask, 0, 1000);
		tfJindu.setText(loadBestTimeStr());
	}
	private String loadBestTimeStr(){
		int time = loadBestTime();
		if(time == 0){
			return "������Ѽ�¼";
		}else{
			return String.format("�����ʱ %d ��", time);
		}
	}
	int use_time =1;
	private Timer timer = null;
	int countTaverse = 0;
//	private View getReadyPuzzle(int resid, int levelid){
////		//�����Ļ�Ŀ�͸�
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
//		LDLog.i("Game", "getOldPuzzle piececount = " + piececount);
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
	
//		//�����Ļ�Ŀ�͸�
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
		LDLog.i("Game", "getOldPuzzle piececount = " + piececount);
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
        
        //�Ը����п�ͼƬ��װ��imagebutton����
        createAllPieceImageButton(allPieces);
        
        //����װ�õ�imageview������Ƶ�ƴͼ����
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
	 * ����ͼƬ
	 * @param resid
	 * @return
	 */
	private Bitmap scalePic(Bitmap wallpaper){
		//�����Ļ�Ŀ�͸�
		
		
		//���ͼƬ�Ŀ�͸�
        int width = wallpaper.getWidth();
        int height = wallpaper.getHeight();
        
        //����������,ʹͼƬ���ֻ���Ļһ����С
        float scaleWidth = (float)screenWidth / width;
        float scaleHeight = (float)screenHeight / height;
        
        //��������ͼƬ�õ�matrix����
        Matrix matrix = new Matrix();
        
        //����ͼƬ����
        matrix.postScale(scaleWidth, scaleHeight);
        
        //�����µ�ͼƬ
        Bitmap newWallpPaper = Bitmap.createBitmap(wallpaper, 0, 0, width, height, matrix, true);

        return newWallpPaper;
		
	}
	
	private void createAllPieceImageButton(Vector<Piece> allPieces){
		for(int i=0; i<allPieces.size(); i++){
			Piece piece = (Piece) allPieces.get(i);			
			PieceImageButton pieceImageButton = new PieceImageButton(this);
			
			pieceImageButton.setId(i);  //��Ƭ��ΨһID
			pieceImageButton.setMinp(piece.getMinp());     //������Ƭ���ⲿ��ʼ��,��ͼǰ�ĵ�λ
			pieceImageButton.setLocation(new Point(0,0));
			
			//ͼƬռ������ImageButton,����1
			/*
			pieceImageButton.setPadding(0, 0, 0, 0);
			pieceImageButton.setScaleType(ScaleType.FIT_XY);
			pieceImageButton.setImageBitmap(piece.getBmPiece());
			pieceImageButton.getBackground().setAlpha(0);
			*/
			
			pieceImageButton.pieceHeight = piece.getPieceHeight();
			pieceImageButton.pieceWidth = piece.getPieceWidth();
			LDLog.e("TAG", "pib.pieceWidth"+piece.getPieceWidth());
			LDLog.e("TAG", piece.getMaxp().x- piece.getMinp().x+"pib.pieceWidth");
			//����ͼƬռ������ImageButton������2
			BitmapDrawable bd = new BitmapDrawable(piece.getBmPiece());
			pieceImageButton.setBackgroundDrawable(bd);			
			
			allImagePieces.add(pieceImageButton);
		}
		
	}
	
	//ͨ��bitmap����
	private Vector<Piece> createAllPieces(Bitmap bitmap, int row, int line){
		PieceFactory pu = new PieceFactory(this);
    	pu.setImage(bitmap);
    	pu.setRowAndLine(row, line);
    	
    	return pu.getAllPiece();
	}
	
//	//ͨ����Դ����
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
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				
				//puzzle.bringChildToFront(v);  //�Ѹ���ͼ����������������ͼ֮��
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
				
				//�����ӳ�
				checkMove((PieceImageButton)v, dx, dy, movePieces);
				moveSomePieces(movePieces);
				movePieces.clear();   //�����ƶ��ı�־����տ��ƶ���¼
				cleanPath();
				
				
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				
				break;
			case MotionEvent.ACTION_UP:
				
				//��ȡ����Ƭ������·����Ȼ���ƶ���Ƭ
				cleanPath();
				PieceImageButton firstPiece = checkAbsorb((PieceImageButton)v);
				cleanPath();
				absorb(firstPiece);
				
				//��������ʾ��ǰ��
				displayFront(firstPiece);
				
				//�ж��Ƿ����
				hasComplete();
				
				break;        		
			}

			return false;
		}
    };
    
    private void displayFront(PieceImageButton curPiece){
    	puzzle.bringChildToFront(curPiece);   //�Ѹ���ͼ����������������ͼ֮��
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
    private void sumAbsortPiece(PieceImageButton piece){
    	if(!piece.isAbsort){
    		piece.isAbsort = true;
    		refreshJindu();
    	}
    	
    }
    //��top��right��feet��left��ʼ����������������־
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
    	if(curRow > 0){   //��ǰ��Ƭ�����������Ƭ
    		int topPieceId = (curRow - 1) * line + curLine;
    		if(!curPiece.isHasTop()){  //����������Ƭ��δ����
    			//��������������Ƭ����û����ײ����õ�������Ƭ��λ���ж��Ƿ�����
    			PieceImageButton topPiece = (PieceImageButton) allImagePieces.get(topPieceId);
	    		Point topLoc = topPiece.getLocation();
	    		Point topMinp = topPiece.getMinp();
	    		
	    		//�����������������������
	    		if(distance(curMinp, topMinp, curLoc, topLoc, INACCURACY)){
	    			curPiece.setHasTop(true);
	    			topPiece.setHasFeet(true);
	    			if(firstPiece == null){
	    				firstPiece = topPiece;
	    			}
	    			sumAbsortPiece(curPiece);
	    		}
    		}else{  //����������Ƭ�Ѿ�����,�Ҳ�����������Դ��������ѭ����,������������Ƭ����
    			PieceImageButton topPiece = (PieceImageButton) allImagePieces.get(topPieceId);
    			if(!topPiece.isTraverse()){
    				checkAbsorb(topPiece);
    			}

    		}
    	}
    	
    	//right
    	if(curLine < (line -1)){  //��ǰ��Ƭ�����������Ƭ
    		int rightPieceId = curId + 1;
    		if(!curPiece.isHasRight()){  //����������Ƭ��Ϊ����
    			//��������������Ƭ����û����ײ����õ�������Ƭ��λ���ж��Ƿ�����
    			PieceImageButton rightPiece = (PieceImageButton) allImagePieces.get(rightPieceId);
	    		Point rightLoc = rightPiece.getLocation();
	    		Point rightMinp = rightPiece.getMinp();

	    		//�����������������������
	    		if(distance(curMinp, rightMinp, curLoc, rightLoc, INACCURACY)){
	    			curPiece.setHasRight(true);
	    			rightPiece.setHasLeft(true);
	    			if(firstPiece == null){
	    				firstPiece = rightPiece;
	    			}
	    			sumAbsortPiece(curPiece);
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
    			//��������������Ƭ����û����ײ����õ�������Ƭ��λ���ж��Ƿ�����
    			PieceImageButton feetPiece = (PieceImageButton) allImagePieces.get(feetPieceId);
	    		Point feetLoc = feetPiece.getLocation();
	    		Point feetMinp = feetPiece.getMinp();
	    		
	    		//�����������������������
	    		if(distance(curMinp, feetMinp, curLoc, feetLoc, INACCURACY)){
	    			curPiece.setHasFeet(true);
	    			feetPiece.setHasTop(true);
	    			if(firstPiece == null){
	    				firstPiece = feetPiece;
	    			}
	    			sumAbsortPiece(curPiece);
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
    			//��������������Ƭ����û����ײ����õ�������Ƭ��λ���ж��Ƿ�����
    			PieceImageButton leftPiece = (PieceImageButton) allImagePieces.get(leftPieceId);
	    		Point leftLoc = leftPiece.getLocation();
	    		Point leftMinp = leftPiece.getMinp();
	    		
	    		//�����������������������
	    		if(distance(curMinp, leftMinp, curLoc, leftLoc, INACCURACY)){
	    			curPiece.setHasLeft(true);
	    			leftPiece.setHasRight(true);
	    			if(firstPiece == null){
	    				firstPiece = leftPiece;
	    			}
	    			sumAbsortPiece(curPiece);
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
    	//��ǰX����Ĳ�ֵ����ԭ����������Ĳ�ֵ�ӽ�ʱ
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
    	if(timer != null){
    		timer.cancel();
    		timer = null;
    	}
    	savaGameBestTime();
    	TextView textView = new TextView(getApplicationContext());
    	textView.setTag(String.format("��ʱ %d ��", use_time));
    	Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("ƴͼ���");
		builder.setCancelable(false);
		builder.setView(textView);
		builder.setPositiveButton("��һ��", new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialogInterface, int which) {
				nextGame();   //��ɺ�����pt_game�еļ�¼
				
			}
			
		}).setNegativeButton("����",new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialogInterface, int which) {
				finish();
			}
			
		}).show();
    }
    /**
     * ��ȡ�û������Ϸʱ��
     * @return
     */
    private int loadBestTime(){
    	db.open();
    	int time = 0;
		Cursor cursor = db.getEntry("BEST_TIME", "where image_name = '"+imageName+"' and level_id =" + levelId);
		Log.e("TAG", "time_"+cursor.getCount());
		while(cursor.moveToNext()){
			time = cursor.getInt(cursor.getColumnIndexOrThrow("image_time"));
			String timex = cursor.getString(cursor.getColumnIndexOrThrow("image_name"));
			Log.e("TAG", timex+"time"+time);
		}
		cursor.close();
		db.close();
		return time;
		
    }
	private void savaGameBestTime() {
		int time = loadBestTime();
		db.open();
		if(use_time < time || time == 0){
			if(time !=0){
				db.removeEntry("BEST_TIME", "image_name = '"+imageName+"' and level_id =" + levelId);
			}
			ContentValues values = new ContentValues();
			values.put("image_name",  imageName);
			values.put("image_time",  use_time);
			values.put("level_id",  levelId);
			long lond =db.insertEntry("BEST_TIME", values);
		}
		db.close();
		
	}
	private ProgressBar progressBar;
	private TextView tfTime;
	private TextView tfJindu;
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
			Log.e("TAG",System.currentTimeMillis() - time + "��saveGame-end");
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
