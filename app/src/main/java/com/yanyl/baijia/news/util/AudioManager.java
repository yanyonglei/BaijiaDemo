package com.yanyl.baijia.news.util;

import android.media.MediaRecorder;

import java.io.File;

import java.util.UUID;

/**
 * Created by yanyl on 2016/10/8.
 */
public class AudioManager {

    private MediaRecorder recorder;

    private String dir;
    private String currentFilePath;

    private boolean isPrepare=false;

    private AudioManager(String dir){
        this.dir=dir;
    }

    public String getCurrentFilePath() {

        return currentFilePath;
    }

    public interface  AudioStateListener{
        void wellPrepared();
    }
    public AudioStateListener mListener;

    public void setmListener(AudioStateListener mListener) {
        this.mListener = mListener;
    }

    public static AudioManager mInstance;
    public static AudioManager getInstace(String dir){

        if (mInstance==null){
            synchronized (AudioManager.class){
                if (mInstance==null){
                    mInstance=new AudioManager(dir);
                }
            }
        }
        return mInstance;
    }

    public void prepareAudio(){

        try {
            isPrepare=false;
            File dirs=new File(dir);
            if(!dirs.exists()){
                dirs.mkdirs();
            }

            String fileName=generateFileName();
            File file=new File(dirs,fileName);


            currentFilePath=file.getAbsolutePath();
            //currentFilePath=file.getPath();


            recorder=new MediaRecorder();
            //设置输出文件
           recorder.setOutputFile(file.getAbsolutePath());
            //设置音频的格式为麦克风
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置音频格式
            recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            //设置音频编码 为amr
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(file.getAbsolutePath());
           // file.createNewFile();
            recorder.prepare();
            recorder.start();

            isPrepare=true;
            if (mListener!=null){
                mListener.wellPrepared();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 随机生成文件名
     * @return
     */
    private String generateFileName() {

        return UUID.randomUUID().toString()+".amr";
    }

    public int getVoiceLevel(int maxLevel){

        if (isPrepare){
            try{
                return (maxLevel*(recorder.getMaxAmplitude()/32768))+1;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return 1;
    }

    public void release(){

        if (recorder!=null){
            recorder.stop();
            recorder.release();
            recorder=null;
        }



    }


    public void cancel(){
        release();
        if (currentFilePath!=null){
            File f=new File(currentFilePath);
            f.delete();

        }
        currentFilePath=null;

    }

}
