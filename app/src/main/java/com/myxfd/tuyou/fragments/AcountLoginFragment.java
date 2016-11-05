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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.activity.LoginActivity;
import com.myxfd.tuyou.activity.MainActivity;
import com.myxfd.tuyou.activity.TuYouActivity;
import com.myxfd.tuyou.model.TuYouUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AcountLoginFragment extends BaseFragment implements View.OnClickListener {


    private EditText mName;
    private EditText mPassword;
    private Button mLogin;
    private static final String TAG = "AcountLoginFragment";
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
    private Context mContext;

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
        mName = (EditText) view.findViewById(R.id.fragment_Account_name);
        mPassword = (EditText) view.findViewById(R.id.fragment_Account_password);
        mLogin = (Button) view.findViewById(R.id.fragment_account_login);
        mLogin.setOnClickListener(this);
        mContext = getContext();
        return view;
    }

    @Override
    public String getFragmentTitle() {
        return "账号密码登录";
    }

    @Override
    public void onClick(View v) {
        String name = mName.getText().toString();
        String pwd = mPassword.getText().toString();
        final View tempView = v;
        TuYouUser user = new TuYouUser();
        user.setUsername(name);
        user.setPassword(pwd);
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
//
                    // 9016: 没有联网
                    // 101: 密码错误
                    // 9018: 用户名为空
                    if (e.getErrorCode() == 9016) {
                        Snackbar.make(tempView, "亲, 请检查网络 ヾ(≧O≦)〃嗷~", Snackbar.LENGTH_SHORT).show();
//                        Toast.makeText(mContext, "亲, 请检查网络 ヾ(≧O≦)〃嗷~", Toast.LENGTH_SHORT).show();
                    } else if (e.getErrorCode() == 101) {
                        Snackbar.make(tempView, "亲, 用户名密码错误!", Snackbar.LENGTH_SHORT).show();
//                        Toast.makeText(mContext, "亲, 用户名密码错误!", Toast.LENGTH_SHORT).show();
                    } else if (e.getErrorCode() == 9018) {
                        Snackbar.make(tempView, "亲, 请输入用户名", Snackbar.LENGTH_SHORT).show();
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
}
