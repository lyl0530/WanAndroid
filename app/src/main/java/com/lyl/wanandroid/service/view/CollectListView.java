package com.lyl.wanandroid.service.view;

import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.base.BaseView;
import com.lyl.wanandroid.service.entity.CollectListResult;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public interface CollectListView extends BaseView {
    void getCollectListSuccess(CollectListResult res);
    void unCollectSuccess(BaseResult res, int pos);
}
