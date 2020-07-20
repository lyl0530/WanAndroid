package com.lyl.wanandroid.mvp.view;

import com.lyl.wanandroid.base.BaseView;
import com.lyl.wanandroid.bean.BannerResult;
import com.lyl.wanandroid.bean.MainArticleResult;
import com.lyl.wanandroid.bean.TopArticleResult;

/**
 * Created by lym on 2020/5/6
 * Describe :
 */
public interface MainView extends BaseView {
    //首页 - banner
    void getBannerSuccess(BannerResult res);
    void getBannerFailed(String msg);
    //置顶文章
    void getTopArticleSuccess(TopArticleResult res);
    void getTopArticleFailed(String msg);
    //首页文章
    void getMainArticleSuccess(MainArticleResult res);
    void getMainArticleFailed(String msg);

    //三个接口 调用完毕
    void Finish();
}
