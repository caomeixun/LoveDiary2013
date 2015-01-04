package com.love.dairy.match;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.WeakHashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.love.dairy.LoveApplication;
import com.love.dairy.main.MainActivity;
import com.love.dairy.main.R;
import com.love.dairy.utils.HPDialog;
import com.love.dairy.utils.ImageUtil;

public class LianlianKanChoosePic extends Activity{
	ViewPager viewPager = null;
	ArrayList<View> list = null;
	ArrayList<String> listSelected = new ArrayList<String>();
	int maxNum = 11;
	int largeNum = 16;
	TextView tvNum = null;
	TextView tvStart = null;
	ProgressDialog mProgressDialog = null;
	private List<String> dots_xmas=null;
	
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
		LoveApplication application = (LoveApplication) this.getApplication();
		dots_xmas = application.photoIds;
		setContentView(R.layout.choose_pic); 
		if(maxNum>dots_xmas.size()){
			maxNum = dots_xmas.size();
		}
		if(dots_xmas.size()==0){
			toGameActivity();
			return;
		}
		list = new ArrayList<View>();
		
		viewPager = (ViewPager)findViewById(R.id.viewPager);
		viewPager.setAdapter(new MyAdapter());
		tvNum = (TextView) findViewById(R.id.tvNum);
		tvStart = (TextView) findViewById(R.id.tvStart);
		tvStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				toGameActivity();
			}
		});
	}
	private LinearLayout addItem(int i){
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout item = (LinearLayout) inflater.inflate(R.layout.choose_pic_item, null);
		packetView(i, item,0,R.id.iv1);
		packetView(i, item,1,R.id.iv2);
		packetView(i, item,2,R.id.iv3);
		packetView(i, item,3,R.id.iv4);
		return item;
	}
	private void packetView(int i, LinearLayout item,int index,int viewId) {
		if(i*4+index < dots_xmas.size()){
				ImageView iv1 = ((ImageView)item.findViewById(viewId));
				iv1.setTag(dots_xmas.get(i*4+index));
				Bitmap bitmap = ImageUtil.decodeBitmapFromResourceForWidth(this.getResources(), MainActivity.path + dots_xmas.get(i*4+index), 400,400);
				iv1.setImageBitmap(bitmap);
				iv1.setBackgroundColor(Color.parseColor("#eeffffff"));
				Drawable da = iv1.getDrawable();
				Log.e("Leon",da.getIntrinsicHeight()+"}}}}}}");
				setOnclick(iv1);
		}
	}
	private void setOnclick(View view){
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				RelativeLayout layout =  (RelativeLayout) arg0.getParent();
				ImageView image =(ImageView) layout.getChildAt(1);
				String index  = arg0.getTag()+"";
				if(listSelected.contains(index)){
					image.setVisibility(View.GONE);
					listSelected.remove(index);
				}else{
					listSelected.add(index);
					image.setVisibility(View.VISIBLE);
				}
				int length = listSelected.size();
				tvNum.setText(String.format("已选择：%d-%d", length,maxNum));
				if(length== maxNum){
					Toast.makeText(LianlianKanChoosePic.this, "开始",Toast.LENGTH_LONG).show();
					toGameActivity();
				}
			}
		});
	}
	private void toGameActivity(){
		Vector<String> vNums = new Vector<String>();
		for (int i = 0; i < dots_xmas.size(); i++)
		{
			if(!listSelected.contains(dots_xmas.get(i))){
				vNums.add(dots_xmas.get(i)+"");
			}
		}
		List<String> list= new ArrayList<String>();
		for(int i=0;i<listSelected.size();i++){
			list.add(MainActivity.path + listSelected.get(i));
		}
		int length = vNums.size();//剩余随机部分总长度
		int randomLength = largeNum - list.size();//剩余的随机个数
		int j = length < randomLength ? length : randomLength;//取得较小的去处理
		for (int i = 0; i < j ; i++)
		{
					String text = getRandom(vNums);//乱序代码部分
					list.add(MainActivity.path + text);
		}
		String ids[] = new String[list.size()];
        new LoadBitmapAsyncTask(list.toArray(ids), this).execute(1);
	}
	/**
	 * 预加载图片
	 * @author leonli
	 *
	 */
	static class LoadBitmapAsyncTask extends AsyncTask<Integer, Integer,Integer>{
		String mIds[] = null;
		Context context = null;
		Activity mActivity = null;
		HPDialog mProgressDialog = null;
		@Override
		protected void onPreExecute() {
		  if (mProgressDialog == null) {
	            mProgressDialog = new HPDialog(mActivity);
	        }
			super.onPreExecute();
		}
		public LoadBitmapAsyncTask(String ids[],Activity activity){
			mIds = ids;
			mActivity = activity;
			context = activity.getApplicationContext();
		}
		@Override
		protected  Integer doInBackground(Integer... inter) {
			int length = mIds.length;
			if(length>18){
				length = 18;
			}
			for(int i=0;  i<length ; i++){
				 int size = context.getResources().getDimensionPixelSize(R.dimen.game_btn_size);
				 Bitmap bitmap = null;
				 LoveApplication application = (LoveApplication) mActivity.getApplication();
				 WeakHashMap<String, Bitmap> bitmapCache = application.matchBitmapCache;
				 bitmap = ImageUtil.decodeBitmapFromResourceForWidth(context.getResources(), (String) mIds[i], size, size);
				 bitmapCache.put((String) mIds[i], bitmap);
			}
			return 1;
		}
		@Override
		protected void onPostExecute(Integer i) {
			super.onPostExecute(i);
			mProgressDialog.dismiss();
			mProgressDialog = null;
			Intent intent = new Intent();
			intent.setClass(mActivity, LianlianKanMainActivity.class);
			intent.putExtra("pics", mIds);
			intent.putExtra("difficult", 8);
			mActivity.startActivity(intent);
			mActivity.finish();
		}
	}
	class MyAdapter extends PagerAdapter {
		public int size = list.size();

		@Override
		public int getCount() {
			int len = dots_xmas.size()/4;
			len += dots_xmas.size() % 4>0?1:0;
			return len;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(arg0);
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			View view  = addItem(arg1);
			((ViewPager) arg0).addView(view);
//			Animation animation1 = AnimationUtils.loadAnimation(LianlianKanChoosePic.this, R.anim.apppop1);
			return view;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}
}

