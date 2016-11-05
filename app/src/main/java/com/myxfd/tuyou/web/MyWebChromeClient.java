package com.myxfd.tuyou.web;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by admin on 2016/11/3.
 */

/**
 * 这个类,可以给 WebView设置上,能够监听 WebView 加载进度、标题显示、JS对话框调用
 */
public class MyWebChromeClient extends WebChromeClient {
    private BrowserInterface mInterface;

    public MyWebChromeClient(BrowserInterface anInterface) {
        mInterface = anInterface;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mInterface.onProgressChanged(view,newProgress);
    }

}
