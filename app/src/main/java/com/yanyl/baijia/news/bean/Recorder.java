package com.yanyl.baijia.news.bean;

/**
 * Created by yanyl on 2016/10/30.
 */
public class Recorder {

    public float time;
    public String filePath;

    public Recorder(float time, String filePath) {
        this.time = time;
        this.filePath = filePath;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
