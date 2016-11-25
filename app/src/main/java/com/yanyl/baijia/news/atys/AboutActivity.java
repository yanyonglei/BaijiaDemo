package com.yanyl.baijia.news.atys;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by yanyl on 2016/11/11.
 */
public class AboutActivity extends BaseActivity {

    private TextView mTvAbout;

    private   ValueAnimator colorAnim;

    private TextView mTvAttention;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.backIntent(AboutActivity.this);
            }
        });
        mTvAbout= (TextView) findViewById(R.id.about_tv);
        mTvAttention= (TextView) findViewById(R.id.about_txt);

        //获取输入流
        InputStream inputStream=getResources().openRawResource(R.raw.about);
        mTvAttention.setText(getStringFromInputStream(inputStream));


        //实现背景颜色在0.3秒内从0xffff8080到0xff8080ff的简便，动画无限循环属性动画
        colorAnim= ObjectAnimator.ofInt(mTvAbout,"backgroundColor",0xffff8080,0xff8080ff);
        colorAnim.setDuration(300);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        colorAnim.cancel();
    }
    private String getStringFromInputStream(InputStream inputStream) {

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int len=0;

        try {
            while((len=inputStream.read(buffer))!=-1){
                baos.write(buffer,0,len);
            }
            inputStream.close();
            String result=new String(baos.toByteArray(),"utf-8");
            baos.close();
            return result.toString();
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

    }

}
