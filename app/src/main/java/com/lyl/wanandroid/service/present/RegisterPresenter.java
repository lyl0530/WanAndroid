package com.lyl.wanandroid.service.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.service.entity.RegisterResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.service.view.RegisterView;

/**
 * Created by lym on 2020/5/5
 * Describe :
 */
public class RegisterPresenter extends BasePresenter<RegisterView> {
    public void register(String uName, String pwd, String rePwd) {
        getModel().register(uName, pwd, rePwd, new RequestListener() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BaseResult data) {
                if (data instanceof RegisterResult) {
                    getView().Success((RegisterResult) data);
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
