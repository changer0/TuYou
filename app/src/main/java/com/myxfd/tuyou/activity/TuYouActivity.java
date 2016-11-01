package com.myxfd.tuyou.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.amap.api.maps2d.SupportMapFragment;
import com.myxfd.tuyou.R;
import com.myxfd.tuyou.fragments.CircleFragment;
import com.myxfd.tuyou.fragments.MapFragment;
import com.myxfd.tuyou.fragments.MessageFragment;
import com.myxfd.tuyou.fragments.MineFragment;

public class TuYouActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private Fragment circleFragment, mineFragment, mapFragment, messageFragment;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_you);
        manager = getSupportFragmentManager();

        messageFragment = new MessageFragment();
        mapFragment = new MapFragment();
        circleFragment = new CircleFragment();
        mineFragment = new MineFragment();
        manager.beginTransaction().add(R.id.main_container, messageFragment).commit();

        //设置默认选中
        RadioButton rbMessage = (RadioButton) findViewById(R.id.main_rb_msg);
        rbMessage.setChecked(true);

        //group的点击事件
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.main_rg);
        if (radioGroup != null){
            radioGroup.setOnCheckedChangeListener(this);
        }

    }

    //===========================RadioGroup回调方法======================================================
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction ft = manager.beginTransaction();
        switch (checkedId) {
            case R.id.main_rb_msg:
                ft.replace(R.id.main_container, messageFragment, "msg");
                break;
            case R.id.main_rb_map:
                ft.replace(R.id.main_container, mapFragment, "map");
                break;
            case R.id.main_rb_circle:
                ft.replace(R.id.main_container, circleFragment, "circle");
                break;
            case R.id.main_rb_mine:
                ft.replace(R.id.main_container, mineFragment, "mine");
                break;
        }
        ft.commit();
    }
}
