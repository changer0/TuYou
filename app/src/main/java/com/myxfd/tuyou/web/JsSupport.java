package com.myxfd.tuyou.web;

/**
 * Created by admin on 2016/11/2.
 */

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * 用于js调用java ,传递进webView
 */
public class JsSupport {
    private Context mContext;
    private String mJson;

    public JsSupport(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public String getJson() {
        return mJson;
    }

    public void setJson(String json) {
        mJson = json;
    }

    @JavascriptInterface
    public void showToast(String str){
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }

}
