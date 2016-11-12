package com.myxfd.tuyou.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myxfd.tuyou.R;
import com.myxfd.tuyou.activity.EditCircleMsgActivity;
import com.myxfd.tuyou.model.TuYouComment;
import com.myxfd.tuyou.model.TuYouTrack;
import com.myxfd.tuyou.model.TuYouUser;
import com.myxfd.tuyou.web.BrowserInterface;
import com.myxfd.tuyou.web.JsSupport;
import com.myxfd.tuyou.web.MyWebChromeClient;
import com.myxfd.tuyou.web.MyWebViewClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class CircleFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BrowserInterface, View.OnTouchListener {


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
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveCommentId(String id) {
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
        mWebView.setOnClickListener(this);
        mWebView.setWebViewClient(new MyWebViewClient(this));
        mWebView.setWebChromeClient(new MyWebChromeClient(this));
        mWebView.setOnTouchListener(this);
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


        BmobUser currentUser = BmobUser.getCurrentUser();
        String id = currentUser.getObjectId();
        BmobQuery<TuYouUser> userBmobQuery = new BmobQuery<>();
        //查找当前用户的头像
        userBmobQuery.getObject(id, new QueryListener<TuYouUser>() {
            @Override
            public void done(TuYouUser tuYouUser, BmobException e) {
                if (e == null) {
                    String icon = tuYouUser.getIcon();
                    mJsSupport.setIcon(icon);
                }
            }
        });



        BmobQuery<TuYouTrack> query = new BmobQuery<>();
        query.findObjects(new FindListener<TuYouTrack>() {
            @Override
            public void done(List<TuYouTrack> list, BmobException e) {
                Gson gson = new Gson();
                String json = gson.toJson(list);
                Log.d(TAG, "done: " + json);
                //给html中的js传入"说说"json
                mJsSupport.setTrackJson(json);

                BmobQuery<TuYouComment> bmobQuery = new BmobQuery<>();
                bmobQuery.findObjects(new FindListener<TuYouComment>() {
                    @Override
                    public void done(List<TuYouComment> list, BmobException e) {
                        Gson gson = new Gson();
                        String commmentjson = gson.toJson(list);
                        Log.d(TAG, "done: " + commmentjson);
                        //传入评论json
                        mJsSupport.setMcommentJson(commmentjson);

                        BmobQuery<TuYouUser> tuYouUserBmobQuery = new BmobQuery<>();
                        tuYouUserBmobQuery.findObjects(new FindListener<TuYouUser>() {
                            @Override
                            public void done(List<TuYouUser> list, BmobException e) {

                                if (e == null) {

                                    Gson gson = new Gson();
                                    String s = gson.toJson(list);
                                    //传入用户json
                                    mJsSupport.setUserJson(s);

                                    //所有信息获取完毕, 传入
                                    mWebView.addJavascriptInterface(mJsSupport, "tuyou");
                                    String path = "file:///android_asset/web/index.html";
                                    mWebView.loadUrl(path);
                                } else {
                                    Log.d(TAG, "done: js中获取TuYouList出现异常: " + e.getMessage());
                                }

                            }
                        });

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

                Intent intent = new Intent(getContext(), EditCircleMsgActivity.class);
                startActivity(intent);
                break;

            case R.id.circle_comment_layout_btn:
                String s = mPingLunEdit.getText().toString().trim();
                mTuYouComment.setText(s);
                String id = BmobUser.getCurrentUser().getObjectId();
                mTuYouComment.setFromUserId(id);
                mTuYouComment.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getContext(), "评论成功", Toast.LENGTH_SHORT).show();
                            mLinearLayout.setVisibility(View.GONE);
                            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                                    .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onRefresh() {
        doRefreshData();
    }

    @Override
    public void onResume() {
        doRefreshData();
        super.onResume();
    }

    private void doRefreshData() {
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
                mJsSupport.setTrackJson(s);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mLinearLayout.setVisibility(View.GONE);
        return false;
    }

}
