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
    //��ҳ - banner
    void getBannerSuccess(BannerResult res);
    void getBannerFailed(String msg);
    //�ö�����
    void getTopArticleSuccess(TopArticleResult res);
    void getTopArticleFailed(String msg);
    //��ҳ����
    void getMainArticleSuccess(MainArticleResult res);
    void getMainArticleFailed(String msg);

    //�����ӿ� �������
    void Finish();
}
