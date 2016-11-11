package com.myxfd.tuyou.utils;

import android.content.Context;

/**
 * Created by admin on 2016/11/11.
 */

public class DensityUtil {
    public static int dp2px(Context context,float dpvalue){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpvalue*density+0.5f);
    }
    public static int px2dp(Context context,float pxvalue){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxvalue/density+0.5f);
    }
}
