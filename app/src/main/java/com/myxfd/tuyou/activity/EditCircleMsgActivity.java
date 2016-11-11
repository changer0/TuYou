package com.myxfd.tuyou.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.model.TuYouTrack;

import java.io.File;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class EditCircleMsgActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mContent;
    private ImageView mImageView;
    private Uri mImgUri;
    private static final int PHOTO_RESULT_CODE = 1;
    private static final int PHOTO_PIC_CODE = 2;

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
        bar.setDisplayHomeAsUpEnabled(true);
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
            case android.R.id.home:
                finish();
                break;
            case R.id.circle_option_menu_send:
                BmobUser currentUser = BmobUser.getCurrentUser();
                String objId = currentUser.getObjectId();
                String content = mContent.getText().toString();
                TuYouTrack tuYouTrack = new TuYouTrack();
                tuYouTrack.setUser(objId);
                tuYouTrack.setText(content);
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
                String path = Environment.getExternalStorageDirectory() + "/" + "posprint" + "/track.jpg";
                mImgUri = Uri.fromFile(new File(path));
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImgUri);
                startActivityForResult(intent, PHOTO_RESULT_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case PHOTO_RESULT_CODE:
                    cropImageUri(mImgUri,PHOTO_PIC_CODE);
                    break;
                case PHOTO_PIC_CODE:
//                    Bundle bundle = data.getExtras();
//                    if (bundle != null) {
//                        Bitmap photo=bundle.getParcelable("data");
//                        mImageView.setImageBitmap(photo);
//                    }
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImgUri);
                        mImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    //裁剪拍照后得到的图片
    private void cropImageUri(Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        intent = Intent.createChooser(intent, "裁剪图片");
        startActivityForResult(intent, requestCode);
    }


}
