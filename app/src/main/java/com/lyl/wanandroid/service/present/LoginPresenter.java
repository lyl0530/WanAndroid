package com.lyl.wanandroid.service.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.service.entity.LoginResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.service.view.LoginView;



/**
 * Created by lym on 2020/3/29
 * Describe :
 *      Presenter中没有Android相关的类, 是一个纯Java的程序.
 *      不再与Android Framework中的类如Activity, Fragment等关联,这样有利于解耦和测试.
 *      (所以一个检查方法是看你的presenter的import中有没有android的包名.)
 */
public class LoginPresenter extends BasePresenter<LoginView> {
    public void login(String userName, String pwd){
        getModel().login(userName, pwd, new RequestListener() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BaseResult res) {
                if (res instanceof LoginResult) {
                    getView().Success((LoginResult)res);
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                getView().Failed(code, msg);
            }

            @Override
            public void onFinish() {
                getView().hideProgressDialog();
            }
        });
    }
}
