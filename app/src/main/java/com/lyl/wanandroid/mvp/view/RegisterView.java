package com.lyl.wanandroid.mvp.view;

import com.lyl.wanandroid.base.BaseView;
import com.lyl.wanandroid.bean.RegisterResult;
import com.lyl.wanandroid.bean.TopArticleResult;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public interface RegisterView extends BaseView {
    void Success(RegisterResult res);
}
