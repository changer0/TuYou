package com.myxfd.tuyou.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

/**
 * Created by Lulu on 2016/11/8.
 */

public class SelectPhotoActivity extends TakePhotoActivity {
       @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
           TakePhoto photo = getTakePhoto();
       }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        TakePhoto photo = getTakePhoto();


    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }
}
