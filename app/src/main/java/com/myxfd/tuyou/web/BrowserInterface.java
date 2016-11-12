package com.myxfd.tuyou.web;

import android.graphics.Bitmap;
import android.webkit.WebView;

/**
 * Created by admin on 2016/11/3.
 */

public interface BrowserInterface {
    void onPageFinished(WebView view, String url);

    void onProgressChanged(WebView view, int newProgress);

    void onPageStarted(WebView view, String url, Bitmap favicon);
}
