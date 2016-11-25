package com.yanyl.baijia.news.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanyl.baijia.news.R;


/**
 * Created by yanyl on 2016/10/8.
 */
public class DialogManager {

    private Dialog dialog;

    private ImageView mIcon;
    private ImageView mVoice;

    private TextView mLable;

    private Context mContext;

    public DialogManager(Context context) {
        mContext = context;
    }

    public void showRecordDialog(){

        dialog=new Dialog(mContext, R.style.Theme_AudioDialog);

        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.dialog_record,null);
        dialog.setContentView(view);
        mIcon= (ImageView) dialog.findViewById(R.id.id_record_dialog_icon);
        mVoice= (ImageView) dialog.findViewById(R.id.id_record_dialog_voice);
        mLable= (TextView) dialog.findViewById(R.id.id_record_dialog);
        dialog.show();
    }

    public void recording(){

        if (dialog!=null&& dialog.isShowing()){

            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.recorder);
            mLable.setText("手指上滑，取消发送");
        }
    }
    public void wantToCancel(){
        if (dialog!=null&& dialog.isShowing()){

            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.cancel);
            mLable.setText("松开手指，取消发送");
        }

    }

    public void tooShort(){

        if (dialog!=null&& dialog.isShowing()){

            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);
            mIcon.setImageResource(R.drawable.voice_to_short);
            mLable.setText("录音时间过短，取消发送");
        }
    }
    public void dismissDialog(){
        if (dialog!=null&& dialog.isShowing()){
            dialog.dismiss();
            dialog=null;
        }
    }

    /***
     * 更新 图片信息
     * @param level
     */
    public void updateVoiceLevel(int level){
        if (dialog!=null&& dialog.isShowing()){
            //通过方法名找到资源名
            int resId=mContext.getResources().getIdentifier("v"+level,"drawable",mContext.getPackageName());
            mVoice.setImageResource(resId);
        }
    }
}
