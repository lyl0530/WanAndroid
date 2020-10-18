package com.lyl.wanandroid.service.view;

import com.lyl.wanandroid.base.BaseView;
import com.lyl.wanandroid.service.entity.RegisterResult;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public interface RegisterView extends BaseView {
    void Success(RegisterResult res);
}
