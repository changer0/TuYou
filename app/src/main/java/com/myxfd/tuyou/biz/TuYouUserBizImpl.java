package com.myxfd.tuyou.biz;

import com.myxfd.tuyou.listerer.OnLoginListener;
import com.myxfd.tuyou.model.TuYouUser;

/**
 * Created by liangyue on 16/10/31.
 */

public class TuYouUserBizImpl implements TuYouUserBiz {
    private OnLoginListener loginListener;
    @Override
    public boolean login(TuYouUser user) {
        return false;
    }
}
