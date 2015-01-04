package com.love.dairy.match;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;
import java.util.WeakHashMap;

import com.love.dairy.LoveApplication;
import com.love.dairy.main.R;
import com.love.dairy.utils.AndroidUtils;
import com.love.dairy.utils.ImageUtil;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LianlianKanMainActivity extends Activity  implements OnTouchListener{

	private static String Setting_Info = "setting_infos";
	protected static final String Level_ID="level_id";
	protected static final String Levels_Time="level_time";
	
	protected static final String Level_Scores="level_scores";
	protected static final String Level_Best_Scores="level_best_scores";
	protected static final String Level_Hints="level_hints";
	
	
	private static final String TAG = "WeAndroids";
	private ImageView[][] dots_grids;
	private int[][] dots_grids_values; 
	//减少即可降低难度
	private Object[] dots_images = {R.drawable.anguilla,R.drawable.canard,R.drawable.crabe,
			R.drawable.croco,R.drawable.escargot,R.drawable.goupil ,
			R.drawable.horse,R.drawable.koala,R.drawable.medusa,
			R.drawable.monkey,R.drawable.morso,R.drawable.oursblanc,
			R.drawable.oursbrun,R.drawable.pico,R.drawable.poulpo,
			R.drawable.rino,R.drawable.saint_jack,R.drawable.tiger,
			R.drawable.toro,R.drawable.ursino};
	private int[] dots_animals={R.drawable.leon_match1,R.drawable.leon_match2,R.drawable.leon_match3,R.drawable.leon_match4,R.drawable.leon_match5,R.drawable.leon_match6,R.drawable.leon_match7,R.drawable.leon_match9,R.drawable.leon_match10,R.drawable.leon_match11,R.drawable.leon_match12,R.drawable.anguilla,R.drawable.canard,R.drawable.crabe,R.drawable.croco,R.drawable.escargot,R.drawable.goupil ,R.drawable.horse,R.drawable.koala,R.drawable.medusa,R.drawable.monkey,R.drawable.morso,R.drawable.oursblanc,R.drawable.oursbrun,R.drawable.pico,R.drawable.poulpo,R.drawable.rino,R.drawable.saint_jack,R.drawable.tiger,R.drawable.toro,R.drawable.ursino,R.drawable.xmas_1,R.drawable.xmas_2,R.drawable.xmas_3,R.drawable.xmas_4,R.drawable.xmas_6 ,R.drawable.xmas_7,R.drawable.xmas_8,R.drawable.xmas_9,R.drawable.xmas_11,R.drawable.xmas_12,R.drawable.xmas_13,R.drawable.xmas_14,R.drawable.xmas_15,R.drawable.xmas_16,R.drawable.xmas_17,R.drawable.xmas_18,R.drawable.xmas_19,R.drawable.xmas_20,R.drawable.xmas_21,R.drawable.xmas_22};
	private int screenHeight;
	private int screenWidth;
	private int each_dot_width;
	private ArrayList<Integer[]> dot_numbers;
	
	private ArrayList<Integer[]> dot_numbers_match;
	
	private MediaPlayer mMediaPlayer;
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	//private int[] levels_time={180,160,150,140,130,120,160,120,100,95,90};
	private int levels_time=240;
	private int[] animals_number={16,17,18,19,20,21,22,23,23,23};
	private int level_id=1;
	private int level_scores=0;
	private int level_best_scores=0;
	private int level_hints=9;
	
	private Handler scorestime_handler;
	private Runnable scorestime_runnable;
	
	private Handler hinttime_handler;
	private Runnable hinttime_runnable;
	
	
	private Handler checkmatchtime_handler;
	private Runnable checkmatchtime_runnable;
	
	private Handler countdowntime_handler;
	private Runnable countdowntime_runnable;

	private Handler dotsclear_handler;
	private Runnable dotsclear_runnable;

	private Handler connection_handler;
	private Runnable connection_runnable;
	
	private Handler Interstitial_handler;
	private Runnable Interstitial_runnable;
	
	private int countdown_seconds;
	private boolean first_time=true;
	private Integer dot1_x;
	private Integer dot1_y;
	private Integer dot2_x;
	private Integer dot2_y;
	
	private TextView leveltime_tv;
	private Button levelinfo_tv;
	private TextView levelscore_tv;
	
	private Button levelpause_btn;
	
	private Button levelmenu_btn;
	private Button leveltip_btn;
	
	private SharedPreferences settings_info;
	private ProgressBar progressBarHorizontal;
	private static String[] menufonts = {"fonts/helvetical_thin.otf","fonts/hobostd.otf","fonts/helvetical_regular.otf","fonts/roboto_thin.ttf","fonts/roboto_light.ttf"};
	
	private String today_str;
	
	private int hint_x1;
	private int hint_y1;
	private int hint_x2;
	private int hint_y2;
	private int hinttime_count=4;
	private int dotsxy3=0;
	private boolean interstitialAd_ready=false; 
	private boolean exit_menu=false; 
	private int dot_height_num = 12;//游戏竖向格数
	private int dot_width_num = dot_height_num + 2;
	
	/**
	 * 乱序代码部分
	 * @param temss
	 * @return
	 */
	private String getRandom(Vector<String> temss)
	{
		if (temss != null && temss.size() > 0)
		{
			double dr = Math.random();
			int tsize = temss.size();
			int num = (int) (dr * tsize);
			String ss = temss.elementAt(num);
			temss.remove(num);
			return ss;
		}
		return null;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		//-------------------
		String[] pics = getIntent().getStringArrayExtra("pics");
		
		dot_height_num = getIntent().getIntExtra("difficult",12);
		dot_width_num = dot_height_num + 2;
		Vector<String> vNums = new Vector<String>();
		for (int i = 0; i < dots_images.length; i++)
		{
			vNums.add("" + i);
		}
		
		for (int i = 0; i < dots_images.length; i++)
		{
			
			if(i<pics.length){
				dots_images[i] = pics[i];
			}else{
				String text = getRandom(vNums);//乱序代码部分
				dots_images[i] = dots_animals[Integer.parseInt(text)];
			}
		}
		
		
		//-------------------
		
		dots_grids=new ImageView[dot_height_num][dot_width_num];
		dots_grids_values=new int[dot_height_num][dot_width_num];
		dot_numbers=new ArrayList<Integer[]>();
		dot_numbers_match=new ArrayList<Integer[]>();
		
		hint_x1=0;
		hint_y1=0;
		hint_x2=0;
		hint_y2=0;
		
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
		Date d =new Date();
		today_str =format.format(d);
		
		settings_info = getSharedPreferences(Setting_Info, 0);
		level_id=settings_info.getInt(Level_ID,1);
		level_scores=settings_info.getInt(Level_Scores,0);
		level_best_scores=settings_info.getInt(Level_Best_Scores+today_str,0);
		levels_time=settings_info.getInt(Levels_Time+level_id,240);
		level_hints=settings_info.getInt(Level_Hints,9);
		
		//Toast.makeText(getApplicationContext(), "levels_time="+levels_time, Toast.LENGTH_SHORT).show();
		
		leveltime_tv= (TextView) findViewById(R.id.leveltime_tv);
		levelscore_tv= (TextView) findViewById(R.id.levelscore_tv);
		
		levelpause_btn= (Button) findViewById(R.id.levelpause_btn);
		levelmenu_btn= (Button) findViewById(R.id.levelmenu_btn);
		leveltip_btn= (Button) findViewById(R.id.leveltip_btn);
		
		
		Typeface fonttype = Typeface.createFromAsset(this.getAssets(),menufonts[1]);
		leveltime_tv.setTypeface(fonttype);
		levelpause_btn.setTypeface(fonttype);
		levelmenu_btn.setTypeface(fonttype);
		leveltip_btn.setTypeface(fonttype);
		levelscore_tv.setTypeface(fonttype);
		
		leveltip_btn.setText("Hints"+level_hints);
		levelscore_tv.setText(""+level_scores);
		AndroidUtils util = new AndroidUtils();
		for(int i = 0;i < dot_height_num ; i++){
			for(int j = 0;j < dot_width_num ; j++){
				int height = i+1;
				String width = "";
				if(height >10){
					width = "0"+(j+1);
				}else{
					width = ""+(j+1);
				}
				dots_grids[i][j] =  (ImageView) findViewById(util.getResourcesID(getApplicationContext(), "dot_iv"+height+""+width, "id"));
			}
		}

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenHeight = dm.heightPixels;
		screenWidth  = dm.widthPixels;
		each_dot_width = (screenHeight-25)/dot_height_num;
		int menu_width = this.getResources().getDimensionPixelSize(R.dimen.match_game_menu_width);
		int each_dot_height = (screenWidth - menu_width)/dot_width_num;
		if(each_dot_height < each_dot_width){
			each_dot_width = each_dot_height;
		}
		mMediaPlayer=new MediaPlayer();
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(1, soundPool.load(this, R.raw.select6, 1));
		soundPoolMap.put(2, soundPool.load(this, R.raw.connect1, 1));
		soundPoolMap.put(3, soundPool.load(this, R.raw.warning6, 1));
		soundPoolMap.put(4, soundPool.load(this, R.raw.fail2, 1));
		soundPoolMap.put(5, soundPool.load(this, R.raw.win2, 1));
		soundPoolMap.put(6, soundPool.load(this, R.raw.select10, 1));
		soundPoolMap.put(7, soundPool.load(this, R.raw.tick1, 1));
		
		progressBarHorizontal = (ProgressBar) findViewById(R.id.progress_horizontal);
		
		levelinfo_tv= (Button) findViewById(R.id.levelinfo_tv);
		
		//countdown_seconds=levels_time[level_id-1];
		countdown_seconds=levels_time;
		leveltime_tv.setText(""+countdown_seconds);
		levelinfo_tv.setText("Level "+level_id);
		levelinfo_tv.setTypeface(fonttype);
		
		progressBarHorizontal.setMax(countdown_seconds);
		progressBarHorizontal.setProgress(countdown_seconds);
		if(each_dot_width>128)
			each_dot_width=128;
		
		
		for (int i = 0; i <dot_height_num; i++) {
			for (int j = 0; j <dot_width_num; j++) {
				dots_grids_values[i][j]=0;
				LayoutParams sq_layout_para = dots_grids[i][j].getLayoutParams();
				sq_layout_para.height = each_dot_width;
				if(i==0||i==dot_height_num-1)
					sq_layout_para.height = each_dot_width;
				
				sq_layout_para.width = each_dot_width+6;
				if(j==0||j==dot_width_num-1)
					sq_layout_para.width = each_dot_width+6;
				
				dots_grids[i][j].setLayoutParams(sq_layout_para);
				dots_grids[i][j].setOnTouchListener(this);
			}
		}
		
		for(int i=0;i<dot_width_num;i++)
		{
			dots_grids[0][i].setBackgroundResource(R.drawable.blank);
			dots_grids[dot_height_num-1][i].setBackgroundResource(R.drawable.blank);
		}
		
		for(int i=0;i<dot_height_num;i++)
		{
			dots_grids[i][0].setBackgroundResource(R.drawable.blank);
			dots_grids[i][dot_width_num-1].setBackgroundResource(R.drawable.blank);
		}
		
		ArrayList<Integer> numbers_array1 = new ArrayList<Integer>();
		ArrayList<Integer> numbers_array2 = new ArrayList<Integer>();
		for(int i = 0;i < (dot_height_num -2) * (dot_width_num - 2) / 2; i++)
		{
			Random temp_random = new Random(i*1000+System.currentTimeMillis());
			int temp_no = temp_random.nextInt(dots_images.length)+1;
			numbers_array1.add(temp_no);
			numbers_array2.add(temp_no);
		}
		
		for(int i = 0;i < (dot_height_num -2) * (dot_width_num - 2) / 2; i++)
		{
			numbers_array1.add(numbers_array2.get(numbers_array2.size()-1));
			numbers_array2.remove(numbers_array2.size()-1);
		}
		
		for(int i=1;i<dot_height_num-1;i++)
			for(int j=1;j<dot_width_num-1;j++)
		 {
			 Random temp_random = new Random(i*j*1000+i+j+System.currentTimeMillis());
			 int temp_no = temp_random.nextInt(numbers_array1.size());
			 if(i==0||i==dot_height_num-1||j==0||j==dot_width_num-1)
			 {
				 dots_grids_values[i][j]=0;
			 }
			 else
			 {
				 dots_grids_values[i][j]=numbers_array1.get(temp_no);
			 	 numbers_array1.remove(temp_no);
			 }
		 }
		

		
		numbers_array1 = new ArrayList<Integer>();
		for(int i=0;i<dot_height_num;i++)
			for(int j=0;j<dot_width_num;j++)
			{
				if(dots_grids_values[i][j]>0)
				{
					numbers_array1.add(dots_grids_values[i][j]);
				}
			}
		
		for(int i=0;i<dot_height_num;i++)
			for(int j=0;j<dot_width_num;j++)
			{
				if(dots_grids_values[i][j]>0)
				{
					 Random temp_random = new Random(i*j*1000+i+j);
					 int temp_no = temp_random.nextInt(numbers_array1.size());
					 dots_grids_values[i][j]=numbers_array1.get(temp_no);
					 numbers_array1.remove(temp_no);
				}
			}
		

		
		for(int i=0;i<dot_height_num;i++)
			for(int j=0;j<dot_width_num;j++)
			{
				if(dots_grids_values[i][j]>0)
				{
					 Object ids = dots_images[dots_grids_values[i][j]-1];
					 if(ids instanceof String){
						 Bitmap bitmap = loadBitmap(ids);
						 dots_grids[i][j].setBackgroundDrawable(new BitmapDrawable(bitmap));
					 }else{
						 dots_grids[i][j].setBackgroundResource((Integer) dots_images[dots_grids_values[i][j]-1]);
					 }
				}
			}
		
		
		
		checkmatchtime_handler = new Handler();
		checkmatchtime_runnable = new Runnable() {
			@Override
			public void run() {
				check_match_available();
				checkmatchtime_handler.postDelayed(this, 2000);
			}
		};
		
		countdowntime_handler = new Handler();
		countdowntime_runnable = new Runnable() {
			@Override
			public void run() {
				countdown_seconds--;
				progressBarHorizontal.setProgress(countdown_seconds);
				leveltime_tv.setText(""+countdown_seconds);
				if(countdown_seconds<=20)
					playEffect(7,0);
				if(countdown_seconds>0)
					countdowntime_handler.postDelayed(countdowntime_runnable, 1000);
				else
				{
					ShowFailLevelMenu();
				}
			}
		};
		
		scorestime_handler = new Handler();
		scorestime_runnable = new Runnable() {
			@Override
			public void run() {
				int score=Integer.valueOf(levelscore_tv.getText().toString());
				if(score<level_scores)
				{
					score++;
					levelscore_tv.setText(String.valueOf(score));
					scorestime_handler.postDelayed(this, 50);
				}
			}
		};
		
		
		hinttime_handler = new Handler();
		hinttime_runnable = new Runnable() {
			@Override
			public void run() {
				
				if(hinttime_count==4 || hinttime_count==2)
				{
					dots_grids[hint_x1][hint_y1].setImageResource(R.drawable.blank);
					dots_grids[hint_x2][hint_y2].setImageResource(R.drawable.blank);
				}
				else
				{
					dots_grids[hint_x1][hint_y1].setImageResource(R.drawable.selecthint);
					dots_grids[hint_x2][hint_y2].setImageResource(R.drawable.selecthint);
				}
				 
				hinttime_count--;
				if(hinttime_count>0)
					hinttime_handler.postDelayed(this, 150);
			}
		};
		
		dotsclear_handler = new Handler();
		dotsclear_runnable = new Runnable() {
			@Override
			public void run() {
				int summary_value=0;
				for(int i=0;i<dot_height_num;i++)
					for(int j=0;j<dot_width_num;j++)
					{
						summary_value=summary_value+dots_grids_values[i][j];
						if(dots_grids_values[i][j]==0)
						{
							dots_grids[i][j].setBackgroundResource(R.drawable.blank);
							dots_grids[i][j].setImageResource(R.drawable.blank);
						}
					}
				if(summary_value==0)
				{
					ShowNextLevelMenu();
				}
			}
		};
		
				
		Interstitial_handler = new Handler();
		Interstitial_runnable = new Runnable() {
			@Override
			public void run() {

			}
		};
		
		connection_handler = new Handler();
		connection_runnable = new Runnable() {
			@Override
			public void run() {
				scorestime_handler.postDelayed(scorestime_runnable, 50);
				Show_Connection();
				playEffect(2,0);
				dotsclear_handler.postDelayed(dotsclear_runnable, 150);
			}
		};
		
		View.OnClickListener vclick =new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				playSounds(R.raw.click);
				switch(arg0.getId())
				{
					case R.id.levelpause_btn:
						ShowResumeLevelMenu();
						break;
					case R.id.levelmenu_btn:
						Intent intentapp1=new Intent(LianlianKanMainActivity.this,RecordsActivity.class);
						startActivity(intentapp1);
						break;
					case R.id.leveltip_btn:
						if(level_hints>0)
						{
							if(hint_x1==0)
							{
								if(dot_numbers.size()==1)
								{
									int presx=dot_numbers.get(0)[0];
									int presy=dot_numbers.get(0)[1];
									dot_numbers.remove(0);
									dots_grids[presx][presy].setImageResource(R.drawable.blank);
									
								}
								show_hints();
							}
						}
						break;
				}
			}
		};
		
		levelmenu_btn.setOnClickListener(vclick);
		levelpause_btn.setOnClickListener(vclick);
		leveltip_btn.setOnClickListener(vclick);
	}
	/**
	 * 读取图片
	 * @param ids
	 * @return
	 */
	private Bitmap loadBitmap(Object ids) {
		 String key = (String) ids;
		 int size = getResources().getDimensionPixelSize(R.dimen.game_btn_size);
		 Bitmap bitmap = null;
		 LoveApplication application = (LoveApplication) this.getApplication();
		 WeakHashMap<String, Bitmap> bitmapCache = application.matchBitmapCache;
		 if(bitmapCache.containsKey(key)){
			 bitmap = bitmapCache.get(key);
		 }else{
			 bitmap = ImageUtil.decodeBitmapFromResourceForWidth(getResources(), (String) ids, size, size);
			 bitmapCache.put(key, bitmap);
		 }
		return bitmap;
	}


	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getAction()==MotionEvent.ACTION_UP)
		{
			Log.d(TAG, "Up");
			for (int i = 1; i < dot_height_num-1; i++)
			for (int j = 1; j < dot_width_num -1; j++)
			if(arg0.equals(dots_grids[i][j])&dots_grids_values[i][j]!=0)
			{
				if(hint_x1>0)
				{
					if((i==hint_x1 & j==hint_y1) ||(i==hint_x2 & j==hint_y2))
					{
						
					}
					else
					{
						playEffect(3, 0);
						Toast.makeText(getApplicationContext(), "Please select the 2 animals with hints.", Toast.LENGTH_SHORT).show();
						return true;
					}
				}
				Log.d(TAG, "dots_grids="+i+","+j);
				playEffect(6, 0);
				Integer[] dot={i,j};
				dot_numbers.add(dot);
				dots_grids[i][j].setImageResource(R.drawable.select);
				if(dot_numbers.size()==2)
				{
					if(hint_x1>0)
					{
					  if(hint_x1==dot_numbers.get(0)[0]&hint_y1==dot_numbers.get(0)[1]&hint_x2==dot_numbers.get(1)[0]&hint_y2==dot_numbers.get(1)[1])
					  {
						 hint_x1=0;
					  }
					  
					  if(hint_x1==dot_numbers.get(1)[0]&hint_y1==dot_numbers.get(1)[1]&hint_x2==dot_numbers.get(0)[0]&hint_y2==dot_numbers.get(0)[1])
					  {
						 hint_x1=0;
					  }
					}
					Clear_Dots();
				}
			}
		}
		
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			Log.d(TAG, "Down");
		}
		return true;
	}

	private void Clear_Dots() {
		dot1_x=dot_numbers.get(0)[0];
		dot1_y=dot_numbers.get(0)[1];
		
		dot2_x=dot_numbers.get(1)[0];
		dot2_y=dot_numbers.get(1)[1];
		if(Check_Dots_xy2(dot1_x,dot1_y,dot2_x,dot2_y))
		{
			if(dots_grids_values[dot1_x][dot1_y]==dots_grids_values[dot2_x][dot2_y])
			{
				Log.d(TAG, "Can match ... ");
				dot_numbers.remove(0);
				dot_numbers.remove(0);
				
				dots_grids_values[dot1_x][dot1_y]=0;
				dots_grids_values[dot2_x][dot2_y]=0;
				countdown_seconds++;
				get_scores(dot1_x,dot1_y,dot2_x,dot2_y);
				leveltime_tv.setText(""+countdown_seconds);
				progressBarHorizontal.setProgress(countdown_seconds);
				connection_handler.postDelayed(connection_runnable, 150);
			}
			else
			{
				//playEffect(3,0);
				if(dot1_x==dot2_x&dot1_y==dot2_y)
				{
					dot_numbers.remove(0);
					dot_numbers.remove(0);
				}
				else
					dot_numbers.remove(0);

				
				Log.d(TAG, "Can not match ... ");
				dots_grids[dot1_x][dot1_y].setImageResource(R.drawable.blank);
				
				//dots_grids[dot2_x][dot2_y].setImageResource(R.drawable.blank);
			}
		}
		else
		{
			//playEffect(3,0);
			if(dot1_x==dot2_x&dot1_y==dot2_y)
			{
				dot_numbers.remove(0);
			}
			else
			{
				if(dots_grids_values[dot1_x][dot1_y]==dots_grids_values[dot2_x][dot2_y])
				{
					    playEffect(3,0);
				}
			}
			
			dot_numbers.remove(0);
			dots_grids[dot1_x][dot1_y].setImageResource(R.drawable.blank);
			
			Log.d(TAG, "Can not move");
			
			//dots_grids[dot2_x][dot2_y].setImageResource(R.drawable.blank);
		}
	}
	
	private boolean Check_Dots_xy2(int x1, int y1, int x2, int y2)
	{

		dot_numbers_match=new ArrayList<Integer[]>();
		//click it self
		if(x1==x2 & y1==y2)
		{
			return false;
		}
		
		if(x1==x2)
		{
			int movevalue=0;
			if(y1<y2)
			{
				for(int i=y1+1;i<y2;i++)
				{
					movevalue=movevalue+dots_grids_values[x1][i];
				}
			}
			else
			{
				for(int i=y2+1;i<y1;i++)
				{
					movevalue=movevalue+dots_grids_values[x1][i];
				}
			}
			if(movevalue==0)
			{
				Integer[] dot_numbers_match1={x1,y1};
				Integer[] dot_numbers_match2={x2,y2};
				dot_numbers_match.add(dot_numbers_match1);
				dot_numbers_match.add(dot_numbers_match2);
				
				Log.d(TAG, "Move to "+x2+","+y2);
				Log.d(TAG, "Can match ... 0");
				return true;
			}
		}
		
		if(y1==y2)
		{
			int movevalue=0;
			if(x1<x2)
			{
				for(int i=x1+1;i<x2;i++)
				{
					movevalue=movevalue+dots_grids_values[i][y1];
				}
			}
			else
			{
				for(int i=x2+1;i<x1;i++)
				{
					movevalue=movevalue+dots_grids_values[i][y1];
				}
			}
			if(movevalue==0)
			{
				Integer[] dot_numbers_match1={x1,y1};
				Integer[] dot_numbers_match2={x2,y2};
				dot_numbers_match.add(dot_numbers_match1);
				dot_numbers_match.add(dot_numbers_match2);
				
				Log.d(TAG, "Move to "+x2+","+y2);
				Log.d(TAG, "Can match ... 1");
				return true;
			}
		}
		
		//x1 y1  -- x1 y2
		int movex1y1x2y2=0;
		if(y2>y1)
		{
			for(int i=y1+1;i<=y2;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[x1][i];
		}
		else
		{
			for(int i=y2;i<=y1-1;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[x1][i];
		}
		
		//x1 y2  -- x2 y2
		if(x2>x1)
		{
			for(int i=x1+1;i<=x2-1;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y2];
		}
		else
		{
			for(int i=x2+1;i<=x1-1;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y2];
		}
		
		if(movex1y1x2y2==0)
		{
			Integer[] dot_numbers_match1={x1,y1};
			Integer[] dot_numbers_match2={x1,y2};
			Integer[] dot_numbers_match3={x2,y2};
			
			dot_numbers_match.add(dot_numbers_match1);
			dot_numbers_match.add(dot_numbers_match2);
			dot_numbers_match.add(dot_numbers_match3);
			
			Log.d(TAG, "Move to "+x2+","+y2);
			Log.d(TAG, "Can match ... 0 0");
			return true;
		}
		
		
			//x1 y1  -- x2 y1
				movex1y1x2y2=0;
				if(x2>x1)
				{
					for(int i=x1+1;i<=x2;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y1];
				}
				else
				{
					for(int i=x2;i<=x1-1;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y1];
				}
				//x2 y1  -- x2 y2
				if(y2>y1)
				{
					for(int i=y1+1;i<=y2-1;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[x2][i];
				}
				else
				{
					for(int i=y2+1;i<=y1-1;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[x2][i];
				}
				
				if(movex1y1x2y2==0)
				{
					Integer[] dot_numbers_match1={x1,y1};
					Integer[] dot_numbers_match2={x2,y1};
					Integer[] dot_numbers_match3={x2,y2};
					
					dot_numbers_match.add(dot_numbers_match1);
					dot_numbers_match.add(dot_numbers_match2);
					dot_numbers_match.add(dot_numbers_match3);
					
					Log.d(TAG, "Move to "+x2+","+y2);
					Log.d(TAG, "Can match ... 1 1");
					return true;
				}
			
		
		// row - x check
		for(int i=1;i<dot_width_num;i++)
		{
			int move1=Check_Dots_x(x1,y1,y1-i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+x1+","+(y1-i)+","+i);
				int move2=Check_Dots_y(y1-i,x1,x2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+x2+","+(y1-i));
					int move3=Check_Dots_x(x2,y1-i,y2);
					if(move3==dots_grids_values[x2][y2])
					{
							Integer[] dot_numbers_match1={x1,y1};
							Integer[] dot_numbers_match2={x1,y1-i};
							Integer[] dot_numbers_match3={x2,y1-i};
							Integer[] dot_numbers_match4={x2,y2};
							dot_numbers_match.add(dot_numbers_match1);
							dot_numbers_match.add(dot_numbers_match2);
							dot_numbers_match.add(dot_numbers_match3);
							dot_numbers_match.add(dot_numbers_match4);
						
							Log.d(TAG, "Move to "+x2+","+y2);
							Log.d(TAG, "Can match ... 4");
							return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & y2==y1-i)
					{
						Integer[] dot_numbers_match1={x1,y1};
						Integer[] dot_numbers_match2={x1,y1-i};
						Integer[] dot_numbers_match3={x2,y2};
						dot_numbers_match.add(dot_numbers_match1);
						dot_numbers_match.add(dot_numbers_match2);
						dot_numbers_match.add(dot_numbers_match3);
						Log.d(TAG, "Move to "+x2+","+y2);
						Log.d(TAG, "Can match ... 3");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1 & y2==y1-i)
				{
					Integer[] dot_numbers_match1={x1,y1};
					Integer[] dot_numbers_match2={x2,y2};
					dot_numbers_match.add(dot_numbers_match1);
					dot_numbers_match.add(dot_numbers_match2);
					
					Log.d(TAG, "Move to "+x2+","+y2);
					Log.d(TAG, "Can match ... 2");
					return true;
				}
			}
			
			
			move1=Check_Dots_x(x1,y1,y1+i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+x1+","+(y1+i)+","+i);
				int move2=Check_Dots_y(y1+i,x1,x2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+x2+","+(y1+i));
					int move3=Check_Dots_x(x2,y1+i,y2);
					if(move3==dots_grids_values[x2][y2])
					{
							Integer[] dot_numbers_match1={x1,y1};
							Integer[] dot_numbers_match2={x1,y1+i};
							Integer[] dot_numbers_match3={x2,y1+i};
							Integer[] dot_numbers_match4={x2,y2};
							dot_numbers_match.add(dot_numbers_match1);
							dot_numbers_match.add(dot_numbers_match2);
							dot_numbers_match.add(dot_numbers_match3);
							dot_numbers_match.add(dot_numbers_match4);
							Log.d(TAG, "Move to "+x2+","+(y2));
							Log.d(TAG, "Can match ... 7");
							return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & y2==y1+i)
					{
						Integer[] dot_numbers_match1={x1,y1};
						Integer[] dot_numbers_match2={x1,y1+i};
						Integer[] dot_numbers_match3={x2,y2};
						dot_numbers_match.add(dot_numbers_match1);
						dot_numbers_match.add(dot_numbers_match2);
						dot_numbers_match.add(dot_numbers_match3);
						Log.d(TAG, "Move to "+x2+","+(y2));
						Log.d(TAG, "Can match ... 6");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1 & y2==y1+i)
				{
					Integer[] dot_numbers_match1={x1,y1};
					Integer[] dot_numbers_match2={x2,y2};
					dot_numbers_match.add(dot_numbers_match1);
					dot_numbers_match.add(dot_numbers_match2);
					Log.d(TAG, "Move to "+x2+","+(y2));
					Log.d(TAG, "Can match ... 5");
					return true;
				}
			}
			
		}
		
//		//column y check
		for(int i=1;i<dot_height_num;i++)
		{
			int move1=Check_Dots_y(y1,x1,x1-i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+(x1-i)+","+(y1)+","+i);
				int move2=Check_Dots_x(x1-i,y1,y2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+(x1-i)+","+(y2));
					int move3=Check_Dots_y(y2,x1-i,x2);
					if(move3==dots_grids_values[x2][y2])
					{
						Integer[] dot_numbers_match1={x1,y1};
						Integer[] dot_numbers_match2={x1-i,y1};
						Integer[] dot_numbers_match3={x1-i,y2};
						Integer[] dot_numbers_match4={x2,y2};
						
						dot_numbers_match.add(dot_numbers_match1);
						dot_numbers_match.add(dot_numbers_match2);
						dot_numbers_match.add(dot_numbers_match3);
						dot_numbers_match.add(dot_numbers_match4);
						
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 10");
						return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & x2==x1-i)
					{
						Integer[] dot_numbers_match1={x1,y1};
						Integer[] dot_numbers_match2={x1-i,y1};
						Integer[] dot_numbers_match3={x2,y2};
						
						dot_numbers_match.add(dot_numbers_match1);
						dot_numbers_match.add(dot_numbers_match2);
						dot_numbers_match.add(dot_numbers_match3);
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 9");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1-i & y2==y1)
				{
					Integer[] dot_numbers_match1={x1,y1};
					Integer[] dot_numbers_match2={x2,y2};
					dot_numbers_match.add(dot_numbers_match1);
					dot_numbers_match.add(dot_numbers_match2);
					Log.d(TAG, "Move to "+(x2)+","+(y2));
					Log.d(TAG, "Can match ... 8");
					return true;
				}
			}
			
			
			move1=Check_Dots_y(y1,x1,x1+i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+(x1+i)+","+(y1)+","+i);
				int move2=Check_Dots_x(x1+i,y1,y2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+(x1+i)+","+(y2));
					int move3=Check_Dots_y(y2,x1+i,x2);
					if(move3==dots_grids_values[x2][y2])
					{
						Integer[] dot_numbers_match1={x1,y1};
						Integer[] dot_numbers_match2={x1+i,y1};
						Integer[] dot_numbers_match3={x1+i,y2};
						Integer[] dot_numbers_match4={x2,y2};
						
						dot_numbers_match.add(dot_numbers_match1);
						dot_numbers_match.add(dot_numbers_match2);
						dot_numbers_match.add(dot_numbers_match3);
						dot_numbers_match.add(dot_numbers_match4);
						
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 13");
						return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & x2==x1+i)
					{
						Integer[] dot_numbers_match1={x1,y1};
						Integer[] dot_numbers_match2={x1+i,y1};
						Integer[] dot_numbers_match3={x2,y2};
						dot_numbers_match.add(dot_numbers_match1);
						dot_numbers_match.add(dot_numbers_match2);
						dot_numbers_match.add(dot_numbers_match3);
						
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 12");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1+i & y2==y1)
				{
					Integer[] dot_numbers_match1={x1,y1};
					Integer[] dot_numbers_match2={x2,y2};
					dot_numbers_match.add(dot_numbers_match1);
					dot_numbers_match.add(dot_numbers_match2);
					
					Log.d(TAG, "Move to "+(x2)+","+(y2));
					Log.d(TAG, "Can match ... 11");
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	private boolean Check_Dots_xy3(int x1, int y1, int x2, int y2)
	{

		ArrayList<Integer[]> dot_numbers_match_check=new ArrayList<Integer[]>();
		//click it self
		if(x1==x2 & y1==y2)
		{
			return false;
		}
		
		if(x1==x2)
		{
			int movevalue=0;
			if(y1<y2)
			{
				for(int i=y1+1;i<y2;i++)
				{
					movevalue=movevalue+dots_grids_values[x1][i];
				}
			}
			else
			{
				for(int i=y2+1;i<y1;i++)
				{
					movevalue=movevalue+dots_grids_values[x1][i];
				}
			}
			if(movevalue==0)
			{
				Integer[] dot_numbers_match_check1={x1,y1};
				Integer[] dot_numbers_match_check2={x2,y2};
				dot_numbers_match_check.add(dot_numbers_match_check1);
				dot_numbers_match_check.add(dot_numbers_match_check2);
				
				Log.d(TAG, "Move to "+x2+","+y2);
				Log.d(TAG, "Can match ... 0");
				return true;
			}
		}
		
		if(y1==y2)
		{
			int movevalue=0;
			if(x1<x2)
			{
				for(int i=x1+1;i<x2;i++)
				{
					movevalue=movevalue+dots_grids_values[i][y1];
				}
			}
			else
			{
				for(int i=x2+1;i<x1;i++)
				{
					movevalue=movevalue+dots_grids_values[i][y1];
				}
			}
			if(movevalue==0)
			{
				Integer[] dot_numbers_match_check1={x1,y1};
				Integer[] dot_numbers_match_check2={x2,y2};
				dot_numbers_match_check.add(dot_numbers_match_check1);
				dot_numbers_match_check.add(dot_numbers_match_check2);
				
				Log.d(TAG, "Move to "+x2+","+y2);
				Log.d(TAG, "Can match ... 1");
				return true;
			}
		}
		
		//x1 y1  -- x1 y2
		int movex1y1x2y2=0;
		if(y2>y1)
		{
			for(int i=y1+1;i<=y2;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[x1][i];
		}
		else
		{
			for(int i=y2;i<=y1-1;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[x1][i];
		}
		
		//x1 y2  -- x2 y2
		if(x2>x1)
		{
			for(int i=x1+1;i<=x2-1;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y2];
		}
		else
		{
			for(int i=x2+1;i<=x1-1;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y2];
		}
		
		if(movex1y1x2y2==0)
		{
			Integer[] dot_numbers_match_check1={x1,y1};
			Integer[] dot_numbers_match_check2={x1,y2};
			Integer[] dot_numbers_match_check3={x2,y2};
			
			dot_numbers_match_check.add(dot_numbers_match_check1);
			dot_numbers_match_check.add(dot_numbers_match_check2);
			dot_numbers_match_check.add(dot_numbers_match_check3);
			
			Log.d(TAG, "Move to "+x2+","+y2);
			Log.d(TAG, "Can match ... 0 0");
			return true;
		}
		
		
			//x1 y1  -- x2 y1
				movex1y1x2y2=0;
				if(x2>x1)
				{
					for(int i=x1+1;i<=x2;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y1];
				}
				else
				{
					for(int i=x2;i<=x1-1;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y1];
				}
				//x2 y1  -- x2 y2
				if(y2>y1)
				{
					for(int i=y1+1;i<=y2-1;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[x2][i];
				}
				else
				{
					for(int i=y2+1;i<=y1-1;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[x2][i];
				}
				
				if(movex1y1x2y2==0)
				{
					Integer[] dot_numbers_match_check1={x1,y1};
					Integer[] dot_numbers_match_check2={x2,y1};
					Integer[] dot_numbers_match_check3={x2,y2};
					
					dot_numbers_match_check.add(dot_numbers_match_check1);
					dot_numbers_match_check.add(dot_numbers_match_check2);
					dot_numbers_match_check.add(dot_numbers_match_check3);
					
					Log.d(TAG, "Move to "+x2+","+y2);
					Log.d(TAG, "Can match ... 1 1");
					return true;
				}
			
		
		// row - x check
		for(int i=1;i<dot_width_num;i++)
		{
			int move1=Check_Dots_x(x1,y1,y1-i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+x1+","+(y1-i)+","+i);
				int move2=Check_Dots_y(y1-i,x1,x2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+x2+","+(y1-i));
					int move3=Check_Dots_x(x2,y1-i,y2);
					if(move3==dots_grids_values[x2][y2])
					{
							Integer[] dot_numbers_match_check1={x1,y1};
							Integer[] dot_numbers_match_check2={x1,y1-i};
							Integer[] dot_numbers_match_check3={x2,y1-i};
							Integer[] dot_numbers_match_check4={x2,y2};
							dot_numbers_match_check.add(dot_numbers_match_check1);
							dot_numbers_match_check.add(dot_numbers_match_check2);
							dot_numbers_match_check.add(dot_numbers_match_check3);
							dot_numbers_match_check.add(dot_numbers_match_check4);
						
							Log.d(TAG, "Move to "+x2+","+y2);
							Log.d(TAG, "Can match ... 4");
							return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & y2==y1-i)
					{
						Integer[] dot_numbers_match_check1={x1,y1};
						Integer[] dot_numbers_match_check2={x1,y1-i};
						Integer[] dot_numbers_match_check3={x2,y2};
						dot_numbers_match_check.add(dot_numbers_match_check1);
						dot_numbers_match_check.add(dot_numbers_match_check2);
						dot_numbers_match_check.add(dot_numbers_match_check3);
						Log.d(TAG, "Move to "+x2+","+y2);
						Log.d(TAG, "Can match ... 3");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1 & y2==y1-i)
				{
					Integer[] dot_numbers_match_check1={x1,y1};
					Integer[] dot_numbers_match_check2={x2,y2};
					dot_numbers_match_check.add(dot_numbers_match_check1);
					dot_numbers_match_check.add(dot_numbers_match_check2);
					
					Log.d(TAG, "Move to "+x2+","+y2);
					Log.d(TAG, "Can match ... 2");
					return true;
				}
			}
			
			
			move1=Check_Dots_x(x1,y1,y1+i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+x1+","+(y1+i)+","+i);
				int move2=Check_Dots_y(y1+i,x1,x2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+x2+","+(y1+i));
					int move3=Check_Dots_x(x2,y1+i,y2);
					if(move3==dots_grids_values[x2][y2])
					{
							Integer[] dot_numbers_match_check1={x1,y1};
							Integer[] dot_numbers_match_check2={x1,y1+i};
							Integer[] dot_numbers_match_check3={x2,y1+i};
							Integer[] dot_numbers_match_check4={x2,y2};
							dot_numbers_match_check.add(dot_numbers_match_check1);
							dot_numbers_match_check.add(dot_numbers_match_check2);
							dot_numbers_match_check.add(dot_numbers_match_check3);
							dot_numbers_match_check.add(dot_numbers_match_check4);
							Log.d(TAG, "Move to "+x2+","+(y2));
							Log.d(TAG, "Can match ... 7");
							return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & y2==y1+i)
					{
						Integer[] dot_numbers_match_check1={x1,y1};
						Integer[] dot_numbers_match_check2={x1,y1+i};
						Integer[] dot_numbers_match_check3={x2,y2};
						dot_numbers_match_check.add(dot_numbers_match_check1);
						dot_numbers_match_check.add(dot_numbers_match_check2);
						dot_numbers_match_check.add(dot_numbers_match_check3);
						Log.d(TAG, "Move to "+x2+","+(y2));
						Log.d(TAG, "Can match ... 6");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1 & y2==y1+i)
				{
					Integer[] dot_numbers_match_check1={x1,y1};
					Integer[] dot_numbers_match_check2={x2,y2};
					dot_numbers_match_check.add(dot_numbers_match_check1);
					dot_numbers_match_check.add(dot_numbers_match_check2);
					Log.d(TAG, "Move to "+x2+","+(y2));
					Log.d(TAG, "Can match ... 5");
					return true;
				}
			}
			
		}
		
//		//column y check
		for(int i=1;i<dot_height_num;i++)
		{
			int move1=Check_Dots_y(y1,x1,x1-i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+(x1-i)+","+(y1)+","+i);
				int move2=Check_Dots_x(x1-i,y1,y2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+(x1-i)+","+(y2));
					int move3=Check_Dots_y(y2,x1-i,x2);
					if(move3==dots_grids_values[x2][y2])
					{
						Integer[] dot_numbers_match_check1={x1,y1};
						Integer[] dot_numbers_match_check2={x1-i,y1};
						Integer[] dot_numbers_match_check3={x1-i,y2};
						Integer[] dot_numbers_match_check4={x2,y2};
						
						dot_numbers_match_check.add(dot_numbers_match_check1);
						dot_numbers_match_check.add(dot_numbers_match_check2);
						dot_numbers_match_check.add(dot_numbers_match_check3);
						dot_numbers_match_check.add(dot_numbers_match_check4);
						
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 10");
						return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & x2==x1-i)
					{
						Integer[] dot_numbers_match_check1={x1,y1};
						Integer[] dot_numbers_match_check2={x1-i,y1};
						Integer[] dot_numbers_match_check3={x2,y2};
						
						dot_numbers_match_check.add(dot_numbers_match_check1);
						dot_numbers_match_check.add(dot_numbers_match_check2);
						dot_numbers_match_check.add(dot_numbers_match_check3);
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 9");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1-i & y2==y1)
				{
					Integer[] dot_numbers_match_check1={x1,y1};
					Integer[] dot_numbers_match_check2={x2,y2};
					dot_numbers_match_check.add(dot_numbers_match_check1);
					dot_numbers_match_check.add(dot_numbers_match_check2);
					Log.d(TAG, "Move to "+(x2)+","+(y2));
					Log.d(TAG, "Can match ... 8");
					return true;
				}
			}
			
			
			move1=Check_Dots_y(y1,x1,x1+i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+(x1+i)+","+(y1)+","+i);
				int move2=Check_Dots_x(x1+i,y1,y2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+(x1+i)+","+(y2));
					int move3=Check_Dots_y(y2,x1+i,x2);
					if(move3==dots_grids_values[x2][y2])
					{
						Integer[] dot_numbers_match_check1={x1,y1};
						Integer[] dot_numbers_match_check2={x1+i,y1};
						Integer[] dot_numbers_match_check3={x1+i,y2};
						Integer[] dot_numbers_match_check4={x2,y2};
						
						dot_numbers_match_check.add(dot_numbers_match_check1);
						dot_numbers_match_check.add(dot_numbers_match_check2);
						dot_numbers_match_check.add(dot_numbers_match_check3);
						dot_numbers_match_check.add(dot_numbers_match_check4);
						
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 13");
						return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & x2==x1+i)
					{
						Integer[] dot_numbers_match_check1={x1,y1};
						Integer[] dot_numbers_match_check2={x1+i,y1};
						Integer[] dot_numbers_match_check3={x2,y2};
						dot_numbers_match_check.add(dot_numbers_match_check1);
						dot_numbers_match_check.add(dot_numbers_match_check2);
						dot_numbers_match_check.add(dot_numbers_match_check3);
						
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 12");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1+i & y2==y1)
				{
					Integer[] dot_numbers_match_check1={x1,y1};
					Integer[] dot_numbers_match_check2={x2,y2};
					dot_numbers_match_check.add(dot_numbers_match_check1);
					dot_numbers_match_check.add(dot_numbers_match_check2);
					
					Log.d(TAG, "Move to "+(x2)+","+(y2));
					Log.d(TAG, "Can match ... 11");
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void ConnectionXY(int x1, int y1, int x2, int y2)
	{
		for(int i=x1;i<=x2;i++)
		{
			dots_grids[i][y1].setImageResource(R.drawable.connect);
		}
		for(int i=x2;i<=x1;i++)
		{
			dots_grids[i][y1].setImageResource(R.drawable.connect);
		}
		
		for(int i=y1;i<=y2;i++)
		{
			dots_grids[x1][i].setImageResource(R.drawable.connect);
		}
		for(int i=y2;i<=y1;i++)
		{
			dots_grids[x1][i].setImageResource(R.drawable.connect);
		}
	}
	
	private void Show_Connection()
	{
		// dot_numbers_match.size() == 2   connect straightly 
		// dot_numbers_match.size() == 3   connect with 1 angle
		// dot_numbers_match.size() == 4   connect with 2 angles
		
		if(dot_numbers_match.size()==2)
		{
		  int x1=dot_numbers_match.get(0)[0];
		  int y1=dot_numbers_match.get(0)[1];
		
		  int x2=dot_numbers_match.get(1)[0];
		  int y2=dot_numbers_match.get(1)[1];
		  
		  ConnectionXY(x1,y1,x2,y2);
		}
		
		if(dot_numbers_match.size()==3)
		{
		  int x1=dot_numbers_match.get(0)[0];
		  int y1=dot_numbers_match.get(0)[1];
		
		  int x2=dot_numbers_match.get(1)[0];
		  int y2=dot_numbers_match.get(1)[1];
		  
		  int x3=dot_numbers_match.get(2)[0];
		  int y3=dot_numbers_match.get(2)[1];
		  
		  ConnectionXY(x1,y1,x2,y2);
		  ConnectionXY(x2,y2,x3,y3);
		}
		
		if(dot_numbers_match.size()==4)
		{
		  int x1=dot_numbers_match.get(0)[0];
		  int y1=dot_numbers_match.get(0)[1];
		
		  int x2=dot_numbers_match.get(1)[0];
		  int y2=dot_numbers_match.get(1)[1];
		  
		  int x3=dot_numbers_match.get(2)[0];
		  int y3=dot_numbers_match.get(2)[1];
		  
		  int x4=dot_numbers_match.get(3)[0];
		  int y4=dot_numbers_match.get(3)[1];
		  
		  ConnectionXY(x1,y1,x2,y2);
		  ConnectionXY(x2,y2,x3,y3);
		  ConnectionXY(x3,y3,x4,y4);
		}
	}
	
	
	private int Check_Dots_x(int x, int y1,int y2)
	{
		int value=0;
		int yy1=y1+1;
		int yy2=y2;
		
		if(y2<0||y2>dot_width_num-1)
			return -1;
		
		if( y1>y2)
		{
			yy1=y2;
			yy2=y1-1;
		}
		
		for(int i=yy1;i<=yy2;i++)
		{
			value=value+dots_grids_values[x][i];
		}
		
		return value;
	}
	
	private int Check_Dots_y(int y, int x1,int x2)
	{
		int value=0;
		int xx1=x1+1;
		int xx2=x2;
		
		if(x2<0||x2>dot_height_num-1)
			return -1;
		
		if( x1>x2)
		{
			xx1=x2;
			xx2=x1-1;
		}
		
		for(int i=xx1;i<=xx2;i++)
		{
			value=value+dots_grids_values[i][y];
		}

		return value;
	}
	
	private boolean Check_Dots_xy(int x1, int y1, int x2, int y2)
	{
		int x=0, y=0;
		if(x1>1)
		{
			x=x1-1;
			y=y1;
			if(x==x2 & y==y2)
			{
				return true;
			}
			else
			{
				if(dots_grids_values[x][y]==0)
				{
					Check_Dots_xy(x,y,x2,y2);
				}
			}
		}
		
		if(x1<dot_height_num)
		{
			x=x1+1;
			y=y1;
			if(x==x2 & y==y2)
			{
				return true;
			}
			else
			{
				if(dots_grids_values[x][y]==0)
				{
					Check_Dots_xy(x,y,x2,y2);
				}
			}
		}
		
		if(y1>1)
		{
			x=x1;
			y=y1-1;
			if(x==x2 & y==y2)
			{
				return true;
			}
			else
			{
				if(dots_grids_values[x][y]==0)
				{
					Check_Dots_xy(x,y,x2,y2);
				}
			}
		}
		
		if(y1<14)
		{
			x=x1;
			y=y1+1;
			if(x==x2 & y==y2)
			{
				return true;
			}
			else
			{
				if(dots_grids_values[x][y]==0)
				{
					Check_Dots_xy(x,y,x2,y2);
				}
			}
		}
		
		return false;
	}

	private void playSounds(int sid) {

		if (mMediaPlayer != null) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.release();
			mMediaPlayer = null;
		}

		mMediaPlayer = MediaPlayer.create(LianlianKanMainActivity.this, sid);

		/* 准备播放 */
		// mMediaPlayer.prepare();
		/* 开始播放 */
		mMediaPlayer.start();
		
		mMediaPlayer.setOnCompletionListener(new OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
		});
	}
	
	 private void playEffect(int sound, int loop) {
			AudioManager mgr = (AudioManager) this
					.getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = mgr
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamVolumeMax = mgr
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = streamVolumeCurrent / streamVolumeMax;
			soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
		}
	 
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	    	AudioManager audioManager=null;
	    	audioManager=(AudioManager)getSystemService(Service.AUDIO_SERVICE);

	    if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
	    	//Toast.makeText(main.this, "Down", Toast.LENGTH_SHORT).show();
	    	audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
	                AudioManager.ADJUST_LOWER, 
	                AudioManager.FLAG_SHOW_UI);
	        	return true;
	    }else if(keyCode==KeyEvent.KEYCODE_VOLUME_UP)
	    {
	    	audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
	                AudioManager.ADJUST_RAISE, 
	                AudioManager.FLAG_SHOW_UI);    
	    		return true;
	    }else if(keyCode==KeyEvent.KEYCODE_BACK)
	    {
	    	playSounds(R.raw.click);
	    	ShowMenuLevelMenu();
	    	return true; 
	    }else{
	    return super.onKeyDown(keyCode, event);    
	    }
	    }
	 
	 @Override
	    protected void onResume() { 
	     	super.onResume();
			if(first_time)
	     	{
	     		countdowntime_handler.postDelayed(countdowntime_runnable, 1000);
	     		checkmatchtime_handler.postDelayed(checkmatchtime_runnable, 2000);
	     		Interstitial_handler.postDelayed(Interstitial_runnable, 1500);
	     		first_time=false;
	     		playSounds(R.raw.start);
	     	}
	 }
	 
		@Override
	    public void onDestroy() 
		{
	        super.onDestroy();
	        countdowntime_handler.removeCallbacks(countdowntime_runnable);
	        checkmatchtime_handler.removeCallbacks(checkmatchtime_runnable);
	        Log.d("WeAndroids", "Destroy====================================");
	    }
		
//		@Override
//		public void onConfigurationChanged(Configuration newConfig)
//		{
//	        // TODO Auto-generated method stub
//			
//	        if (this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE) 
//	        {
//	           // Nothing need to be done here
//	            
//	        } else {
//	           // Nothing need to be done here
//	        }
//	        super.onConfigurationChanged(newConfig);
//	    }
		
		 private void ShowResumeLevelMenu() {
			 countdowntime_handler.removeCallbacks(countdowntime_runnable);
			 final AlertDialog dlg = new AlertDialog.Builder(this).setCancelable(false).create();
				dlg.show();
				Window window = dlg.getWindow();
				window.setContentView(R.layout.resume_level_menu);
				Button btnresume = (Button) window.findViewById(R.id.btnresume);
				Typeface fonttype = Typeface.createFromAsset(this.getAssets(),menufonts[1]);
				btnresume.setTypeface(fonttype);
				
				LinearLayout resumelayout= (LinearLayout) window.findViewById(R.id.resumelayout);
				LayoutParams sq_layout_para = resumelayout.getLayoutParams();
				sq_layout_para.height = screenHeight-60;
				sq_layout_para.width = screenWidth-60;
				resumelayout.setLayoutParams(sq_layout_para);
//				
//				Animation animation1 = AnimationUtils.loadAnimation(
//						MainActivity.this, R.anim.apppop1);
//				btnapp1.setAnimation(animation1);
//				btnapp2.setAnimation(animation1);
//				btnapp3.setAnimation(animation1);
//				btnapp4.setAnimation(animation1);
//				btnapp5.setAnimation(animation1);

				
				
				
				
				
				
				btnresume.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						playSounds(R.raw.click);
						dlg.cancel();
						countdowntime_handler.postDelayed(countdowntime_runnable, 500);
					}
				});
		 }
		 
		 private void ShowMenuLevelMenu() {
			 countdowntime_handler.removeCallbacks(countdowntime_runnable);
			 final AlertDialog dlg = new AlertDialog.Builder(this).setCancelable(false).create();
				dlg.show();
				Window window = dlg.getWindow();
				window.setContentView(R.layout.menu_level_menu);

				Button btnyes = (Button) window.findViewById(R.id.btnyes);
				Button btnno = (Button) window.findViewById(R.id.btnno);
				TextView levelinfotv= (TextView) window.findViewById(R.id.levelinfotv);
				Typeface fonttype = Typeface.createFromAsset(this.getAssets(),menufonts[1]);
				btnyes.setTypeface(fonttype);
				btnno.setTypeface(fonttype);
				levelinfotv.setTypeface(fonttype);
				
				LinearLayout resumelayout= (LinearLayout) window.findViewById(R.id.resumelayout);
				LayoutParams sq_layout_para = resumelayout.getLayoutParams();
				sq_layout_para.height = screenHeight-60;
				sq_layout_para.width = screenWidth-60;
				resumelayout.setLayoutParams(sq_layout_para);
				
				btnno.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						
						playSounds(R.raw.click);
						dlg.cancel();
						countdowntime_handler.postDelayed(countdowntime_runnable, 500);
					}
				});
				
				btnyes.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						playSounds(R.raw.click);
						dlg.cancel();
						
						if(level_best_scores<level_scores)
							level_best_scores=level_scores;
						
						settings_info.edit().putInt(Level_Best_Scores+today_str, level_best_scores).commit();
						
						settings_info.edit().putInt(Level_ID, 1).commit();
						settings_info.edit().putInt(Level_Scores, 0).commit();
						settings_info.edit().putInt(Level_Hints, 9).commit();
						LoveApplication application = (LoveApplication) getApplication();
						application.releaseMatchBitmapCache();
						finish();
					}
				});
		 }
		 
		 private void ShowNextLevelMenu() {
			    countdowntime_handler.removeCallbacks(countdowntime_runnable);
			    final AlertDialog dlg = new AlertDialog.Builder(this).setCancelable(false).create();
				dlg.show();
				Window window = dlg.getWindow();
				window.setContentView(R.layout.next_level_menu);
				Button btnnext = (Button) window.findViewById(R.id.btnnext);
				TextView levelcleartv= (TextView) window.findViewById(R.id.levelcleartv);
				TextView levelscoretv= (TextView) window.findViewById(R.id.levelscoretv);
				Typeface fonttype = Typeface.createFromAsset(this.getAssets(),menufonts[1]);
				btnnext.setTypeface(fonttype);
				levelcleartv.setTypeface(fonttype);
				levelscoretv.setTypeface(fonttype);
				levelcleartv.setText("Level "+level_id+" cleared!");
				//level_scores=level_scores+levels_time[level_id-1]-countdown_seconds;
				level_scores=level_scores+countdown_seconds;
				levelscoretv.setText(""+level_scores);
				scorestime_handler.postDelayed(scorestime_runnable, 50);
				LinearLayout resumelayout= (LinearLayout) window.findViewById(R.id.resumelayout);
				LayoutParams sq_layout_para = resumelayout.getLayoutParams();
				sq_layout_para.height = screenHeight-60;
				sq_layout_para.width = screenWidth-60;
				resumelayout.setLayoutParams(sq_layout_para);
				playEffect(5,0);
				
				if(level_best_scores<level_scores)
					level_best_scores=level_scores;
				
				int difftime=countdown_seconds/3;
				if(difftime>=20)
					difftime=20;
				else if(difftime>=15)
					difftime=15;
				else if(difftime>=10)
					difftime=10;
				else if(difftime>=5)
					difftime=5;
				else
					difftime=2;
				
				if(level_id<=2)
					difftime=15;
				
				level_hints=level_hints+1;
				settings_info.edit().putInt(Level_Best_Scores+today_str, level_best_scores).commit();
				settings_info.edit().putInt(Levels_Time+(level_id+1), levels_time-difftime).commit();
				settings_info.edit().putInt(Level_Hints, level_hints).commit();
				
				btnnext.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						playSounds(R.raw.click);
						level_id=level_id+1;
						settings_info.edit().putInt(Level_ID, level_id).commit();
						settings_info.edit().putInt(Level_Scores, level_scores).commit();
						dlg.cancel();
						Intent intentapp=new Intent(LianlianKanMainActivity.this,LianlianKanMainActivity.class);
						String[] pics = getIntent().getStringArrayExtra("pics");
						intentapp.putExtra("pics", pics);
						int difficult = dot_height_num + 2;
						intentapp.putExtra("difficult", difficult > 12 ? 12:difficult);
						startActivity(intentapp);
						finish();
						
					}
				});
		 }
		 
		 private void ShowFailLevelMenu() {
			    final AlertDialog dlg = new AlertDialog.Builder(this).setCancelable(false).create();
				dlg.show();
				Window window = dlg.getWindow();
				window.setContentView(R.layout.fail_level_menu);
				
				Button btnok = (Button) window.findViewById(R.id.btnok);
				TextView levelinfotv= (TextView) window.findViewById(R.id.levelinfotv);
				TextView levelfailtv= (TextView) window.findViewById(R.id.levelfailtv);
				
				TextView levelscoretv1= (TextView) window.findViewById(R.id.levelscoretv1);
				TextView levelbestscoretv1= (TextView) window.findViewById(R.id.levelbestscoretv1);
				TextView levelscoretv2= (TextView) window.findViewById(R.id.levelscoretv2);
				TextView levelbestscoretv2= (TextView) window.findViewById(R.id.levelbestscoretv2);
				
				Typeface fonttype = Typeface.createFromAsset(this.getAssets(),menufonts[1]);
				btnok.setTypeface(fonttype);
				levelinfotv.setTypeface(fonttype);
				levelfailtv.setTypeface(fonttype);
				levelscoretv1.setTypeface(fonttype);
				levelbestscoretv1.setTypeface(fonttype);
				levelscoretv2.setTypeface(fonttype);
				levelbestscoretv2.setTypeface(fonttype);
				
				levelinfotv.setText("You are stopped by Level "+level_id+"!");
				LinearLayout resumelayout= (LinearLayout) window.findViewById(R.id.resumelayout);
				LayoutParams sq_layout_para = resumelayout.getLayoutParams();
				sq_layout_para.height = screenHeight-60;
				sq_layout_para.width = screenWidth-60;
				resumelayout.setLayoutParams(sq_layout_para);
				playEffect(4,0);
				if(level_best_scores<level_scores)
					level_best_scores=level_scores;
				
				levelscoretv2.setText(""+level_scores);
				levelbestscoretv2.setText(""+level_best_scores);
				
				settings_info.edit().putInt(Level_Best_Scores+today_str, level_best_scores).commit();
				settings_info.edit().putInt(Level_Hints, 9).commit();
				
				btnok.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						playSounds(R.raw.click);
						level_id=1;
						level_scores=0;
						settings_info.edit().putInt(Level_ID, level_id).commit();
						settings_info.edit().putInt(Level_Scores, level_scores).commit();
						dlg.cancel();
						Intent intentapp=new Intent(LianlianKanMainActivity.this,LianlianKanMainActivity.class);
						String[] pics = getIntent().getStringArrayExtra("pics");
						intentapp.putExtra("pics", pics);
						intentapp.putExtra("difficult", dot_height_num);
						startActivity(intentapp);
						finish();
						
					}
				});
		 }
		 
		 private void get_scores(int x1,int y1, int x2, int y2)
		 {
			 level_scores=level_scores+10;
//			 int xspace=Math.abs(x1-x2);
//			 int yspace=Math.abs(y1-y2);
//			 
//			 if(xspace+yspace>1)
//			 {
//				 level_scores=level_scores+xspace+yspace-1;
//			 }
		 }
		 
		 
		 
		 
		 private void show_hints()
		 {
			hint_x1=0;
			hint_y1=0;
			hint_x2=0;
			hint_y2=0;
				
			 for(int i=1;i<dot_height_num-1;i++)
				 for(int j=1;j<dot_width_num-1;j++)
				 {
					 if(dots_grids_values[i][j]>0)
					 {
						 
						 for(int ii=1;ii<dot_height_num-1;ii++)
							 for(int jj=1;jj<dot_width_num-1;jj++)
							 {
								 if(dots_grids_values[i][j]==dots_grids_values[ii][jj])
								 {
									 if(Check_Dots_xy2(i,j,ii,jj))
									 {
										 hint_x1=i;
										 hint_y1=j;
										 hint_x2=ii;
										 hint_y2=jj;
											
										 dots_grids[i][j].setImageResource(R.drawable.selecthint);
										 dots_grids[ii][jj].setImageResource(R.drawable.selecthint);
										 level_hints --;
										 leveltip_btn.setText("Hints"+level_hints);
										 settings_info.edit().putInt(Level_Hints, level_hints).commit();
										 
										 if(countdown_seconds>2)
										 {
											 countdown_seconds=countdown_seconds-2;
											 progressBarHorizontal.setProgress(countdown_seconds);
										 }
										 hinttime_count = 4;
										 hinttime_handler.postDelayed(hinttime_runnable, 150);
										 return;
									 }
								 }
							 }
					 }
				 }
		 }
		 
		 private void check_match_available()
		 {
				 for(int i=1;i<dot_height_num-1;i++)
					 for(int j=1;j<dot_width_num-1;j++)
					 {
						 if(dots_grids_values[i][j]>0)
						 {
							 
							 for(int ii=1;ii<dot_height_num-1;ii++)
								 for(int jj=1;jj<dot_width_num-1;jj++)
								 {
									 if(dots_grids_values[i][j]==dots_grids_values[ii][jj])
									 {
										 if(Check_Dots_xy3(i,j,ii,jj))
										 {
											 return;
										 }
									 }
								 }
						 }
					 }
				 
				 
				 ArrayList<Integer> numbers_array1 = new ArrayList<Integer>();
				 
				 for(int i=1;i<dot_height_num-1;i++)
					 for(int j=1;j<dot_width_num-1;j++)
					 {
						 if(dots_grids_values[i][j]>0)
						 {
							 numbers_array1.add(dots_grids_values[i][j]);
						 }
					 }
				 
				 
				 for(int i=1;i<dot_height_num-1;i++)
					 for(int j=1;j<dot_width_num-1;j++)
					 {
						 if(dots_grids_values[i][j]>0)
						 {
							 Random temp_random = new Random(i*j*1000+i+j);
							 int temp_no = temp_random.nextInt(numbers_array1.size());
							 dots_grids_values[i][j]=numbers_array1.get(temp_no);
							 Object ids = dots_images[dots_grids_values[i][j]-1];
							 if(ids instanceof String){
								 Bitmap bitmap = loadBitmap(ids);
								 dots_grids[i][j].setBackgroundDrawable(new BitmapDrawable(bitmap));
							 }else{
								 dots_grids[i][j].setBackgroundResource((Integer) dots_images[dots_grids_values[i][j]-1]);
							 }
							 numbers_array1.remove(temp_no);
						 }
					 }
		 }
		 
	
}
