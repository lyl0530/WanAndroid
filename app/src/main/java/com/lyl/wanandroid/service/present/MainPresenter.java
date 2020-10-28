package com.lyl.wanandroid.service.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.service.entity.BannerResult;
import com.lyl.wanandroid.service.entity.MainArticleResult;
import com.lyl.wanandroid.service.entity.TopArticleResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.service.view.MainView;
import com.lyl.wanandroid.utils.ErrorUtil;
import com.lyl.wanandroid.utils.LogUtil;

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
            getView().Failed(ErrorUtil.CODE_OTHERS, "all request failed!");
        }

        if (getBannerFinish && getTopArticleFinish && getMainArticleFinish) {
            LogUtil.e(TAG, " All Finish:");
            getView().Finish();
            //三个变量要置false，否则，每次刷新都会走三次Finish方法。
            //登录成功收藏功能就会有问题。
            getBannerFinish = false;
            getTopArticleFinish = false;
            getMainArticleFinish = false;
        }
    }

    public void getBanner() {
        getModel().getBanner(new RequestListener() {
            @Override
            public void onStart() {
                //getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BaseResult res) {
                if (res instanceof BannerResult) {
                    getBannerSuccess = true;
                    getView().getBannerSuccess((BannerResult)res);
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                getBannerSuccess = false;
                getView().getBannerFailed(code, msg);
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
        getModel().getTopArticle(new RequestListener() {
            @Override
            public void onStart() {
                //getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BaseResult res) {
                try {
                    if (res instanceof TopArticleResult) {
                        getTopArticleSuccess = true;
                        getView().getTopArticleSuccess((TopArticleResult)res);
                    }
                } catch (Exception e){
                    LogUtil.e(TAG, e.toString());
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                getTopArticleSuccess = false;
                getView().getTopArticleFailed(code, msg);
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
        getModel().getMainArticle(pageIndex, new RequestListener() {
            @Override
            public void onStart() {
                //getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BaseResult res) {
                try {
                    if (res instanceof MainArticleResult) {
                        getMainArticleSuccess = true;
                        getView().getMainArticleSuccess((MainArticleResult)res);
                    }
                } catch (Exception e){
                    LogUtil.e(TAG, e.toString());
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                getMainArticleSuccess = false;
                getView().getMainArticleFailed(code, msg);
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
