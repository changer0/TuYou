package com.myxfd.tuyou.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.model.TuYouUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText password;
    private EditText name;
    private Button register;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        name = (EditText) findViewById(R.id.reg_et_username);
        password = (EditText) findViewById(R.id.reg_et_password);
        register = (Button) findViewById(R.id.reg_btn_register);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String Uname = name.getText().toString();
        String Upwd = password.getText().toString();
        TuYouUser user = new TuYouUser();
        user.setUsername(Uname);
        user.setPassword(Upwd);
        user.setEmail("463430762@qq.com");
        user.signUp(new SaveListener<TuYouUser>() {
            @Override
            public void done(TuYouUser tuYouUser, BmobException e) {
                if (e == null) {
                    Log.d(TAG, "done: 注册成功");
                }else {
                    Log.d(TAG, "done: "+e);
                }
            }
        });
    }
}
