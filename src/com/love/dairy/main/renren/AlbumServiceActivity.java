package com.love.dairy.main.renren;

import com.renn.rennsdk.RennExecutor.CallBack;
import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.exception.RennException;
import com.renn.rennsdk.RennResponse;
import com.renn.rennsdk.param.AccessControl;
import com.renn.rennsdk.param.GetAlbumParam;
import com.renn.rennsdk.param.ListAlbumParam;
import com.renn.rennsdk.param.PutAlbumParam;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class AlbumServiceActivity extends Activity {

    private Button getAlbumBtn;
    private Button putAlbumBtn;
    private Button listAlbumBtn;
    private TextView textView;
    private RennClient rennClient;
    private ProgressDialog mProgressDialog;
    private View view;
    private Button btn;
    private EditText et;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_albumservice);
//        initView();
    }
    
//    private void initView(){
//        rennClient = RennClient.getInstance(this);
//        getAlbumBtn = (Button) findViewById(R.id.getAlbum);
//        getAlbumBtn.setOnClickListener(this);
//        putAlbumBtn = (Button) findViewById(R.id.putAlbum);
//        putAlbumBtn.setOnClickListener(this);
//        listAlbumBtn = (Button) findViewById(R.id.listAlbum);
//        listAlbumBtn.setOnClickListener(this);
//        textView = (TextView) findViewById(R.id.textView);
//        view = findViewById(R.id.album_input);
//        view.setVisibility(View.GONE);
//        btn = (Button) findViewById(R.id.btn);
//        btn.setOnClickListener(this);
//        et = (EditText) findViewById(R.id.album_edit);
//        scrollView = (ScrollView) findViewById(R.id.scrollView);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.getAlbum:                
//            	view.setVisibility(View.VISIBLE);
//    			scrollView.setVisibility(View.GONE);
//                break;
//            case R.id.putAlbum:
//            	view.setVisibility(View.GONE);
//    			scrollView.setVisibility(View.VISIBLE);
//                PutAlbumParam param2 = new PutAlbumParam();
//                param2.setName("测试相册");
//                param2.setDescription("测试测试测试相册");
//                param2.setDescription("静安中心");
//              //  param2.setPassword("123456");
//                param2.setAccessControl(AccessControl.PUBLIC);
//                if (mProgressDialog == null) {
//                    mProgressDialog = new ProgressDialog(AlbumServiceActivity.this);
//                    mProgressDialog.setCancelable(true);
//                    mProgressDialog.setTitle("请等待");
//                    mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
//                    mProgressDialog.setMessage("正在获取信息");
//                    mProgressDialog.show();
//                }
//                try {
//                    rennClient.getRennService().sendAsynRequest(param2, new CallBack() {    
//                        @Override
//                        public void onSuccess(RennResponse response) {
//                            textView.setText(response.toString());
//                            Toast.makeText(AlbumServiceActivity.this, "创建成功", Toast.LENGTH_SHORT).show();  
//                            if (mProgressDialog != null) {
//                                mProgressDialog.dismiss();
//                                mProgressDialog = null;
//                            }                           
//                        }
//                        
//                        @Override
//                        public void onFailed(String errorCode, String errorMessage) {
//                            textView.setText(errorCode+":"+errorMessage);
//                            Toast.makeText(AlbumServiceActivity.this, "创建失败", Toast.LENGTH_SHORT).show();
//                            if (mProgressDialog != null) {
//                                mProgressDialog.dismiss();
//                                mProgressDialog = null;
//                            }                            
//                        }
//                    });
//                } catch (RennException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//                break;
//            case R.id.listAlbum:
//            	view.setVisibility(View.GONE);
//    			scrollView.setVisibility(View.VISIBLE);
//                ListAlbumParam param3 = new ListAlbumParam();
//                param3.setOwnerId(rennClient.getUid());
//                if (mProgressDialog == null) {
//                    mProgressDialog = new ProgressDialog(AlbumServiceActivity.this);
//                    mProgressDialog.setCancelable(true);
//                    mProgressDialog.setTitle("请等待");
//                    mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
//                    mProgressDialog.setMessage("正在获取信息");
//                    mProgressDialog.show();
//                }
//                try {
//                    rennClient.getRennService().sendAsynRequest(param3, new CallBack() {    
//                        
//                        @Override
//                        public void onSuccess(RennResponse response) {
//                            textView.setText(response.toString());
//                            Toast.makeText(AlbumServiceActivity.this, "获取成功", Toast.LENGTH_SHORT).show();  
//                            if (mProgressDialog != null) {
//                                mProgressDialog.dismiss();
//                                mProgressDialog = null;
//                            }                           
//                        }
//                        
//                        @Override
//                        public void onFailed(String errorCode, String errorMessage) {
//                            textView.setText(errorCode+":"+errorMessage);
//                            Toast.makeText(AlbumServiceActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
//                            if (mProgressDialog != null) {
//                                mProgressDialog.dismiss();
//                                mProgressDialog = null;
//                            }                            
//                        }
//                    });
//                } catch (RennException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//                break;
//            case R.id.btn:
//            	view.setVisibility(View.GONE);
//    			scrollView.setVisibility(View.VISIBLE);
//            	GetAlbumParam param1 = new GetAlbumParam();
//            	try{
//            		 param1.setOwnerId(rennClient.getUid());
//                     param1.setAlbumId(Long.valueOf(et.getText().toString()));
//            	}catch(Exception e){
//            		
//            	}               
//                if (mProgressDialog == null) {
//                    mProgressDialog = new ProgressDialog(AlbumServiceActivity.this);
//                    mProgressDialog.setCancelable(true);
//                    mProgressDialog.setTitle("请等待");
//                    mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
//                    mProgressDialog.setMessage("正在获取相册");
//                    mProgressDialog.show();
//                }
//                try {
//                    rennClient.getRennService().sendAsynRequest(param1, new CallBack() {    
//                        @Override
//                        public void onSuccess(RennResponse response) {
//                            textView.setText(response.toString());
//                            Toast.makeText(AlbumServiceActivity.this, "获取成功", Toast.LENGTH_SHORT).show();  
//                            if (mProgressDialog != null) {
//                                mProgressDialog.dismiss();
//                                mProgressDialog = null;
//                            }                           
//                        }
//                        
//                        @Override
//                        public void onFailed(String errorCode, String errorMessage) {
//                            textView.setText(errorCode+":"+errorMessage);
//                            Toast.makeText(AlbumServiceActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
//                            if (mProgressDialog != null) {
//                                mProgressDialog.dismiss();
//                                mProgressDialog = null;
//                            }                            
//                        }
//                    });
//                } catch (RennException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//            	break;
//            default:
//                break;
//        }
//        
//    }

}
