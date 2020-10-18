package com.lyl.wanandroid.service.view;

import com.lyl.wanandroid.base.BaseView;
import com.lyl.wanandroid.service.entity.NavigationResult;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public interface NavigationView extends BaseView {
    void Success(NavigationResult res);
}
