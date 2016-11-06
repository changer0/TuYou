package com.myxfd.tuyou.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    private EditText password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        name = (EditText) findViewById(R.id.reg_et_username);
        password = (EditText) findViewById(R.id.reg_et_password);
        password2 = (EditText) findViewById(R.id.reg_et_password2);
        register = (Button) findViewById(R.id.reg_btn_register);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_btn_register:
                String userName = name.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                String userPassword2 = password2.getText().toString().trim();
                if (userPassword.equals(userPassword2)) {
                    TuYouUser user = new TuYouUser();
                    user.setUsername(userName);
                    user.setPassword(userPassword);
                    final TuYouUser tempUser = user;
                    user.signUp(new SaveListener<TuYouUser>() { //注册
                        @Override
                        public void done(TuYouUser tuYouUser, BmobException e) {
                            if (e == null) {
                                //立即登录, 登录成功后跳转

                                tempUser.login(new SaveListener<TuYouUser>() { //登录
                                    @Override
                                    public void done(TuYouUser tuYouUser, BmobException e) {
                                        if (e == null) {
                                            Intent intent = new Intent(RegisterActivity.this, TuYouActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Snackbar.make(getWindow().getDecorView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
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
                            }
                        }
                    });
                } else {
                    Snackbar.make(getWindow().getDecorView(), "密码不一致,请重新输入", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
