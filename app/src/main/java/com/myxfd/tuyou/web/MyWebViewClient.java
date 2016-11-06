package com.myxfd.tuyou.web;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by admin on 2016/11/3.
 */

public class MyWebViewClient extends WebViewClient {

    private BrowserInterface mInterface;

    public MyWebViewClient(BrowserInterface anInterface) {
        mInterface = anInterface;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
       mInterface.onPageStarted(view,url,favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        mInterface.onPageFinished(view,url);
    }

}
