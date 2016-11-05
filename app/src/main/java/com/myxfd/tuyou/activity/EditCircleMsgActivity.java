package com.myxfd.tuyou.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.model.TuYouTrack;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class EditCircleMsgActivity extends AppCompatActivity {

    private EditText mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_circle_msg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_circle_msg_toolbar);
        mContent = (EditText) findViewById(R.id.edit_circle_msg_content);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.circle_option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            case R.id.circle_option_menu_send:
                BmobUser currentUser = BmobUser.getCurrentUser();
                String objId = currentUser.getObjectId();
                String content = mContent.getText().toString();
                TuYouTrack tuYouTrack = new TuYouTrack();
                tuYouTrack.setUser(objId);
                tuYouTrack.setText(content);
                tuYouTrack.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e != null) {
                            Toast.makeText(EditCircleMsgActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }else {
                           finish();
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
