package com.lyl.wanandroid.service.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.service.entity.ProjectArticleListResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.service.view.SearchView;

/**
 * Created by lym on 2020/5/5
 * Describe :
 */
public class SearchPresenter extends BasePresenter<SearchView> {
    public void search(int pageIndex, String key) {
        getModel().search(pageIndex, key, new RequestListener() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BaseResult data) {
                if (data instanceof ProjectArticleListResult) {
                    getView().Success((ProjectArticleListResult)data);
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                getView().Failed(code, msg);
            }

            @Override
            public void onFinish() {
                getView().hideProgressDialog();
            }
        });
    }
}
