package com.lyl.wanandroid.service.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.service.view.CollectView;

/**
 * Created by lym on 2020/5/6
 * Describe :
 */
public class CollectPresenter extends BasePresenter<CollectView> {
    private static final String TAG = "CollectPresenter lyl123";

    //收藏站内文章
    public void collectArticle(int id, int position) {
        getModel().collectArticle(id, new RequestListener() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BaseResult res) {
                getView().collectArticleSuccess(res, position);
            }

            @Override
            public void onFailed(int code, String msg) {
                getView().collectArticleFailed(code, msg);
            }

            @Override
            public void onFinish() {
                getView().hideProgressDialog();
            }
        });
    }

    //文章列表处取消收藏
    public void unCollectArticle(int id, int position) {
        getModel().unCollectArticle(id, new RequestListener() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BaseResult res) {
                getView().unCollectArticleSuccess(res, position);
            }

            @Override
            public void onFailed(int code, String msg) {
                getView().unCollectArticleFailed(code, msg);
            }

            @Override
            public void onFinish() {
                getView().hideProgressDialog();
            }
        });
    }
}
