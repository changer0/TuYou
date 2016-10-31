package com.myxfd.tuyou.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Lulu on 2016/10/31.
 */

/**
 * 评论实体类
 */
public class TuYouComment extends BmobObject {

    private String commentId;//评论id
    private TuYouUser user;//用户
    private String text;//评论内容
    private TuYouTrack track;//说说

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public TuYouUser getUser() {
        return user;
    }

    public void setUser(TuYouUser user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TuYouTrack getTrack() {
        return track;
    }

    public void setTrack(TuYouTrack track) {
        this.track = track;
    }
}
