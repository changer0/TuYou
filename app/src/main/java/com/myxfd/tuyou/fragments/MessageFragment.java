package com.myxfd.tuyou.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.myxfd.tuyou.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends BaseFragment {
    private List<String> list;
    private ArrayAdapter adapter;
    private ListView listView;


    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTitle() {
        return "消息";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_message, container, false);
        listView = (ListView) ret.findViewById(R.id.message_list);


        return ret;
    }


}
