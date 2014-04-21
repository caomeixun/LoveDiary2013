package com.love.dairy.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.love.dairy.main.R;
import com.love.dairy.widget.FlipCards;

public class PicView extends Activity {
	private int imageId;
	private MyDBAdapter db = null;
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		LinearLayout picReview = (LinearLayout) inflater.inflate(R.layout.pic_view, null);
		setContentView(picReview);
		
		Intent i = this.getIntent();
		Bundle bundle = i.getExtras();
		imageId = bundle.getInt("imageId");
		Drawable dg = MyBitmapFactory.BitmapToDrawable(FlipCards.dateCache.get(imageId));
		picReview.setBackgroundDrawable(dg);
		
		Button btnContinue = (Button)findViewById(R.id.btnContinue);
		btnContinue.setOnClickListener(onClickListener);
        
        Button btnExit = (Button)findViewById(R.id.btnExit);
        btnExit.setOnClickListener(onClickListener);
        db = new MyDBAdapter(this);
		db.open();
		
		Button setting_level = (Button)findViewById(R.id.setting_level);
		setting_level.setOnClickListener(onClickListener);
	}
	private void openLevelDialog(){
		int currentLevel = getCurrentLevel();
		
		String table_level = "pt_level";
    	Cursor cursor = db.getAllEntries(table_level);
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择难易度");
		builder.setSingleChoiceItems(cursor, currentLevel-1, "desc", new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int item) {
				// TODO Auto-generated method stub
				String table = "pt_game";
				ContentValues cv = new ContentValues();
				cv.put("level_id", item+1);
				db.insertEntry(table, cv);
				
				dialog.cancel();
				doDirect("NEW_GAME_ACTION");
			}
			
		}).show();
		
	}
	private int getCurrentLevel(){
		String table = "pt_game";
		String col_id = "_id";
		String col_level_id = "level_id";
		//Cursor cursor = db.getLastEntry(table, col_id);
		//return cursor.getInt(cursor.getColumnIndexOrThrow(col_level_id));
		
		//Log.i("", "db.getMaxValue(table, col_level_id)" + db.getMaxValue(table, col_level_id));
		return db.getMaxValue(table, col_level_id, col_id);
		
	}
	private OnClickListener onClickListener = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			int currentViewID = ((Button)v).getId();
		
			switch(currentViewID){
			case R.id.btnContinue:
				doDirect("RETURN_GAME_ACTION");
				break;
				
			case R.id.btnExit:
				finish();
				break;
			case R.id.setting_level:
				openLevelDialog();
				return;
			}
			
    		
		}
		
	};
	private void doDirect(String action){
		Intent i = null;
		i = new Intent(PicView.this, Game.class);
		i.setAction(action);
		Bundle bundle = new Bundle();
		bundle.putInt("imageId", imageId);
		i.putExtras(bundle);
		startActivity(i);
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		finish();
	}
}
