package com.lyl.wanandroid.mvp.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.bean.BannerResult;
import com.lyl.wanandroid.bean.TopArticleResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.mvp.view.MainView;

/**
 * Created by lym on 2020/5/6
 * Describe :
 */
public class MainPresenter extends BasePresenter<MainView> {
    public void getBanner() {
        getModel().getBanner(new RequestListener<BannerResult>() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BannerResult res) {
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

    public void getTopArticle() {
        getModel().getTopArticle(new RequestListener<TopArticleResult>() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(TopArticleResult res) {
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
