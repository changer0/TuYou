package com.myxfd.tuyou.listerer;

import com.myxfd.tuyou.model.TuYouUser;

/**
 * Created by liangyue on 16/10/31.
 */

public interface OnLoginListener {
    void onLoginSuccess(TuYouUser user);
    void onLoginFailed(String message);
}
