package com.lyl.wanandroid.ui.adapter.home;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.lyl.wanandroid.service.entity.DataBean;

public interface IDelegateAdapter {
 
    // 查找委托时调用的方法，返回自己能处理的类型即可。
    boolean isForViewType(DataBean bean);

    // 用于委托Adapter的onCreateViewHolder方法
    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);
 
    // 用于委托Adapter的onBindViewHolder方法
    void onBindViewHolder(RecyclerView.ViewHolder holder, int position, DataBean bean, int topArticleCnt);
 
}