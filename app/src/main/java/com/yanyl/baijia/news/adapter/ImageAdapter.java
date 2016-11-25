package com.yanyl.baijia.news.adapter;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.atys.DownActivity;
import com.yanyl.baijia.news.atys.FankuiActivity;
import com.yanyl.baijia.news.bean.ImageBean;
import com.yanyl.baijia.news.util.Util;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by yanyl on 2016/10/25.
 */
public class ImageAdapter extends MyBaseAdapter<ImageBean> {

    private int num=0;


    private int anim[]=new int[]{R.anim.item_alpha,R.anim.item_rotate,R.anim.item_scale};
    private int id;
    private ImageBean imageBean;
    private Context context;
    public ImageAdapter(List<ImageBean> datas, Context context,int id) {
        super(datas, context);
        this.context=context;
        this.id=id;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=getInflater().inflate(R.layout.list_item_photo,null);
            holder.mTitle= (TextView) convertView.findViewById(R.id.title_video);
            holder.mImage= (ImageView) convertView.findViewById(R.id.id_img);
            holder.mImgDownLoad= (ImageView) convertView.findViewById(R.id.id_img_down_load);
            holder.mTime= (TextView) convertView.findViewById(R.id.time_video);

            holder.mShareView= (ImageView) convertView.findViewById(R.id.id_share);
            holder.mCommentView= (ImageView) convertView.findViewById(R.id.yijian);
            holder.mDianZanView= (ImageView) convertView.findViewById(R.id.dianzan);
            holder.mNumView= (TextView) convertView.findViewById(R.id.num);
            
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        imageBean =getItem(position);
        holder.mImgDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DownActivity.class);
                intent.putExtra("detail", imageBean.getImg());

                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);

            }
        });
        //获取子viedo 的对象

        if (imageBean.getTitle()!=null){
            holder.mTitle.setText("vid:"+ imageBean.getTitle());
        }
        if (imageBean.getTime()!=null){

            holder.mTime.setText("时间:"+ imageBean.getTime());
        }
        if (imageBean.getImg()!=null){
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)//是否具有内存缓存
                    .cacheOnDisk(true)//是否具有磁盘缓存
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)//图片的缩放类型
                    .bitmapConfig(Bitmap.Config.RGB_565)//图片的解码方式
                    .showImageForEmptyUri(R.mipmap.ic_launcher)//空url地址所显示的图片
                    .showImageOnFail(R.mipmap.ic_launcher)//失败的显示图片
                    .showImageOnLoading(R.mipmap.ic_launcher)//默认显示图片
                    .build();

            ImageLoader.getInstance().displayImage(imageBean.getImg(), holder.mImage,options);
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
                oks.setTitle(imageBean.getTitle());
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                oks.setImageUrl(imageBean.getContent());
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
        if (id==0){
            ObjectAnimator animator = ObjectAnimator.ofFloat(convertView,"translationX",1000,0);
            animator.setDuration(800);
            animator.start();
        }else if (id==1){
            ObjectAnimator animator = ObjectAnimator.ofFloat(convertView,"translationY",1000,0);
            animator.setDuration(800);
            animator.start();
        }else {

            Animation animation= AnimationUtils.loadAnimation(context, anim[id%anim.length]);
            animation.setFillAfter(true);
            convertView.startAnimation(animation);
        }
        return convertView;
    }

    class ViewHolder{
         public ImageView mImage;
         public ImageView mImgDownLoad;
         public TextView mTitle;
         public TextView mTime;


        public   ImageView mShareView;
        public   ImageView mCommentView;
        public   ImageView mDianZanView;
        public   TextView mNumView;
    }
}
