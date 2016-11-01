package com.myxfd.tuyou.activity;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.adapters.CommonFragmentPagerAdapter;
import com.myxfd.tuyou.fragments.BaseFragment;
import com.myxfd.tuyou.fragments.CircleFragment;
import com.myxfd.tuyou.fragments.MapFragment;
import com.myxfd.tuyou.fragments.MessageFragment;
import com.myxfd.tuyou.fragments.MineFragment;

import java.util.ArrayList;
import java.util.List;

public class TuYouActivity extends AppCompatActivity {
    private Fragment circleFragment, mineFragment, mapFragment, messageFragment;
    private FragmentManager manager;
    private ViewPager pager;
    private CommonFragmentPagerAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_you);
        pager = (ViewPager) findViewById(R.id.main_container);
        tabLayout = (TabLayout) findViewById(R.id.main_ty);

        //添加ViewPager
        manager = getSupportFragmentManager();
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new MessageFragment());
        fragments.add(new MapFragment());
        fragments.add(new CircleFragment());
        fragments.add(new MineFragment());
        //设置tabLayout

        adapter = new CommonFragmentPagerAdapter(manager, fragments);
        pager.setAdapter(adapter);
        //让tabLayout与ViewPager联动
        tabLayout.setupWithViewPager(pager);
    }

}
