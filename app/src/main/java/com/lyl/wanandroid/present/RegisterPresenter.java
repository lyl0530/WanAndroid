package com.lyl.wanandroid.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.bean.RegisterResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.model.RegisterModel;
import com.lyl.wanandroid.view.RegisterView;

/**
 * Created by lym on 2020/5/5
 * Describe :
 */
public class RegisterPresenter extends BasePresenter<RegisterView> {

    private RegisterView mView;
    private final RegisterModel mModel;

    public RegisterPresenter() {
        mModel = new RegisterModel();
    }

    @Override
    public void attach(RegisterView view) {
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

    public void register(String uName, String pwd, String rePwd) {
        mModel.register(uName, pwd, rePwd, new RequestListener<RegisterResult>() {
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
