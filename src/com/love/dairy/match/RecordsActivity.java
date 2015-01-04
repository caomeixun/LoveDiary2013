package com.love.dairy.match;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Service;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.love.dairy.main.R;

public class RecordsActivity  extends Activity {
	private static String Setting_Info = "setting_infos";
	protected static final String Level_Best_Scores="level_best_scores";
	
	private MyAdapter recordslist_adapter;
	private ArrayList<String> RecordsScore;
	private ArrayList<String> RecordsDate;
	private ListView recordslist;
	private TextView titletv;
	private SharedPreferences settings_info;
	private static String[] menufonts={"fonts/helvetical_thin.otf","fonts/hobostd.otf","fonts/helvetical_regular.otf","fonts/roboto_thin.ttf","fonts/roboto_light.ttf"};
	private int bestscore=0;
	private MediaPlayer mMediaPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_records);
		mMediaPlayer=new MediaPlayer();
		recordslist=(ListView)findViewById(R.id.recordslist);
		settings_info = getSharedPreferences(Setting_Info, 0);
		RecordsScore=new  ArrayList<String>();
		RecordsDate=new  ArrayList<String>();
		titletv=(TextView)findViewById(R.id.titletv);
		Typeface fonttype = Typeface.createFromAsset(this.getAssets(),menufonts[1]);
		titletv.setTypeface(fonttype);
		
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
		for(int i=0;i<90;i++)
		{
			//Date d =new Date(System.currentTimeMillis()-24*3600*1000*i);
			Date d=beforeMonthDate(0,i,0);
			String today_str = format.format(d);
			//Log.d("WeAndroids", today_str);
			int today_record =settings_info.getInt(Level_Best_Scores+today_str, 0);
			if(today_record>0)
			{
				if(bestscore<today_record)
					bestscore=today_record;
				
				RecordsScore.add(""+today_record);
				RecordsDate.add(today_str);
				//Toast.makeText(getApplicationContext(), ""+today_str+" "+i, Toast.LENGTH_SHORT).show();
			}
		}
		
		recordslist_adapter = new MyAdapter();
		recordslist.setAdapter(recordslist_adapter);
	}
	
	private class MyAdapter extends BaseAdapter{
		public int getCount() {
            return RecordsScore.size();
        }

        public Object getItem(int pos) {
            return pos;
        }

        public long getItemId(int pos) {
            return pos;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) 
        {
        	LayoutInflater inflater = LayoutInflater.from(RecordsActivity.this);
            View view = inflater.inflate(R.layout.records_item, null);
            TextView recordscoretv=(TextView)view.findViewById(R.id.recordscoretv);
            TextView recorddatetv=(TextView)view.findViewById(R.id.recorddatetv);
//            Typeface fonttype = Typeface.createFromAsset(RecordsActivity.this.getAssets(),menufonts[1]);
//            recordidtv.setTypeface(fonttype);
//            recorddatetv.setTypeface(fonttype);
//            recordscoretv.setTypeface(fonttype);
            
//            recordidtv.setText(""+(position+1));
            
            if(bestscore==Integer.valueOf(RecordsScore.get(position)))
            {
            	recordscoretv.setTextColor(Color.rgb(222, 10, 10));
            	recorddatetv.setTextColor(Color.rgb(222, 10, 10));
            }
            recordscoretv.setText(RecordsScore.get(position));
            recorddatetv.setText(RecordsDate.get(position));
            return view;
        }
    }

	public static Date beforeMonthDate(int hourTime,int beforeDate,int beforeMonth){
	       Calendar calendar = Calendar.getInstance();
	       if(beforeMonth>0){
	           calendar.add(Calendar.MONTH, -beforeMonth);
	       }else if(beforeDate>0){
	           calendar.add(Calendar.DATE, -beforeDate);
	       }else if(hourTime>0){
	           calendar.add(Calendar.HOUR_OF_DAY, -hourTime);
	       }
	       Date date = calendar.getTime();
	       return date;
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
    	return super.onKeyDown(keyCode, event);    
    }else{
    return super.onKeyDown(keyCode, event);    
    }
    }
	
	private void playSounds(int sid) {

		if (mMediaPlayer != null) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.release();
			mMediaPlayer = null;
		}

		mMediaPlayer = MediaPlayer.create(RecordsActivity.this, sid);

		/* 准备播放 */
		// mMediaPlayer.prepare();
		/* 开始播放 */
		mMediaPlayer.start();
		
		mMediaPlayer.setOnCompletionListener(new OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer arg0) {
				mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
		});
	}
}
