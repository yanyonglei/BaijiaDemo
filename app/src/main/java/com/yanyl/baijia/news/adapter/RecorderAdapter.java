package com.yanyl.baijia.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.bean.Recorder;
import com.yanyl.baijia.news.util.Util;

import java.util.List;

/**
 * Created by yanyl on 2016/10/8.
 */
public class RecorderAdapter extends ArrayAdapter<Recorder> {


    private int minItemWith;
    private int maxItemWith;

    private LayoutInflater inflater;
    public RecorderAdapter(Context context, List<Recorder> mDatas) {
        super(context, -1,mDatas);
        inflater=LayoutInflater.from(context);

        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();

        wm.getDefaultDisplay().getMetrics(outMetrics);
        minItemWith= (int) (outMetrics.widthPixels*0.05f);
        maxItemWith= (int) (outMetrics.widthPixels*0.5f);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.item_recorder,parent,false);
            holder=new ViewHolder();
            holder.second= (TextView) convertView.findViewById(R.id.id_time);
            holder.dateView= (TextView) convertView.findViewById(R.id.id_time_date);
            holder.length=convertView.findViewById(R.id.id_amin);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.second.setText(Math.round(getItem(position).time)+"\"");
        holder.dateView.setText(Util.getTime());
        ViewGroup.LayoutParams lp=holder.length.getLayoutParams();
        lp.width= (int) (minItemWith+(maxItemWith/60f*getItem(position).time));
        return convertView;
    }

    class ViewHolder {
        TextView second;
        TextView dateView;
        View length;
    }
}
