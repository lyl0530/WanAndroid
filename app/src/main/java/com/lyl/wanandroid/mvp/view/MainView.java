package com.lyl.wanandroid.mvp.view;

import com.lyl.wanandroid.base.BaseView;
import com.lyl.wanandroid.bean.BannerResult;
import com.lyl.wanandroid.bean.TopArticleResult;

/**
 * Created by lym on 2020/5/6
 * Describe :
 */
public interface MainView extends BaseView {
    void Success(BannerResult res);
    void Success(TopArticleResult res);
}
