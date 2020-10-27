package com.lyl.wanandroid.listener;

/**
 * Created by lym on 2020/10/27
 * Describe :收藏和取消收藏 点击item的对外接口
 */
public interface OnItemCollectListener {
    void onItemCollect(int articleId, int position, boolean isCollect);
}
