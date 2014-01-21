package com.love.dairy.cutimage;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

/**
 * 主Activity,用于初始化一些界面和Dialog
 * 
 * @author rendongwei
 * 
 */
public class KXActivity extends Activity {
	/**
	 * 屏幕的宽度和高度
	 */
	protected int mScreenWidth;
	protected int mScreenHeight;
	/**
	 * 表情控件
	 */
	private PopupWindow mFacePop;
	private View mFaceView;
	protected ImageView mFaceClose;
	protected GridView mFaceGridView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 获取屏幕宽度和高度
		 */
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;

	}


	/**
	 * 显示表情控件
	 * 
	 * @param parent
	 *            显示位置的根布局
	 */
	protected void showFace(View parent) {
		if (!mFacePop.isShowing()) {
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
			.hideSoftInputFromWindow(KXActivity.this
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			mFacePop.showAtLocation(parent, Gravity.CENTER, 0, 0);
		}
	}

	/**
	 * 隐藏表情控件
	 */
	protected void dismissFace() {
		if (mFacePop != null && mFacePop.isShowing()) {
			mFacePop.dismiss();
		}
	}

	
}
