package com.myxfd.tuyou.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myxfd.tuyou.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment {


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTitle() {
        return "地图";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

}
