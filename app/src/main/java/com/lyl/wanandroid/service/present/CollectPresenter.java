package com.lyl.wanandroid.service.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.service.entity.BannerResult;
import com.lyl.wanandroid.service.entity.MainArticleResult;
import com.lyl.wanandroid.service.entity.TopArticleResult;
import com.lyl.wanandroid.service.view.CollectView;
import com.lyl.wanandroid.service.view.MainView;
import com.lyl.wanandroid.utils.LogUtil;

/**
 * Created by lym on 2020/5/6
 * Describe :
 */
public class CollectPresenter extends BasePresenter<CollectView> {
    private static final String TAG = "CollectPresenter lyl123";

    //收藏站内文章
    public void collectArticle(int id, int position) {
        getModel().collectArticle(id, new RequestListener<BaseResult>() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BaseResult res) {
                getView().collectArticleSuccess(res, position);
            }

            @Override
            public void onFailed(String msg) {
                getView().collectArticleFailed(msg);
            }

            @Override
            public void onFinish() {
                getView().hideProgressDialog();
            }
        });
    }

    //文章列表处取消收藏
    public void unCollectArticle(int id, int position) {
        getModel().unCollectArticle(id, new RequestListener<BaseResult>() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BaseResult res) {
                getView().unCollectArticleSuccess(res, position);
            }

            @Override
            public void onFailed(String msg) {
                getView().unCollectArticleFailed(msg);
            }

            @Override
            public void onFinish() {
                getView().hideProgressDialog();
            }
        });
    }
}
