package com.love.dairy.game;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;


public class MyDBAdapter {
	private static final String DATABASE_NAME = "puzzle.db";
	private static final String TABLE_LEVEL = "pt_level";
	private static final String TABLE_EFFECT = "pt_effect";
	private static final String TABLE_GAME = "pt_game";
	private static final String TABLE_PIECE = "pt_piece";
	private static final int DATABASE_VERSION = 1;
	
	//where子句中使用的索引列的名字
	private static final String KEY_ID = "_id";
	private static final String DESC = "desc";
	
	//数据库中的每一列的名称和列索引
	//level
	private static final String LEVEL_ID = "level_id";
	private static final String PIECE_ROW = "piece_row";
	private static final String PIECE_LINE = "piece_line";
	
	//effect
	private static final String EFFECT_ID = "effect_id";
	private static final String IS_CHECKED = "is_checked";
	
	//game
	private static final String IMAGE_ID = "image_id";
	private static final String IMAGE_NAME = "image_name";
	
	//piece
	private static final String PIECE_ID = "piece_id";
	private static final String PIECE_MINP_X = "piece_minp_x";
	private static final String PIECE_MINP_Y = "piece_minp_y";
	private static final String PIECE_LOC_X = "piece_loc_x";
	private static final String PIECE_LOC_Y = "piece_loc_y";
	private static final String PIECE_IMAGE = "piece_image";
	private static final String PIECE_TOP = "piece_top";
	private static final String PIECE_RIGHT = "piece_right";
	private static final String PIECE_FEET = "piece_feet";
	private static final String PIECE_LEFT = "piece_left";
	

	
	//SQL语句
	//难易度level
	private static final String LEVEL_TABLE_CREATE_SQL = "create table " +
			TABLE_LEVEL + " (" + KEY_ID + " integer primary key autoincrement, " +
			LEVEL_ID + " integer not null, " +
			PIECE_ROW + " integer not null, " +
			PIECE_LINE + " integer not null, " +
			DESC + " text ); ";
	
	//效果effect
	private static final String EFFECT_TABLE_CREATE_SQL = "create table " +
		TABLE_EFFECT + " (" + KEY_ID + " integer primary key autoincrement, " +
		EFFECT_ID + " integer not null, " +
		IS_CHECKED + " boolean not null, " +
		DESC + " text ); ";
	
	//正在进行游戏game
	private static final String GAME_TABLE_CREATE_SQL = "create table " +
			TABLE_GAME + " (" + KEY_ID + " integer primary key autoincrement, " +
			IMAGE_ID + " integer, " +
			IMAGE_NAME + " text, " +
			LEVEL_ID + " integer, " +
			DESC + " text ); ";
	
	//图片碎片piece
	private static final String PIECE_TABLE_CREATE_SQL = "create table " +
			TABLE_PIECE + " (" + KEY_ID + " integer primary key autoincrement, " +
			IMAGE_ID + " integer, " +
			LEVEL_ID + " integer, " +
			PIECE_ID + " integer, " +
			PIECE_MINP_X + " integer, " +
			PIECE_MINP_Y + " integer, " +
			PIECE_LOC_X + " integer, " +
			PIECE_LOC_Y + " integer, " +
			PIECE_IMAGE + " blob, " +
			PIECE_TOP + " boolean, " +
			PIECE_RIGHT + " boolean, " +
			PIECE_FEET + " boolean, " +
			PIECE_LEFT + " boolean, " +
			DESC + " text ); ";
	
	//保存数据库实例的变量
	private SQLiteDatabase db;
	
	//使用数据库的应用程序上下文
	private final Context context;
	
	//数据库打开/更新helper
	private MyDbHelper dbHelper;
	
	public MyDBAdapter(Context _context) {
		context = _context;
		dbHelper = new MyDbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
	
	public MyDBAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		db.close();
	}
	
	public long insertPiece(String table, PieceImageButton pib, int imageid, int levelid){
		ContentValues values = new ContentValues();
		//碎片所属拼图原图
		values.put("image_id", imageid);
		values.put("level_id", levelid);
		
		int pieceid = pib.getId();
		values.put("piece_id", pieceid);
		
		Point minp = pib.getMinp();
		int pieceminpx = minp.x;
		int pieceminpy = minp.y;
		values.put("piece_minp_x", pieceminpx);
		values.put("piece_minp_y", pieceminpy);
		
		Point loc = pib.getLocation();
		int piecelocx = loc.x;
		int piecelocy = loc.y;
		values.put("piece_loc_x", piecelocx);
		values.put("piece_loc_y", piecelocy);
		
		Bitmap bmp = MyBitmapFactory.DrawableToBitmap(pib.getBackground());
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
		values.put("piece_image", os.toByteArray());
		
		values.put("piece_top", pib.isHasTop());
		values.put("piece_right", pib.isHasRight());
		values.put("piece_feet", pib.isHasFeet());
		values.put("piece_left", pib.isHasLeft());
		values.put("desc", pib.getWidth() + ":" + pib.getHeight());
		
		return db.insert(table, null, values);
	}
	
	public long insertEntry(String table, ContentValues values){
		return db.insert(table, null, values);
	}
	
	public int removeEntry(String table, String col, String val){
		return db.delete(table, col + "=" + val, null);
	}
	
	public boolean removeEntry(String table, long _rowIndex){
		return db.delete(table, KEY_ID + "=" + _rowIndex, null) > 0;
	}
	
	public ArrayList getPieces(int imageid, int levelid){
		ArrayList allImagePieces = new ArrayList();
		String sql = " select * from pt_piece where image_id = " + imageid + " and level_id = " + levelid + "; ";
		
		Cursor cursor = db.rawQuery(sql, null);
		for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
			PieceImageButton pib = new PieceImageButton(context);
			int pieceid = cursor.getInt(cursor.getColumnIndexOrThrow("piece_id"));
			pib.setId(pieceid);
			
			int pieceminpx = cursor.getInt(cursor.getColumnIndexOrThrow("piece_minp_x"));
			int pieceminpy = cursor.getInt(cursor.getColumnIndexOrThrow("piece_minp_y"));
			pib.setMinp(new Point(pieceminpx, pieceminpy));
			
			int piecelocx = cursor.getInt(cursor.getColumnIndexOrThrow("piece_loc_x"));
			int piecelocy = cursor.getInt(cursor.getColumnIndexOrThrow("piece_loc_y"));
			pib.setLocation(new Point(piecelocx, piecelocy));
			
			byte[] in = cursor.getBlob(cursor.getColumnIndexOrThrow("piece_image"));
			Bitmap bmp = BitmapFactory.decodeByteArray(in, 0, in.length);
			pib.setBackgroundDrawable(MyBitmapFactory.BitmapToDrawable(bmp));
			
			pib.setHasTop(intToBoolean(cursor.getInt(cursor.getColumnIndexOrThrow("piece_top"))));
			pib.setHasRight(intToBoolean(cursor.getInt(cursor.getColumnIndexOrThrow("piece_right"))));
			pib.setHasFeet(intToBoolean(cursor.getInt(cursor.getColumnIndexOrThrow("piece_feet"))));
			pib.setHasLeft(intToBoolean(cursor.getInt(cursor.getColumnIndexOrThrow("piece_left"))));
			
			allImagePieces.add(pib);
		}
		
		return allImagePieces;
		
	}
	
	public Cursor getAllEntries(String table){
		return db.query(table, null, null, null, null, null, null);
	}
	
	public Cursor getALLEntries(String table, String[] cols){
		return db.query(table, cols, null, null, null, null, null);
	}
	
	/**
	 * 
	 * @param table
	 * @param get_col
	 * @param order_col
	 * @return
	 */
	public int getMaxValue(String table, String get_col, String order_col){
		String sql = " select " + get_col + " from " + table + " order by " + order_col + " desc; ";
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.moveToFirst()){
			return cursor.getInt(cursor.getColumnIndexOrThrow(get_col));
		}else{
			return 0;
		}
		
	}
	
	public int getEntryCount(String table, String col, int value){
		Cursor cursor = getEntry(table, col, value);
		int count = cursor.getCount();
		cursor.close();
		return count;
		
	}
	
	public Cursor getEntry(String table, String col, int value){
		String sql = " select * from " + table + " where " + col + "=" + value + " ; ";
		return db.rawQuery(sql, null);
	}
	
	public Cursor getEntry(String table, String col, String value){
		String sql = " select * from " + table + " where " + col + "=" + value + " ; ";
		return db.rawQuery(sql, null);
	}
	
	public int getEntryCount(String table, String where){
		String sql = " select * from " + table + " where " + where + " ; ";
		Cursor cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}
	
	public Cursor getEntry(String table, String where){
		String sql = " select * from " + table + " " + where + " ; ";
		return db.rawQuery(sql, null);
	}
	
	/**
	 * 返回col最后的那一行记录
	 * @param table
	 * @param col
	 * @return Cursor
	 */
	public Cursor getLastEntry(String table, String col){
		String last_sql = " select * from " + table + " order by " + col + " desc; ";
		Cursor cursor = db.rawQuery(last_sql, null);
		if(cursor.moveToFirst()){
			return cursor;
		}else{
			return null;
		}
		
	}
	
	public ContentValues getEntry(long _rowIndex){
		ContentValues contentValues = new ContentValues();
		
		return contentValues;
	}
	
	public int updatePiece(String table, PieceImageButton pib, int imageid){
		String where = IMAGE_ID + "=" + imageid + "; ";
		ContentValues values = new ContentValues();
		
		Point loc = pib.getLocation();
		int piecelocx = loc.x;
		int piecelocy = loc.y;
		values.put("piece_loc_x", piecelocx);
		values.put("piece_loc_y", piecelocy);
		
		values.put("piece_top", pib.isHasTop());
		values.put("piece_right", pib.isHasRight());
		values.put("piece_feet", pib.isHasFeet());
		values.put("piece_left", pib.isHasLeft());
		
		return db.update(table, values, where, null);
	}
	
	public int updateEntry(String table, long _rowIndex, String key, boolean val){
		String where = KEY_ID + "=" + _rowIndex;
		ContentValues values = new ContentValues();
		values.put(key, val);
		
		return db.update(table, values, where, null);
		
	}
	
	public int updateEntry(String table, long _rowIndex, String key, String val){
		String where = KEY_ID + "=" + _rowIndex;
		ContentValues values = new ContentValues();
		values.put(key, val);
		
		return db.update(table, values, where, null);
		
	}
	
	public int updateEntry(String table, long _rowIndex, String key, int val){
		String where = KEY_ID + "=" + _rowIndex;
		ContentValues values = new ContentValues();
		values.put(key, val);
		
		return db.update(table, values, where, null);
		
	}
	
	private boolean intToBoolean(int value){
		if(value > 0){
			return true;
		}else{
			return false;
		}
	}
	
	private static class MyDbHelper extends SQLiteOpenHelper{

		public MyDbHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			// TODO Auto-generated method stub
			_db.execSQL(EFFECT_TABLE_CREATE_SQL);
			_db.execSQL(LEVEL_TABLE_CREATE_SQL);
			_db.execSQL(PIECE_TABLE_CREATE_SQL);
			_db.execSQL(GAME_TABLE_CREATE_SQL);
			
			////pt_level
			ContentValues values = new ContentValues();
			values.put("level_id", 1);
			values.put("piece_row", 5);
			values.put("piece_line", 3);
			values.put("desc", "5*3");
			_db.insertOrThrow("pt_level", null, values);
			
			///
			values.clear();
			values.put("level_id", 2);
			values.put("piece_row", 7);
			values.put("piece_line", 5);
			values.put("desc", "7*5");
			_db.insertOrThrow("pt_level", null, values);
			
			///
			values.clear();
			values.put("level_id", 3);
			values.put("piece_row", 9);
			values.put("piece_line", 7);
			values.put("desc", "9*7");
			_db.insertOrThrow("pt_level", null, values);
			
			
			///pt_effect
			values.clear();
			values.put("effect_id", 1);
			values.put("is_checked", 0);
			values.put("desc", "effect 1");
			_db.insertOrThrow("pt_effect", null, values);
			
			///
			values.clear();
			values.put("effect_id", 2);
			values.put("is_checked", 0);
			values.put("desc", "effect 2");
			_db.insertOrThrow("pt_effect", null, values);
			
			///pt_game
			values.clear();
			values.put("level_id", 2);
			_db.insertOrThrow("pt_game", null, values);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
			// TODO Auto-generated method stub
			Log.w("TaskDBAdapter","Upgrade from version " + _oldVersion + " to " + _newVersion + ", which will destroy all old data");
			
			_db.execSQL("DROP TABLE IF EXISTS " + EFFECT_TABLE_CREATE_SQL);
			_db.execSQL("DROP TABLE IF EXISTS " + LEVEL_TABLE_CREATE_SQL);
			_db.execSQL("DROP TABLE IF EXISTS " + PIECE_TABLE_CREATE_SQL);
			_db.execSQL("DROP TABLE IF EXISTS " + GAME_TABLE_CREATE_SQL);
			onCreate(_db);
			
		}
		
	}

}
