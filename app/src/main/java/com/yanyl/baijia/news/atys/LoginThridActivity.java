package com.yanyl.baijia.news.atys;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.util.Util;


import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by yanyl on 2016/10/31.
 */
public class LoginThridActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener {

    //回复短信标记
    private static  final int SMS_RE=0;

    //撤销标记
    private static final int MSG_AUTH_CANCEL = 1;
    //失败标记
    private static final int MSG_AUTH_ERROR= 2;
    //登录完成标记
    private static final int MSG_AUTH_COMPLETE = 3;
    //开启倒计时
    private static final int MSG_START_TIME = 4;

    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String SMS_APPKEY = "1825c77964077";
    private static String SHARE_APPKEY = "18280f6b61ba8";
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String SMS_APPSECRET = "2f7a257c52f7b3b9b0a2b85d8866909e";
    private static String SHARE_APPSECRET = "333541751857ab11d801a17dd751ba0b";


    String phone=null;
    //获取手机号文本框
    private EditText mEtPhone;
    //验证码文本框
    private EditText mEtNumber;

    //验证码按钮
    private Button mBtngetComfirm;
    //登录按钮
    private Button mBtnDengLu;

    //数据库
    private SharedPreferences mSpThrid;
    //注册
    private TextView mTvRegister;
    //倒计时
    private TextView mTvTime;
    //关闭
    private ImageView mImgClose;
    //Qq 登录
    private ImageView mImgQQ;
    //微信 登录
    private ImageView mImgWX;
    //微博登录
    private ImageView mImgWB;


    //手机号文本区
    private TextInputLayout mLayoutPhone;
    //验证码文本区
    private TextInputLayout mLayoutNum;

    //handler 消息传递
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case SMS_RE:
                    int event=msg.arg1;
                    int result=msg.arg2;
                    Object data=msg.obj;
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //回调完成
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //提交验证码成功
                            HashMap<String, Object> mData = (HashMap<String, Object>) data;
                            String country = (String) mData.get("country");//返回的国家编号
                            String number = (String) mData.get("phone");//返回用户注册的手机号
                            if (mEtPhone.getText().toString().trim().equals(number)){

                                SharedPreferences.Editor mEditor=mSpThrid.edit();

                                mEditor.putString("phone",mEtPhone.getText().toString().trim());
                                mEditor.commit();

                                Util.enterIntent(LoginThridActivity.this, UserLoginActivity.class);
                            }

                        }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                            //获取验证码成功
                        }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                            //返回支持发送验证码的国家列表
                        }
                    }else{
                        ((Throwable)data).printStackTrace();
                    }
                    break;

                case MSG_AUTH_CANCEL:
                    Util.showToast(LoginThridActivity.this,"授权撤销");
                    break;
                case MSG_AUTH_ERROR:
                    Util.showToast(LoginThridActivity.this,"出现错误");
                    break;
                case MSG_AUTH_COMPLETE:
                    PlatformDb mDb= (PlatformDb) msg.obj;
                    SharedPreferences.Editor mEditor=mSpThrid.edit();
                    mEditor.putString("username",mDb.getUserName());
                    mEditor.putString("icon",mDb.getUserIcon());
                    mEditor.commit();
                    Util.enterIntent(LoginThridActivity.this,UserLoginActivity.class);
                    LoginThridActivity.this.finish();
                    break;
                case MSG_START_TIME:
                    int time= (int) msg.obj;
                    if (time>0){
                        time--;
                        mTvTime.setVisibility(View.VISIBLE);
                        mTvTime.setText("短信已发送: "+time+" s");
                        Message message=new Message();
                        message.what=MSG_START_TIME;
                        message.obj=time;
                        handler.sendMessageDelayed(message,1000);
                    }else{
                        mTvTime.setVisibility(View.INVISIBLE);
                    }
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thrid_part);
        mSpThrid=getSharedPreferences("User",MODE_PRIVATE);

        initViews();
        initSDK();
    }

    private void initViews() {
        mBtngetComfirm= (Button) findViewById(R.id.id_get_message);
        mBtnDengLu= (Button) findViewById(R.id.id_denglu);

        mEtPhone= (EditText) findViewById(R.id.id_phone_number);
        mEtNumber= (EditText) findViewById(R.id.id_yanzhengma);

        mEtPhone.addTextChangedListener(new MyTextWatcher(mEtPhone));
        mEtNumber.addTextChangedListener(new MyTextWatcher(mEtNumber));

        mLayoutPhone= (TextInputLayout) findViewById(R.id.layout_phone);

        mLayoutNum= (TextInputLayout) findViewById(R.id.layout_num);

        mTvRegister= (TextView) findViewById(R.id.id_zhuce);
        mTvTime= (TextView) findViewById(R.id.time);

        mImgClose= (ImageView) findViewById(R.id.id_close);
        //第三方账号登录
        mImgQQ= (ImageView) findViewById(R.id.id_qq);
        mImgWX= (ImageView) findViewById(R.id.id_wx);
        mImgWB= (ImageView) findViewById(R.id.id_wb);
        //登录
        mBtnDengLu.setOnClickListener(this);
        //
        mBtngetComfirm.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        mImgClose.setOnClickListener(this);
        mImgQQ.setOnClickListener(this);
        mImgWX.setOnClickListener(this);
        mImgWB.setOnClickListener(this);
    }

    private void initSDK() {
        ShareSDK.initSDK(this);
        SMSSDK.initSDK(this,SMS_APPKEY,SMS_APPSECRET);
        //短信数据
        EventHandler eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg=new Message();
                msg.what=SMS_RE;
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.id_zhuce:
                Util.enterIntent(this, RegisterActivity.class);
                break;
            case R.id.id_close:
                Util.backIntent(this);
                break;
            case R.id.id_denglu:
                //设置按钮动画
                confirm();
                break;
            case R.id.id_get_message:
                getNumMessage();
                startDaojishi();
                break;
            //qq控件
            case R.id.id_qq:
                //QQ空间
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                authorize(qzone);
                break;
            //微信
            case R.id.id_wx:
                //微信登录
                //测试时，需要打包签名；sample测试时，用项目里面的demokey.keystore
                //打包签名apk,然后才能产生微信的登录
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
            //新浪微博
            case R.id.id_wb:
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(sina);
                break;
        }
    }

    //倒计时
    private void startDaojishi() {
        Message msg=new Message();
        msg.what=MSG_START_TIME;
        msg.obj=30;
        handler.sendMessageDelayed(msg,1000);
    }

    private void confirm() {
        if (isPhoneValid()&&isNumValid()){
            SMSSDK.submitVerificationCode("+86",
                    mEtPhone.getText().toString().trim(),
                    mEtNumber.getText().toString().trim());
        }
    }
    public void getNumMessage() {
        if(isPhoneValid()){
            //获取验证码
            SMSSDK.getVerificationCode("+86",mEtPhone.getText().toString().trim());
        }
    }
    //动态监听输入过程
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.id_phone_number:
                    isPhoneValid();
                    break;
                case R.id.id_yanzhengma:
                    isNumValid();
                    break;
            }

        }
    }

    //判断输入的数字是合理
    private boolean isNumValid() {
        if (mEtNumber.getText().toString().trim().isEmpty()||
                mEtNumber.getText().toString().trim().equals("")||
                mEtNumber.getText().toString().trim().length()>4){
            mEtNumber.setError("验证码输入有误!!!");
            mEtNumber.requestFocus();
            return false;
        }

        mLayoutNum.setErrorEnabled(true);
        return true;

    }

    //判断输入的手机号是否可以使用
    private boolean isPhoneValid() {
        if (mEtPhone.getText().toString().trim().isEmpty()||
                mEtPhone.getText().toString().trim().equals("")||
                mEtPhone.getText().toString().trim().length()>11){
            mEtPhone.setError("手机号输入有误!!!");
            mEtPhone.requestFocus();
            return false;
        }

        mLayoutPhone.setErrorEnabled(true);
        return true;
    }

    //执行授权,获取用户信息
    //文档：http://wiki.mob.com/Android_%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99
    private void authorize(Platform plat) {

        if (plat==null){
            return;
        }
        if (plat.isAuthValid()) {
            plat.removeAccount(true);
        }
        plat.setPlatformActionListener(this);
        //关闭SSO授权
        plat.SSOSetting(true);
        //获取用户的资料
        plat.showUser(null);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> res) {

        if (i == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj =platform.getDb();
            handler.sendMessage(msg);
        }
    }
    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

        if (i == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_ERROR);
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {

        if (i == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_CANCEL);
        }
    }
}