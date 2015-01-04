package com.love.dairy.utils;

import com.love.dairy.main.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;



/**
 * 
 *	自定义的dialog
 */
public class HPDialog extends Dialog {

	// 先调用构造方法在调用oncreate方法

	private static boolean isShow = true;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public HPDialog(Context context) {
		super(context);
		new Builder(context).create().show();
	}

	public HPDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	public void show() {
		super.show();
	}
	
	public static class Builder {

		private Context context;
		protected DialogInterface.OnClickListener positiveButtonClickListener,
				negativeButtonClickListener;

		// private TextView msg=(TextView)findViewById(R.id.message);
		public Builder(Context context) {
			this.context = context;
		}

		
		public boolean setCancelable(boolean cancelable){
			
			isShow = cancelable;
			return isShow;
		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonClickListener = listener;
			return this;
		}

		public HPDialog show() {
			HPDialog dialog = create();
			dialog.show();
			return dialog;
		}
		
		public HPDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			// instantiate the dialog with the custom Theme
			final HPDialog dialog = new HPDialog(context, R.style.HPDialog);
			dialog.setCanceledOnTouchOutside(false);//android 4.0以上dialog点击其他地方也会消失false以后就只能点击按钮消失
			
			View layout = inflater.inflate(R.layout.loading, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			dialog.setContentView(layout);
			return dialog;
		}
		
		
	}
}

