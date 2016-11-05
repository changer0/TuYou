package com.myxfd.tuyou.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.adapters.WelcomePagerAdapter;

import java.util.ArrayList;

import okhttp3.internal.framed.Variant;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private Handler mHandler= new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            int what = msg.what;
            if (what==998) {
                Intent intent = new Intent(mContext, TuYouActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        SharedPreferences app = getSharedPreferences("app", MODE_PRIVATE);
        int first = app.getInt("first", 0);

        if (first != 0) {
            setContentView(R.layout.activity_welcome2);
            mHandler.sendEmptyMessageDelayed(998,2000);
        } else {
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
        Intent intent = new Intent(this, TuYouActivity.class);
        startActivity(intent);
        finish();
    }

}
