package com.yanyl.baijia.news.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by yanyl on 2016/11/11.
 * 自定义viiew weekDayView 周的显示编写
 */
public class WeekDayView extends View {
     /*  上横线颜色*/
    private int mTopLineColor = Color.parseColor("#000000");
  /*  //下横线颜色*/
    private int mBottomLineColor = Color.parseColor("#000000");
  /* 周一到周五的颜色*/
    private int mWeedayColor = Color.parseColor("#1FC2F3");
   /* 周六、周日的颜色*/
    private int mWeekendColor = Color.parseColor("#fa4451");
   /* 线的宽度*/
    private int mStrokeWidth = 4;
   /* //字体大小 14*/
    private int mWeekSize = 14;
    //画笔
    private Paint paint;
    private Paint mLinePaint;


   /* //获取窗口大小的工具*/
    private DisplayMetrics mDisplayMetrics;

    //一周 周一到周日
    private String[] weekString = new String[]{"日","一","二","三","四","五","六"};

    public WeekDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取对象
        mDisplayMetrics = getResources().getDisplayMetrics();
        //初始化 画笔工具
        paint = new Paint();
        mLinePaint=new Paint();
    }

    @Override
    /**
     * 测量其大小
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //获取父布局宽度
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //获取父布局宽度模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        // //获取父布局高度
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //获取父布局高度模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //判断高度是否为最大值
        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = mDisplayMetrics.densityDpi * 30;
        }
        //判断宽度是否为最大值
        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
    /*    这个方法必须由onMeasure(int, int)来调用，来存储测量的宽，高值。*/
        setMeasuredDimension(widthSize, heightSize);

    }

    @Override
    /**
     * 重新绘制
     */
    protected void onDraw(Canvas canvas) {
        //获取当前的宽度
        int width = getWidth();
        //获取当当前的高度
        int height = getHeight();
        //进行画上下线
       // paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.FILL);
       // paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(mTopLineColor);
        paint.setStrokeWidth(mStrokeWidth);
        //画直线
        canvas.drawLine(0, 0, width, 0, paint);
        //画下横线
        paint.setColor(mBottomLineColor);
        canvas.drawLine(0, height, width, height, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(mWeekSize * mDisplayMetrics.scaledDensity);

        //横行划分7份
        int columnWidth = width / 7;
        for(int i=0;i < weekString.length;i++){

            String text = weekString[i];

            //
            int fontWidth = (int) paint.measureText(text);

            int startX = columnWidth * i + (columnWidth - fontWidth)/2;

            int startY = (int) (height/2 - (paint.ascent() + paint.descent())/2);

            if(text.indexOf("日") > -1|| text.indexOf("六") > -1){
                paint.setColor(mWeekendColor);
            }else{
                paint.setColor(mWeedayColor);
            }

            mLinePaint.setStyle(Paint.Style.STROKE);
            mLinePaint.setColor(Color.parseColor("#000000"));
            canvas.drawLine(i*columnWidth,0,i*columnWidth,height,mLinePaint);
            canvas.drawLine(width-5,0,width-5,height,mLinePaint);
            canvas.drawText(text, startX, startY, paint);
        }
    }

    /**
     * 设置顶线的颜色
     * @param mTopLineColor
     */
    public void setmTopLineColor(int mTopLineColor) {
        this.mTopLineColor = mTopLineColor;
    }

    /**
     * 设置底线的颜色
     * @param mBottomLineColor
     */
    public void setmBottomLineColor(int mBottomLineColor) {
        this.mBottomLineColor = mBottomLineColor;
    }

    /**
     * 设置周一-五的颜色
     * @return
     */
    public void setmWeedayColor(int mWeedayColor) {
        this.mWeedayColor = mWeedayColor;
    }

    /**
     * 设置周六、周日的颜色
     * @param mWeekendColor
     */
    public void setmWeekendColor(int mWeekendColor) {
        this.mWeekendColor = mWeekendColor;
    }

    /**
     * 设置边线的宽度
     * @param mStrokeWidth
     */
    public void setmStrokeWidth(int mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
    }


    /**
     * 设置字体的大小
     * @param mWeekSize
     */
    public void setmWeekSize(int mWeekSize) {
        this.mWeekSize = mWeekSize;
    }


    /**
     * 设置星期的形式
     * @param weekString
     * 默认值	"日","一","二","三","四","五","六"
     */
    public void setWeekString(String[] weekString) {
        this.weekString = weekString;
    }
}
