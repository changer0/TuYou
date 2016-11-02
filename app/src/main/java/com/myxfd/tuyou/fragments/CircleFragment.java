package com.myxfd.tuyou.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.activity.EditCircleMsgActivity;
import com.myxfd.tuyou.web.JsSupport;

/**
 * A simple {@link Fragment} subclass.
 */
public class CircleFragment extends BaseFragment implements View.OnClickListener {




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
        button.setOnClickListener(this);
        WebView mWebView = (WebView) view.findViewById(R.id.circle_webView);
        //开启js支持
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setAllowFileAccess(true);
        // 设置WebView排版算法, 实现单列显示,不允许横向滚动
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        JsSupport jsSupport = new JsSupport(getContext());

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), EditCircleMsgActivity.class);
        startActivity(intent);
    }
}
