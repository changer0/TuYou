package com.myxfd.tuyou.web;

/**
 * Created by admin on 2016/11/2.
 */

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myxfd.tuyou.model.TuYouComment;
import com.myxfd.tuyou.model.TuYouPraise;

import org.greenrobot.eventbus.EventBus;

import java.security.PrivateKey;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 用于js调用java ,传递进webView
 */
public class JsSupport {
    private Context mContext;
    private String mJson;
    private String mcommentJson;

    private static final String TAG = "JsSupport";


    public void setMcommentJson(String mcommentJson) {
        this.mcommentJson = mcommentJson;
    }

    public JsSupport(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setJson(String json) {
        mJson = json;
    }


    @JavascriptInterface
    public String getJson() {
        Log.d("1111111111111111", "getJson: "+mJson);
        return mJson;
    }
    @JavascriptInterface
    public String getCommentJson(){
        return mcommentJson;
    }


    @JavascriptInterface
    public void showToast(String str){
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void onClickComment(String id){
        EventBus.getDefault().post(id);
    }
    @JavascriptInterface
    public void onClickZan(String id){
        BmobUser currentUser = BmobUser.getCurrentUser();
        String objectId = currentUser.getObjectId();
        TuYouPraise praise = new TuYouPraise();
        praise.setFromUserId(objectId);
        praise.setTrackId(id);
        praise.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null) {
                    Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d(TAG, "done: "+e.getMessage());
                }
            }
        });
    }




}
