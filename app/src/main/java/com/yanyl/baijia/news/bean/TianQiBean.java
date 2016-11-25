package com.yanyl.baijia.news.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by yanyl on 2016/11/17.
 */
public class TianQiBean extends  DataSupport{

    private String msg;//反馈信息 成功与否

    //日期
    private String date;
    private String province;//省份
    private String city;//城市
    private String dis;//地区
    private String wind;//风
    private String day;//白天
    private String night;//夜间
    private String temperature;//温度




    public TianQiBean(String msg, String date, String temperature,
                      String wind,String night,String city,String dis,String province,
                      String day) {
        this.msg = msg;
        this.date = date;
        this.temperature = temperature;
        this.wind = wind;
        this.night=night;
        this.city=city;
        this.dis=dis;
        this.province=province;
        this.day=day;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public String getNight() {
        return night;
    }

    public void setNight(String night) {
        this.night = night;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }


    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    @Override
    public String toString() {
        return "TianQiBean{" +
                "msg='" + msg + '\'' +
                ", date='" + date + '\'' +
                ", temperature='" + temperature + '\'' +
                ", wind='" + wind + '\'' +
                ", night='" + night + '\'' +
                ", city='" + city + '\'' +
                ", dis='" + dis + '\'' +
                ", province='" + province + '\'' +
                ", day='" + day + '\'' +
                '}';
    }
}
