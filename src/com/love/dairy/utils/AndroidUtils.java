package com.love.dairy.utils;

import java.util.ArrayList;

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
}
