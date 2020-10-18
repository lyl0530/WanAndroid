package com.lyl.wanandroid.listener;

import android.widget.HorizontalScrollView;

/**
 * Created by lym on 2020/9/27
 * Describe :
 */
public interface ScrollViewListener {
    void onScrollChanged(HorizontalScrollView scrollView, int l, int t, int oldl, int oldt);
}
