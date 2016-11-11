package com.myxfd.tuyou.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.myxfd.tuyou.R;
import com.myxfd.tuyou.model.TuYouUser;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText password;
    private EditText name;
    private Button register;
    private static final String TAG = "RegisterActivity";
    private EditText password2;
    private EMClient emclient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emclient = EMClient.getInstance();
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

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setTitle("注册中");
                progressDialog.setMessage("请稍等...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                final String userName = name.getText().toString().trim();
                final String userPassword = password.getText().toString().trim();
                String userPassword2 = password2.getText().toString().trim();
                if (userPassword.equals(userPassword2)) {
                    final TuYouUser user = new TuYouUser();
                    user.setUsername(userName);
                    user.setPassword(userPassword);
                    user.signUp(new SaveListener<TuYouUser>() { //注册
                        @Override
                        public void done(TuYouUser tuYouUser, BmobException e) {
                            if (e == null) {
                                //使用RxJava控制xiancheng
                                Observable.create(new Observable.OnSubscribe<TuYouUser>() {
                                    @Override
                                    public void call(final Subscriber<? super TuYouUser> subscriber) {
                                        try {
                                            //环信注册
                                            emclient.createAccount(userName, userPassword);
                                            emclient.logout(true);
                                            //环信登陆
                                            emclient.login(userName, userPassword, new EMCallBack() {
                                                @Override
                                                public void onSuccess() {
                                                    emclient.chatManager().loadAllConversations();
                                                    emclient.groupManager().loadAllGroups();
                                                    //立即登录, 登录成功后跳转
                                                    user.login(new SaveListener<TuYouUser>() { //登录
                                                        @Override
                                                        public void done(TuYouUser tuYouUser, BmobException e) {
                                                            if (e == null) {
                                                                subscriber.onNext(tuYouUser);
                                                            } else {
                                                                subscriber.onError(e);
                                                            }
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onError(int i, String s) {
                                                    JSONObject jsonObject = getExceptionJsonStr(s, user);
                                                    HyphenateException hyphenateException = new HyphenateException(i, jsonObject.toString());
                                                    subscriber.onError(hyphenateException);
                                                }

                                                @Override
                                                public void onProgress(int i, String s) {
                                                }
                                            });

                                        } catch (HyphenateException e1) {
                                            JSONObject exceptionJson = getExceptionJsonStr(e1.getMessage(), user);
                                            subscriber.onError(new HyphenateException(e1.getErrorCode(), exceptionJson.toString()));
                                            e1.printStackTrace();

                                        }

                                    }
                                })
                                        .subscribeOn(Schedulers.io())
//
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<TuYouUser>() {
                                            @Override
                                            public void onCompleted() {
                                            }

                                            @Override
                                            public void onError(Throwable te) {
                                                progressDialog.cancel();
                                                // TODO: 2016/11/11 注册出现问题, 一定删除bmob内部的用户

                                                if (te instanceof HyphenateException) {
                                                    HyphenateException he = (HyphenateException) te;

                                                    String message = te.getMessage();
                                                    JSONObject jsonObject = null;
                                                    try {
                                                        jsonObject = new JSONObject(message);
                                                        String error = jsonObject.optString("error");
                                                        String id = jsonObject.optString("userId");
                                                        TuYouUser user = new TuYouUser();
                                                        user.setObjectId(id);
                                                        user.delete(new UpdateListener() {
                                                            @Override
                                                            public void done(BmobException e) {
                                                                if (e != null) {
                                                                    Snackbar.make(getWindow().getDecorView(), "回滚异常", Snackbar.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        Snackbar.make(getWindow().getDecorView(), "环信错误" + error, Snackbar.LENGTH_SHORT).show();
                                                    } catch (JSONException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                } else {
                                                    Snackbar.make(getWindow().getDecorView(), "未知错误", Snackbar.LENGTH_SHORT).show();
                                                }

                                            }

                                            @Override
                                            public void onNext(TuYouUser tuYouUser) {
                                                progressDialog.cancel();
                                                Intent intent = new Intent(RegisterActivity.this, TuYouActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                            } else {
                                progressDialog.cancel();
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
                    progressDialog.cancel();
                    Snackbar.make(getWindow().getDecorView(), "密码不一致,请重新输入", Snackbar.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @NonNull
    private JSONObject getExceptionJsonStr(String s, TuYouUser user) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("error", s);
            jsonObject.put("userId", user.getObjectId());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return jsonObject;
    }
}
