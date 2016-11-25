package com.yanyl.baijia.news.down;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yanyl on 2016/9/14.
 */
public class DownloadThread extends Thread {

    private File saveFile;
    private URL downurl;
    private int block;
    private int threadId=-1;
    private int downloadLength;
    private boolean finished=false;
    private FileDownLoaded downLoaded;

    public DownloadThread(FileDownLoaded downLoaded,URL downurl,File saveFile,int block,int downloadLength,int threadId){
        this.downLoaded=downLoaded;
        this.downurl=downurl;
        this.saveFile=saveFile;
        this.block=block;
        this.downloadLength=downloadLength;
        this.threadId=threadId;
    }

    @Override
    public void run() {
        try {
            HttpURLConnection connection= (HttpURLConnection) downurl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);

            connection.setRequestProperty("Accept",
                    "image/gif,image/jpeg,image/pjpeg,image/pjpeg," +
                            "application/x-shockwave-flash,application/xmal+xml,application/x-xpsdocument,"+
                            "application/x-ms-xbap,application/x-ms-application," +
                            "application/vnd.ms-excel,application/vnd.ms-powerpoint," +
                            "application/msword,*/*");
            //设置客户端语言
            connection.setRequestProperty("Accept-Language","zh-CN");
            //设置请求页面来源
            connection.setRequestProperty("Referer",downurl.toString());
            //设置客户端编码
            connection.setRequestProperty("Chartset","utf-8");

            int startPos=block*(threadId-1)+downloadLength;
            int endPos=block*threadId-1;

            connection.setRequestProperty("Range","bytes"+startPos+"-"+endPos);
            //设置用户代理
            connection.setRequestProperty("User-Agent","Mozilla/4.0(compatible;MSIE 8.0;Windows NT 5.2;" +
                    "Trident/4.0;.NET CLR 1.1.4322;.NET CLR 3.0.04506.30;.NET CLR 3.0.4506.2152;.NET CLR 3.5.3.729)");
            //设置Connection的方式
            connection.setRequestProperty("Connection","Keep-Alive");


            InputStream inputStream=connection.getInputStream();
            byte[] buffer=new byte[1024];
            int offset=0;
            print("Thread "+this.threadId+"  from----"+startPos+"-----to  ----"+endPos);
            RandomAccessFile threadFile=new RandomAccessFile(this.saveFile,"rwd");
            threadFile.seek(startPos);
            while (!downLoaded.getExits()&&(offset=inputStream.read(buffer,0,1024))!=-1){

                threadFile.write(buffer,0,offset);
                downloadLength+=offset;
                downLoaded.update(this.threadId,downloadLength);
                downLoaded.append(offset);
            }

            threadFile.close();
            inputStream.close();

            if (downLoaded.getExits()){
                print("Thread"+this.threadId+"has been pause");
            }else {
                print("Thread"+this.threadId+"finish");
            }

            this.finished=true;

        } catch (Exception e) {
            e.printStackTrace();
            this.downloadLength=-1;
            print("Thread"+this.threadId+":"+e);
        }

    }

    private void print(String s) {
        System.out.println(s);
    }

    public boolean isFinished(){
        return finished;
    }

    public  long getDownLoadedLength(){
        return downloadLength;
    }
}
