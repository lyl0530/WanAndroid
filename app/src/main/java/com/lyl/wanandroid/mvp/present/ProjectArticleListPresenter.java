package com.lyl.wanandroid.mvp.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.bean.ProjectArticleListResult;
import com.lyl.wanandroid.bean.ProjectResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.mvp.view.ProjectArticleListView;
import com.lyl.wanandroid.mvp.view.ProjectView;

/**
 * Created by lym on 2020/5/5
 * Describe :
 */
public class ProjectArticleListPresenter extends BasePresenter<ProjectArticleListView> {
    public void getProjectArticleList(int curPageId,int cid) {
        getModel().getProjectArticleList(curPageId, cid, new RequestListener<ProjectArticleListResult>() {
            @Override
            public void onStart() {
                //getView().showProgressDialog();
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
