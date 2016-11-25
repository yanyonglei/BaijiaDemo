package com.yanyl.baijia.news.atys;


import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.down.DownLoadProgressListener;
import com.yanyl.baijia.news.down.FileDownLoaded;
import com.yanyl.baijia.news.util.Util;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yanyl on 2016/10/26.
 */
public class DownActivity extends BaseActivity{


    private static final int PROCESSING = 1;
    private static final int FAILUER = 0;
    private String path;

    private Button mBtnDown;
    private Button mBtnStop;

    private ButtonListener listener;

    private ProgressBar mProBar;

    private TextView mTvwResult;



    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROCESSING:
                    int size=msg.getData().getInt("size");
                    mProBar.setProgress(size);
                    float num=(float) mProBar.getProgress()/(float)mProBar.getMax();
                    int result=(int)(num *100);
                    mTvwResult.setText("完成 ："+result+" %");

                    if (mProBar.getProgress()==mProBar.getMax()){

                        Util.showToast(getApplicationContext(),"下载成功");
                    }
                    break;
                case FAILUER:
                    Util.showToast(getApplicationContext(),"下载失败");
                    break;

            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down);
        path = getIntent().getStringExtra("detail");

        mBtnDown= (Button) findViewById(R.id.id_down);
        mBtnStop= (Button) findViewById(R.id.id_stop);
        mProBar= (ProgressBar) findViewById(R.id.progressBar);
        mTvwResult= (TextView) findViewById(R.id.id_result);

        listener=new ButtonListener();

        mBtnDown.setOnClickListener(listener);
        mBtnStop.setOnClickListener(listener);

    }

    private final class ButtonListener implements View.OnClickListener {
        @Override
       public void onClick(View v) {
            switch (v.getId()){
                case R.id.id_down:
                        File saveDir=Environment.getExternalStorageDirectory();
                        download(path,saveDir);
                        mBtnDown.setEnabled(false);
                        mBtnStop.setEnabled(true);
                    break;
                case R.id.id_stop:
                    exit();
                    mBtnDown.setEnabled(true);
                    mBtnStop.setEnabled(false);
                    break;
            }
        }
        private DownLoadTask task;
        public void exit(){
            if (task!=null){
                task.exit();
            }
        }
        private void download(String path,File saveDir){
            task=new DownLoadTask(path,saveDir);
            new Thread(task).start();
        }

        private final class DownLoadTask implements Runnable{

            private String path;
            private File saveDir;
            private FileDownLoaded loader;

            public DownLoadTask(String path, File saveDir) {
                this.path = path;
                this.saveDir = saveDir;
            }

            public void exit(){
                if (loader!=null){
                    loader.exits();
                }
            }

            DownLoadProgressListener listener = new DownLoadProgressListener() {
                @Override
                public void onDownloadSize(int size) {
                    Message message=new Message();
                    message.what=PROCESSING;
                    message.getData().putInt("size",size);
                    handler.sendMessage(message);

                }

            };

            @Override
            public void run() {
                try {
                    loader=new FileDownLoaded(getApplicationContext(),path,saveDir,3);
                    mProBar.setMax(loader.getFileSize());
                    loader.download(listener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
