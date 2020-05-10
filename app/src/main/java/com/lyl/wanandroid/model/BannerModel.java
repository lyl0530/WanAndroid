package com.lyl.wanandroid.model;

import com.lyl.wanandroid.bean.BannerResult;
import com.lyl.wanandroid.constant.Const;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.retrofit.RetrofitHelper;
import com.lyl.wanandroid.util.LogUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lym on 2020/3/29
 * Describe : 数据层，负责从网络或数据库中获取数据
 */
public class BannerModel {
    private static final String TAG = BannerModel.class.getSimpleName();

    public void getBanner(RequestListener<BannerResult> l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .getBanner()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BannerResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BannerResult result) {
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
