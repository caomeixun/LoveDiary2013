package com.love.dairy.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.renren.api.connect.android.photos.PhotoBean;

public class FileDownload  {
	public static String path = "/sdcard/LoveStory/";
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	private List<PhotoBean> photos ;
	private Context context = null;
	private int maxNumber = -1;

	public FileDownload(List<PhotoBean> photos , Context context) {
		this.photos = photos;
		this.context = context;
		startDownload();
	}

	private void startDownload() {
		maxNumber = photos.size();
		new DownloadFileAsync().execute(photos.get(0).getUrlLarge());
	}

	class DownloadFileAsync extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(context);
			int index = maxNumber-photos.size()+1;
			mProgressDialog.setMessage("Downloading file.."+index+"/"+maxNumber);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... aurl) {
			int count;
			try {
				URL url = new URL(aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();
				int lenghtOfFile = conexion.getContentLength();
				Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
				InputStream input = new BufferedInputStream(url.openStream());
				File outFile  = new File(path);
				if(!outFile.exists())
					outFile.mkdir();
				OutputStream output = new FileOutputStream(path+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+aurl[0].substring(aurl[0].lastIndexOf(".")));
				byte data[] = new byte[1024];
				long total = 0;
				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				Log.e("error", e.getMessage().toString());
				System.out.println(e.getMessage().toString());
			}
			return null;
		}

		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String unused) {
			mProgressDialog.dismiss();
			photos.remove(0);
			if(photos.size()>0){
				new DownloadFileAsync().execute(photos.get(0).getUrlLarge());
			}else{
				Toast.makeText(context, "œ¬‘ÿÕÍ≥…", 2000).show();
			}
		}
	}

}
