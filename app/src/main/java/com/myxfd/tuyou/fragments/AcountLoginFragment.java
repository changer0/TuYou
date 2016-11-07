package com.myxfd.tuyou.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.myxfd.tuyou.R;
import com.myxfd.tuyou.activity.LoginActivity;
import com.myxfd.tuyou.activity.MainActivity;
import com.myxfd.tuyou.activity.RegisterActivity;
import com.myxfd.tuyou.activity.TuYouActivity;
import com.myxfd.tuyou.model.TuYouUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AcountLoginFragment extends BaseFragment implements View.OnClickListener {


    private static final String TAG = "AcountLoginFragment";
    private EditText mName;
    private EditText mPassword;
    private TextView mTvReg;

    private Context mContext;
    private TextView mTvForget;

    public AcountLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acount_login, container, false);
        mName = (EditText) view.findViewById(R.id.fragment_account_name);
        mPassword = (EditText) view.findViewById(R.id.fragment_account_password);
        mTvReg = ((TextView) view.findViewById(R.id.fragment_account_reg));
        mTvForget = ((TextView) view.findViewById(R.id.fragment_account_forget_pwd));
        Button login = (Button) view.findViewById(R.id.fragment_account_login);
        login.setOnClickListener(this);
        mTvReg.setOnClickListener(this);
        mTvForget.setOnClickListener(this);
        mContext = getContext();
        return view;
    }

    @Override
    public String getFragmentTitle() {
        return "账号密码登录";
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_account_login:
                accountLogin(v);
                break;
            case R.id.fragment_account_reg:
                //跳转到注册页面
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);

                break;
            case R.id.fragment_account_forget_pwd:
                // TODO: 2016/11/6 忘记密码待实现
                break;
        }


    }

    private void accountLogin(View v) {
        //此时隐藏输入法
        InputMethodManager manager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            if (manager.isActive()) {
                LoginActivity activity = (LoginActivity) mContext;
                manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

        String name = mName.getText().toString();
        String pwd = mPassword.getText().toString();
        final View tempView = v;
        final TuYouUser user = new TuYouUser();
        user.setUsername(name);
        user.setPassword(pwd);
        EMClient.getInstance().logout(true);
        EMClient.getInstance().login(name, pwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().chatManager().loadAllConversations();
                EMClient.getInstance().groupManager().loadAllGroups();
                user.login(new SaveListener<TuYouUser>() {
                    @Override
                    public void done(TuYouUser tuYouUser, BmobException e) {
                        if (e == null) {
                            Log.d(TAG, "done: 登录验证通过");
                            Intent intent = new Intent(mContext, TuYouActivity.class);
                            startActivity(intent);
                            if (mContext instanceof LoginActivity) {
                                ((LoginActivity) mContext).finish();
                            }
                        } else {
                            // 9016: 没有联网
                            // 101: 密码错误
                            // 9018: 用户名为空
                            if (e.getErrorCode() == 9016) {
                                Snackbar.make(tempView, "亲, 请检查网络 ヾ(≧O≦)〃嗷~", Snackbar.LENGTH_SHORT).show();
//                        Toast.makeText(mContext, "亲, 请检查网络 ヾ(≧O≦)〃嗷~", Toast.LENGTH_SHORT).show();
                            } else if (e.getErrorCode() == 101) {
                                Snackbar.make(tempView, "亲, 用户名或密码错误!", Snackbar.LENGTH_SHORT).show();
//                        Toast.makeText(mContext, "亲, 用户名密码错误!", Toast.LENGTH_SHORT).show();
                            } else if (e.getErrorCode() == 9018) {
                                Snackbar.make(tempView, "亲, 请输入用户名或密码", Snackbar.LENGTH_SHORT).show();
//                        Toast.makeText(mContext, "亲, 请输入用户名", Toast.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(tempView, "未知错误", Snackbar.LENGTH_SHORT).show();
//                        Toast.makeText(mContext, "未知错误", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "done: 登录错误信息: " + e.getMessage() + " 错误码: " + e.getErrorCode());
                            }
                        }
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
