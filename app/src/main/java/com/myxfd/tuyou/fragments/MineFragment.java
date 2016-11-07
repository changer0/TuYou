package com.myxfd.tuyou.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.activity.MineAboutActivity;
import com.myxfd.tuyou.activity.MineAttentionActivity;
import com.myxfd.tuyou.activity.MineSettingActivity;
import com.myxfd.tuyou.activity.MineUserActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {


    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTitle() {
        return "我的";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_mine, container, false);
        CardView cardView1 = (CardView) ret.findViewById(R.id.mine_cv_1);
        CardView cardView2 = (CardView) ret.findViewById(R.id.mine_cv_2);
        CardView cardView4 = (CardView) ret.findViewById(R.id.mine_cv_4);
        CardView cardView5 = (CardView) ret.findViewById(R.id.mine_cv_5);


        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView4.setOnClickListener(this);
        cardView5.setOnClickListener(this);


        return ret;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.mine_cv_1:
                //用户名点击
                Intent intent = new Intent(getContext(), MineUserActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_cv_2:
                //关注的人列表
                intent = new Intent(getContext(), MineAttentionActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_cv_4:
                //关于图友
                intent = new Intent(getContext(), MineAboutActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_cv_5:
                //设置
                intent = new Intent(getContext(), MineSettingActivity.class);
                startActivity(intent);
                break;
        }

    }
}
