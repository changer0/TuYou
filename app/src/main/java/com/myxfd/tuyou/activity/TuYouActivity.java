package com.myxfd.tuyou.activity;


import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.adapters.CommonFragmentPagerAdapter;
import com.myxfd.tuyou.fragments.BaseFragment;
import com.myxfd.tuyou.fragments.CircleFragment;
import com.myxfd.tuyou.fragments.MapFragment;
import com.myxfd.tuyou.fragments.MessageFragment;
import com.myxfd.tuyou.fragments.MineFragment;
import com.myxfd.tuyou.widgets.ViewPagerCompat;


import java.util.ArrayList;
import java.util.List;

public class TuYouActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

    private FragmentManager manager;
    private ViewPagerCompat pager;
    private CommonFragmentPagerAdapter adapter;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_you);
        pager = (ViewPagerCompat) findViewById(R.id.main_container);


        //添加ViewPager
        manager = getSupportFragmentManager();
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new MessageFragment());
        fragments.add(new MapFragment());
        fragments.add(new CircleFragment());
        fragments.add(new MineFragment());

        adapter = new CommonFragmentPagerAdapter(manager, fragments);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(this);


        mRadioGroup = (RadioGroup) findViewById(R.id.main_rg);

        mRadioGroup.setOnCheckedChangeListener(this);
        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);

    }


    // -------------------------------------
    // Viewpager的监听回调
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        View childAt = mRadioGroup.getChildAt(position);
        if (childAt instanceof RadioButton) {
            RadioButton radioButton = (RadioButton) childAt;
            radioButton.setChecked(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    // ------------------------------------------
    // RadioGroup的回调监听
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_rb_msg:
                pager.setCurrentItem(0);
                break;
            case R.id.main_rb_map:
                pager.setCurrentItem(1);
                break;

            case R.id.main_rb_circle:

                pager.setCurrentItem(2);
                break;
            case R.id.main_rb_mine:
                pager.setCurrentItem(3);
                break;


        }
    }
}
