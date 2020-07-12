package com.lyl.wanandroid.mvp.view;

import com.lyl.wanandroid.base.BaseView;
import com.lyl.wanandroid.bean.LoginResult;
import com.lyl.wanandroid.bean.TopArticleResult;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public interface LoginView extends BaseView {
    void Success(LoginResult res);
}
