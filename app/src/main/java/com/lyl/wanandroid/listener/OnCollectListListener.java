package com.lyl.wanandroid.listener;

import com.lyl.wanandroid.service.entity.Article1Bean;

/**
 * Created by lym on 2020/10/27
 * Describe : 收藏列表界面 item点击和取消收藏
 */
public interface OnCollectListListener {
    void onItemClick(Article1Bean bean);
    void onItemUnCollect(int articleId, int originId, int position);
}
