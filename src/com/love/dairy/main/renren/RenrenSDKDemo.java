package com.love.dairy.main.renren;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.love.dairy.main.R;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.view.RenrenAuthListener;


public class RenrenSDKDemo extends BaseActivity {
	
	private static final String API_KEY = "6b1016db20c540e78bd1b20be4c707a3";
	
	private static final String SECRET_KEY = "4723a695c09e4ddebbe8d87393d95fb4";
	
	private static final String APP_ID = "105381";
	
	private Renren renren;
	
	private Handler handler;
	
	private Button uploadbButton;
	
	private Button oAuthButton;
	
	private Button ssoButton;
	
	private Button pwdFlowButton;
	
	private LinearLayout mainLayout;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.renren_main, null);
        root.addView(mainLayout);
        // 左边的返回按钮不显示
        titlebarLeftButton.setVisibility(View.GONE);
        // 设置标题
        titlebarText.setText("登录");
        
        renren = new Renren(API_KEY, SECRET_KEY, APP_ID, this);
        handler = new Handler();
        
        initButtons();
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		renren.init(this);
	}

	/**
     * start the api list activity
     */
    private void startAlarmList() {
    	Intent intent = new Intent(this, ApiGetAlbumsActivity.class);
    	intent.putExtra(Renren.RENREN_LABEL, renren);
    	startActivity(intent);
    	finish();
    }
    /**
     * initialize the buttons and events
     */
    private void initButtons() {
    	final RenrenAuthListener listener = new RenrenAuthListener() {

			@Override
			public void onComplete(Bundle values) {
				Log.d("test",values.toString());
				startAlarmList();
			}

			@Override
			public void onRenrenAuthError(
					RenrenAuthError renrenAuthError) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(RenrenSDKDemo.this, 
								RenrenSDKDemo.this.getString(R.string.auth_failed), 
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onCancelLogin() {
			}

			@Override
			public void onCancelAuth(Bundle values) {
			}
			
		};
		
    	
    	oAuthButton = (Button)mainLayout.findViewById(R.id.auth_site_mode);
    	oAuthButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				renren.authorize(RenrenSDKDemo.this, listener);
			}
		});
    	
    	ssoButton = (Button)mainLayout.findViewById(R.id.sso_mode);
    	ssoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//use default permissions here, see Renren class for detail
				renren.authorize(RenrenSDKDemo.this, null, listener, 1);
			}
		});
    	
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (renren != null) {
			renren.authorizeCallback(requestCode, resultCode, data);
		}
	}
    
    
    
    
}