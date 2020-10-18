package com.lyl.wanandroid.mvp.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.bean.ProjectArticleListResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.mvp.view.SearchView;

/**
 * Created by lym on 2020/5/5
 * Describe :
 */
public class SearchPresenter extends BasePresenter<SearchView> {
    public void search(int pageIndex, String key) {
        getModel().search(pageIndex, key, new RequestListener<ProjectArticleListResult>() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(ProjectArticleListResult data) {
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
