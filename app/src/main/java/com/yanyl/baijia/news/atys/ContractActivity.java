package com.yanyl.baijia.news.atys;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.adapter.RecorderAdapter;
import com.yanyl.baijia.news.bean.Recorder;
import com.yanyl.baijia.news.util.MediaManager;
import com.yanyl.baijia.news.util.Util;
import com.yanyl.baijia.news.view.AudioRecorderButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanyl on 2016/10/30.
 */
public class ContractActivity extends BaseActivity {

    private ListView listView;
    private ArrayAdapter<Recorder> adapter;
    private List<Recorder> datas=new ArrayList<Recorder>();
    //语音按钮
    private AudioRecorderButton audioRecorderButton;

    private View animView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);
        initView();
        audioRecorderButton.setmListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float second, String filepath) {

                Recorder recorder=new Recorder(second,filepath);
                datas.add(recorder);
                adapter.notifyDataSetChanged();

                listView.setSelection(datas.size()-1);

            }
        });
        adapter=new RecorderAdapter(this,datas);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (animView!=null){
                    animView.setBackgroundResource(R.drawable.adj);
                    animView=null;
                }
                //播放 动画
                animView=view.findViewById(R.id.id_amin);
                animView.setBackgroundResource(R.drawable.play_anim);
                //设置帧动画
                final AnimationDrawable anim= (AnimationDrawable) animView.getBackground();
                anim.start();

                //播放音频
                MediaManager.playSound(datas.get(position).filePath,new MediaPlayer.OnCompletionListener(){
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        animView.setBackgroundResource(R.drawable.adj);
                    }
                });

            }
        });
    }

    @Override
    protected void onPause() {

        super.onPause();
        MediaManager.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }

    private void initView() {
        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.backIntent(ContractActivity.this);
            }
        });
        listView= (ListView) findViewById(R.id.id_listview);
        audioRecorderButton= (AudioRecorderButton) findViewById(R.id.id_record_button);
    }
}
