package com.lyl.wanandroid.mvp.view;

import com.lyl.wanandroid.base.BaseView;
import com.lyl.wanandroid.bean.NavigationResult;
import com.lyl.wanandroid.bean.TopArticleResult;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public interface NavigationView extends BaseView {
    void Success(NavigationResult res);
}
