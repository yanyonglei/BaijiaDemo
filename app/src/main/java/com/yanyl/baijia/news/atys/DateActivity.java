package com.yanyl.baijia.news.atys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.util.Util;
import com.yanyl.baijia.news.view.MonthDateView;

/**
 * Created by yanyl on 2016/11/3.
 */
public class DateActivity extends BaseActivity {


    //月份减按钮
    private ImageView mImgLeft;
    //月份加
    private ImageView mImgRight;

    private MonthDateView mMonthDateView;



    private TextView tv_date;
    private TextView tv_week;
    private TextView tv_today;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        initView();
    }

    private void initView() {
        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.backIntent(DateActivity.this);
            }
        });
        mMonthDateView= (MonthDateView) findViewById(R.id.monthDateView);
        tv_date = (TextView) findViewById(R.id.date_text);
        tv_week  =(TextView) findViewById(R.id.week_text);
        tv_today = (TextView) findViewById(R.id.tv_today);
        mImgLeft= (ImageView) findViewById(R.id.img_left);
        mMonthDateView.setTextView(tv_date,tv_week);
        mImgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMonthDateView.onLeftClick();
            }
        });
        mImgRight= (ImageView) findViewById(R.id.img_right);
        mImgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMonthDateView.onRightClick();
            }
        });
        tv_today.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mMonthDateView.setTodayToView();
            }
        });
    }
}
