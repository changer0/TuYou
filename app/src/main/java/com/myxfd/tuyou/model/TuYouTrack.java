package com.myxfd.tuyou.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by liangyue on 16/10/31.
 */

public class TuYouTrack extends BmobObject {
    public final static Integer TYPE_IMAGE = 1;
    public final static Integer TYPE_VIDEO = 2;
    private TuYouUser userId;//用户id
    private String text;//内容
    private String image;//图片
    private String Video;//视频
    private Integer type;//布局类型

    public static Integer getTypeImage() {
        return TYPE_IMAGE;
    }

    public static Integer getTypeVideo() {
        return TYPE_VIDEO;
    }

    public TuYouUser getUserId() {
        return userId;
    }

    public void setUserId(TuYouUser userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
