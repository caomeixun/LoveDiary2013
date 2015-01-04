package com.love.dairy.sql;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.love.dairy.pojo.ImageInfo;

/**
 * 增删改主类
 * @author leon~
 * @time 2011-10-12下午04:34:52
 * Email:caomeixun_li@sohu.com
 */
	public class DataHelper {
		//数据库名称
		private static String DB_NAME = "myloveData.db";
		//数据库版本
		private static int DB_VERSION = 2;
		private SQLiteDatabase db;
		private SqliteHelper dbHelper;
	
		public DataHelper(Context context){
			dbHelper=new SqliteHelper(context,DB_NAME, null, DB_VERSION);
			db= dbHelper.getWritableDatabase();
		}
		
		public void close()
		{
			if(db!=null)
				db.close();
			if(dbHelper!=null)
				dbHelper.close();
		}
		//获取users表中的UserID、Access Token、Access Secret的记录
		public List<ImageInfo> getUserList()
		{
			List<ImageInfo> userList = new ArrayList<ImageInfo>();
			Cursor cursor=db.query(SqliteHelper.TB_NAME, null, null, null, null, null, ImageInfo.ID+" DESC");
			cursor.moveToFirst();
			while(!cursor.isAfterLast()&& (cursor.getString(1)!=null)){
				ImageInfo image=new ImageInfo();
				image.id = cursor.getString(0);
				image.name = cursor.getString(1);
				image.ivDate = cursor.getString(2);
				image.path = cursor.getString(3);
				image.content = cursor.getString(4);
				image.title = cursor.getString(5);
				userList.add(image);
				cursor.moveToNext();
			}
			cursor.close();
			return userList;
		}
		public ImageInfo getImageInfo(String imagePath)
		{
			ImageInfo image = null;
			Cursor cursor=db.query(SqliteHelper.TB_NAME, null, ImageInfo.PATH + "='" + imagePath+"'", null, null, null, ImageInfo.ID+" DESC");
			cursor.moveToFirst();
			while(!cursor.isAfterLast()&& (cursor.getString(1)!=null)){
				image=new ImageInfo();
				image.id = cursor.getString(0);
				image.name = cursor.getString(1);
				image.ivDate = cursor.getString(2);
				image.path = cursor.getString(3);
				image.content = cursor.getString(4);
				image.title = cursor.getString(5);
				cursor.moveToNext();
			}
			cursor.close();
			return image;
		}
		
		
	//判断users表中的是否包含某个UserID的记录
		public Boolean haveUserInfo(String imagePath)
		{
			Boolean b=false;
			Cursor cursor=db.query(SqliteHelper.TB_NAME, null,  ImageInfo.PATH + "='" + imagePath+"'", null, null, null,null);
			b=cursor.moveToFirst();
			Log.e("HaveUserInfo",b.toString());
			cursor.close();
			return b;
		}
		
//		//更新users表的记录，根据UserId更新用户昵称和用户图标
//		public int UpdateUserInfo(String userName,Bitmap userIcon,String UserId)
//		{
//			ContentValues values = new ContentValues();
//			values.put(UserInfo.USERNAME, userName);
//			// BLOB类型 
//			final ByteArrayOutputStream os = new ByteArrayOutputStream(); 
//			// 将Bitmap压缩成PNG编码，质量为100%存储 
//			userIcon.compress(Bitmap.CompressFormat.PNG, 100, os); 
//			// 构造SQLite的Content对象，这里也可以使用raw 
//			values.put(UserInfo.USERICON, os.toByteArray());
//			int id= db.update(SqliteHelper.TB_NAME, values, UserInfo.USERID + "=" + UserId, null);
//			Log.e("UpdateUserInfo2",id+"");
//			return id;
//		}
		
		//更新users表的记录
		public long updateUserInfo(ImageInfo image)
		{
			ContentValues values = new ContentValues();
			values.put(ImageInfo.TITLE, image.title);
			values.put(ImageInfo.CONTENT, image.content);
			values.put(ImageInfo.IVDATE, image.ivDate);
			values.put(ImageInfo.NAME, image.name);
			values.put(ImageInfo.PATH, image.path);
			int id= db.update(SqliteHelper.TB_NAME, values, ImageInfo.PATH + "='" + image.path+"'", null);
			Log.e("UpdateUserInfo",id+"");
			return id;
		}
		
		//添加users表的记录
		public Long addUserInfo(ImageInfo image)
		{
			if(haveUserInfo(image.path)){
				return updateUserInfo(image);
			}
			ContentValues values = new ContentValues();
			values.put(ImageInfo.ID, image.id);
			values.put(ImageInfo.TITLE, image.title);
			values.put(ImageInfo.CONTENT, image.content);
			values.put(ImageInfo.IVDATE, image.ivDate);
			values.put(ImageInfo.NAME, image.name);
			values.put(ImageInfo.PATH, image.path);
			Long uid = db.insert(SqliteHelper.TB_NAME, ImageInfo.ID, values);
			Log.e("SaveUserInfo",uid+"");
			return uid;
		}
		
		
		//删除users表的记录
		public int delUserInfo(String imagePath){
			int id= db.delete(SqliteHelper.TB_NAME, ImageInfo.PATH + "='" + imagePath+"'", null);
			Log.e("DelUserInfo",id+"");
			return id;
			}
		}
