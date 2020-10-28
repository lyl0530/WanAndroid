package com.lyl.wanandroid.service.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.service.entity.ProjectResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.service.view.ProjectView;

/**
 * Created by lym on 2020/5/5
 * Describe :
 */
public class ProjectPresenter extends BasePresenter<ProjectView> {
    public void getProject() {
        getModel().getProject(new RequestListener() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BaseResult data) {
                if (data instanceof ProjectResult) {
                    getView().Success((ProjectResult)data);
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
