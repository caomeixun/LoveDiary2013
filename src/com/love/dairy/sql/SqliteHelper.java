package com.love.dairy.sql;

import com.love.dairy.pojo.ImageInfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * sqlist帮助类，主要是新建数据库和更新
 * @author leon~
 * @time 2011-10-12下午04:34:27
 * Email:530746075@qq.com
 */
public class SqliteHelper extends SQLiteOpenHelper {
	// 用来保存 UserID、Access Token、Access Secret
	public static final String TB_NAME = "images";

	public SqliteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	// 创建表
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NAME + "(" + ImageInfo.ID
				+ " integer primary key," + ImageInfo.NAME + " varchar,"
				+ ImageInfo.IVDATE + " varchar," + ImageInfo.PATH
				+ " varchar," + ImageInfo.CONTENT + " varchar,"
				+ ImageInfo.TITLE + " varchar" + ")");
		Log.e("Database", "onCreate");
	}

	// 更新表
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
		onCreate(db);
		Log.e("Database", "onUpgrade");
	}

	// 更新列
	public void updateColumn(SQLiteDatabase db, String oldColumn,
			String newColumn, String typeColumn) {
		try {
			db.execSQL("ALTER TABLE " + TB_NAME + " CHANGE " + oldColumn + " "
					+ newColumn + " " + typeColumn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
