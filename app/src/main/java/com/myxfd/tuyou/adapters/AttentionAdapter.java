package com.myxfd.tuyou.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.model.TuYouUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by liangyue on 16/11/8.
 */

public class AttentionAdapter extends BaseAdapter {
    private Context context;
    private List<TuYouUser> users;

    public AttentionAdapter(Context context, List<TuYouUser> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        if (users != null) {
            return users.size();
        }
        return 0;
    }

    @Override
    public TuYouUser getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mine_attention, parent, false);
        }
        Object tag = convertView.getTag();
        if (tag == null) {
            ViewHolder viewHolder = new ViewHolder(convertView);
            tag = viewHolder;
            convertView.setTag(tag);
        }
        ViewHolder viewHolder = (ViewHolder) tag;
        viewHolder.bindView(users.get(position));
        return convertView;
    }
    private static class ViewHolder{
        private ImageView icon;
        private TextView name;
        private ImageView sex;
        private TextView age;
        private TextView distance;
        private Picasso picasso;

        ViewHolder(View itemView) {
            icon = (ImageView) itemView.findViewById(R.id.item_map_friends_icon);
            name = (TextView) itemView.findViewById(R.id.item_map_friends_name);
            sex = (ImageView) itemView.findViewById(R.id.item_map_friends_sex);
            age = (TextView) itemView.findViewById(R.id.item_map_friends_age);
            distance = (TextView) itemView.findViewById(R.id.item_map_friends_duration);
            picasso = Picasso.with(itemView.getContext());
        }
        void bindView(TuYouUser user){
            picasso.load(user.getIcon()).into(icon);
            name.setText(user.getNickName());
            if ("男".equals(user.getSex())) {
                sex.setImageResource(R.mipmap.sex_boy);
            }else{
                sex.setImageResource(R.mipmap.sex_girl);
            }
            age.setText(String.valueOf(user.getAge()));
            distance.setText(String.valueOf(user.getDistance()));
        }
    }
}
