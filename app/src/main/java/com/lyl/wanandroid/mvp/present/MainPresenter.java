package com.lyl.wanandroid.mvp.present;

import android.util.Log;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.bean.BannerResult;
import com.lyl.wanandroid.bean.MainArticleResult;
import com.lyl.wanandroid.bean.TopArticleResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.mvp.view.MainView;
import com.lyl.wanandroid.util.LogUtils;

/**
 * Created by lym on 2020/5/6
 * Describe :
 */
public class MainPresenter extends BasePresenter<MainView> {
    private boolean getBannerSuccess = false;
    private boolean getTopArticleSuccess = false;
    private boolean getMainArticleSuccess = false;

    private boolean getBannerFinish = false;
    private boolean getTopArticleFinish = false;
    private boolean getMainArticleFinish = false;
    private static final String TAG = "MainPresenter lyl123";

    private void isAllFinish(){
        if (!getBannerSuccess && !getTopArticleSuccess && !getMainArticleSuccess){
            getView().Failed("all_fail");
        }

        if (getBannerFinish && getTopArticleFinish && getMainArticleFinish) {
            LogUtils.e(TAG, " All Finish:");
            getView().Finish();
        }
    }

    public void getBanner() {
        getModel().getBanner(new RequestListener<BannerResult>() {
            @Override
            public void onStart() {
                //getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BannerResult res) {
                getBannerSuccess = true;
                getView().getBannerSuccess(res);
            }

            @Override
            public void onFailed(String msg) {
                getView().getBannerFailed(msg);
            }

            @Override
            public void onFinish() {
                //getView().hideProgressDialog();
                getBannerFinish = true;
                isAllFinish();
            }
        });
    }

    public void getTopArticle() {
        getModel().getTopArticle(new RequestListener<TopArticleResult>() {
            @Override
            public void onStart() {
                //getView().showProgressDialog();
            }

            @Override
            public void onSuccess(TopArticleResult res) {
                getTopArticleSuccess = true;
                getView().getTopArticleSuccess(res);
            }

            @Override
            public void onFailed(String msg) {
                getView().getTopArticleFailed(msg);
            }

            @Override
            public void onFinish() {
                //getView().hideProgressDialog();
                getTopArticleFinish = true;
                isAllFinish();
            }
        });
    }

    public void getMainArticle(int pageIndex) {
        getModel().getMainArticle(pageIndex, new RequestListener<MainArticleResult>() {
            @Override
            public void onStart() {
                //getView().showProgressDialog();
            }

            @Override
            public void onSuccess(MainArticleResult res) {
                getMainArticleSuccess = true;
                getView().getMainArticleSuccess(res);
            }

            @Override
            public void onFailed(String msg) {
                getView().getMainArticleFailed(msg);
            }

            @Override
            public void onFinish() {
                //getView().hideProgressDialog();
                getMainArticleFinish = true;
                isAllFinish();
            }
        });
    }
}
