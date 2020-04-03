package com.lyl.wanandroid.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.bean.LoginResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.model.LoginModel;
import com.lyl.wanandroid.view.LoginView;



/**
 * Created by lym on 2020/3/29
 * Describe :
 *      Presenter中没有Android相关的类, 是一个纯Java的程序.
 *      不再与Android Framework中的类如Activity, Fragment等关联,这样有利于解耦和测试.
 *      (所以一个检查方法是看你的presenter的import中有没有android的包名.)
 */
public class LoginPresent extends BasePresenter<LoginView> {
    private final LoginModel mModel;
    private LoginView mView;

    public LoginPresent(){
        mModel = new LoginModel();
    }

    @Override
    public void attach(LoginView view) {
        super.attach(view);
        mView = view;
    }

    @Override
    public void detach() {
        if (null != getView()) {
            mView = null;
        }
        super.detach();
    }
    
    public void login(String userName, String pwd){
        mModel.login(userName, pwd, new RequestListener<LoginResult>() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(LoginResult res) {
                getView().loginSuccess(res);
            }

            @Override
            public void onFailed(/*int code, */String msg) {
                getView().loginFailed(msg);
            }

            @Override
            public void onFinish() {
                getView().hideProgressDialog();
            }
        });
    }
}
