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

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.myxfd.tuyou.R;
import com.myxfd.tuyou.adapters.CommonFragmentPagerAdapter;
import com.myxfd.tuyou.fragments.AcountLoginFragment;
import com.myxfd.tuyou.fragments.BaseFragment;
import com.myxfd.tuyou.fragments.PhoneLoginFragment;
import com.myxfd.tuyou.model.TuYouUser;
import com.myxfd.tuyou.ndk.NativeHelper;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;


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

        BmobUser user = BmobUser.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(this, TuYouActivity.class));
        }


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
    public void onComplete(Platform platform, int i, final HashMap<String, Object> map) {
        String name = platform.getName();

        currentPlatName = name;
        Log.d(TAG, "onComplete: name =>" + name);
        switch (name) {
            case "QQ":
                final TuYouUser user = new TuYouUser();
                final String token = platform.getDb().getToken();
                user.setQqToken(token);
                //注册之前, 需要检查token是否在bmob中存在
                BmobQuery<TuYouUser> query = new BmobQuery<>();
                query.addWhereEqualTo("qqToken", token);
                query.findObjects(new FindListener<TuYouUser>() {
                    @Override
                    public void done(List<TuYouUser> list, BmobException e) {
                        if (e == null) {
                            if (list.size() > 0) {
                                //已经绑定过QQ, 直接登陆
                                TuYouUser tuyouUser = list.get(0);
                                // TODO: 2016/11/7 未完成qq的登陆问题
                                tuyouUser.setPassword(tuyouUser.getPassword());
                                //直接登陆
                                tuyouUser.login(new SaveListener<TuYouUser>() {
                                    @Override
                                    public void done(TuYouUser user, BmobException e) {
                                        if (e != null) {
                                            Snackbar.make(getWindow().getDecorView(), "登陆异常: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                        } else {
                                            EMClient.getInstance().logout(true);
                                            EMClient.getInstance().login(user.getUsername(), user.getPassword(), new EMCallBack() {
                                                @Override
                                                public void onSuccess() {
                                                    EMClient.getInstance().chatManager().loadAllConversations();
                                                    EMClient.getInstance().groupManager().loadAllGroups();
                                                }

                                                @Override
                                                public void onError(int i, String s) {

                                                }

                                                @Override
                                                public void onProgress(int i, String s) {

                                                }
                                            });
                                            Intent intent = new Intent(mContext, TuYouActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                            } else {
                                //新用戶注册并登录
                                newUserBindQQ(token, map, user);
                            }
                        } else {
                            Snackbar.make(getWindow().getDecorView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
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

    // qq登陆中用于新用户绑定登陆
    private void newUserBindQQ(String token, HashMap<String, Object> map, final TuYouUser user) {
        //新用户登陆, 绑定
        final String tuYouPwd = "mustChange";
        String username = "TuYou" + token;//登陆用户名默认为TuYou+token
        String sex = (String) map.get("gender");
        String icon = (String) map.get("figureurl_qq_1");
        user.setUsername(username);
        user.setPassword(tuYouPwd);
        user.setSex(sex);
        user.setIcon(icon);
        user.setType(currentPlatName);
        //注册
        user.signUp(new SaveListener<TuYouUser>() {
            @Override
            public void done(final TuYouUser tuYouUser, BmobException e) {
                if (e != null) {
                    // 网络异常: 9016
                    if (e.getErrorCode() == 9016) {
                        Snackbar.make(getWindow().getDecorView(), "亲, 请连接网络 ヾ(≧O≦)〃嗷~", Snackbar.LENGTH_SHORT).show();
                    } else if (e.getErrorCode() == 304) {
                        Snackbar.make(getWindow().getDecorView(), "亲, 用户名密码不能为空....", Snackbar.LENGTH_SHORT).show();
                    } else if (e.getErrorCode() == 202) {
                        Snackbar.make(getWindow().getDecorView(), "该用户名已经注册, 请重试..", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(getWindow().getDecorView(), "未知错误", Snackbar.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "done: message: " + e.getMessage() + " code:" + e.getErrorCode());
                } else {
                    //直接登陆
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().logout(true);
                                EMClient.getInstance().createAccount(tuYouUser.getUsername(), tuYouUser.getPassword());
                                EMClient.getInstance().login(tuYouUser.getUsername(), tuYouUser.getPassword(), new EMCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        EMClient.getInstance().chatManager().loadAllConversations();
                                        EMClient.getInstance().groupManager().loadAllGroups();
                                    }

                                    @Override
                                    public void onError(int i, String s) {

                                    }

                                    @Override
                                    public void onProgress(int i, String s) {

                                    }
                                });
                            } catch (HyphenateException e1) {
                                e1.printStackTrace();

                            }
                        }
                    }.start();
                    user.login(new SaveListener<TuYouUser>() {
                        @Override
                        public void done(TuYouUser user, BmobException e) {
                            if (e != null) {
                                Snackbar.make(getWindow().getDecorView(), "登陆异常: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(mContext, TuYouActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
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
