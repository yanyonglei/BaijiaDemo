package com.yanyl.baijia.news.down;

import android.content.Context;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;

import java.net.URL;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yanyl on 2016/9/14.
 */
public class FileDownLoaded {
    /**
     * 响应码
     */
    private static final int RESPONSEOK=200;
    private Context context;//上下文对象
   // private FileService fileService;//获取本地的业务bean
    private boolean exites;//停止下载的标志
    private int downloadsize=0;//已下载文件的长度
    private int filesize=0;//原始文件的大小
    private DownloadThread[] threads;//根据线程数设置下载线程池
    private File saveFile;//数据保存到本地

    private Map<Integer,Integer> data=
            new ConcurrentHashMap<Integer, Integer>();//缓存各个线程的长度

    private int block;//每条线程下载的长度
    private String downloadurl;//下载的路径

    /**
     * 获取线程数
     * @return
     */
    public int getThreadSize(){
        return threads.length;
    }
    public void exits(){
        this.exites=true;
    }

    public boolean getExits(){
        return this.exites;
    }

    /**
     * 获取文件的大小
     * @return
     */
    public int getFileSize(){
        return filesize;
    }

    /**
     * 累计已下载大小
     * @param size
     */
    protected synchronized void append(int size){

        downloadsize+=size;
    }

    protected synchronized  void update(int threadid,int pos){
        this.data.put(threadid,pos);
    }

    public FileDownLoaded(Context context,String downloadurl,File fileSaveDir,int threadNum){
        try {
            this.context=context;
            this.downloadurl=downloadurl;

            URL url=new URL(this.downloadurl);

            if (!fileSaveDir.exists()) fileSaveDir.mkdirs();
            this.threads=new DownloadThread[threadNum];

            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);


            //设置客户端可以接受的媒体类型
            connection.setRequestProperty("Accept",
                    "image/gif,image/jpeg,image/pjpeg,image/pjpeg," +
                    "application/x-shockwave-flash,application/xmal+xml,application/x-xpsdocument,"+
                    "application/x-ms-xbap,application/x-ms-application," +
                            "application/vnd.ms-excel,application/vnd.ms-powerpoint," +
                            "application/msword,*/*");
            //设置客户端语言
            connection.setRequestProperty("Accept-Language","zh-CN");
            //设置请求页面来源
            connection.setRequestProperty("Referer",downloadurl);
            //设置客户端编码
            connection.setRequestProperty("Chartset","utf-8");
            //设置用户代理
            connection.setRequestProperty("User-Agent","Mozilla/4.0(compatible;MSIE 8.0;Windows NT 5.2;" +
                    "Trident/4.0;.NET CLR 1.1.4322;.NET CLR 3.0.04506.30;.NET CLR 3.0.4506.2152;.NET CLR 3.5.3.729)");
            //设置Connection的方式
            connection.setRequestProperty("Connection","Keep-Alive");


            connection.connect();//和远程资源建立的真正联系
            printResponseHeader(connection);//返回http头字段
            if (connection.getResponseCode()==RESPONSEOK){
                this.filesize=connection.getContentLength();
                if (this.filesize<=0){
                    throw new RuntimeException("unKnow file size");
                }
                String filename=getFileName(connection);
                this.saveFile=new File(fileSaveDir,filename);
                if (this.data.size()==this.threads.length){
                    for (int i=0;i<this.threads.length;i++){
                        this.downloadsize+=this.data.get(i+1);
                    }
                    print("已经下载的长度"+this.downloadsize+"个字节");
                }
                this.block=
                        (this.filesize%this.threads.length)==0
                                ?this.filesize/this.threads.length:
                                this.filesize/this.threads.length+1;
            }else{
                print("服务器的响应吗"+connection.getResponseCode());
                connection.getResponseMessage();
                throw new RuntimeException("server response error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getFileName(HttpURLConnection connection){
        String filename=this.downloadurl.substring(this.downloadurl.lastIndexOf('/')+1);
        if (filename==null||"".equals(filename.trim())){
            for (int i=0;;i++){
                String mine=connection.getHeaderField(i);
                if (mine==null) break;
                if ("content-disposition".equals(connection.getHeaderFieldKey(i).toLowerCase())){
                    Matcher m= Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
                    if (m.find())
                        return m.group(1);
                }
            }
            filename= UUID.randomUUID()+".jpg";
        }
        return filename;
    }
    public int download(DownLoadProgressListener listener)throws Exception{
        RandomAccessFile raf=new RandomAccessFile(this.saveFile,"rwd");
        if (this.filesize>0)raf.setLength(this.filesize);
        raf.close();

        URL url=new URL(this.downloadurl);
        if (this.data.size()!=this.threads.length){
            this.data.clear();

            for (int i=0;i<this.threads.length;i++){
                this.data.put(i+1,0);
            }
            this.downloadsize=0;
        }
        for (int i=0;i<this.threads.length;i++){
            int downloadLength=this.data.get(i+1);

            if (downloadLength<this.block&&this.downloadsize<this.filesize){
                this.threads[i]=new DownloadThread(this,url,this.saveFile,this.block,this.data.get(i+1),i+1);
                this.threads[i].setPriority(7);//设置优先级

                this.threads[i].start();
            }else{
                this.threads[i]=null;
            }
        }


        boolean notFinished=true;
        while (notFinished){
            Thread.sleep(900);
            notFinished=false;
            for (int i=0;i<this.threads.length;i++){
                if (this.threads[i]!=null&& !this.threads[i].isFinished()){
                    notFinished=true;
                    if (this.threads[i].getDownLoadedLength()==-1){

                        this.threads[i]=new DownloadThread(this,url,this.saveFile,this.block,this.data.get(i+1),i+1);
                        this.threads[i].setPriority(7);//设置优先级

                        this.threads[i].start();
                    }

                }
                if (listener!=null)
                    listener.onDownloadSize(this.downloadsize);
            }
        }
        return this.downloadsize;
    }

    /**
     * 获取Http响应
     * @param connection
     * @return
     */
    public static Map<String ,String> getHttpResposeHeader(HttpURLConnection connection){

        //使用LinkedHashMap保证写入和遍历顺序相同，允许存在空值
        Map<String ,String> header= new LinkedHashMap<String, String>();
        for (int i=0;;i++){

            String fileValue=connection.getHeaderField(i);//返回第n个头字段的值
            if (fileValue==null) break;
            header.put(connection.getHeaderFieldKey(i),fileValue);
        }
        return header;
    }
    public static void printResponseHeader(HttpURLConnection http){

        Map<String ,String> header= getHttpResposeHeader(http);
        for (Map.Entry<String,String> entry:header.entrySet()){
            //
            String key=entry.getKey()!=null?entry.getKey()+":":"";
            print(key+entry.getValue());
        }
    }
    public static  void print(String msg){
        System.out.println(msg);
    }

}
