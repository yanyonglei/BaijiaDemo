package com.yanyl.baijia.news.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by yanyl on 2016/10/22.
 */
public class XinwenBean extends DataSupport{


    private String num;//新闻的编号
    private String time;//新闻的时间
    private String title;//标题

    private String imageUrl;//图片路径
    private String contentUrl;//具体路径

    public XinwenBean() {
    }
    public XinwenBean(String num, String time, String title, String imageUrl, String contentUrl) {
        this.num =num;
        this.time = time;
        this.title = title;
        this.imageUrl = imageUrl;
        this.contentUrl = contentUrl;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    @Override
    public String toString() {
        return "XinwenBean{" +
                "id='" + num + '\'' +
                ", time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                '}';
    }
}
