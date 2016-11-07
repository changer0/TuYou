package com.myxfd.tuyou.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.myxfd.tuyou.R;
import com.myxfd.tuyou.activity.ChatActivity;
import com.myxfd.tuyou.adapters.MessageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import easeui.EaseConstant;
import easeui.ui.EaseConversationListFragment;

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
//        listView = (ListView) ret.findViewById(R.id.message_list);
//        list = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            list.add(String.format(Locale.CHINA, "测试 %03d", i+1));
//        }
//        adapter = new MessageAdapter(getContext(), list);
//        listView.setAdapter(adapter);
//        this.registerForContextMenu(listView);
        EMMessage message = EMMessage.createTxtSendMessage("aaa", "qqq");
//发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

        FragmentManager supportFragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        EaseConversationListFragment fragment = new EaseConversationListFragment();
        fragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(getContext(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
            }
        });
        transaction.replace(R.id.fragment_message, fragment);
        transaction.commit();
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
                //TODO:删除一条记录
                Log.d(TAG, "onContextItemSelected: " + "删除了一条记录");
                break;
            case 2:
                //TODO:将某一条记录置顶
                Log.d(TAG, "onContextItemSelected: " + "记录已置顶");
                break;
        }
        return super.onContextItemSelected(item);
    }
}
