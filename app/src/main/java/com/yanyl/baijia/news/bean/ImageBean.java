package com.yanyl.baijia.news.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by yanyl on 2016/10/25.
 */
public class ImageBean extends DataSupport {

    private String title;//标题
    private String img;//图片的路径
    private String content;
    private String time;//图片的额时间

    public ImageBean() {
    }

    public ImageBean(String title, String img, String content, String time) {
        this.title = title;
        this.img = img;
        this.content = content;
        this.time=time;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", videoUrl='" + content + '\'' +
                '}';
    }
}
