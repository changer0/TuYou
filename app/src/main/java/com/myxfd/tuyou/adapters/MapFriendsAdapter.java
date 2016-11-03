package com.myxfd.tuyou.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.model.TuYouUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Lulu on 2016/11/3.
 */

public class MapFriendsAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<TuYouUser> mUsers;
    private OnItemClick mOnItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public MapFriendsAdapter(Context context, List<TuYouUser> users) {
        mContext = context;
        mUsers = users;
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
                viewHolder.bindView(position, mUsers.get(position), this);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_map_friends, parent, false);
        return new MapViewHolder(itemView);
    }

    private static class MapViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MapFriendsAdapter mAdapter;

        private Button mBtnAdd;
        private TextView mTvAge;
        private TextView mTvDuration;
        private ImageView mImgIcon;
        private TextView mName;
        private ImageView mSex;

        public MapViewHolder(View itemView) {
            super(itemView);
            mBtnAdd = ((Button) itemView.findViewById(R.id.item_map_friends_add));
            mTvAge = ((TextView) itemView.findViewById(R.id.item_map_friends_age));
            mTvDuration = ((TextView) itemView.findViewById(R.id.item_map_friends_duration));
            mImgIcon = ((ImageView) itemView.findViewById(R.id.item_map_friends_icon));
            mName = ((TextView) itemView.findViewById(R.id.item_map_friends_name));
            mSex = ((ImageView) itemView.findViewById(R.id.item_map_friends_sex));
        }

        public void bindView(int position, TuYouUser user, MapFriendsAdapter adapter) {
            mAdapter = adapter;
            mTvAge.setText(String.valueOf(user.getAge()));
            mTvDuration.setText(String.valueOf(user.getDistance()));
            mName.setText(user.getUsername());
            Picasso.with(mImgIcon.getContext()).load(user.getIcon()).config(Bitmap.Config.ARGB_4444).into(mImgIcon);
            String sex = user.getSex();
            if (sex.equals("男")) {
                mSex.setImageResource(R.mipmap.sex_boy);
            } else if (sex.equals("女")) {
                mSex.setImageResource(R.mipmap.sex_girl);
            } else {
                //不知道
            }
            mBtnAdd.setOnClickListener(this);
            //用于点击后传递
            mBtnAdd.setTag(user);
        }

        @Override
        public void onClick(View v) {
            if (mAdapter != null) {
                mAdapter.mOnItemClick.onItemClick(v);
            }
        }
    }

    public interface OnItemClick {
        void onItemClick(View view);
    }
}
