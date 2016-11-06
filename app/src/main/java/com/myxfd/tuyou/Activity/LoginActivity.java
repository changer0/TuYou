package com.myxfd.tuyou.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.adapters.CommonFragmentPagerAdapter;
import com.myxfd.tuyou.fragments.AcountLoginFragment;
import com.myxfd.tuyou.fragments.BaseFragment;
import com.myxfd.tuyou.fragments.PhoneLoginFragment;
import com.myxfd.tuyou.model.TuYouUser;
import com.myxfd.tuyou.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;


public class LoginActivity extends AppCompatActivity implements PlatformActionListener, View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private ArrayList<BaseFragment> mArrayList;
    private String currentPlatName;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.login_tabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.login_viewPager);
        tabLayout.setupWithViewPager(viewPager);

        mArrayList = new ArrayList<>();
        //添加账户登录和手机号快捷登陆的两个Fragment
        mArrayList.add(new AcountLoginFragment());
        mArrayList.add(new PhoneLoginFragment());
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), mArrayList);
        viewPager.setAdapter(adapter);

        TextView qqBtn = (TextView) findViewById(R.id.other_login_btn_qq);
        TextView sinaBtn = (TextView) findViewById(R.id.other_login_btn_sina);

        qqBtn.setOnClickListener(this);
        sinaBtn.setOnClickListener(this);

    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> map) {
        String name = platform.getName();

        currentPlatName = name;
        Log.d(TAG, "onComplete: name =>" + name);
        switch (name) {
            case "QQ" :
                TuYouUser user = new TuYouUser();
                String tuYouPwd = "mustChange";
                String username = "TuYou" + platform.getDb().getToken();


                String sex = (String) map.get("gender");
                String icon = (String) map.get("figureurl_qq_1");
                user.setUsername(username);
                user.setPassword(tuYouPwd);
                user.setSex(sex);
                user.setIcon(icon);

                user.setType(currentPlatName);

                user.signUp(new SaveListener<TuYouUser>() {
                    @Override
                    public void done(TuYouUser tuYouUser, BmobException e) {
                        if (e != null) {
                            Log.d(TAG, "done1: " + e);
                            int errorCode = e.getErrorCode();
                            if (errorCode == 202) {
                                Log.d(TAG, "done: 当前QQ已经注册过了");
                                Intent intent = new Intent(mContext, TuYouActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // TODO: 2016/11/6 处理未知异常

                            }
                        } else {
                            Intent intent = new Intent(mContext, TuYouActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });


                break;

            case "SinaWeibo":
                Snackbar.make(getWindow().getDecorView(), "程序猿正在努力完成中......", Snackbar.LENGTH_SHORT).show();
                break;
        }
        //遍历Map
        Iterator ite = map.entrySet().iterator();
        while (ite.hasNext()) {
            Log.d(TAG, "onComplete:  ");
            Map.Entry entry = (Map.Entry) ite.next();
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
            // TODO: 2016/11/5 取消授权, 在登出中使用
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
}
