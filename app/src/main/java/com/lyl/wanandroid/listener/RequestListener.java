package com.lyl.wanandroid.listener;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public interface RequestListener<T> {
    void onStart();
    void onSuccess(/*int code, */T data);
    void onFailed(/*int code, */String msg);
    //    void onError(ExceptionHandle handle);
    void onFinish();
}
