package com.lyl.wanandroid.service.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.service.entity.RegisterResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.service.view.RegisterView;

/**
 * Created by lym on 2020/5/5
 * Describe :
 */
public class RegisterPresenter extends BasePresenter<RegisterView> {
    public void register(String uName, String pwd, String rePwd) {
        getModel().register(uName, pwd, rePwd, new RequestListener<RegisterResult>() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(RegisterResult data) {
                getView().Success(data);
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
