package com.love.dairy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsoluteLayout;

@SuppressWarnings("deprecation")
public class LDAbsoluteLayout extends AbsoluteLayout{

	private boolean isReferesh = true;
	public LDAbsoluteLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(isReferesh){
			isReferesh = false;
		super.onLayout(changed, l, t, r, b);
		}
	}
}
