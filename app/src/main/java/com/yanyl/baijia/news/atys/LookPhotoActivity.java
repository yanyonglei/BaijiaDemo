package com.yanyl.baijia.news.atys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.url.Url;


/**
 * Created by yanyl on 2016/10/20.
 * @author yanyl
 * 图片种类
 *
 */
public class LookPhotoActivity extends BaseActivity implements View.OnClickListener {

    //具体路径
    private static final String PATH="url";
    //动画的ID
    private static final String ID="id";

    //动画编号
    private static final int TWEEN_ZERO=0;
    private static final int TWEEN_ONE=1;
    private static final int TWEEN_TWO=2;
    private static final int TWEEN_THREE=3;
    private static final int TWEEN_FOUR=4;
    private static final int TWEEN_FIVE=5;

    //音乐
    private TextView mMusicView;
    //全景
    private TextView mFullView;
    //影视
    private TextView mYingshiView;
    //军事
    private TextView mArmView;
    //运动
    private TextView mSportView;
    //黑科技
    private TextView mBlackTechView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_photo);
        //初始化组件
        initViews();
    }

    private void initViews() {

        mMusicView= (TextView) findViewById(R.id.id_music);
        mFullView= (TextView) findViewById(R.id.id_fullview);
        mYingshiView= (TextView) findViewById(R.id.id_yingshi);
        mArmView= (TextView) findViewById(R.id.id_arm);
        mSportView= (TextView) findViewById(R.id.id_sport);
        mBlackTechView= (TextView) findViewById(R.id.id_black_tech);


        //设置监听事件
        mMusicView.setOnClickListener(this);
        mFullView.setOnClickListener(this);
        mYingshiView.setOnClickListener(this);
        mSportView.setOnClickListener(this);
        mArmView.setOnClickListener(this);
        mBlackTechView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.id_music:

               Intent intent1=new Intent(LookPhotoActivity.this,PhotoActivity.class);
                intent1.putExtra(PATH, Url.WNAGYIN_MUSIC);
                intent1.putExtra(ID,TWEEN_ZERO);
                startActivity(intent1);
                overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
                break;
            case R.id.id_fullview:

                Intent intent2=new Intent(LookPhotoActivity.this,PhotoActivity.class);
                intent2.putExtra(PATH, Url.WANGYIN_FULLVIEW);
                intent2.putExtra(ID,TWEEN_ONE);
                startActivity(intent2);
                overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
                break;
            case R.id.id_arm:

                Intent intent3=new Intent(LookPhotoActivity.this,PhotoActivity.class);
                intent3.putExtra(PATH, Url.WANGYIN_ARM);
                intent3.putExtra(ID,TWEEN_TWO);
                startActivity(intent3);
                overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
                break;
            case R.id.id_yingshi:

                Intent intent4=new Intent(LookPhotoActivity.this,PhotoActivity.class);
                intent4.putExtra(PATH, Url.WANGYIN_TINGSHI);
                intent4.putExtra(ID,TWEEN_THREE);
                startActivity(intent4);
                overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
                break;
            case R.id.id_sport:
                Intent intent6=new Intent(LookPhotoActivity.this,PhotoActivity.class);
                intent6.putExtra(PATH, Url.WANGYIN_SPORT);
                intent6.putExtra(ID,TWEEN_FOUR);
                startActivity(intent6);
                overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
                break;
            case R.id.id_black_tech:

                Intent  intent5=new Intent(LookPhotoActivity.this,PhotoActivity.class);
                intent5.putExtra(PATH, Url.WANGYIN_TECH);
                intent5.putExtra(ID,TWEEN_FIVE);
                startActivity(intent5);
                overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
                break;
            default:
                break;
        }

    }


}
