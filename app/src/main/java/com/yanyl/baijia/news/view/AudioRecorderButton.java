package com.yanyl.baijia.news.view;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.util.AudioManager;


/**
 * Created by yanyl on 2016/10/8.
 */
public class AudioRecorderButton extends Button implements AudioManager.AudioStateListener {


    //
    private static final int DISTANCE_Y_CANCEL=50;
    //正常状态
    private static final int STATTE_NORMAL=1;
    //录音状态
    private static final int STATTE_RECORDING=2;
    //撤销状态
    private static final int STATTE_WANT_TO_CANEL=3;

    private  int mCurState=STATTE_NORMAL;
    //开始录音
    private boolean isRecording=false;

    //dialog 的管理
    private DialogManager manager;

    private AudioManager audioManager;

    private float mTime;
    //是否触发
    private boolean mReady;


    public AudioRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        manager=new DialogManager(context);

        String dir=Environment.getExternalStorageDirectory()+"/yanyl";
       // String dir="/data/data/sdcard"+"/yanyl";
        audioManager=AudioManager.getInstace(dir);
        audioManager.setmListener(this);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                audioManager.prepareAudio();
                mReady=true;
                return false;
            }
        });
    }

    /**
     * 录音完成后的回调
     */
    public interface  AudioFinishRecorderListener{
        void onFinish(float second, String filepath);
    }

    private AudioFinishRecorderListener mListener;

    public void setmListener(AudioFinishRecorderListener mListener) {
        this.mListener = mListener;
    }

     private Runnable mGetVoiceLevelRunnale=new Runnable() {
        @Override
        public void run() {
            while (isRecording){
                try {
                    Thread.sleep(100);
                    mTime+=0.1f;
                    handler.sendEmptyMessage(MSG_VOICE_CHANGE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private static final int MSG_AUDIO_PREPARED=0x1110;
    private static final int MSG_VOICE_CHANGE=0x1111;
    private static final int MSG_DIALOG_DISMISS=0x1112;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_AUDIO_PREPARED:
                    manager.showRecordDialog();
                    isRecording=true;

                    new Thread(mGetVoiceLevelRunnale).start();
                    break;
                case MSG_VOICE_CHANGE:

                    manager.updateVoiceLevel(audioManager.getVoiceLevel(7));

                    break;
                case  MSG_DIALOG_DISMISS:
                    manager.dismissDialog();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    public void wellPrepared() {

        handler.sendEmptyMessage(MSG_AUDIO_PREPARED);

    }

    public AudioRecorderButton(Context context) {
        this(context,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();

        int x= (int) event.getX();
        int y= (int) event.getY();

        switch (action){

            case MotionEvent.ACTION_DOWN:
                changeState(STATTE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                //判断是否开始录音
                if (isRecording){
                    //根据x,y的坐标，判断是否取消
                    if (wantToCancel(x,y)){

                        changeState(STATTE_WANT_TO_CANEL);

                    }else{
                        changeState(STATTE_RECORDING);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!mReady){
                    reset();
                    return  super.onTouchEvent(event);
                }

                if (!isRecording || mTime<0.6f){
                    manager.tooShort();
                    audioManager.cancel();
                    handler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS,1300);
                }else if (mCurState==STATTE_RECORDING){
                    manager.dismissDialog();

                    if (mListener!=null){
                        mListener.onFinish(mTime,audioManager.getCurrentFilePath());
                    }
                    audioManager.release();
                }else if(mCurState==STATTE_WANT_TO_CANEL){

                    manager.dismissDialog();
                    audioManager.cancel();
                }
                reset();
                break;
        }
        return super.onTouchEvent(event);
    }

    /***
     * 回复标志位 状态
     */
    private void reset() {

        isRecording=false;
        mReady=false;
        mTime=0;
        changeState(STATTE_NORMAL);
    }


    private boolean wantToCancel(int x, int y) {

        if (x<0||x>getWidth()){
            return true;
        }
        if (y<-DISTANCE_Y_CANCEL||y>getHeight()+DISTANCE_Y_CANCEL){
            return true;
        }
        return false;
    }

    private void changeState(int state) {
        if (mCurState!=state){
            mCurState=state;
            switch (state){
                case STATTE_NORMAL:

                    setBackgroundResource(R.drawable.btn_record_nomal);
                    setText(R.string.str_record_nomal);
                    break;
                case STATTE_RECORDING:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.str_record_recording);
                    //开始录音
                    if (isRecording){

                        manager.recording();
                    }
                    break;
                case STATTE_WANT_TO_CANEL:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.str_record_want_cancel);

                    manager.wantToCancel();
                    break;
            }
        }
    }
}
