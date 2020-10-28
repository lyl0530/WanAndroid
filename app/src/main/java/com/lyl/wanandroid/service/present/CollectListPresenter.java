package com.lyl.wanandroid.service.present;

import android.support.constraint.Barrier;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.service.RetrofitHelper;
import com.lyl.wanandroid.service.entity.CollectListResult;
import com.lyl.wanandroid.service.view.CollectListView;
import com.lyl.wanandroid.service.view.CollectView;
import com.lyl.wanandroid.utils.ConstUtil;
import com.lyl.wanandroid.utils.LogUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lym on 2020/5/6
 * Describe : 收藏页面，收藏文章的列表 和 取消收藏功能
 */
public class CollectListPresenter extends BasePresenter<CollectListView> {
    private static final String TAG = "CollectListPresenter lyl123";

    public void collectList(int id) {
        getModel().collectList(id, new RequestListener<BaseResult>() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BaseResult res) {
                if (res instanceof CollectListResult) {
                    getView().getCollectListSuccess((CollectListResult)res);
                }
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

    public void unCollectArticle(int pageIndex, int originId, int pos) {
        getModel().unCollectArticle(pageIndex, originId,
                new RequestListener<BaseResult>() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BaseResult res) {
                getView().unCollectSuccess(res, pos);
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
