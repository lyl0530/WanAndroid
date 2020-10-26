package com.lyl.wanandroid.service.view;

import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.base.BaseView;

/**
 * Created by lym on 2020/10/26
 * Describe :
 */
public interface CollectView extends BaseView {
    void collectArticleSuccess(BaseResult res, int position);
    void collectArticleFailed(String msg);
    void unCollectArticleSuccess(BaseResult res, int position);
    void unCollectArticleFailed(String msg);
}
