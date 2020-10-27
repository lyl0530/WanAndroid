package com.lyl.wanandroid.service.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.service.entity.BannerResult;
import com.lyl.wanandroid.service.entity.MainArticleResult;
import com.lyl.wanandroid.service.entity.TopArticleResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.service.view.MainView;
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
            getView().Failed("all_fail");
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
                getBannerSuccess = false;
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
                try {
                    getView().getTopArticleSuccess(res);
                } catch (Exception e){
                    LogUtil.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailed(String msg) {
                getTopArticleSuccess = false;
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
                try {
                    getView().getMainArticleSuccess(res);
                } catch (Exception e){
                    LogUtil.e(TAG, e.getMessage());
                }

            }

            @Override
            public void onFailed(String msg) {
                getMainArticleSuccess = false;
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

//    //收藏站内文章
//    public void collectArticle(int id, int position) {
//        getModel().collectArticle(id, new RequestListener<BaseResult>() {
//            @Override
//            public void onStart() {
//                getView().showProgressDialog();
//            }
//
//            @Override
//            public void onSuccess(BaseResult res) {
//                getView().collectArticleSuccess(res, position);
//            }
//
//            @Override
//            public void onFailed(String msg) {
//                getView().collectArticleFailed(msg);
//            }
//
//            @Override
//            public void onFinish() {
//                getView().hideProgressDialog();
//            }
//        });
//    }
//
//    //文章列表处取消收藏
//    public void unCollectArticle(int id, int position) {
//        getModel().unCollectArticle(id, new RequestListener<BaseResult>() {
//            @Override
//            public void onStart() {
//                getView().showProgressDialog();
//            }
//
//            @Override
//            public void onSuccess(BaseResult res) {
//                getView().unCollectArticleSuccess(res, position);
//            }
//
//            @Override
//            public void onFailed(String msg) {
//                getView().unCollectArticleFailed(msg);
//            }
//
//            @Override
//            public void onFinish() {
//                getView().hideProgressDialog();
//            }
//        });
//    }
}
