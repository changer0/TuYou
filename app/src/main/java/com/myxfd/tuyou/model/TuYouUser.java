package com.myxfd.tuyou.model;

import java.util.Date;

import cn.bmob.v3.BmobUser;

/**
 * Created by Lulu on 2016/10/31.
 */

public class TuYouUser extends BmobUser{

    private Double lat;//维度
    private Double lgt;//经度
    private String icon;//用户头像
    private String sex;//性别
    private String birthday;//生日
    private Integer level;//等级
    private Double money;//余额
    private String qqId;//qq号
    private String sinaId;//新浪
    private String weiChatId;//微信
    private String type;//账号类型

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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
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
}
