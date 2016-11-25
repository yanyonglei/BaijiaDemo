package com.yanyl.baijia.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


/**
 * Created by yanyl on 2016/10/22.
 */
 public abstract class MyBaseAdapter<T> extends BaseAdapter{
        private List<T> datas;
        private LayoutInflater inflater;

        public LayoutInflater getInflater() {
            return inflater;
        }

        public MyBaseAdapter(List<T> datas, Context context) {
            this.datas = datas;
            this.inflater = inflater.from(context);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public T getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addAll(List<T> dd){
            datas.addAll(dd);
            notifyDataSetChanged();
        }

        public void clear(){
            datas.clear();
            notifyDataSetChanged();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItemView(position,convertView,parent);
        }
    public abstract View getItemView(int position, View convertView, ViewGroup parent);
}
