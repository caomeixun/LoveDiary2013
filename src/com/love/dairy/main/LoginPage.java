package com.love.dairy.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.love.dairy.LoveApplication;
import com.love.dairy.cutimage.ImageFilterActivity;
import com.love.dairy.game.Game;
import com.love.dairy.match.LianlianKanChoosePic;
import com.love.dairy.pojo.ImageInfo;
import com.love.dairy.sql.DataHelper;
import com.love.dairy.utils.Blur;
import com.love.dairy.widget.FlipCards;

public class LoginPage extends BaseActivity implements OnClickListener{
	public static String IMAGE_ID = "IMAGE_ID"; 
	public static String OPEN_TYPE_PATH = "OPEN_TYPE_PATH"; 
	private int request_code_path = 123;
	private int imagePosition = -1;
	private View title = null;
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle save){
		super.onCreate(save);
		setContentView(R.layout.flip_login);
		imagePosition= getIntent().getIntExtra(IMAGE_ID, -1);
		findViewById(R.id.btnOK).setOnClickListener(this);
		findViewById(R.id.btnRenren).setOnClickListener(this);
		findViewById(R.id.btnPath).setOnClickListener(this);
		findViewById(R.id.btnAbout).setOnClickListener(this);
		findViewById(R.id.btnCut).setOnClickListener(this);
		findViewById(R.id.btnBack).setOnClickListener(this);
		findViewById(R.id.btnDate).setOnClickListener(this);
		findViewById(R.id.btnAuthor).setOnClickListener(this);
		findViewById(R.id.btnMatch).setOnClickListener(this);
		title = findViewById(R.id.topRl);
		if(getIntent().getIntExtra(OPEN_TYPE_PATH, -1)!=-1){
			getPicPath();
		}else{
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						Bitmap bitmap = FlipCards.dateCache.get(imagePosition);
						if(bitmap != null){
							double scaleSize = MainActivity.screenWidth / bitmap.getWidth();
							bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), (int) (getResources().getDimensionPixelSize(R.dimen.title_height)/scaleSize));
							bitmap = Blur.fastblur(getApplicationContext(), bitmap, 12);
							final Bitmap bit = bitmap;
							LoginPage.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									title.setBackgroundDrawable(new BitmapDrawable(bit));;
									
								}
							});
						}
					}
				}).start();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.stack_pop,
					R.anim.slide_out_to_right);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnOK){
			String etTitle = ((EditText)(findViewById(R.id.etTitle))).getText().toString();
			String etContent = ((EditText)(findViewById(R.id.etContent))).getText().toString();
			ImageInfo info = new ImageInfo();
			info.content = etContent;
			info.ivDate = "一年前";
			info.title = etTitle;
			LoveApplication application = (LoveApplication) this.getApplication();
			info.path =MainActivity.path + application.photoIds.get(imagePosition);
			info.name = "Love_"+application.photoIds.get(imagePosition);
			DataHelper dh = new DataHelper(getApplicationContext());
			long result =  dh.addUserInfo(info);
			if(result > 0){
				Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
			}
			dh.close();
			finish();
		}else if(v.getId() == R.id.btnRenren){
			Intent intent = new Intent();
			intent.setClass(this,com.love.dairy.main.renren. MainActivity.class);
			startActivity(intent);
			finish();
		}else if(v.getId() == R.id.btnPath){
			getPicPath();
		}else if(v.getId() == R.id.btnDate){
			chooseDate(); 
		}else if(v.getId() == R.id.btnMatch){
			Intent intent = new Intent(this, LianlianKanChoosePic.class);
			intent.putExtra("path",imagePosition);
			startActivity(intent);
			finish();
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}else if(v.getId() == R.id.btnBack){
			finish();
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}else if(v.getId() == R.id.btnCut){
			Intent intent = new Intent(this, ImageFilterActivity.class);
			intent.putExtra("path",imagePosition);
			startActivity(intent);
			finish();
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}else if(v.getId() == R.id.btnAuthor){
			Uri uri = Uri.parse("http://weibo.com/lovedairy2014");
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
		}
		else {
			Intent intent = new Intent(this, Game.class);
			intent.setAction("NEW_GAME_ACTION");
			Bundle bundle = new Bundle();
			bundle.putInt("imageId",imagePosition);
			intent.putExtras(bundle);
			startActivity(intent);
    		finish();
    		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}
	}
	/**
	 * 选择日期
	 */
	private void chooseDate() {
		String recordTime = getSharedPreferencesData("recordTime");
		Calendar calendar = Calendar.getInstance();
		if(recordTime != null){
				try {
					calendar.setTime(new SimpleDateFormat("yyyyMMhh", Locale.US).parse(recordTime));
				} catch (ParseException e) {
					e.printStackTrace();
				}
		}
		
		DatePickerDialog.OnDateSetListener dateListener =  
			    new DatePickerDialog.OnDateSetListener() { 
			        @Override 
			        public void onDateSet(DatePicker datePicker,  
			                int year, int month, int dayOfMonth) { 
			        	month++;
			        	String monthStr = month < 10 ? "0"+month : month+"";
			        	saveSharedPreferencesData("recordTime", year+monthStr+dayOfMonth);
			        } 
			    }; 
		new DatePickerDialog(this, 
		         dateListener, 
		         calendar.get(Calendar.YEAR), 
		         calendar.get(Calendar.MONTH), 
		         calendar.get(Calendar.DAY_OF_MONTH)).show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			if(requestCode  == request_code_path){
				Uri originalUri = data.getData();
				if (originalUri != null)
				{
					String path = originalUri.getPath();
					if(originalUri.getScheme().equals("content")){
						String[] proj = { MediaStore.Images.Media.DATA };
						@SuppressWarnings("deprecation")
						Cursor actualimagecursor = managedQuery(originalUri,proj,null,null,null);
						int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						actualimagecursor.moveToFirst();
						path = actualimagecursor.getString(actual_image_column_index);
					}
					saveSharedPreferencesData(IMAGE_PATH, path.substring(0,path.lastIndexOf("/")+1));
					finish();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 获取歌曲文件路径
	 * @return
	 */
	public void getPicPath(){
		final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, request_code_path);
	}
}
