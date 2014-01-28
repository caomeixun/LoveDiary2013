package com.love.dairy.main.renren;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.love.dairy.cutimage.KXActivity;
import com.love.dairy.main.BaseActivity;
import com.love.dairy.main.MainActivity;
import com.love.dairy.main.R;
import com.love.dairy.main.renren.AsyncImageLoader.ImageCallback;
import com.love.dairy.utils.BitmapUtils;
import com.love.dairy.utils.FileDownload;
import com.love.dairy.utils.ImageUtil;
import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.RennExecutor.CallBack;
import com.renn.rennsdk.RennResponse;
import com.renn.rennsdk.exception.RennException;
import com.renn.rennsdk.param.ListAlbumParam;
import com.renn.rennsdk.param.ListPhotoParam;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 手机图片文件类
 * 
 * @author rendongwei
 * 
 */
public class PhoneAlbumActivity extends BaseActivity {
	private Button mCancel;
	private ListView mDisplay;
	private RennClient rennClient;
	private ProgressDialog mProgressDialog = null;
	private List<AlbumPojo> lists = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phonealbum_activity);
		findViewById();
		setListener();
		init();
	}

	private void findViewById() {
		mCancel = (Button) findViewById(R.id.phonealbum_cancel);
		mDisplay = (ListView) findViewById(R.id.phonealbum_display);
	}

	private void setListener() {
		mCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//关闭当前界面
				finish();
			}
		});
		mDisplay.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				downloadImages(position);
			}
		});
	}
	private Handler handler = new Handler(){
		@Override
		public void dispatchMessage(android.os.Message msg) {
			String path = (String) msg.obj;
			saveSharedPreferencesData(IMAGE_PATH, path);
			finish();
		};
	};
	protected void downloadImages(final int position) {
		ListPhotoParam param = new ListPhotoParam();
        param.setAlbumId(lists.get(position).id);
        param.setOwnerId(rennClient.getUid());
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setTitle("请等待");
            mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
            mProgressDialog.setMessage("正在下载图片中");
            mProgressDialog.show();
        }
        try {
            rennClient.getRennService().sendAsynRequest(param, new CallBack() {    

                @Override
                public void onSuccess(RennResponse response) {
                    Toast.makeText(PhoneAlbumActivity.this, "获取成功", Toast.LENGTH_SHORT)
                            .show();
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                    try{
                    	JSONArray jsons = response.getResponseArray();
						List<String> paths = new ArrayList<String>(jsons.length());
						for(int i=0;i<jsons.length();i++){
							JSONObject json = jsons.getJSONObject(i);
							JSONArray images = json.getJSONArray("images");
							String path = null;
							for(int j = 0;j<images.length();j++){
								String size = images.getJSONObject(j).getString("size");
								path = images.getJSONObject(j).getString("url");
								try{
									if(path.indexOf(size.toLowerCase()+".jpg")==-1){
										break;
									}
								}catch(Exception ex){
									
								}
							}
							paths.add(path);
						}	
					new FileDownload(paths, PhoneAlbumActivity.this,lists.get(position).id,handler);
                    }catch(Exception ex){
                    	ex.printStackTrace();
                    }
                }

                @Override
                public void onFailed(String errorCode, String errorMessage) {
                    Toast.makeText(PhoneAlbumActivity.this, "获取失败", Toast.LENGTH_SHORT)
                            .show();
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                }
            });
        } catch (RennException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
	}
	
	private void init() {
		rennClient = RennClient.getInstance(this);
		// 获取手机里的图片内容
        ListAlbumParam param3 = new ListAlbumParam();
        param3.setOwnerId(rennClient.getUid());
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setTitle("请等待");
            mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
            mProgressDialog.setMessage("正在获取信息");
            mProgressDialog.show();
        }
        try {
            rennClient.getRennService().sendAsynRequest(param3, new CallBack() {    
                
                @Override
                public void onSuccess(RennResponse response) {
                    Log.e("TAG",response.toString());
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }  
                    parseData(response);
                  mDisplay.setAdapter(new PhoneAlnumAdapter());
                }

				private void parseData(RennResponse response) {
					try {
						JSONArray jsons = response.getResponseArray();
						lists = new ArrayList<AlbumPojo>(jsons.length());
						for(int i=0;i<jsons.length();i++){
							AlbumPojo album = new AlbumPojo();
							JSONObject json = jsons.getJSONObject(i);
							album.name = (String) json.get("name");
							album.id = json.getLong("id");
							JSONArray images = json.getJSONArray("cover");
							for(int j = 0;j<images.length();j++){
								String size = images.getJSONObject(j).getString("size");
								album.cover = images.getJSONObject(j).getString("url");
								try{
									if(album.cover.indexOf(size.toLowerCase()+".jpg")==-1){
										break;
									}
								}catch(Exception ex){
									
								}
							}
							album.createTime = (String) json.get("createTime");
							album.photoCount = (Integer) json.get("photoCount");
							if(album.photoCount>0)
							lists.add(album);
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
                
                @Override
                public void onFailed(String errorCode, String errorMessage) {
                	Log.e("TAG",errorCode+":"+errorMessage);
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }                            
                }
            });
        } catch (RennException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
		
	}
	class AlbumPojo{
		private Long id;
    	private String cover;//	Image[]	相册的封面
    	private String name	;//String	相册的名字
    	private String createTime;//	相册的创建时间
    	private int photoCount;
    }
	private class PhoneAlnumAdapter extends BaseAdapter {

		public int getCount() {
			return lists.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(PhoneAlbumActivity.this)
						.inflate(R.layout.phonealbum_activity_item, null);
				holder = new ViewHolder();
				holder.photo = (ImageView) convertView
						.findViewById(R.id.phonealbum_item_photo);
				holder.name = (TextView) convertView
						.findViewById(R.id.phonealbum_item_name);
				holder.count = (TextView) convertView
						.findViewById(R.id.phonealbum_item_count);
				holder.time = (TextView) convertView
						.findViewById(R.id.phonealbum_item_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
//			holder.photo.setImageBitmap(null);
			new AsyncImageLoader().loadDrawable(lists.get(position).cover, lists.get(position).cover, holder.photo, new ImageCallback() {
				
				@Override
				public void imageLoaded(Drawable imageDrawable, ImageView imageView,
						String imageUrl) {
					BitmapDrawable bd = (BitmapDrawable) imageDrawable;
					
					imageView.setImageBitmap(ImageUtil.getRoundedCornerBitmap(bd.getBitmap(), 30));
					
				};
			});
			holder.name.setText(lists.get(position).name);
			holder.time.setText(lists.get(position).createTime);
			holder.count.setText("(" +lists.get(position).photoCount+ ")");
			return convertView;
		}

		class ViewHolder {
			ImageView photo;
			TextView name;
			TextView count;
			TextView time;
		}
	}

	
	/**
	 * 判断是否为图片
	 * 
	 * @param fName
	 *            文件的名字
	 * @return
	 */
	private boolean getImageFile(String fName) {
		boolean re;

		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			re = true;
		} else {
			re = false;
		}
		return re;
	}

}
