package com.lyl.wanandroid.listener;

import com.lyl.wanandroid.service.entity.ArticleBean;

/**
 * Created by lym on 2020/10/27
 * Describe : 适配RecycleView的item点击事件
 */
public interface OnArticleItemListener {
    void onItemClick(ArticleBean bean);
}
