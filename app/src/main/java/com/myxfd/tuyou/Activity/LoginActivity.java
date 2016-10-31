package com.myxfd.tuyou.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.model.TuYouUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private EditText password;
    private Button btnLogin;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        username = (EditText) findViewById(R.id.login_et_username);
        password = (EditText) findViewById(R.id.login_et_password);
        btnLogin = (Button) findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name = username.getText().toString();
        String pwd = password.getText().toString();
        TuYouUser user = new TuYouUser();
        user.setUsername(name);
        user.setPassword(pwd);
        user.login(new SaveListener<TuYouUser>() {
            @Override
            public void done(TuYouUser tuYouUser, BmobException e) {
                if (e == null) {
                    Log.d(TAG, "done: 登录成功");
                }else {
                    Log.d(TAG, "done: "+e);
                }
            }
        });
    }

}
