package com.myxfd.tuyou.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.myxfd.tuyou.R;

import easeui.EaseConstant;
import easeui.ui.EaseChatFragment;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        EaseChatFragment fragment = new EaseChatFragment();
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID));
        fragment.setArguments(args);
        transaction.replace(R.id.activity_chat, fragment);
        transaction.commit();
    }
}
