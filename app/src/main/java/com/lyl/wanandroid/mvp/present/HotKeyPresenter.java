package com.lyl.wanandroid.mvp.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.bean.HotKeyResult;
import com.lyl.wanandroid.bean.ProjectArticleListResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.mvp.view.HotKeyView;
import com.lyl.wanandroid.mvp.view.SearchView;

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
