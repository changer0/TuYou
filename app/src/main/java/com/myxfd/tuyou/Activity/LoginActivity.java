package com.myxfd.tuyou.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.myxfd.tuyou.R;
import com.myxfd.tuyou.adapter.CommonFragmentPagerAdapter;
import com.myxfd.tuyou.fragment.AcountLoginFragment;
import com.myxfd.tuyou.fragment.LoginBaseFragment;
import com.myxfd.tuyou.fragment.PhoneLoginFragment;
import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {
    
    private static final String TAG = "LoginActivity";
    private ArrayList<LoginBaseFragment> mArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.login_tabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.login_viewPager);
        tabLayout.setupWithViewPager(viewPager);

        mArrayList = new ArrayList<>();
        mArrayList.add(new AcountLoginFragment());
        mArrayList.add(new PhoneLoginFragment());
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), mArrayList);
        viewPager.setAdapter(adapter);
    }

}
