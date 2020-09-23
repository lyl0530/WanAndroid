package com.lyl.wanandroid.mvp.view;

import com.lyl.wanandroid.base.BaseView;
import com.lyl.wanandroid.bean.ProjectResult;
import com.lyl.wanandroid.bean.RegisterResult;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public interface ProjectView extends BaseView {
    void Success(ProjectResult res);
}
