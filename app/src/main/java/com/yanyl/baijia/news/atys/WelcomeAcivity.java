package com.yanyl.baijia.news.atys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.util.Util;
import com.yanyl.baijia.news.view.RoundProgressBar;

/**
 * Created by yanyl on 2016/11/2.
 */
public class WelcomeAcivity extends BaseActivity {


    //圆形进度条
    private RoundProgressBar mRpb;
    //显示时间的控件
    private TextView mTvTime;

    //图片控件
    private ImageView mImgView;

    //倒计时总时间
    private int time=5;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int t=msg.what;
            mRpb.setVisibility(View.VISIBLE);
            if (t>0){
                t--;
                mRpb.setProgress(t);
                mTvTime.setText(t+"s");
                handler.sendEmptyMessageDelayed(t,1000);
            }else {

              Util.enterIntent(WelcomeAcivity.this,MainActivity.class);
                finish();
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel);


        intView();
    }

    private void intView() {
        mRpb= (RoundProgressBar) findViewById(R.id.id_wel_pb);
        mImgView= (ImageView) findViewById(R.id.id_img);

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.animation_welcome);
        //开启动画
        mImgView.startAnimation(animation);

        mRpb.setMax(time);
        mTvTime= (TextView) findViewById(R.id.id_wel_time);
        handler.sendEmptyMessageDelayed(time,1000);
    }
}
