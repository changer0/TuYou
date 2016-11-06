package com.myxfd.tuyou.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myxfd.tuyou.R;

import java.util.List;

/**
 * Created by Lucky on 2016/11/1.
 */

public class MessageAdapter extends BaseAdapter{
    private List<String> list;
    private Context context;
    private LayoutInflater inflater;

    public MessageAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_message, parent, false);
            convertView.setTag(new MyViewHolder(convertView));
        }
        MyViewHolder holder = (MyViewHolder) convertView.getTag();
        holder.itemContent.setText(list.get(position));
        return convertView;
    }

    private static class MyViewHolder{

        private ImageView itemIcon;
        private TextView itemUserName;
        private TextView itemContent;
        private TextView itemTime;

        public MyViewHolder(View view){
            itemIcon = (ImageView) view.findViewById(R.id.item_message_iv_icon);
            itemUserName = (TextView) view.findViewById(R.id.item_message_tv_username);
            itemContent = (TextView) view.findViewById(R.id.item_message_tv_content);
            itemTime = (TextView) view.findViewById(R.id.item_message_tv_time);
        }
    }
}
