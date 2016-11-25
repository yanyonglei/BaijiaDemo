package com.yanyl.baijia.news.atys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.yanyl.baijia.news.R;

import com.yanyl.baijia.news.fragment.SettingFragment;
import com.yanyl.baijia.news.fragment.FEFragment;
import com.yanyl.baijia.news.fragment.XinWenFragment;
import com.yanyl.baijia.news.util.ThemeUtils;
import com.yanyl.baijia.news.util.Util;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * @author yanyl
 * 主界面
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {


    //新闻 部分
    private Fragment mXinwenFragment;
    //娱乐
    private Fragment mFeFragment;
    //搜索部分
    //设置部分
    private Fragment mSettingFragment;

    private LinearLayout mTabXinWen;
    private LinearLayout mTabFe;
    private LinearLayout mTabSetting;


    private ImageView mXinWen;
    private ImageView mVideo;
    private ImageView mSetting;


   private FragmentManager     fm;
   private FragmentTransaction ft;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        //初始化组件
        initViews();
        //初始化监听事件
        initEvents();
        selected(0);

    }
    //点击事件
    private void initEvents() {
        mTabXinWen.setOnClickListener(this);
        mTabFe.setOnClickListener(this);
        mTabSetting.setOnClickListener(this);
    }

    private void initViews() {
        mTabXinWen= (LinearLayout) findViewById(R.id.id_xinwen);
        mTabFe= (LinearLayout) findViewById(R.id.id_video);
        mTabSetting= (LinearLayout) findViewById(R.id.id_setting);

        mXinWen= (ImageView) findViewById(R.id.id_xinwen_img);
        mVideo= (ImageView) findViewById(R.id.id_video_img);
        mSetting= (ImageView) findViewById(R.id.id_setting_img);

    }

    private void selected(int i){

       fm=getSupportFragmentManager();
       ft=fm.beginTransaction();
        //设置fragment 动画
        ft.setCustomAnimations(R.anim.in_from_down,R.anim.out_to_right);

        switch (i){
            case 0:
                if (mXinwenFragment==null){
                    mXinwenFragment=XinWenFragment.getInstance();
                }
                ft.replace(R.id.id_content_frame,mXinwenFragment);
                mXinWen.setImageResource(R.drawable.newscenter_press2);
                break;
            case 1:
                if (mFeFragment==null){
                    mFeFragment=FEFragment.getInstance();
                }
                ft.replace(R.id.id_content_frame,mFeFragment);

                mVideo.setImageResource(R.drawable.govaffairs_press2);
                break;
            case 2:
                if (mSettingFragment==null){
                    mSettingFragment=SettingFragment.getInstance();
                }
                ft.replace(R.id.id_content_frame,mSettingFragment);

                mSetting.setImageResource(R.drawable.setting_press2);
                break;
        }
        ft.commit();
    }
    @Override
    public void onClick(View v) {
        resetImg();
        switch (v.getId()){
            case R.id.id_xinwen:
                selected(0);
                break;

            case R.id.id_video:
                selected(1);
                break;
            case R.id.id_setting:
                selected(2);
                break;

        }
    }
    private void resetImg() {
        mXinWen.setImageResource(R.drawable.newscenter2);
        mVideo.setImageResource(R.drawable.govaffairs2);
        mSetting.setImageResource(R.drawable.setting2);
    }

    private long timeSpace=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (System.currentTimeMillis()-timeSpace>2000){
            timeSpace=System.currentTimeMillis();
            Util.showToast(MainActivity.this,"再按一次退出百家新闻");
        }else{
            finish();
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
