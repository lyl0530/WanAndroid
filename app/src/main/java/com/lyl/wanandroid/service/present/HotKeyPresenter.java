package com.lyl.wanandroid.service.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.service.entity.HotKeyResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.service.view.HotKeyView;

/**
 * Created by lym on 2020/5/5
 * Describe :
 */
public class HotKeyPresenter extends BasePresenter<HotKeyView> {
    public void getHotKey() {
        getModel().getHotKey(new RequestListener<HotKeyResult>() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(HotKeyResult data) {
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
