package com.lyl.wanandroid.listener;

import com.lyl.wanandroid.base.BaseResult;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public interface RequestListener/*<T>*/ {
    void onStart();
    void onSuccess(/*int code, */BaseResult data);
    void onFailed(int code, String msg);
    //    void onError(ExceptionHandle handle);
    void onFinish();
}
