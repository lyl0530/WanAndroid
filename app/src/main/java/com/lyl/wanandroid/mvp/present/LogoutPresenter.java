package com.lyl.wanandroid.mvp.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.bean.LogoutResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.mvp.view.LogoutView;


/**
 * Created by lym on 2020/3/29
 * Describe :
 * Presenter中没有Android相关的类, 是一个纯Java的程序.
 * 不再与Android Framework中的类如Activity, Fragment等关联,这样有利于解耦和测试.
 * (所以一个检查方法是看你的presenter的import中有没有android的包名.)
 */
public class LogoutPresenter extends BasePresenter<LogoutView> {
    public void logout() {
        getModel().logout(new RequestListener<LogoutResult>() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(LogoutResult res) {
                getView().Success(res);
            }

            @Override
            public void onFailed(String msg) {
                getView().Failed(msg);
            }

            @Override
            public void onFinish() {
                getView().hideProgressDialog();
            }
        });
    }
}
