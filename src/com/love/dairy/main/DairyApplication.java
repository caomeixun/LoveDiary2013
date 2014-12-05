package com.love.dairy.main;

import java.util.HashMap;

import android.app.Application;

public class DairyApplication extends Application{

	/**
	 * 用于Activity之间缓存数据
	 */
	public HashMap<String, Object> activityBundle = new HashMap<String, Object>();
	/**
	 * 一级缓存
	 */
	public HashMap<String, Object> dataCache = new HashMap<String, Object>();
	
}
