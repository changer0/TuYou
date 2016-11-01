package com.myxfd.tuyou.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.activity.MainActivity;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhoneLoginFragment extends LoginBaseFragment implements View.OnClickListener {


    private EditText mPhoneNumber;
    private Button mBtnLogin;
    private Button mBtnGetMsg;
    private EditText mReceiveMsg;
    private Handler mHandler;
    private Context mContext;
    private static final String TAG = "PhoneLoginFragment";
    public PhoneLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getContext();
        mHandler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        int i = msg.arg1;
                        Log.d(TAG, "dispatchMessage: "+i);
                        mBtnGetMsg.setText(String.format("%ds重新获取",i));
                        if (1==i){
                            mBtnGetMsg.setText("重新获取");
                            mBtnGetMsg.setBackgroundColor(Color.YELLOW);
                        }
                        break;
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phone_login, container, false);
        mPhoneNumber = (EditText) view.findViewById(R.id.fragment_Phone_phoneNumber);
        mBtnLogin = ((Button) view.findViewById(R.id.fragment_Phone_login));
        mBtnGetMsg = ((Button) view.findViewById(R.id.fragment_Phone_sendSms));
        mReceiveMsg = ((EditText) view.findViewById(R.id.fragment_Phone_receiveSms));
        mBtnGetMsg.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_Phone_sendSms:
                String number = mPhoneNumber.getText().toString().trim();
                BmobSMS.requestSMSCode(number, "sms", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e != null) {
                            Log.d(TAG, "done: 发送失败");
                        } else {
                            // TODO: 2016/11/1 发送成功
                            Log.d(TAG, "done: " + integer);
                            mBtnGetMsg.setClickable(false);
                            mBtnGetMsg.setBackgroundColor(Color.GRAY);
                            Thread thread = new Thread(){
                                @Override
                                public void run() {
                                    for (int i = 60; i > 0; i--) {
                                        Message message = mHandler.obtainMessage(1);
                                        message.arg1=i;
                                        mHandler.sendMessage(message);
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            };
                            thread.start();
                        }
                    }
                });
                break;

            case R.id.fragment_Phone_login:
                BmobSMS.verifySmsCode(mPhoneNumber.getText().toString(), mReceiveMsg.getText().toString(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null) {
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "done: "+e);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public String getFragmentTitle() {
        return "手机号快捷登录";
    }

}
