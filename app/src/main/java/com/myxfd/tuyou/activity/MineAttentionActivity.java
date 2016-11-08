package com.myxfd.tuyou.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.model.TuYouRelation;
import com.myxfd.tuyou.model.TuYouUser;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import easeui.EaseConstant;

public class MineAttentionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<String> strings;
    private List<TuYouRelation> addAll;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10086:
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 10010:
                    Snackbar.make(getWindow().getDecorView(), ((String) msg.obj) + msg.arg1, Snackbar.LENGTH_LONG).show();
                    break;
                case 10011:
                    Intent intent = new Intent(MineAttentionActivity.this, ChatActivity.class);
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, ((String) msg.obj));
                    startActivity(intent);
                    break;
            }
        }
    };
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_attention);
        ListView listView = (ListView) findViewById(R.id.activity_mine_attention);
        strings = new ArrayList<>();
        addAll = new ArrayList<>();
        adapter = new ArrayAdapter<>(MineAttentionActivity.this, android.R.layout.simple_expandable_list_item_1, strings);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        BmobUser currentUser = BmobUser.getCurrentUser();
        String currentUserObjectId = currentUser.getObjectId();
        Log.d("AAA", "onCreate: " + currentUserObjectId);
        BmobQuery<TuYouRelation> tuYouRelationBmobQuery = new BmobQuery<>();
        tuYouRelationBmobQuery.addWhereEqualTo("fromUser", currentUserObjectId);
        tuYouRelationBmobQuery.findObjects(new FindListener<TuYouRelation>() {
            @Override
            public void done(List<TuYouRelation> list, BmobException e) {
                if (e == null) {
                    Log.d("AAA", "done: " + list.size());
                    addAll.clear();
                    addAll.addAll(list);
                    strings.clear();
                    for (TuYouRelation tuYouRelation : addAll) {
                        TuYouUser toUser = tuYouRelation.getToUser();
                        BmobQuery<TuYouUser> tuYouUserBmobQuery = new BmobQuery<>();
                        String objectId = toUser.getObjectId();
                        Log.d("AAA", "done: " + objectId);
                        tuYouUserBmobQuery.getObject(toUser.getObjectId().trim(), new QueryListener<TuYouUser>() {
                            @Override
                            public void done(TuYouUser user, BmobException e) {
                                if (e == null) {
                                    strings.add(user.getUsername());
                                    handler.sendEmptyMessage(10086);
                                }else {
                                    Message message = handler.obtainMessage(10010);
                                    message.obj = e.getMessage();
                                    message.arg1 = e.getErrorCode();
                                    message.sendToTarget();
                                }
                            }
                        });
                    }
                }else{
                    Log.d("AAA", "done: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String objectId = addAll.get(position).getToUser().getObjectId();
        BmobQuery<TuYouUser> tuYouUserBmobQuery = new BmobQuery<>();
        tuYouUserBmobQuery.getObject(objectId.trim(), new QueryListener<TuYouUser>() {
            @Override
            public void done(TuYouUser user, BmobException e) {
                if (e == null) {
                    Message message = handler.obtainMessage(10011);
                    message.obj = user.getUsername();
                    message.sendToTarget();
                }
            }
        });

    }
}
