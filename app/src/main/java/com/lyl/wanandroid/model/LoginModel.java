package com.lyl.wanandroid.model;

import android.util.Log;

import com.lyl.wanandroid.bean.LoginResult;
import com.lyl.wanandroid.constant.Const;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.retrofit.RetrofitHelper;
import com.lyl.wanandroid.retrofit.WanApi;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lym on 2020/3/29
 * Describe : 数据层，负责从网络或数据库中获取数据
 */
public class LoginModel {
    private static final String TAG = "lym LoginModel";
    public void login(String userName, String pwd, RequestListener<LoginResult> l) {
        l.onStart();
        RetrofitHelper.getInstance().getRetrofit(Const.WAN_ANDROID_BASE_URL)
                .create(WanApi.class)
                .login(userName, pwd)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(LoginResult result) {
                        Log.d(TAG, "onNext: " + result.toString());
                        if (Const.SUCCESS_CODE == result.getErrorCode()) {
                            l.onSuccess(result);
                        } else {
                            l.onFailed(result.getErrorCode(), result.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onNext: " + e.getMessage());
                        l.onFailed(Const.FAILED_CODE_LOGIN, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

}
