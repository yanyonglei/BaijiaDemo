package com.yanyl.baijia.news.bean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanyl on 2016/11/23.
 */
public class IdeaBean  extends DataSupport{

    //建议字符串
    private String ideaStr;
    //图片路径
    private List<String> photoPaths=new ArrayList<String>();
    //时间
    private String time;


    public String getIdeaStr() {
        return ideaStr;
    }

    public void setIdeaStr(String ideaStr) {
        this.ideaStr = ideaStr;
    }

    public List<String> getPhotoPaths() {
        return photoPaths;
    }

    public void setPhotoPaths(List<String> photoPaths) {
        this.photoPaths = photoPaths;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
