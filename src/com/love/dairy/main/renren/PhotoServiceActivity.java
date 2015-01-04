package com.love.dairy.main.renren;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.love.dairy.main.R;
import com.love.dairy.utils.HPDialog;
import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.RennExecutor.CallBack;
import com.renn.rennsdk.RennResponse;
import com.renn.rennsdk.exception.RennException;
import com.renn.rennsdk.param.GetPhotoParam;
import com.renn.rennsdk.param.ListPhotoParam;
import com.renn.rennsdk.param.UploadPhotoParam;

public class PhotoServiceActivity extends Activity implements OnClickListener {

    private Button uploadPhotoBtn;

    private Button getPhotoBtn;

    private Button listPhotoBtn;

    private View view;
    
    private View view2;
    
    private Button selectPicBtn;
    
    private TextView picText;
    
    private EditText picInfo;
    
    private Button submit2;
    
    private File picFile;
    
    private View photoIdView;

    private EditText albumId;

    private EditText photoId;

    private Button submit;

    private ScrollView scrollView;

    private TextView textView;

    private RennClient rennClient;

    private HPDialog mProgressDialog;
    
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoservice);
        initView();
    }

    private void initView() {
        rennClient = RennClient.getInstance(this);
        uploadPhotoBtn = (Button) findViewById(R.id.uploadPhoto);
        uploadPhotoBtn.setOnClickListener(this);
        getPhotoBtn = (Button) findViewById(R.id.getPhoto);
        getPhotoBtn.setOnClickListener(this);
        listPhotoBtn = (Button) findViewById(R.id.listPhoto);
        listPhotoBtn.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.textView);
        view = findViewById(R.id.photo_input);
        view.setVisibility(View.GONE);
        view2 = findViewById(R.id.photo_upload);
        view2.setVisibility(View.GONE);
        selectPicBtn = (Button) findViewById(R.id.upload_btn);
        selectPicBtn.setOnClickListener(this);
        picText = (TextView) findViewById(R.id.upload_text);
        picInfo = (EditText) findViewById(R.id.description_edit);
        submit2 = (Button) findViewById(R.id.upload_submit);
        submit2.setOnClickListener(this);
        photoIdView = findViewById(R.id.photoId_cover);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        albumId = (EditText) findViewById(R.id.albumId_edit);
        photoId = (EditText) findViewById(R.id.photoId_edit);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }
    
    
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (data != null) {
                    Uri selectedImageUri = data.getData();  
                    if (selectedImageUri != null) { 
                        String imageUrl = getPath(selectedImageUri);
                        picText.setText(imageUrl);
                        picFile = new File(imageUrl);
                    }
                }
                break;

            default:
                break;
        }
    }
    
    //获得图片的物理地址
    @SuppressWarnings("deprecation")
	public  String getPath(Uri uri) {
        Cursor cursor;
        String[] projection = { MediaStore.Images.Media.DATA };
        if(uri.toString().contains("file:///")){
            Uri mUri = Uri.parse("content://media/external/images/media"); 
            Uri mImageUri = null;
            Cursor cursor1 = managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    String data = cursor1.getString(cursor1
                            .getColumnIndex(MediaStore.MediaColumns.DATA));
                    if (uri.getPath().equals(data)) {
                        int ringtoneID = cursor1.getInt(cursor1
                                .getColumnIndex(MediaStore.MediaColumns._ID));
                        mImageUri = Uri.withAppendedPath(mUri, ""
                                + ringtoneID);
                        break;
                    }
                    cursor1.moveToNext();
                }     
                if(mImageUri == null){
                    return null;
                }else{
                    cursor = managedQuery(mImageUri, projection, null, null, null);
                }                                       
        }else{
            cursor = managedQuery(uri, projection, null, null, null);
        }
                 
        if(cursor == null){
            return null;//如果不是图片 直接返回null；
        }else{
            startManagingCursor(cursor);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadPhoto:
                flag = 1;
                view2.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                break;
            case R.id.upload_btn:
                Intent localIntent = new Intent();
                localIntent.setType("image/*");
                localIntent.setAction("android.intent.action.GET_CONTENT");
                Intent localIntent2 = Intent.createChooser(localIntent,
                        "选择图片");
                this.startActivityForResult(localIntent2, 1);
                break;
            case R.id.getPhoto:
                flag = 2;
                view.setVisibility(View.VISIBLE);
                photoIdView.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);

                break;
            case R.id.listPhoto:
                flag = 3;
                view.setVisibility(View.VISIBLE);
                photoIdView.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                break;
            case R.id.submit:
                view.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                if(flag == 2){
                    GetPhotoParam param = new GetPhotoParam();
                    try{
                    	param.setAlbumId(Long.valueOf(albumId.getText().toString()));
                        param.setPhotoId(Long.valueOf(photoId.getText().toString()));
                        param.setOwnerId(rennClient.getUid());
                    }catch(Exception e){                   	
                    }                   
                    if (mProgressDialog == null) {
                        mProgressDialog = new HPDialog(PhotoServiceActivity.this);
                    }
                    try {
                        rennClient.getRennService().sendAsynRequest(param, new CallBack() {    

                            @Override
                            public void onSuccess(RennResponse response) {
                                textView.setText(response.toString());
                                Toast.makeText(PhotoServiceActivity.this, "获取成功", Toast.LENGTH_SHORT)
                                        .show();
                                if (mProgressDialog != null) {
                                    mProgressDialog.dismiss();
                                    mProgressDialog = null;
                                }
                            }

                            @Override
                            public void onFailed(String errorCode, String errorMessage) {
                                textView.setText(errorCode + ":" + errorMessage);
                                Toast.makeText(PhotoServiceActivity.this, "获取失败", Toast.LENGTH_SHORT)
                                        .show();
                                if (mProgressDialog != null) {
                                    mProgressDialog.dismiss();
                                    mProgressDialog = null;
                                }
                            }
                        });
                    } catch (RennException e1) {
                        e1.printStackTrace();
                    }
                }else if(flag == 3){
                    ListPhotoParam param = new ListPhotoParam();
                    try{
                        param.setAlbumId(Long.valueOf(albumId.getText().toString()));
                        param.setOwnerId(rennClient.getUid());
                    }catch(Exception e){
                    	
                    }
                    if (mProgressDialog == null) {
                        mProgressDialog = new HPDialog(PhotoServiceActivity.this);
                    }
                    try {
                        rennClient.getRennService().sendAsynRequest(param, new CallBack() {    

                            @Override
                            public void onSuccess(RennResponse response) {
                                textView.setText(response.toString());
                                Toast.makeText(PhotoServiceActivity.this, "获取成功", Toast.LENGTH_SHORT)
                                        .show();
                                if (mProgressDialog != null) {
                                    mProgressDialog.dismiss();
                                    mProgressDialog = null;
                                }
                            }

                            @Override
                            public void onFailed(String errorCode, String errorMessage) {
                                textView.setText(errorCode + ":" + errorMessage);
                                Toast.makeText(PhotoServiceActivity.this, "获取失败", Toast.LENGTH_SHORT)
                                        .show();
                                if (mProgressDialog != null) {
                                    mProgressDialog.dismiss();
                                    mProgressDialog = null;
                                }
                            }
                        });
                    } catch (RennException e1) {
                        e1.printStackTrace();
                    }
                }                
                break;
            case R.id.upload_submit:
                view.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                UploadPhotoParam param = new UploadPhotoParam();
                try{
                	 param.setFile(picFile);
                     param.setDescription(picInfo.getText().toString());
                }catch(Exception e){                	
                }              
                if (mProgressDialog == null) {
                    mProgressDialog = new HPDialog(PhotoServiceActivity.this);
                }
                try {
                    rennClient.getRennService().sendAsynRequest(param, new CallBack() {    

                        @Override
                        public void onSuccess(RennResponse response) {
                            textView.setText(response.toString());
                            Toast.makeText(PhotoServiceActivity.this, "获取成功", Toast.LENGTH_SHORT)
                                    .show();
                            if (mProgressDialog != null) {
                                mProgressDialog.dismiss();
                                mProgressDialog = null;
                            }
                        }

                        @Override
                        public void onFailed(String errorCode, String errorMessage) {
                            textView.setText(errorCode + ":" + errorMessage);
                            Toast.makeText(PhotoServiceActivity.this, "获取失败", Toast.LENGTH_SHORT)
                                    .show();
                            if (mProgressDialog != null) {
                                mProgressDialog.dismiss();
                                mProgressDialog = null;
                            }
                        }
                    });
                } catch (RennException e1) {
                    e1.printStackTrace();
                }
                break;
            default:
                break;
        }

    }

}
