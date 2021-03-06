package com.lyl.wanandroid.service.entity;

import com.lyl.wanandroid.base.BaseResult;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by lym on 2020/7/12
 * Describe : 置顶文章的返回结果
 */
public class TopArticleResult extends BaseResult {

    private List<ArticleBean> data;

    public List<ArticleBean> getData() {
        return data;
    }

    public void setData(List<ArticleBean> data) {
        this.data = data;
    }

    @NotNull
    @Override
    public String toString() {
        return "TopArticleResult{" +
                "data=" + data +
                '}';
    }
}
