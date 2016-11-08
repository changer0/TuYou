package com.myxfd.tuyou.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amap.api.col.bu;
import com.google.gson.Gson;
import com.myxfd.tuyou.R;
import com.myxfd.tuyou.activity.EditCircleMsgActivity;
import com.myxfd.tuyou.model.TuYouComment;
import com.myxfd.tuyou.model.TuYouTrack;
import com.myxfd.tuyou.web.BrowserInterface;
import com.myxfd.tuyou.web.JsSupport;
import com.myxfd.tuyou.web.MyWebChromeClient;
import com.myxfd.tuyou.web.MyWebViewClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class CircleFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BrowserInterface {


    private static final String TAG = "CircleFragment";
    private WebView mWebView;
    private SwipeRefreshLayout mRefreshLayout;
    private ProgressBar mProgressBar;
    private EditText mPingLunEdit;
    private LinearLayout mLinearLayout;
    private TuYouComment mTuYouComment;
    private JsSupport mJsSupport;

    public CircleFragment() {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTitle() {
        return "圈子";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().register(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveCommentId(String id){
        mTuYouComment = new TuYouComment();
        mTuYouComment.setTrackId(id);
        mLinearLayout.setVisibility(View.VISIBLE);
        mPingLunEdit.setFocusable(true);
        InputMethodManager imm = (InputMethodManager) mPingLunEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        mPingLunEdit.setFocusable(true);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        Button button = (Button) view.findViewById(R.id.circle_sendShuoShuo);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.circle_swipeRefreshLayout);
        mProgressBar = (ProgressBar) view.findViewById(R.id.circle_progressBar);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.circle_comment_linearLayout);
        Button pinglun = (Button) view.findViewById(R.id.circle_comment_layout_btn);
        pinglun.setOnClickListener(this);
        mPingLunEdit = (EditText) view.findViewById(R.id.circle_comment_layout_edit);


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

        mJsSupport = new JsSupport(getContext());
        BmobQuery<TuYouTrack> query = new BmobQuery<>();
        query.findObjects(new FindListener<TuYouTrack>() {
            @Override
            public void done(List<TuYouTrack> list, BmobException e) {
                Gson gson = new Gson();
                String json = gson.toJson(list);
                Log.d(TAG, "done: " + json);
                mJsSupport.setJson(json);

                BmobQuery<TuYouComment> bmobQuery = new BmobQuery<>();
                bmobQuery.findObjects(new FindListener<TuYouComment>() {
                    @Override
                    public void done(List<TuYouComment> list, BmobException e) {
                        Gson gson = new Gson();
                        String commmentjson = gson.toJson(list);
                        Log.d(TAG, "done: "+commmentjson);
                        mJsSupport.setMcommentJson(commmentjson);
                        mWebView.addJavascriptInterface(mJsSupport, "tuyou");
                        String path = "file:///android_asset/web/index.html";
                        mWebView.loadUrl(path);
                    }
                });

            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.circle_sendShuoShuo:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("选择说说类型");
                builder.setItems(new String[]{"文本说说", "图片说说", "视频说说"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(getContext(), EditCircleMsgActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                Toast.makeText(getContext(), "图片说说", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(getContext(), "视频说说", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;

            case R.id.circle_comment_layout_btn:
                String s = mPingLunEdit.getText().toString().trim();
                mTuYouComment.setText(s);
                String id = BmobUser.getCurrentUser().getObjectId();
                mTuYouComment.setFromUserId(id);
                mTuYouComment.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null) {
                            Toast.makeText(getContext(), "评论成功", Toast.LENGTH_SHORT).show();
                            mLinearLayout.setVisibility(View.GONE);
                            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                                    .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                });

                break;
        }
    }

    @Override
    public void onRefresh() {
        BmobQuery<TuYouComment> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<TuYouComment>() {
            @Override
            public void done(List<TuYouComment> list, BmobException e) {
                Gson gson = new Gson();
                String json = gson.toJson(list);
                mJsSupport.setMcommentJson(json);
                mWebView.reload();
            }
        });
        BmobQuery<TuYouTrack> trackBmobQuery = new BmobQuery<>();
        trackBmobQuery.findObjects(new FindListener<TuYouTrack>() {
            @Override
            public void done(List<TuYouTrack> list, BmobException e) {
                Gson gson = new Gson();
                String s = gson.toJson(list);
                mJsSupport.setJson(s);
                mWebView.reload();
            }
        });
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
