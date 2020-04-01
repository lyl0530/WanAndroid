package com.lyl.wanandroid.present;

import android.util.Log;

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
public class LoginPresent {
    private static final String TAG = "lym LoginPresent";
    private LoginModel mModel;
    private LoginView mView;

    public LoginPresent(LoginView view){
        mView = view;
        mModel = new LoginModel();
    }

    public void login(String userName, String pwd){
        mView.showProgressDialog();
        mModel.login(userName, pwd, new RequestListener<LoginResult>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(LoginResult data) {
                hidePD();
                Log.d(TAG, "onSuccess: " + data.toString());
            }

            @Override
            public void onFailed(int code, String msg) {
                hidePD();
                Log.d(TAG, "onFailed: " + code + ", " + msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void hidePD(){
        try {
            Thread.sleep(2000);
            mView.hideProgressDialog();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
