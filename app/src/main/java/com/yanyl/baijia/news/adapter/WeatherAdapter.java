package com.yanyl.baijia.news.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.bean.TianQiBean;

import java.util.List;

/**
 * Created by yanyl on 2016/11/18.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>{

    //天气集合
    private List<TianQiBean> tianQiBeenList;
    private Context context;

    //天气
    private TianQiBean tianQiBean;
    private LayoutInflater mInfalter;

    public WeatherAdapter(List<TianQiBean> tianQiBeenList, Context context) {
        this.tianQiBeenList = tianQiBeenList;
        this.context = context;
        mInfalter=LayoutInflater.from(context);
    }

    @Override
    /**
     * 获取子View
     */
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=mInfalter.inflate(R.layout.weather_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    /**
     * 绑定数据
     */
    public void onBindViewHolder(ViewHolder holder, int position) {
        tianQiBean=tianQiBeenList.get(position);
        String t[]= new String[2];
        if(tianQiBean.getTemperature().contains("/")){
            //字符串分割
            t=tianQiBean.getTemperature().split("/");
            holder.mTvTemperatureDay.setText(" "+t[0]);
            holder.mTvTemperatureNight.setText(""+t[1]);
        }else{
            holder.mTvTemperatureDay.setText(" "+tianQiBean.getTemperature());
            holder.mTvTemperatureNight.setText(" "+tianQiBean.getTemperature());
        }

        if (tianQiBean.getCity().equals(tianQiBean.getDis())){

            holder.mTvDis.setText ("查询位置: "+tianQiBean.getProvince()+"    "+tianQiBean.getCity());
        }else{
            holder.mTvDis.setText ("查询位置: "+tianQiBean.getCity()+"      "+tianQiBean.getDis());
        }
        holder.mTvDate.setText("   "+tianQiBean.getDate()+"    ");
        if (tianQiBean.getDay().isEmpty()){
            holder.mTvStatusDay.setText("数据更新... "+tianQiBean.getDay());
        }else{
            holder.mTvStatusDay.setText(tianQiBean.getDay());
        }
        holder.mTvStatusNight.setText(tianQiBean.getNight());
        holder.mTvWind.setText("  "+tianQiBean.getWind());
    }

    @Override
    public int getItemCount() {
        return tianQiBeenList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        //日期
        public TextView mTvDate;

        //白天温度
        public TextView mTvTemperatureDay;
        //夜间温度
        public TextView mTvTemperatureNight;
        //风的状况
        public TextView mTvWind;
        //白天天气状况
        public TextView mTvStatusDay;
        //夜间天气状况
        public TextView mTvStatusNight;
        //地区
        public TextView mTvDis;

       public ViewHolder(View itemView) {
           super(itemView);
           mTvDate= (TextView) itemView.findViewById(R.id.weather_date);
           mTvTemperatureDay= (TextView) itemView.findViewById(R.id.day_wendu);
           mTvTemperatureNight= (TextView) itemView.findViewById(R.id.night_wendu);
           mTvWind= (TextView) itemView.findViewById(R.id.wind);
           mTvStatusDay= (TextView) itemView.findViewById(R.id.status_day);
           mTvStatusNight= (TextView) itemView.findViewById(R.id.status_night);
           mTvDis= (TextView) itemView.findViewById(R.id.weather_dis);
       }
   }
}
