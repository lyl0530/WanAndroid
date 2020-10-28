package com.lyl.wanandroid.listener;

import com.lyl.wanandroid.service.entity.ArticleBean;

/**
 * Created by lym on 2020/10/27
 * Describe : 文章列表界面 对外的接口
 */
public interface OnArticleListListener {
    //文章 点击item的对外接口
    void onItemClick(ArticleBean bean);
    //收藏和取消收藏 点击item的对外接口
    void onItemCollect(int articleId, int position, boolean isCollect);
}
