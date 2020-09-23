package com.lyl.wanandroid.mvp.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.bean.ProjectResult;
import com.lyl.wanandroid.bean.RegisterResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.mvp.view.ProjectView;
import com.lyl.wanandroid.mvp.view.RegisterView;

/**
 * Created by lym on 2020/5/5
 * Describe :
 */
public class ProjectPresenter extends BasePresenter<ProjectView> {
    public void getProject() {
        getModel().getProject(new RequestListener<ProjectResult>() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(ProjectResult data) {
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
