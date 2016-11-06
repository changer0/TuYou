package com.myxfd.tuyou.model;

import android.icu.text.SimpleDateFormat;
import android.os.SystemClock;
import android.util.Log;
import android.widget.CompoundButton;

import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.BmobUser;

/**
 * Created by Lulu on 2016/10/31.
 */

public class TuYouUser extends BmobUser implements Comparable<TuYouUser> {
    private static final String TAG = "TuYouUser";
    private Double lat;//维度
    private Double lgt;//经度
    private String icon;//用户头像
    private String sex;//性别
    private Long birthday;//生日
    private Integer level;//等级
    private Double money;//余额
    private String qqId;//qq号
    private String sinaId;//新浪
    private String weiChatId;//微信
    private String type;//账号类型

    //注意此项不用与上传Bmob, 仅作为RecycleView中使用
    private int distance;// 与当前用户的距离

    //用于获取年龄
    public int getAge() {
        int ret = 0;
        if (birthday != null) {
            Calendar calendar = Calendar.getInstance();
            Date b = new Date(birthday);
            Date t = new Date(System.currentTimeMillis());
            calendar.setTime(b);
            int by = calendar.get(Calendar.YEAR);
            calendar.clear();
            calendar.setTime(t);
            int ty = calendar.get(Calendar.YEAR);
            ret = ty - by;
        }
        return ret;

    }

    //设置
    public void setAge(int age) {
        Calendar calendar = Calendar.getInstance();
        Date t = new Date(System.currentTimeMillis());


        calendar.setTime(t);
        int ty = calendar.get(Calendar.YEAR);
        System.out.println("年" + ty);
        calendar.clear();
        int by = ty - age; //出生年份
        System.out.println("by=>" + by);
        calendar.set(by, 1, 1);
        birthday = calendar.getTimeInMillis();
    }


    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQqId() {
        return qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId;
    }

    public String getSinaId() {
        return sinaId;
    }

    public void setSinaId(String sinaId) {
        this.sinaId = sinaId;
    }

    public String getWeiChatId() {
        return weiChatId;
    }

    public void setWeiChatId(String weiChatId) {
        this.weiChatId = weiChatId;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLgt() {
        return lgt;
    }

    public void setLgt(Double lgt) {
        this.lgt = lgt;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    @Override
    public int compareTo(TuYouUser o) {

        return distance - o.distance;
    }


}
