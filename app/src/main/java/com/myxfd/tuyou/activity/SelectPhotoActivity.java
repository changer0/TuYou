package com.myxfd.tuyou.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.myxfd.tuyou.R;
import com.myxfd.tuyou.model.TuYouUser;

import java.io.File;
import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import me.shaohui.advancedluban.Luban;

/**
 * Created by Lulu on 2016/11/8.
 */
public class SelectPhotoActivity extends TakePhotoActivity {
    public static final int FROM_CAMERA = 0x1;
    public static final int FROM_PHOTO = 0x2;
    public static final String FROM_SOURCE = "source";
    public static final String UPDATE_STATE = "state";
    private Intent intent;
    private String mPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = LayoutInflater.from(this).inflate(R.layout.common_layout, null);
        setContentView(contentView);
        intent = new Intent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent();
        intent.putExtra("path",mPath);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Snackbar.make(getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        TImage image = result.getImage();
        mPath = image.getPath();


    }


    public void onClick(View view) {
        TakePhoto takePhoto = getTakePhoto();
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        configCompress(takePhoto);
        configTakePhotoOpthion(takePhoto);
        switch (view.getId()) {
            //从相册中选取
            case R.id.btnPickBySelect:
                takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                break;
            case R.id.btnPickByTake:
                //拍照
                takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
                break;
            default:
                break;
        }

    }

    private CropOptions getCropOptions() {
        int height = 800;
        int width = 800;
        boolean withWonCrop = false;

        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

    //压缩
    private void configCompress(TakePhoto takePhoto) {

        int maxSize = 102400;
        int width = 800;
        int height = 800;
        boolean showProgressBar = true;
        CompressConfig config;

        LubanOptions option = new LubanOptions.Builder()
                .setGear(Luban.CUSTOM_GEAR)
                .setMaxHeight(height)
                .setMaxWidth(width)
                .setMaxSize(maxSize)
                .create();
        config = CompressConfig.ofLuban(option);

        takePhoto.onEnableCompress(config, showProgressBar);

    }

    private void configTakePhotoOpthion(TakePhoto takePhoto) {
        takePhoto.setTakePhotoOptions(new TakePhotoOptions.Builder().setWithOwnGallery(true).create());
    }
}
