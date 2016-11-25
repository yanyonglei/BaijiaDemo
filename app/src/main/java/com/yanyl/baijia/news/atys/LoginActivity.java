package com.yanyl.baijia.news.atys;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.bean.User;
import com.yanyl.baijia.news.util.Util;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

/**
 * @author yanyl
 * Created by yanyl on 2016/10/19.
 * 注册界面
 */
public class LoginActivity extends BaseActivity {


    //手机注册
    private TextView mTvOther;
    //控件 忘记密码
    private TextView mWangjiTextView;
    //注册控件
    private TextView mZheceTextView;

    //关闭控件
    private ImageView mImageViewColse;

    //用户名
    private EditText mUserEdidText;
    //密码框
    private EditText mPwdEditText;

    //登录Btn
    private Button mDlButton;



    private List<User> list;
    //数据库
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getSharedPreferences("User", MODE_PRIVATE);
        initViews();
    }

    private void initViews() {
        //注册
        mZheceTextView = (TextView) findViewById(R.id.id_zhuce);
        mZheceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.enterIntent(LoginActivity.this, RegisterActivity.class);
            }
        });

        mImageViewColse = (ImageView) findViewById(R.id.id_close);
        mImageViewColse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.backIntent(LoginActivity.this);
            }
        });

        mTvOther = (TextView) findViewById(R.id.id_other);
        mTvOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.enterIntent(LoginActivity.this, LoginThridActivity.class);
            }
        });

        mWangjiTextView = (TextView) findViewById(R.id.id_wangji);
        mWangjiTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Util.enterIntent(LoginActivity.this,ChongzhiPaw.class);
            }
        });

        mUserEdidText = (EditText) findViewById(R.id.id_login_user);
        mPwdEditText = (EditText) findViewById(R.id.id_login_pwd);
        mDlButton = (Button) findViewById(R.id.id_denglu);

        mDlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mUser = mUserEdidText.getText().toString().trim();
                String password = mPwdEditText.getText().toString().trim();

                if (TextUtils.isEmpty(mUser)) {
                    Util.showToast(LoginActivity.this, "用户名不能为空");
                } else {
                }
                if (TextUtils.isEmpty(password)) {
                    Util.showToast(LoginActivity.this, "密码不能为空");
                } else if (password.length() < 6) {
                    Util.showToast(LoginActivity.this, "密码少于6位");
                } else {
                    //数据库查询
                    //数据库中找出名字为mUser的对象
                    User user=DataSupport.where("name=?",mUser).findFirst(User.class);
                    if (user!=null){
                        if (user.getPassword().equals(password)) {

                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("name", mUser);
                            editor.commit();

                            //获取当前窗体的宽度
                            DisplayMetrics dm = new DisplayMetrics();
                            LoginActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                            //按钮宽度增加到整个窗体宽度
                            performAnimate(mDlButton,mDlButton.getWidth(), dm.widthPixels);
                            //进入下一个窗体
                            Util.enterIntent(LoginActivity.this, UserLoginActivity.class);
                        }else {
                            Util.showToast(LoginActivity.this,"密码错误");
                        }
                    }else{
                        Util.showToast(LoginActivity.this,"用户名不存在！！！");
                    }
                }
            }
        });
    }

    private void performAnimate(final View target, final int start, final int end) {
        ValueAnimator valueAm=ValueAnimator.ofInt(1,100);
        valueAm.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private IntEvaluator mEvaluator=new IntEvaluator();
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //当前的进度值
                int currentvalue= (Integer) animation.getAnimatedValue();
                Log.d("Login","current value"+currentvalue);
                //获取当前进度占整个动画的比例 0-1 之间
                float fraction=animation.getAnimatedFraction();
                //调用整形估值器
                target.getLayoutParams().width=mEvaluator.evaluate(fraction,start,end);
                target.requestLayout();
            }
        });
        valueAm.setDuration(800).start();
    }

}
