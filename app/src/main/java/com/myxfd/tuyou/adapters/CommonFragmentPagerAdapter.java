package com.myxfd.tuyou.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.myxfd.tuyou.fragments.BaseFragment;

import java.util.List;

/**
 * Created by Lucky on 2016/11/1.
 */

public class CommonFragmentPagerAdapter extends FragmentPagerAdapter {
    public List<BaseFragment> fragments;

    public CommonFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm);
        fragments = fragmentList;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getFragmentTitle();
    }
}
