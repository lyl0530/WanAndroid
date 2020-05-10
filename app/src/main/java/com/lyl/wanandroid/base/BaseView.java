package com.lyl.wanandroid.base;


/**
 * Created by lym on 2020/5/5
 * Describe :
 */
public interface BaseView<T> {
    void showProgressDialog();

    void hideProgressDialog();

    void Success(T res);

    void Failed(String msg);
}
