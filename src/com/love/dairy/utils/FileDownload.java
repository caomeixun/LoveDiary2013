package com.love.dairy.utils;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.love.dairy.cutimage.PhotoUtil;


public class FileDownload  {
	public static String path = Environment.getExternalStorageDirectory().getPath() + "LoveStory/";
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	private List<String> photos ;
	private Context context = null;
	private int maxNumber = -1;
	private Long albumId;
	private Handler postHandler = null;

	public FileDownload(List<String> photos , Context context,Long albumId,Handler postHandler) {
		this.photos = photos;
		this.context = context;
		this.albumId = albumId;
		this.postHandler = postHandler;
		startDownload();
	}

	private void startDownload() {
		maxNumber = photos.size();
		if(mProgressDialog == null){
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMax(maxNumber);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i=0;i< photos.size();i++){
					String urlStr = photos.get(i);
					try {
						URL m;
					    InputStream input = null;
					    m = new URL(urlStr);
					    input = (InputStream) m.getContent();
					    String savePath = albumId+urlStr.substring(urlStr.lastIndexOf("/"));
					    PhotoUtil.saveToLocal(BitmapFactory.decodeStream(input),savePath,false);
						Message msg = handler.obtainMessage();
						msg.arg1 = i+1;
						msg.obj = mProgressDialog;
						handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				Message msg = new Message();
				msg.obj = FileDownload.path+"cutimage/"+ albumId+"/";
				postHandler.sendMessage(msg);
			}
		}).start();
		
	}
	private static Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			if(msg.obj != null){
				ProgressDialog mProgressDialog = (ProgressDialog) msg.obj;
				mProgressDialog.setProgress(msg.arg1);
			}
			
		};
	};
//	class DownloadFileAsync extends AsyncTask<String, String, String> {
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			Log.e("TAg", "onPreExecute"+photos.size());
//			if(mProgressDialog == null){
//				Message msg = handler.obtainMessage();
//				msg.arg1 = 0;
//				handler.sendMessage(msg);
//			}
//		}
//
//		@Override
//		protected String doInBackground(String... aurl) {
//			int count;
//			try {
//				URL url = new URL(aurl[0]);
//				URLConnection conexion = url.openConnection();
//				conexion.connect();
//				int lenghtOfFile = conexion.getContentLength();
//				Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
//				InputStream input = new BufferedInputStream(url.openStream());
//				File outFile  = new File(path);
//				if(!outFile.exists())
//					outFile.mkdir();
//				OutputStream output = new FileOutputStream(path+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+aurl[0].substring(aurl[0].lastIndexOf(".")));
//				byte data[] = new byte[1024];
//				long total = 0;
//				while ((count = input.read(data)) != -1) {
//					total += count;
//					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
//					output.write(data, 0, count);
//				}
//				output.flush();
//				output.close();
//				input.close();
//			} catch (Exception e) {
//				Log.e("error", e.getMessage().toString());
//				System.out.println(e.getMessage().toString());
//			}
//			return null;
//		}
//
//		protected void onProgressUpdate(String... progress) {
//			
//		}
//
//		@Override
//		protected void onPostExecute(String unused) {
//			Log.e("TAg", "onPostExecute"+photos.size());
//			Message msg = handler.obtainMessage();
//			msg.arg1 = maxNumber - photos.size();
//			handler.sendMessage(msg);
//			if(photos.size()>1){
//				photos.remove(0);
//				new DownloadFileAsync().execute(photos.get(0));
//			}else{
//				mProgressDialog.dismiss();
//				Toast.makeText(context, "œ¬‘ÿÕÍ≥…", 2000).show();
//			}
//		}
//	}

}
