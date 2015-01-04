package com.love.dairy.utils;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;

public class AndroidUtils {

	public static int[] test(View paramView) {
		int[] arr = new int[2];
		arr[0] = paramView.getLeft();
		arr[1] = paramView.getTop();
		ArrayList<Integer> list = new ArrayList<Integer>();
		View view;
		for (ViewParent viewParent = paramView.getParent(); 
			(viewParent instanceof View); viewParent = view.getParent()) {
			view = (View) viewParent;
			list.add(Integer.valueOf(view.getTop()));
			arr[0] += view.getLeft() - view.getScrollX();
			arr[1] += view.getTop() - view.getScrollY();
		}
		if (list.size() > 2){
			arr[1] -= ((Integer) list.get(-3+ list.size())).intValue();
		}
		return arr;
	}
	
	/**
	 * String型资源名称转int型资源ID R.string.ok/android.R.string.ok
	 * 
	 * @param id
	 *            ok/android.R.string.ok
	 * @param type
	 *            string/string
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public int getResourcesID(Context mContext,String id, String type)
	{
		
		try
		{
			Resources res;
			String defPackage = null;
			if (id.startsWith("android.R."))
			{
				res = Resources.getSystem();
				int lastIndex = id.lastIndexOf(".");
				id = id.substring(lastIndex + 1);
				defPackage = "android";
			}
			else
			{
				res = mContext.getResources();
				defPackage = mContext.getPackageName();
			}
			
			int rid = res.getIdentifier(id, type, defPackage);
			if (rid <= 0)
			{
				rid = res.getIdentifier(id.toLowerCase(), type, defPackage);
			}
			return rid;
		}
		catch (Exception e)
		{
			Log.e("getResourcesID", e.toString() + "id" + id);
		}
		return -1;
	}
}
