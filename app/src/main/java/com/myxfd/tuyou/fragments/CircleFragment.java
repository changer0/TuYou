package com.myxfd.tuyou.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Button;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.activity.EditCircleMsgActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CircleFragment extends BaseFragment implements View.OnClickListener {


    private View mWebView;

    public CircleFragment() {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTitle() {
        return "圈子";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        Button button = (Button) view.findViewById(R.id.circle_sendShuoShuo);
        button.setOnClickListener(this);
        mWebView = view.findViewById(R.id.circle_webView);
        //开启js支持

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), EditCircleMsgActivity.class);
        startActivity(intent);
    }
}
