package com.yanyl.baijia.news.atys;

import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.ViewGroup;


import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.util.PreferenceUtils;
import com.yanyl.baijia.news.util.ThemeUtils;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


/**
 * Created by yanyl on 2016/10/20.
 * @author yanyl
 * BaseActivity继承AppCompatActivity
 *
 */
public class BaseActivity extends AppCompatActivity {


    protected PreferenceUtils preferenceUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferenceUtils = PreferenceUtils.getInstance(this);
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

    }
    private void initTheme() {
        ThemeUtils.Theme theme = ThemeUtils.getCurrentTheme(this);
        ThemeUtils.changTheme(this, theme);
    }

    //eventbus
    public void onEventMainThread(ThemeUtils.Theme theme) {
        ThemeUtils.changTheme(this,theme);
        // setTheme(theme);
        this.recreate();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
