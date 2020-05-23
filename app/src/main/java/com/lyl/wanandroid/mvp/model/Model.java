package com.lyl.wanandroid.mvp.model;

import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.bean.BannerResult;
import com.lyl.wanandroid.bean.LoginResult;
import com.lyl.wanandroid.bean.LogoutResult;
import com.lyl.wanandroid.bean.RegisterResult;
import com.lyl.wanandroid.constant.Const;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.retrofit.RetrofitHelper;
import com.lyl.wanandroid.util.LogUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lym on 2020/5/15
 * Describe :
 */
public class Model {
    private static final String TAG = "Model";

    public void register(String userName, String pwd, String rePwd, RequestListener<RegisterResult> l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .register(userName, pwd, rePwd)
                .compose(getTransformer())
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

    public void login(String userName, String pwd, RequestListener<LoginResult> l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .login(userName, pwd)
                .compose(getTransformer())
                .subscribe(new Observer<LoginResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(LoginResult result) {
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

    public void logout(RequestListener<LogoutResult> l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .logout()
                .compose(getTransformer())
                .subscribe(new Observer<LogoutResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LogoutResult result) {
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

    public void getBanner(RequestListener<BannerResult> l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .getBanner()
                .compose(getTransformer())
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

    protected <T> ObservableTransformer getTransformer() {
        return /*(ObservableTransformer<T, T>) */RESULT_TRANSFORMER;
    }

    protected static final ObservableTransformer RESULT_TRANSFORMER = new ResultTransformer();

    private static class ResultTransformer<T, R> implements ObservableTransformer<T, R> {
        @Override
        public ObservableSource<R> apply(Observable<T> upstream) {
            return upstream
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .map(new Function<T, R>() {
                        @Override
                        public R apply(T result) throws Exception {
                            if (result == null) {
                                LogUtils.e(TAG, "result == null");
                                throw new RuntimeException("response body is null");
                            }
                            if (result instanceof BaseResult) {
                                BaseResult r = (BaseResult) result;
                                int code = r.getErrorCode();
                                LogUtils.e(TAG, "result error code " + code);
                            }
                            return (R) result;
                        }
                    })
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.e(TAG, throwable.getMessage());
                        }
                    });
        }
    }

    private <T, V> ObservableTransformer<T, V> getResultTransformer() {
        return new ObservableTransformer<T, V>() {
            @Override
            public ObservableSource<V> apply(Observable<T> upstream) {
                return upstream
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .map(new Function<T, V>() {
                            @Override
                            public V apply(T result) throws Exception {
                                if (result == null) {
                                    LogUtils.e(TAG, "result == null");
                                    throw new RuntimeException("response body is null");
                                }
                                if (result instanceof BaseResult) {
                                    BaseResult r = (BaseResult) result;
                                    int code = r.getErrorCode();
                                    LogUtils.e(TAG, "result error code " + code);
                                }
                                return (V) result;
                            }
                        })
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtils.e(TAG, throwable.getMessage());
                            }
                        });
            }
        };
    }
}