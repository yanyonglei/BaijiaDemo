package com.yanyl.baijia.news.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.atys.FankuiActivity;
import com.yanyl.baijia.news.bean.XinwenBean;
import com.yanyl.baijia.news.util.Util;


import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
/**
 * Created by yanyl on 2016/10/22.
 */
public class NewsAdapter extends MyBaseAdapter<XinwenBean> {


    //点赞计数
    private int num=0;


    private XinwenBean xinwenBean;
    private Context context;
    public NewsAdapter(List<XinwenBean> datas, Context context) {
        super(datas, context);
        this.context=context;

    }
    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView==null){
            convertView=getInflater().inflate(R.layout.list_item,null);
            holder=new ViewHolder();
            holder.imageView= (ImageView) convertView.findViewById(R.id.id_img);
            holder.mShareView= (ImageView) convertView.findViewById(R.id.id_share);
            holder.mTiltleView= (TextView) convertView.findViewById(R.id.id_title);
            holder.mtimeView=(TextView)convertView.findViewById(R.id.id_time);
            holder.mCommentView= (ImageView) convertView.findViewById(R.id.yijian);
            holder.mDianZanView= (ImageView) convertView.findViewById(R.id.dianzan);
            holder.mNumView= (TextView) convertView.findViewById(R.id.num);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
         xinwenBean=getItem(position);

         xinwenBean.save();
        if (xinwenBean.getImageUrl()!=null){
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)//是否具有内存缓存
                    .cacheOnDisk(true)//是否具有磁盘缓存
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)//图片的缩放类型
                    .bitmapConfig(Bitmap.Config.RGB_565)//图片的解码方式
                    .showImageForEmptyUri(R.mipmap.ic_launcher)//空url地址所显示的图片
                    .showImageOnFail(R.mipmap.ic_launcher)//失败的显示图片
                    .showImageOnLoading(R.mipmap.ic_launcher)//默认显示图片
                    .build();
            ImageLoader.getInstance().displayImage(xinwenBean.getImageUrl(), holder.imageView,options);
        }
        if (xinwenBean.getTitle()!=null){
            holder.mTiltleView.setText(xinwenBean.getTitle());
        }
        if (xinwenBean.getTime()!=null){
            holder.mtimeView.setText(xinwenBean.getTime());
        }
        //分享按钮
        holder.mShareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareSDK.initSDK(context);
                OnekeyShare oks = new OnekeyShare();
                oks.setDialogMode();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                oks.setTitle(xinwenBean.getTitle());
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                oks.setTitleUrl(xinwenBean.getContentUrl());
                oks.setImageUrl(xinwenBean.getImageUrl());
                // text是分享文本，所有平台都需要这个字段
                oks.setText("hello 来 看看");
                oks.setSite("百家");
                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                oks.setSiteUrl("http://www.baijia.baidu.com");
                // 启动分享GUI
                oks.show(context);
            }
        });

        holder.mCommentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.enterIntent(context, FankuiActivity.class);
            }
        });
        holder.mDianZanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                holder.mNumView.setText(num+"");
            }
        });
        //开启属性动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(convertView,"translationX",1000,0);
        animator.setDuration(800);
        animator.start();
        return convertView;
    }

    class ViewHolder{
        //标题
        public   TextView mTiltleView;
        //时间控件
        public   TextView mtimeView;

        public   ImageView imageView;
        //分享
        public   ImageView mShareView;
        //评论
        public   ImageView mCommentView;
        //点赞
        public   ImageView mDianZanView;
        //点赞数
        public   TextView mNumView;
    }
}