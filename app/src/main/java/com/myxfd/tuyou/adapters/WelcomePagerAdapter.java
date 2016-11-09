package com.myxfd.tuyou.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.myxfd.tuyou.R;
import com.myxfd.tuyou.activity.TuYouActivity;

import java.util.List;

/**
 * Created by admin on 2016/11/4.
 */

public class WelcomePagerAdapter extends PagerAdapter  {

    private Context mContext;
    private List<Integer> mList;
    private View.OnClickListener mOnClickListener;
    public WelcomePagerAdapter(Context context, List<Integer> list,View.OnClickListener listener) {
        mContext = context;
        mList = list;
        mOnClickListener=listener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (position==mList.size()-1){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.welcome_last_pager,container,false);
            ImageView img = (ImageView) view.findViewById(R.id.last_pager_image);
            img.setImageResource(mList.get(position));
            Button button = (Button) view.findViewById(R.id.last_pager_btn);
            button.setOnClickListener(mOnClickListener);
            container.addView(view);
            return view;
        }else {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(mList.get(position));
            container.addView(imageView);
            return imageView;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
