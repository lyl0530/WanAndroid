package com.lyl.wanandroid.view;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public interface LoginView {

    void showProgressDialog();
    void hideProgressDialog();
    void loginSuccess();
    void loginFailed(String msg);
}
