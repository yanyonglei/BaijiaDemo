package com.yanyl.baijia.news.bean;



import org.litepal.crud.DataSupport;

/**
 * Created by yanyl on 2016/10/20.
 * @author yanyl
 *
 * 数据库的设计 bean
 */
public class User extends DataSupport{



    private int _id;

    private String name;

    private String password;

    private String pwd;

    private String time;


    public User() {
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", pwd='" + pwd + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
