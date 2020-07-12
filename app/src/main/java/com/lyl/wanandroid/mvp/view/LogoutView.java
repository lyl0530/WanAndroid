package com.lyl.wanandroid.mvp.view;

import com.lyl.wanandroid.base.BaseView;
import com.lyl.wanandroid.bean.LogoutResult;
import com.lyl.wanandroid.bean.TopArticleResult;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public interface LogoutView extends BaseView {
    void Success(LogoutResult res);
}
