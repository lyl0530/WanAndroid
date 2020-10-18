package com.lyl.wanandroid.service.view;

import com.lyl.wanandroid.base.BaseView;
import com.lyl.wanandroid.service.entity.LogoutResult;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public interface LogoutView extends BaseView {
    void Success(LogoutResult res);
}
