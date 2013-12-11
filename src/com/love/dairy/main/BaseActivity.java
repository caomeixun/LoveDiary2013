package com.love.dairy.main;

import com.love.dairy.sql.BackupTask;

import android.app.Activity;
import android.content.SharedPreferences;

public class BaseActivity extends Activity {
	public static String IMAGE_PATH = "IMAGE_PATH";
	public void saveSharedPreferencesData(String key,String value){
		SharedPreferences sp = this.getSharedPreferences("love_data", 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public String getSharedPreferencesData(String key){
		SharedPreferences sp = this.getSharedPreferences("love_data", 0);
		return sp.getString(key, null);
	}
	
	// 数据恢复

	public void dataRecover() {


		new BackupTask(this).execute("restroeDatabase");

	}

	// 数据备份

	public void dataBackup() {


		new BackupTask(this).execute("backupDatabase");

	}
}
