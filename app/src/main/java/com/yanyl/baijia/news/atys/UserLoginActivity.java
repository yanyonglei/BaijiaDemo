package com.yanyl.baijia.news.atys;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanyl.baijia.news.R;

import com.yanyl.baijia.news.bean.User;
import com.yanyl.baijia.news.common.Const;
import com.yanyl.baijia.news.util.Util;
import com.yanyl.baijia.news.view.CircleImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yanyl on 2016/10/21.
 * 用户登录界面
 */
public class UserLoginActivity extends BaseActivity {

    private static final int CAM=1;
    private static final int PHOTO=2;

    //退出标记
    private static final int EXIT=0;

    //登录用户图片
    private CircleImageView mLoginImageView;
    //返回
    private LinearLayout mBackLinear;

    //用户名
    private TextView mUserNameView;
    //退出登录按钮
    private Button mExitButton;


    private String url= Environment.getExternalStorageDirectory().getPath();
    //图片的文件名
    private File phoneFile;

    private SharedPreferences sp;
    private   Bitmap bitmap=null;

    //是否登录的标记
    private boolean isDengLu=false;

    private LinearLayout mLlDLu;

    private Handler handlerThird=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==EXIT){

                //清空 xml 文件的数据
                SharedPreferences.Editor mEditor=sp.edit();
                mEditor.remove("username");
                mEditor.remove("icon");
                mEditor.remove("phone");
                mEditor.remove("name");
                mEditor.commit();
                mUserNameView.setText("点击登录");
                mLoginImageView.setImageResource(R.drawable.default_avatar);

            }
            super.handleMessage(msg);
        }
    };
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user);
        initView();
        sp = getSharedPreferences("User", MODE_PRIVATE);

        //第三方账号  qq  微信 微博
        String name=sp.getString("username",null);
        String icon=sp.getString("icon",null);
        //手机号登录用户
        String phone=sp.getString("phone",null);
        //注册的用户
        String name_comme=sp.getString("name",null);

        if (name_comme!=null){

            mUserNameView.setText(name_comme);
            isDengLu=true;
        }

        if (phone!=null){
            isDengLu=true;
            mUserNameView.setText(phone);
        }

        if (icon!=null&&name!=null){
            isDengLu=true;
            mUserNameView.setText(name);
            getBitmap(icon);
        }
    }

    private void getBitmap(String url) {
        //异步获取第三方登录用户的图片
        new AsyncTask<String ,Void ,Bitmap>()
        {

            @Override
            protected Bitmap doInBackground(String... params) {

                String path=params[0];
                try {
                    URL url=new URL(path);
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    int code=connection.getResponseCode();
                    if (code==200){
                        InputStream inputStream=connection.getInputStream();
                        bitmap= BitmapFactory.decodeStream(inputStream);
                    }
                    return bitmap;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                mLoginImageView.setImageBitmap(bitmap);
            }
        }.execute(url);
    }
    private void initView() {
        mLlDLu= (LinearLayout) findViewById(R.id.id_ll);
        mLlDLu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.enterIntent(UserLoginActivity.this, LoginActivity.class);
            }
        });
        mUserNameView = (TextView) findViewById(R.id.id_user_setting);
        mUserNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.enterIntent(UserLoginActivity.this, LoginActivity.class);
            }
        });
        mLoginImageView = (CircleImageView) findViewById(R.id.id_login);
        mLoginImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDengLu){
                    dialog = new Dialog(UserLoginActivity.this,R.style.ActionSheetDialogStyle);
                    View view=getLayoutInflater().inflate(R.layout.dialog_photo,null);
                    Button mBtnPhoto= (Button) view.findViewById(R.id.id_photo);
                    mBtnPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, PHOTO);
                            dialog.dismiss();
                        }
                    });
                    Button mBtnCam= (Button) view.findViewById(R.id.id_cam);
                    mBtnCam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentImg=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            phoneFile=new File(url+"/"+Util.getTime()+".jpg");
                            intentImg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));
                            startActivityForResult(intentImg,CAM);
                            dialog.dismiss();
                        }
                    });
                    Button mBtnCancel= (Button) view.findViewById(R.id.id_cancel);
                    mBtnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setContentView(view);
                    //获取当前Activity所在的窗体
                    Window dialogWindow = dialog.getWindow();
                    //设置Dialog从窗体底部弹出
                    dialogWindow.setGravity( Gravity.BOTTOM);
                    //获得窗体的属性
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.y = 10;//设置Dialog距离底部的距离
                    //将属性设置给窗体
                    dialogWindow.setAttributes(lp);
                    dialog.show();//显示对话框
                }else{
                    Util.showToast(UserLoginActivity.this,"请先登录");
                }
            }
        });
        mBackLinear = (LinearLayout) findViewById(R.id.id_back);
        mBackLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Util.backIntent(UserLoginActivity.this);
            }
        });

        mExitButton = (Button) findViewById(R.id.id_exit);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDengLu){
                    Util.showToast(UserLoginActivity.this,"已经离线");

                }else{
                    isDengLu=false;
                    Message message=new Message();
                    message.what=EXIT;
                    handlerThird.sendMessage(message);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //调用照相机
            case CAM:
                //  Bitmap map = BitmapFactory.decodeFile(phoneFile.getAbsolutePath());
                //压缩图片
                Bitmap bitmap=Util.scaleImg(phoneFile.getAbsolutePath(),200,200);
                mLoginImageView.setImageBitmap(bitmap);

                break;
            //相册
            case PHOTO:
                if (data!=null&&resultCode==RESULT_OK){
                    Uri selectedImage = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    //获取文件的路径
                    String imagePath = c.getString(columnIndex);
                    Bitmap map= Util.scaleImg(imagePath,200,200);
                    mLoginImageView.setImageBitmap(map);
                }
                break;
        }
    }
}