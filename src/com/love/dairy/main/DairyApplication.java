package com.love.dairy.main;

import java.util.HashMap;

import android.app.Application;

public class DairyApplication extends Application{

	/**
	 * ����Activity֮�仺������
	 */
	public HashMap<String, Object> activityBundle = new HashMap<String, Object>();
	/**
	 * һ������
	 */
	public HashMap<String, Object> dataCache = new HashMap<String, Object>();
	
}
