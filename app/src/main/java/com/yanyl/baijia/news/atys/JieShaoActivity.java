package com.yanyl.baijia.news.atys;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.common.Const;
import com.yanyl.baijia.news.util.Util;

/**
 * Created by yanyl on 2016/10/24.
 * 游戏介绍的界面
 */
public class JieShaoActivity extends BaseActivity implements View.OnClickListener {

    //游戏开始控件
    private Button mBeginBtn;
    //游戏退出控件
    private Button mBackBtn;
    //游戏介绍控件
    private Button mJieShaoBtn;
    //进入游戏控件
    private ImageView  mEnterView;
    //退出 游戏介绍
    private ImageView mBackView;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jieshao);
        //初始化控件
        initViews();
    }

    private void initViews() {

        mBeginBtn= (Button) findViewById(R.id.id_btn_begin);
        mBackBtn= (Button) findViewById(R.id.id_btn_back);
        mJieShaoBtn= (Button) findViewById(R.id.id_btn_jieshao);
        mEnterView= (ImageView) findViewById(R.id.id_img_enter);
         mBackView= (ImageView) findViewById(R.id.id_img_back);

        mBeginBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mJieShaoBtn.setOnClickListener(this);
        mEnterView.setOnClickListener(this);
        mBackView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_btn_begin:
                //开始游戏
                begin();
                break;

            case R.id.id_btn_back:
                back();
                break;
            case R.id.id_btn_jieshao:
                //初始化弹出对话框
                initDialog();
                break;
            case R.id.id_img_enter:
                Util.enterIntent(this,GameActivity.class);
                break;
            case R.id.id_img_back:
                back();
                break;
        }

    }

    private void initDialog() {
        final AlertDialog dialog=new AlertDialog.Builder(JieShaoActivity.this).create();
        View view=getLayoutInflater().inflate(R.layout.dialog,null);
        //确定按钮
        Button mButtonQd= (Button) view.findViewById(R.id.id_queding);
        //取消按钮
        Button mButtonCancel= (Button) view.findViewById(R.id.id_cancel);
        mButtonQd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.setView(view);
        dialog.show();
    }


    private void back() {
        Util.backIntent(JieShaoActivity.this);
    }


    private void begin() {

        final AlertDialog  dialog=new AlertDialog.Builder(JieShaoActivity.this).create();
        View view=getLayoutInflater().inflate(R.layout.dialog_class,null);
        Button mButtonDiff= (Button) view.findViewById(R.id.id_diff);
        Button mButtonEasy= (Button) view.findViewById(R.id.id_easy);
        Button mButtonCancel= (Button) view.findViewById(R.id.id_cancel);
        mButtonDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(JieShaoActivity.this,GameActivity.class);
                //设置游戏的难易等级
                intent.putExtra("grid", Const.DIFFERICAT);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
                dialog.dismiss();
            }
        });
        mButtonEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(JieShaoActivity.this,GameActivity.class);
                //设置游戏的难易等级
                intent.putExtra("grid",Const.EASY);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
                dialog.dismiss();

            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();

    }
}
