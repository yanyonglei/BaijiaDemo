package com.yanyl.baijia.news.atys;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.bean.IdeaBean;
import com.yanyl.baijia.news.util.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yanyl on 2016/11/7.
 * 意见反馈
 */
public class FankuiActivity extends BaseActivity implements View.OnClickListener {

    private static final int CAM=1;
    private static final int PHOTO=2;
    //添加图片按钮
    private ImageButton mImgPhoto;

    //图片显示控件
    private LinearLayout mLlImgView;

    private EditText mEtFk;

    private String mStrYiJian;

    //内存路径
    private String url= Environment.getExternalStorageDirectory().getPath();
    //图片的文件名
    private File phoneFile;
    //图片路径
    private String imagePath;

    private Dialog dialog;

    //返回按钮
    private Button mBtnBack;
    //发送按钮
    private Button mBtnSend;
    //建议类
    private IdeaBean ideaBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fankui);

        initViews();
    }

    private void initViews() {
        mEtFk= (EditText) findViewById(R.id.id_yijian);


        mImgPhoto= (ImageButton) findViewById(R.id.id_fankui_photo);
        mImgPhoto.setOnClickListener(this);
        mEtFk= (EditText) findViewById(R.id.id_yijian);
        mBtnBack= (Button) findViewById(R.id.back);
        mBtnBack.setOnClickListener(this);
        mBtnSend= (Button) findViewById(R.id.id_send);
        mBtnSend.setOnClickListener(this);
        mLlImgView= (LinearLayout) findViewById(R.id.id_img_ll);
    }

    @Override
    public void onClick(View v) {
        ideaBean=new IdeaBean();
        switch (v.getId()){
            case R.id.id_fankui_photo:
                showDialog();
                break;
            //发送
            case R.id.id_send:
                saveToDb();
                Util.showToast(FankuiActivity.this,"已经发送");
                mEtFk.setText("");
                mLlImgView.removeAllViews();
                break;

            case R.id.back:
                Util.backIntent(FankuiActivity.this);
                break;
            //取消
            case R.id.id_cancel:
                dialog.dismiss();
                break;
            //照相机
            case R.id.id_cam:
                Intent intentImg=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                phoneFile=new File(url+"/"+Util.getTime()+".jpg");
                intentImg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));
                startActivityForResult(intentImg,CAM);
                dialog.dismiss();
                break;

            //相册
            case R.id.id_photo:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PHOTO);
                dialog.dismiss();
                break;
        }
    }

    private void saveToDb() {
        ideaBean.setTime(Util.getTime());
        ideaBean.setIdeaStr(mEtFk.getText().toString());
        ideaBean.getPhotoPaths().add(imagePath);
        ideaBean.getPhotoPaths().add(phoneFile.getAbsolutePath());
        ideaBean.save();
    }
    //弹出对话框
    private void showDialog() {
        dialog = new Dialog(FankuiActivity.this, R.style.ActionSheetDialogStyle);
        View view=getLayoutInflater().inflate(R.layout.dialog_photo,null);
        Button mBtnPhoto= (Button) view.findViewById(R.id.id_photo);
        mBtnPhoto.setOnClickListener(this);
        Button mBtnCam= (Button) view.findViewById(R.id.id_cam);
        mBtnCam.setOnClickListener(this);
        Button mBtnCancel= (Button) view.findViewById(R.id.id_cancel);
        mBtnCancel.setOnClickListener(this);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //调用照相机
            case CAM:
                Bitmap bitmap=Util.scaleImg(phoneFile.getAbsolutePath(),200,200);
                //压缩图片
                ImageView image=new ImageView(this);
                image.setLayoutParams(new ViewGroup.LayoutParams(300,300));
                image.setImageBitmap(bitmap);
                mLlImgView.addView(image);
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
                     imagePath = c.getString(columnIndex);
                    //图片的路径加入数据库
                    Bitmap map= Util.scaleImg(imagePath,200,200);
                    ImageView imageView=new ImageView(this);
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(300,300));
                    imageView.setImageBitmap(map);
                    mLlImgView.addView(imageView);
                }
                break;
        }
    }
}