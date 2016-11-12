package com.myxfd.tuyou.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.model.TuYouTrack;
import com.myxfd.tuyou.utils.DensityUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class EditCircleMsgActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mContent;
    private ImageView mImageView;
    private Uri mImgUri;
    private static final int PHOTO_RESULT_CODE = 1;
    private static final int PHOTO_PIC_CODE = 2;
    private String mPath;
    private static final String TAG = "EditCircleMsgActivity";
    private BmobFile mBmobFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_circle_msg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_circle_msg_toolbar);
        mImageView = (ImageView) findViewById(R.id.edit_circle_msg_img);
        mContent = (EditText) findViewById(R.id.edit_circle_msg_content);
        Button button = (Button) findViewById(R.id.edit_circle_msg_choice_pic);
        button.setOnClickListener(this);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_icon_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.circle_option_menu_send:
                BmobUser currentUser = BmobUser.getCurrentUser();
                String objId = currentUser.getObjectId();
                String content = mContent.getText().toString();
                TuYouTrack tuYouTrack = new TuYouTrack();
                tuYouTrack.setUser(objId);
                tuYouTrack.setText(content);
                if (mBmobFile != null) {
                    tuYouTrack.setImage(mBmobFile.getFileUrl());
                }
                tuYouTrack.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e != null) {
                            Toast.makeText(EditCircleMsgActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            finish();
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_circle_msg_choice_pic:
                Intent intent = new Intent(this, SelectPhotoActivity.class);
                startActivityForResult(intent, PHOTO_RESULT_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            mPath = data.getStringExtra(SelectPhotoActivity.FROM_SOURCE);
            Log.d(TAG, "onActivityResult: path    "+ mPath);
            File file = new File(mPath);
            final Uri uri = Uri.fromFile(file);
            mBmobFile = new BmobFile(file);
            mBmobFile.upload(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e==null) {
                        int px = DensityUtil.dp2px(getApplication(), 120);
                        Picasso.with(EditCircleMsgActivity.this).load(uri).resize(px,px).into(mImageView);
                    }
                }
            });
        }
    }

//    //裁剪拍照后得到的图片
//    private void cropImageUri(Uri uri, int requestCode) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        //intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 500);
//        intent.putExtra("outputY", 500);
//        intent.putExtra("scale", true);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        intent.putExtra("return-data", false);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", true); // no face detection
//        intent = Intent.createChooser(intent, "裁剪图片");
//        startActivityForResult(intent, requestCode);
//    }


}
