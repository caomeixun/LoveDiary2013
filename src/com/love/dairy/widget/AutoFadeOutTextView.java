package com.love.dairy.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.love.dairy.main.R;

public class AutoFadeOutTextView extends LinearLayout {
	private int alpha_time = 300;
	int index = 0;
	private String firstLine;
	private String secondLine;
	private FlipViewGroup flip = null;
	public AutoFadeOutTextView(Context context){
		super(context);
	}
	public AutoFadeOutTextView(Context paramContext,FlipViewGroup flip,String time) {
		super(paramContext);
		setOrientation(1);
		this.flip = flip;
		time = time == null ? "20090313":time;
		try {
			long l2 = System.currentTimeMillis();
			long l3 = new SimpleDateFormat("yyyyMMdd", Locale.US).parse(
					time).getTime();
			long l1 = l2 - l3;
			String str = "第" + l1 / 1000L / 60L / 60L / 24L + "天";
			firstLine = "今天是";
			secondLine = "我们相爱的";
			addTextView(firstLine);
			addTextView(secondLine);
			addTextView(str);
			displaySelf();
			return;
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}
	private void displaySelf() {
		this.index++;
		AlphaAnimation localAlphaAnimation = new AlphaAnimation(1.0F, 0.0F);
		localAlphaAnimation.setDuration(this.alpha_time * 2);
		localAlphaAnimation.setStartOffset(this.index
				* (-100 + this.alpha_time));
		localAlphaAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				flip.startFlipping();
			}
		});
		localAlphaAnimation.setFillAfter(true);
		this.startAnimation(localAlphaAnimation);
		
	}
	private void addTextView(String paramString) {
		LinearLayout localLinearLayout = new LinearLayout(getContext());
		localLinearLayout.setGravity(5);
		char[] arrayOfChar = paramString.toCharArray();
		int i = arrayOfChar.length;
		for (int j = 0;; ++j) {
			if (j >= i) {
				addView(localLinearLayout);
				return;
			}
			char c = arrayOfChar[j];
			TextView localTextView = new TextView(getContext());
			localTextView.setText(c + "");
			localTextView.setTextAppearance(getContext(), R.style.title_shadow);
			localTextView.setGravity(Gravity.CENTER);
			int textSize = 16;
			localTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
			AnimationSet localAnimationSet = new AnimationSet(true);
			AlphaAnimation localAlphaAnimation = new AlphaAnimation(0.0F, 1.0F);
			localAlphaAnimation.setDuration(this.alpha_time);
			localAlphaAnimation.setStartOffset(this.index
					* (-100 + this.alpha_time));
			localAnimationSet.addAnimation(localAlphaAnimation);
			if ((c >= '0') && (c <= '9')) {
				localTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,
						textSize + 5);
			}
			this.index = (1 + this.index);
			localAnimationSet.setFillAfter(true);
			localTextView.startAnimation(localAnimationSet);
			localTextView.setPadding(8,8,8,8);
			localLinearLayout.addView(localTextView);
		}

	}
}
