package com.myxfd.tuyou.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.model.TuYouRelation;
import com.myxfd.tuyou.model.TuYouUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Lulu on 2016/11/3.
 */

public class MapFriendsAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<TuYouUser> mUsers;
    private OnItemClick mOnItemClick;
    private BmobUser currentBmobUser;
    private static final String TAG = "MapFriendsAdapter";

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public MapFriendsAdapter(Context context, List<TuYouUser> users) {
        mContext = context;
        mUsers = users;
        currentBmobUser = BmobUser.getCurrentUser();
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            if (holder instanceof MapViewHolder) {
                MapViewHolder viewHolder = (MapViewHolder) holder;
                viewHolder.bindView(position, mUsers.get(position),currentBmobUser,  this);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_map_friends, parent, false);
        return new MapViewHolder(itemView);
    }

    private static class MapViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mHi;
        private MapFriendsAdapter mAdapter;

        private ImageView mAdd;
        private TextView mTvAge;
        private TextView mTvDistance;
        private ImageView mImgIcon;
        private TextView mName;
        private ImageView mSex;

        public MapViewHolder(View itemView) {
            super(itemView);
            mAdd = ((ImageView) itemView.findViewById(R.id.item_map_friends_add));
            mTvAge = ((TextView) itemView.findViewById(R.id.item_map_friends_age));
            mTvDistance = ((TextView) itemView.findViewById(R.id.item_map_friends_duration));
            mImgIcon = ((ImageView) itemView.findViewById(R.id.item_map_friends_icon));
            mName = ((TextView) itemView.findViewById(R.id.item_map_friends_name));
            mSex = ((ImageView) itemView.findViewById(R.id.item_map_friends_sex));
            mHi = ((ImageView) itemView.findViewById(R.id.item_map_friends_hi));
        }

        public void bindView(int position, TuYouUser user, BmobUser currentBmobUser, MapFriendsAdapter adapter) {
            mAdapter = adapter;
            mTvAge.setText(String.valueOf(user.getAge()));
            mTvDistance.setText(String.valueOf(user.getDistance()) + " 米");
            mName.setText(user.getNickName());
            Picasso.with(mImgIcon.getContext()).load(user.getIcon()).config(Bitmap.Config.ARGB_8888).transform(new CircleTransform()).into(mImgIcon);
            String sex = user.getSex();
            if ("男".equals(sex)) {
                mSex.setImageResource(R.mipmap.sex_boy);
            } else if ("女".equals(sex)) {
                mSex.setImageResource(R.mipmap.sex_girl);
            } else {
                //不知道
            }
            mAdd.setTag(user);
            mAdd.setOnClickListener(this);
            mHi.setTag(user);
            mHi.setOnClickListener(this);
            //用于点击后传递当前的User
            mAdd.setTag(R.id.tag_user, user);
            mAdd.setTag(R.id.tag_add_img, mAdd);

            //设置是否加关注?
            BmobQuery<TuYouRelation> query = new BmobQuery<>();
            query.addWhereEqualTo("fromUser", currentBmobUser.getObjectId());
            query.addWhereEqualTo("toUser", user.getObjectId());
            query.findObjects(new FindListener<TuYouRelation>() {
                @Override
                public void done(List<TuYouRelation> list, BmobException e) {
                    if (e == null) {
                        if (list != null && list.size() > 0) {
                            mAdd.setImageResource(R.mipmap.ic_attention);
                        } else {
                            mAdd.setImageResource(R.mipmap.ic_no_attention);
                        }
                    } else {
                        Log.d(TAG, "done: MapFriendsAdapter有问题");
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_map_friends_add:
                    if (mAdapter != null) {
                        if (mAdapter.mOnItemClick != null) {
                            mAdapter.mOnItemClick.onItemClick(v);
                        }
                    }
                    break;
                case R.id.item_map_friends_hi:
                    if (mAdapter != null) {
                        if (mAdapter.mOnItemClick != null) {
                            mAdapter.mOnItemClick.onItemMsgClick(v);
                        }
                    }
                    break;
            }
        }
    }

    public interface OnItemClick {
        void onItemClick(View view);
        void onItemMsgClick(View view);
    }
}
