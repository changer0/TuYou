package com.myxfd.tuyou.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myxfd.tuyou.R;

/**
 * Created by liangyue on 16/11/11.
 */

public class ProgressDialogUtil {
    public static Dialog createLoadingDialog(Context context, String msg) {

        ProgressDialog loadingDialog = new ProgressDialog(context);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setTitle(msg);
        loadingDialog.setMessage("请稍等...");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        return loadingDialog;

    }
}
