package com.yanyl.baijia.news.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.yanyl.baijia.news.util.DateUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by yanyl on 2016/11/11.
 */
public class MonthDateView extends View {

    //列数 7列
    private static final int NUM_COLUMNS = 7;
    //6行
    private static final int NUM_ROWS = 6;
    //画笔
    private Paint mPaint;
    //网格画笔
    private Paint mWgPaint;
    //设置day 的颜色
    private int mDayColor = Color.parseColor("#000000");
    //当天的颜色
    private int mSelectDayColor = Color.parseColor("#ffffff");
    //当天的背景颜色
    private int mSelectBGColor = Color.parseColor("#ff0000");
    //当前的颜色
    private int mCurrentColor = Color.parseColor("#ff0000");
    //当前年月日
    private int mCurrYear, mCurrMonth, mCurrDay;
    //选择 的年月日
    private int mSelYear, mSelMonth, mSelDay;
    //行与列的尺寸
    private int mColumnSize, mRowSize;

    private DisplayMetrics mDisplayMetrics;
    private int mDaySize = 18;

    private TextView tv_date, tv_week;
    private int weekRow;

    private int[][] daysString;
    //半径

    private DateClick dateClick;


    public MonthDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化
        mDisplayMetrics = getResources().getDisplayMetrics();
        //获取当前的时间
        Calendar calendar = Calendar.getInstance();
        mPaint = new Paint();//获取画笔对象
        mWgPaint=new Paint();
        //年
        mCurrYear = calendar.get(Calendar.YEAR);
        //月
        mCurrMonth = calendar.get(Calendar.MONTH);
        //日
        mCurrDay = calendar.get(Calendar.DATE);
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
    }

    @Override
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
    protected void onDraw(Canvas canvas) {
        initSize();
        daysString = new int[6][7];
        mPaint.setTextSize(mDaySize * mDisplayMetrics.scaledDensity);
        String dayString;
        int mMonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth);
        int weekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth);

        //获取当前的宽度
        int width = getWidth();
        //获取当当前的高度
        int height = getHeight();

        for (int day = 0; day < mMonthDays; day++) {

            dayString = (day + 1) + "";

            int column = (day + weekNumber - 1) % 7;
            int row = (day + weekNumber - 1) / 7;
            daysString[row][column] = day + 1;
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText
                    (dayString)) / 2);
            int startY = (int) (mRowSize * row + mRowSize / 2 - (mPaint.ascent() + mPaint.descent
                    ()) / 2);
            
            mWgPaint.setColor(Color.BLUE);
            mWgPaint.setStyle(Paint.Style.STROKE);
            mWgPaint.setAntiAlias(true);
            mWgPaint.setStrokeWidth(4);
            //画网格 6x7
            for (int i=0;i<6;i++){
                //画行
                canvas.drawLine(0,mRowSize*(i+1),width,mRowSize*(i+1),mWgPaint);

            }
            canvas.drawLine(0,height,width,height,mWgPaint);
            canvas.drawLine(1,0,0,height,mWgPaint);

            for (int i=0;i<7;i++){
                //画列
                canvas.drawLine(mColumnSize*(i+1),0,mColumnSize*(i+1),height,mWgPaint);
            }


            if (dayString.equals(mSelDay + "")) {
                //绘制背景色 圆形
                int startRecX = mColumnSize * column;
                int startRecY = mRowSize * row;
                int endRecX = startRecX + mColumnSize;
                int endRecY = startRecY + mRowSize;
                canvas.drawLine(0,row*height,width,row*height,mPaint);
                mPaint.setColor(mSelectBGColor);
               //当天日期背景椭圆
                canvas.drawOval(new RectF(startRecX,startRecY,endRecX,endRecY),mPaint);

                //记录第几行，即第几周
                weekRow = row + 1;
            }
            if (dayString.equals(mSelDay + "")) {
                mPaint.setColor(mSelectDayColor);
            } else if (dayString.equals(mCurrDay + "") && mCurrDay != mSelDay && mCurrMonth ==
                    mSelMonth) {
                //正常月，选中其他日期，则今日为红色
                mPaint.setColor(mCurrentColor);
            } else {
                mPaint.setColor(mDayColor);
            }
            canvas.drawText(dayString, startX, startY, mPaint);


            if (tv_date != null) {
                tv_date.setText(mSelYear + "年" + (mSelMonth + 1) + "月");
            }

            if (tv_week != null) {
                tv_week.setText("第" + weekRow + "周");
            }
        }
    }



    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int downX = 0, downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventCode = event.getAction();
        switch (eventCode) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (Math.abs(upX - downX) < 10 && Math.abs(upY - downY) < 10) {//点击事件
                    performClick();
                    doClickAction((upX + downX) / 2, (upY + downY) / 2);
                }
                break;
        }
        return true;
    }

    /**
     * 初始化列宽行高
     */
    private void initSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
    }

    /**
     * 设置年月
     *
     * @param year
     * @param month
     */
    private void setSelectYearMonth(int year, int month, int day) {
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    /**
     * 执行点击事件
     *
     * @param x
     * @param y
     */
    private void doClickAction(int x, int y) {
        int row = y / mRowSize;
        int column = x / mColumnSize;
        setSelectYearMonth(mSelYear, mSelMonth, daysString[row][column]);
        invalidate();
        //执行activity发送过来的点击处理事件
        if (dateClick != null) {
            dateClick.onClickOnDate();
        }
    }

    /**
     * 左点击，日历向后翻页
     */
    public void onLeftClick() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 0) {//若果是1月份，则变成12月份
            year = mSelYear - 1;
            month = 11;
        } else if (DateUtils.getMonthDays(year, month) == day) {
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month - 1;
            day = DateUtils.getMonthDays(year, month);
        } else {
            month = month - 1;
        }
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    /**
     * 右点击，日历向前翻页
     */
    public void onRightClick() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 11) {//若果是12月份，则变成1月份
            year = mSelYear + 1;
            month = 0;
        } else if (DateUtils.getMonthDays(year, month) == day) {
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month + 1;
            day = DateUtils.getMonthDays(year, month);
        } else {
            month = month + 1;
        }
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    /**
     * 获取选择的年份
     *
     * @return
     */
    public int getmSelYear() {
        return mSelYear;
    }

    /**
     * 获取选择的月份
     *
     * @return
     */
    public int getmSelMonth() {
        return mSelMonth;
    }

    /**
     * 获取选择的日期
     *
     * @param
     */
    public int getmSelDay() {
        return this.mSelDay;
    }

    /**
     * 普通日期的字体颜色，默认黑色
     *
     * @param mDayColor
     */
    public void setmDayColor(int mDayColor) {
        this.mDayColor = mDayColor;
    }

    /**
     * 选择日期的颜色，默认为白色
     *
     * @param mSelectDayColor
     */
    public void setmSelectDayColor(int mSelectDayColor) {
        this.mSelectDayColor = mSelectDayColor;
    }

    /**
     * 选中日期的背景颜色，默认蓝色
     *
     * @param mSelectBGColor
     */
    public void setmSelectBGColor(int mSelectBGColor) {
        this.mSelectBGColor = mSelectBGColor;
    }

    /**
     * 当前日期不是选中的颜色，默认红色
     *
     * @param mCurrentColor
     */
    public void setmCurrentColor(int mCurrentColor) {
        this.mCurrentColor = mCurrentColor;
    }

    /**
     * 日期的大小，默认18sp
     *
     * @param mDaySize
     */
    public void setmDaySize(int mDaySize) {
        this.mDaySize = mDaySize;
    }

    /**
     * 设置显示当前日期的控件
     *
     * @param tv_date 显示日期
     * @param tv_week 显示周
     */
    public void setTextView(TextView tv_date, TextView tv_week) {
        this.tv_date = tv_date;
        this.tv_week = tv_week;
        invalidate();
    }
    /**
     * 设置日期的点击回调事件
     *
     * @author shiwei.deng
     */
    public interface DateClick {
        public void onClickOnDate();
    }

    /**
     * 设置日期点击事件
     *
     * @param dateClick
     */
    public void setDateClick(DateClick dateClick) {
        this.dateClick = dateClick;
    }

    /**
     * 跳转至今天
     */
    public void setTodayToView() {
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
        invalidate();
    }
}
