package com.yanyl.baijia.news.bean;

/**
 * Created by yanyl on 2016/10/4.
 */
public class Dot {


    int x,y;//记录当前的位置坐标
    int status;//状态

    public static final int STATUS_ON=1;//设置为障碍状态
    public static final int STATUS_OFF=0;//初始化下的状态
    public static final int STATUS_IN=9;//有猫存在状态

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
        status=STATUS_OFF;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setXY(int x,int y){
        this.x=x;
        this.y=y;
    }
}
