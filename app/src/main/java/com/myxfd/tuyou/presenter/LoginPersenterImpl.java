package com.myxfd.tuyou.presenter;

import com.myxfd.tuyou.biz.TuYouUserBiz;
import com.myxfd.tuyou.listerer.OnLoginListener;
import com.myxfd.tuyou.model.TuYouUser;
import com.myxfd.tuyou.view.MainView;

/**
 * Created by liangyue on 16/10/31.
 */

class LoginPresenterImpl implements LoginPresenter , OnLoginListener{
    private TuYouUserBiz userBiz;
    private MainView mainView;

    public LoginPresenterImpl(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onLoginSuccess(TuYouUser user) {

    }

    @Override
    public void onLoginFailed(String message) {

    }
}
