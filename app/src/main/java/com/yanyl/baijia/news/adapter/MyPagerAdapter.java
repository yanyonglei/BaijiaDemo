package com.yanyl.baijia.news.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yanyl on 2016/11/2.
 */
public class MyPagerAdapter extends PagerAdapter {


    private Context context;

    private List<View> views;
    public MyPagerAdapter(Context context,List<View> views) {

        this.context = context;
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
        //super.destroyItem(container, position, object);
    }
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(views.get(position));
        return views.get(position);
    }
}
