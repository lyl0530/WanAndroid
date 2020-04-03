package com.lyl.wanandroid.view;

import com.lyl.wanandroid.bean.LoginResult;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public interface LoginView {

    void showProgressDialog();
    void hideProgressDialog();
    void loginSuccess(LoginResult res);
    void loginFailed(String msg);
}
