package com.myxfd.tuyou.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.myxfd.tuyou.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class OtherLogin extends AppCompatActivity implements View.OnClickListener, PlatformActionListener {
    private String currentPlatName;
    private static final String TAG = "OtherLogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_login);

        Button qqBtn = (Button) findViewById(R.id.other_login_btn_qq);
        Button sinaBtn = (Button) findViewById(R.id.other_login_btn_sina);
        Button weiChatBtn = (Button) findViewById(R.id.other_login_btn_wei_chat);
        Button cancelBtn = (Button) findViewById(R.id.other_login_btn_cancel);

        qqBtn.setOnClickListener(this);
        sinaBtn.setOnClickListener(this);
        weiChatBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Platform platform = null;

        switch (id) {
            case R.id.other_login_btn_qq:
                platform = ShareSDK.getPlatform(this, QQ.NAME);
                break;
            case R.id.other_login_btn_sina:
                platform = ShareSDK.getPlatform(this, SinaWeibo.NAME);
                break;
            case R.id.other_login_btn_wei_chat:
                platform = ShareSDK.getPlatform(this, Wechat.NAME);

                break;
            case R.id.other_login_btn_cancel:
                //取消授权
                if (currentPlatName != null) {

                    Platform plat = ShareSDK.getPlatform(this, currentPlatName);
                    if (plat.isAuthValid()) {
                        plat.removeAccount();
                    }
                }
                break;
        }
        if (platform != null) {
            platform.SSOSetting(false);
            platform.setPlatformActionListener(this);
            platform.showUser(null);
        }

    }

    // --------------------------------------
    //三方登陆成功
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> map) {

        String name = platform.getName();
        currentPlatName = name;
        Log.d(TAG, "onComplete: name =>" + name);
        //遍历Map
        Iterator ite =map.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry entry = (Map.Entry)ite.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            Log.d(TAG, "onComplete: key=>" + key + " : " + "value : " + value);
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
