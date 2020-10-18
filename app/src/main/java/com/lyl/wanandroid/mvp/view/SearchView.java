package com.lyl.wanandroid.mvp.view;

import com.lyl.wanandroid.base.BaseView;
import com.lyl.wanandroid.bean.ProjectArticleListResult;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public interface SearchView extends BaseView {
    void Success(ProjectArticleListResult res);
}
