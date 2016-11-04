package com.myxfd.tuyou.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.myxfd.tuyou.R;
import com.myxfd.tuyou.activity.EditCircleMsgActivity;
import com.myxfd.tuyou.model.TuYouTrack;
import com.myxfd.tuyou.web.BrowserInterface;
import com.myxfd.tuyou.web.JsSupport;
import com.myxfd.tuyou.web.MyWebChromeClient;
import com.myxfd.tuyou.web.MyWebViewClient;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class CircleFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BrowserInterface {


    private static final String TAG = "CircleFragment";
    private WebView mWebView;
    private SwipeRefreshLayout mRefreshLayout;
    private ProgressBar mProgressBar;

    public CircleFragment() {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTitle() {
        return "圈子";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        Button button = (Button) view.findViewById(R.id.circle_sendShuoShuo);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.circle_swipeRefreshLayout);
        mProgressBar = (ProgressBar) view.findViewById(R.id.circle_progressBar);

        mRefreshLayout.setOnRefreshListener(this);
        button.setOnClickListener(this);
        mWebView = (WebView) view.findViewById(R.id.circle_webView);
        mWebView.setWebViewClient(new MyWebViewClient(this));
        mWebView.setWebChromeClient(new MyWebChromeClient(this));
        //开启js支持
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true); //允许使用js调用java
        settings.setSupportZoom(false);   // 在网页中显示 放大缩小 (通常不要使用)
        settings.setAllowFileAccess(true);//允许访问asset目录
        settings.setBuiltInZoomControls(false); // 启用内置缩放装置
        // 设置WebView排版算法, 实现单列显示,不允许横向滚动
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);

        final JsSupport jsSupport = new JsSupport(getContext());
        BmobQuery<TuYouTrack> query = new BmobQuery<>();
        query.findObjects(new FindListener<TuYouTrack>() {
            @Override
            public void done(List<TuYouTrack> list, BmobException e) {
                Gson gson = new Gson();
                String json = gson.toJson(list);
                Log.d(TAG, "done: " + json);

                jsSupport.setJson(json);
                mWebView.addJavascriptInterface(jsSupport, "tuyou");
                String path = "file:///android_asset/web/index.html";
                mWebView.loadUrl(path);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), EditCircleMsgActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mWebView.reload();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        mRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mProgressBar.setProgress(newProgress);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        mProgressBar.setVisibility(View.VISIBLE);
    }

}
