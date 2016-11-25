package com.yanyl.baijia.news.atys;

import android.os.Bundle;

import com.yanyl.baijia.news.view.Playground;

/**
 * Created by yanyl on 2016/10/24.
 */
public class GameActivity extends BaseActivity {


    //游戏的等级
    private int grid=0;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        grid=getIntent().getIntExtra("grid",0);
        setContentView(new Playground(this ,grid));
    }
}
