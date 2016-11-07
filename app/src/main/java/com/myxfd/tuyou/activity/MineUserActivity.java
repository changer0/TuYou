package com.myxfd.tuyou.activity;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.model.TuYouUser;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class MineUserActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MineUserActivity";
    private TextView mSetSex;
    private BmobUser mCurrentUser;
    private TextView mSetAge;
    private TuYouUser mTuYouUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_user);

        initView();//

    }

    private void initView() {
        mSetSex = (TextView) findViewById(R.id.user_tv_setSex);
        mSetAge = (TextView) findViewById(R.id.user_tv_setAge);


        mCurrentUser = BmobUser.getCurrentUser();
        BmobQuery<TuYouUser> query = new BmobQuery<>();
        query.getObject(mCurrentUser.getObjectId(), new QueryListener<TuYouUser>() {
            @Override
            public void done(TuYouUser tuYouUser, BmobException e) {
                if (e == null) {
                    mTuYouUser = tuYouUser;
                    mSetSex.setText(tuYouUser.getSex());
                    mSetAge.setText(String.valueOf(tuYouUser.getAge()));
                } else {
                    Log.d(TAG, "done: e:" + e.getMessage());
                }
            }
        });


        CardView cardViewIcon = (CardView) findViewById(R.id.user_cv_icon);
        CardView cardViewUsername = (CardView) findViewById(R.id.user_cv_username);
        CardView cardViewSex = (CardView) findViewById(R.id.user_cv_sex);
        CardView cardViewAge = (CardView) findViewById(R.id.user_cv_age);
        CardView cardViewSign = (CardView) findViewById(R.id.user_cv_sign);

        cardViewIcon.setOnClickListener(this);
        cardViewUsername.setOnClickListener(this);
        cardViewSex.setOnClickListener(this);
        cardViewAge.setOnClickListener(this);
        cardViewSign.setOnClickListener(this);
    }

    private void dialogSex() {
        final String[] items = {"男", "女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改性别");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                    case 0:
                        updateSex(items[0]);
                        break;
                    case 1:
                        updateSex(items[1]);
                        break;
                }
            }
        });
        builder.create().show();
    }

    // 用于更新性别
    private void updateSex(final String sex) {
        TuYouUser tuYouUser = new TuYouUser();
        tuYouUser.setSex(sex);
        tuYouUser.update(mCurrentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mSetSex.setText(sex);
                    Snackbar.make(getWindow().getDecorView(), "修改成功", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(getWindow().getDecorView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dialogAge() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改年龄");
        final EditText newAge = new EditText(this);

        if (mTuYouUser != null) {
            int age = mTuYouUser.getAge();
            newAge.setText(String.valueOf(age));
        }
        builder.setView(newAge);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (newAge != null) {
                    final String ageStr = newAge.getText().toString().trim();
                    int age = Integer.valueOf(ageStr);
                    if (age > 0 && age < 150) {
                        TuYouUser tuYouUser = new TuYouUser();
                        tuYouUser.setAge(age);
                        tuYouUser.update(mCurrentUser.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    mSetAge.setText(ageStr);
                                    Snackbar.make(getWindow().getDecorView(), "修改成功", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(getWindow().getDecorView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Snackbar.make(getWindow().getDecorView(), "我靠!! 你年龄超了!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.user_cv_sex:
                dialogSex();
                break;
            case R.id.user_cv_age:
                dialogAge();

                break;

        }


    }
}
