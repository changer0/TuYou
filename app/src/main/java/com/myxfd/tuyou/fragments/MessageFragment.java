package com.myxfd.tuyou.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.adapters.MessageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends BaseFragment {
    private List<String> list;
    private MessageAdapter adapter;
    private ListView listView;
    private static final String TAG = "MessageFragment";


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
        this.registerForContextMenu(listView);
        return ret;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Message");
        menu.setHeaderIcon(R.mipmap.icon);
        menu.add(1, 1, 1, "del");
        menu.add(1, 2, 1, "setTop");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                //删除
                Log.d(TAG, "onContextItemSelected: " + "删除了一条记录");
                break;
            case 2:
                //置顶
                Log.d(TAG, "onContextItemSelected: " + "记录已置顶");
                break;
        }
        return super.onContextItemSelected(item);
    }
}
