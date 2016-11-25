package com.yanyl.baijia.news.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.widget.Toast;

import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.bean.XinwenBean;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yanyl on 2016/10/19.
 * @author yanyl
 */
public class Util {

    public  static  void enterNext(Context context,Class cls){
        Intent intent = new Intent(context,cls);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.out_to_up,
                R.anim.in_from_down);
    }
    public  static  void enterIntent(Context context,Class cls){
        Intent intent = new Intent(context,cls);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right,
                R.anim.out_to_left);
    }
    public  static  void backIntent(Context context){
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    //获取当前的时间
    public static String getTime(){
        SimpleDateFormat format=new SimpleDateFormat("yyyMMddHHmmss");
        Date date=new Date();
        String time=format.format(date);
        return time;
    }

    public static void  showToast(Context context,String meg){
        Toast.makeText(context,meg,Toast.LENGTH_SHORT).show();
    }
    //dpi转px
    public static float Dp2Px(Context context, float dpi) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, context.getResources().getDisplayMetrics());
    }
    //sp转px
    public static float Sp2Px(Context context, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }


    //尺寸压缩 不改变像素
    public static Bitmap scaleImg(String imgPath, float pixelW, float pixelH) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        newOpts.inJustDecodeBounds = true;

        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;

        Bitmap bitmap = BitmapFactory.decodeFile(imgPath,newOpts);

        newOpts.inJustDecodeBounds = false;
        //图片的宽度
        int w = newOpts.outWidth;
        //图片的高度
        int h = newOpts.outHeight;
        // 想要缩放的目标尺寸
        float hh = pixelH;// 设置高度
        float ww = pixelW;// 设置宽度为
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        //设置图片的采样率
        newOpts.inSampleSize = be;//设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        // 压缩好比例大小后再进行质量压缩
        return bitmap;
    }
}