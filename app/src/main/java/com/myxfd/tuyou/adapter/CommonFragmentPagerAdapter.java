package com.myxfd.tuyou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.myxfd.tuyou.fragment.LoginBaseFragment;

import java.util.List;

/**
 * Created by admin on 2016/11/1.
 */

public class CommonFragmentPagerAdapter extends FragmentPagerAdapter {

    List<LoginBaseFragment> mList;

    public CommonFragmentPagerAdapter(FragmentManager fm,List list) {
        super(fm);
        mList=list;
    }


    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getFragmentTitle();
    }
}
