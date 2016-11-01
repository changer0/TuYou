package com.myxfd.tuyou.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Lulu on 2016/10/31.
 * 点赞
 */
public class TuYouPraise extends BmobObject {

    private TuYouTrack track;//说说
    private TuYouUser user;//用户

    public TuYouTrack getTrack() {
        return track;
    }

    public void setTrack(TuYouTrack track) {
        this.track = track;
    }

    public TuYouUser getUser() {
        return user;
    }

    public void setUser(TuYouUser user) {
        this.user = user;
    }
}
