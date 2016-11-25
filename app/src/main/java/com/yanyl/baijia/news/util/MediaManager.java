package com.yanyl.baijia.news.util;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by yanyl on 2016/10/8.
 */
public class MediaManager {
    private  static MediaPlayer player;
    private static boolean isPause;
    public static void playSound(String filePath, MediaPlayer.OnCompletionListener onCompletionListener) {
        if (player==null){
            player=new MediaPlayer();
            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    player.reset();
                    return false;
                }
            });
        }else{
            player.reset();
        }

        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnCompletionListener(onCompletionListener);
            player.setDataSource(filePath);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void pause(){
        if (player!=null&&player.isPlaying()){

            player.pause();
            isPause=true;
        }
    }

    public static void resume(){
        if (player!=null && isPause){
            player.start();
            isPause=false;
        }
    }

    public static void release(){
        if(player!=null){
            player.release();
            player=null;
        }
    }
}
