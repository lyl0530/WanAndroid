package com.lyl.wanandroid.listener;

import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

/**
 * Created by lym on 2020/9/27
 * Describe :
 */
public interface ScrollViewListener {
    void onScrollChanged(HorizontalScrollView scrollView, int l, int t, int oldl, int oldt);
}
