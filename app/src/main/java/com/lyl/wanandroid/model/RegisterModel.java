package com.lyl.wanandroid.model;

import com.lyl.wanandroid.bean.RegisterResult;
import com.lyl.wanandroid.constant.Const;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.retrofit.RetrofitHelper;
import com.lyl.wanandroid.util.LogUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lym on 2020/5/5
 * Describe : 数据层，负责从网络或数据库中获取数据
 */
public class RegisterModel {
    private static final String TAG = RegisterModel.class.getSimpleName();

    public void register(String userName, String pwd, String rePwd, RequestListener<RegisterResult> l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .register(userName, pwd, rePwd)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RegisterResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(RegisterResult result) {
                        LogUtils.d(TAG, "onNext: " + result);
                        if (null == result) {
                            l.onFailed("结果为空");
                            return;
                        }
                        if (Const.SUCCESS_CODE == result.getErrorCode()) {
                            l.onSuccess(result);
                        } else {
                            l.onFailed(result.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onNext: " + e.getMessage());
                        l.onFailed(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

}
