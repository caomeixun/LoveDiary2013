package com.love.dairy.main.renren;

import com.love.dairy.main.R;
import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.RennClient.LoginListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Button loginBtn;

	private Button logoutBtn;

	private Button userServiceBtn;

	private Button albumServiceBtn;

	private Button commentServiceBtn;

	private Button feedServiceBtn;

	private Button unotificationServiceBtn;

	private Button photoServiceBtn;

	private Button profileServiceBtn;

	private Button shareServiceBtn;

	private Button statusServiceBtn;

	private Button ubbServiceBtn;
	
	private Button blogServiceBtn;
	
	private Button likeServiceBtn;
	
	private Button appServiceButton;

	private RennClient rennClient;

	private static final String APP_ID = "168802";

	private static final String API_KEY = "e884884ac90c4182a426444db12915bf";

	private static final String SECRET_KEY = "094de55dc157411e8a5435c6a7c134c5";

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		rennClient = RennClient.getInstance(this);
		rennClient.init(APP_ID, API_KEY, SECRET_KEY);
		rennClient
				.setScope("read_user_blog read_user_photo read_user_status read_user_album "
						+ "read_user_comment read_user_share publish_blog publish_share "
						+ "send_notification photo_upload status_update create_album "
						+ "publish_comment publish_feed");
		// rennClient
		// .setScope("read_user_blog read_user_photo read_user_status read_user_album "
		// + "read_user_comment publish_blog publish_share "
		// + "send_notification photo_upload status_update create_album "
		// + "publish_feed");
		// rennClient.setScope("read_user_blog read_user_status");
		rennClient.setTokenType("bearer");

		setContentView(R.layout.activity_main);
		intiView();
		
		
		
	}

	private void intiView() {
		loginBtn = (Button) findViewById(R.id.login_btn);
		loginBtn.setOnClickListener(this);
		logoutBtn = (Button) findViewById(R.id.logout_btn);
		logoutBtn.setOnClickListener(this);
		userServiceBtn = (Button) findViewById(R.id.userService_btn);
		userServiceBtn.setOnClickListener(this);
		albumServiceBtn = (Button) findViewById(R.id.albumService_btn);
		albumServiceBtn.setOnClickListener(this);
		commentServiceBtn = (Button) findViewById(R.id.commentService_btn);
		commentServiceBtn.setOnClickListener(this);
		feedServiceBtn = (Button) findViewById(R.id.feedService_btn);
		feedServiceBtn.setOnClickListener(this);
		unotificationServiceBtn = (Button) findViewById(R.id.notificationService_btn);
		unotificationServiceBtn.setOnClickListener(this);
		photoServiceBtn = (Button) findViewById(R.id.photoService_btn);
		photoServiceBtn.setOnClickListener(this);
		profileServiceBtn = (Button) findViewById(R.id.profileService_btn);
		profileServiceBtn.setOnClickListener(this);
		shareServiceBtn = (Button) findViewById(R.id.shareService_btn);
		shareServiceBtn.setOnClickListener(this);
		statusServiceBtn = (Button) findViewById(R.id.statusService_btn);
		statusServiceBtn.setOnClickListener(this);
		ubbServiceBtn = (Button) findViewById(R.id.ubbService_btn);
		ubbServiceBtn.setOnClickListener(this);
		blogServiceBtn=(Button)findViewById(R.id.blogService_btn);
		blogServiceBtn.setOnClickListener(this);
		likeServiceBtn=(Button)findViewById(R.id.likeService_btn);
		likeServiceBtn.setOnClickListener(this);
		appServiceButton=(Button)findViewById(R.id.appService_btn);
		appServiceButton.setOnClickListener(this);

		if (rennClient.isLogin()) {
			Intent intent2 = new Intent(MainActivity.this,
					PhoneAlbumActivity.class);
			MainActivity.this.startActivity(intent2);
			finish();
		} else {
			rennClient.setLoginListener(new LoginListener() {
				@Override
				public void onLoginSuccess() {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "登录成功",
							Toast.LENGTH_SHORT).show();
					Intent intent2 = new Intent(MainActivity.this,
							PhoneAlbumActivity.class);
					MainActivity.this.startActivity(intent2);
					finish();
				}

				@Override
				public void onLoginCanceled() {
					Toast.makeText(MainActivity.this, "登录失败",
							Toast.LENGTH_SHORT).show();
					finish();
				}
			});
			rennClient.login(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			rennClient.setLoginListener(new LoginListener() {
				@Override
				public void onLoginSuccess() {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "登录成功",
							Toast.LENGTH_SHORT).show();
					loginBtn.setVisibility(View.GONE);
					logoutBtn.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoginCanceled() {
					loginBtn.setVisibility(View.VISIBLE);
					logoutBtn.setVisibility(View.GONE);
				}
			});
			rennClient.login(this);
			break;
		case R.id.logout_btn:
			rennClient.logout();
			loginBtn.setVisibility(View.VISIBLE);
			logoutBtn.setVisibility(View.GONE);
			break;
		default:
			break;
		}

		if (rennClient.isLogin() == false)
			return;

		switch (v.getId()) {
		
		case R.id.albumService_btn:
			Intent intent2 = new Intent(MainActivity.this,
					PhoneAlbumActivity.class);
			MainActivity.this.startActivity(intent2);
			break;
		
		case R.id.photoService_btn:
			Intent intent5 = new Intent(MainActivity.this,
					PhotoServiceActivity.class);
			MainActivity.this.startActivity(intent5);
			break;
		
		default:
			break;
		}
	}
}
