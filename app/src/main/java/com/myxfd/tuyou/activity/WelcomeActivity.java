package com.myxfd.tuyou.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.myxfd.tuyou.R;
import com.myxfd.tuyou.adapters.WelcomePagerAdapter;
import com.myxfd.tuyou.model.TuYouUser;

import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import okhttp3.internal.framed.Variant;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String LOGIN_STATE = "state";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        SharedPreferences app = getSharedPreferences("app", MODE_PRIVATE);
        int first = app.getInt("first", 0);

        if (first != 0) {
            setContentView(R.layout.activity_welcome2);

            BmobUser user = BmobUser.getCurrentUser();
            if (user != null) {
                BmobQuery<TuYouUser> query = new BmobQuery<>();
                query.getObject(user.getObjectId(), new QueryListener<TuYouUser>() {
                    @Override
                    public void done(TuYouUser tuYouUser, BmobException e) {
                        if (e == null) {
                            //登陆环信

                            EMClient.getInstance().login(tuYouUser.getUsername(), tuYouUser.getPassword(), new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    startActivity(new Intent(WelcomeActivity.this, TuYouActivity.class));
                                    finish();
                                }

                                @Override
                                public void onError(int i, String s) {
                                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                                    intent.putExtra(LOGIN_STATE, s);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onProgress(int i, String s) {

                                }
                            });


                        } else {
                            // TODO: 2016/11/11 Toast当前网络无响应
                            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                            intent.putExtra(LOGIN_STATE, e.getMessage());
                            startActivity(intent);
                            finish();
                        }
                    }
                });

            } else {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }


        } else {
            //第一次
            setContentView(R.layout.activity_welcome);
            ViewPager pager = (ViewPager) findViewById(R.id.welcome_pager);
            ArrayList<Integer> list = new ArrayList<>();
            list.add(R.mipmap.wel1);
            list.add(R.mipmap.wel2);
            list.add(R.mipmap.wel3);
            WelcomePagerAdapter adapter = new WelcomePagerAdapter(this, list, this);
            pager.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View v) {
        SharedPreferences app = getSharedPreferences("app", MODE_PRIVATE);
        SharedPreferences.Editor edit = app.edit();
        edit.putInt("first", 1);
        edit.apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
