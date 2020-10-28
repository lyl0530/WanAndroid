package com.lyl.wanandroid.service.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.service.entity.ProjectArticleListResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.service.view.ProjectArticleListView;

/**
 * Created by lym on 2020/5/5
 * Describe :
 */
public class ProjectArticleListPresenter extends BasePresenter<ProjectArticleListView> {
    public void getProjectArticleList(int curPageId,int cid) {
        getModel().getProjectArticleList(curPageId, cid, new RequestListener() {
            @Override
            public void onStart() {
                //getView().showProgressDialog();
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
