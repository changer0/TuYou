package com.myxfd.tuyou.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.adapters.MessageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends BaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private List<String> list;
    private MessageAdapter adapter;
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
        list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(String.format(Locale.CHINA, "测试 %03d", i+1));
        }
        adapter = new MessageAdapter(getContext(), list);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        return ret;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
//        popupMenu.getMenuInflater().inflate(R.menu.item_message_menu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
