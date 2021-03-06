package com.lyl.wanandroid.service.manager;

import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.service.entity.BannerResult;
import com.lyl.wanandroid.service.entity.CollectListResult;
import com.lyl.wanandroid.service.entity.HierarchyResult;
import com.lyl.wanandroid.service.entity.HotKeyResult;
import com.lyl.wanandroid.service.entity.LoginResult;
import com.lyl.wanandroid.service.entity.LogoutResult;
import com.lyl.wanandroid.service.entity.MainArticleResult;
import com.lyl.wanandroid.service.entity.NavigationResult;
import com.lyl.wanandroid.service.entity.ProjectArticleListResult;
import com.lyl.wanandroid.service.entity.ProjectResult;
import com.lyl.wanandroid.service.entity.RegisterResult;
import com.lyl.wanandroid.service.entity.TopArticleResult;
import com.lyl.wanandroid.utils.ConstUtil;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.service.RetrofitHelper;
import com.lyl.wanandroid.utils.ErrorUtil;
import com.lyl.wanandroid.utils.LogUtil;

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
public class DataManager {
    private static final String TAG = "DataManager";

    public void register(String userName, String pwd, String rePwd, RequestListener l) {
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
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    public void login(String userName, String pwd, RequestListener l) {
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
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    public void logout(RequestListener l) {
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
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    public void getBanner(RequestListener l) {
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
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    //获取置顶文章
    public void getTopArticle(RequestListener l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .getTopArticle()
                .compose(getTransformer())
                .subscribe(new Observer<TopArticleResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(TopArticleResult result) {
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    //获取置顶文章
    public void getMainArticle(int pageIndex, RequestListener l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .getMainArticle(pageIndex)
                .compose(getTransformer())
                .subscribe(new Observer<MainArticleResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(MainArticleResult result) {
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    public void getHierarchy(RequestListener l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .getHierarchy()
                .compose(getTransformer())
                .subscribe(new Observer<HierarchyResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(HierarchyResult result) {
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    public void getNavigation(RequestListener l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .getNavigation()
                .compose(getTransformer())
                .subscribe(new Observer<NavigationResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(NavigationResult result) {
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    //收藏站内文章
    public void collectArticle(int id, RequestListener l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .collectArticle(id)
                .compose(getTransformer())
                .subscribe(new Observer<BaseResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BaseResult result) {
//                        LogUtils.d(TAG, "onNext: " + result);
//                        if (null == result) {
//                            l.onFailed("结果为空");
//                            return;
//                        }
//                        if (ConstUtil.SUCCESS_CODE == result.getErrorCode()) {
//                            l.onSuccess(result);
//                        } else if (ConstUtil.LOGIN_CODE == result.getErrorCode()) {
//                            l.onFailed(ConstUtil.LOGIN_MSG+id);
//                        } else {
//                            l.onFailed(result.getErrorMsg());
//                        }

                        if (ErrorUtil.CODE_NOT_LOGIN == result.getErrorCode()){
                            result.setErrorMsg(ConstUtil.LOGIN_MSG+id);
                        }
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    //文章列表处取消收藏
    public void unCollectArticle(int id, RequestListener l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .unCollectArticle(id)
                .compose(getTransformer())
                .subscribe(new Observer<BaseResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BaseResult result) {
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    //获取项目的整个分类
    public void getProject(RequestListener l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .getProject()
                .compose(getTransformer())
                .subscribe(new Observer<ProjectResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ProjectResult result) {
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    //获取单个项目的文章列表
    public void getProjectArticleList(int curPageId,
                                      int cid,
                                      RequestListener l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .getProjectArticleList(curPageId, cid)
                .compose(getTransformer())
                .subscribe(new Observer<ProjectArticleListResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ProjectArticleListResult result) {
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    //热词搜索
    public void getHotKey(RequestListener l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .getHotKey()
                .compose(getTransformer())
                .subscribe(new Observer<HotKeyResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(HotKeyResult result) {
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    //搜索
    public void search(int pageIndex, String key, RequestListener l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .search(pageIndex, key)
                .compose(getTransformer())
                .subscribe(new Observer<ProjectArticleListResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ProjectArticleListResult result) {
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    public void collectList(int pageIndex,RequestListener l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .collectList(pageIndex)
                .compose(getTransformer())
                .subscribe(new Observer<CollectListResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CollectListResult result) {
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    public void unCollectArticle(int pageIndex, int originId, RequestListener l) {
        l.onStart();
        RetrofitHelper.getWanApi()
                .unCollectArticle(pageIndex, originId)
                .compose(getTransformer())
                .subscribe(new Observer<BaseResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BaseResult result) {
                        handlerBaseResult(result, l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError: " + e.getMessage());
                        l.onFailed(ErrorUtil.CODE_OTHERS, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        l.onFinish();
                    }
                });
    }

    /**
     * result为空 -1002
     * 修改未登录的错误码为 -1001
     * 其他错误码为 -1
     * 成功为 0
     * 建议对errorCode 判断当不为0的时候，均为错误。
     */
    private void handlerBaseResult(BaseResult result, RequestListener l){
        LogUtil.d(TAG, "handlerBaseResult: " + result);
        if (null == result) {
            l.onFailed(ErrorUtil.CODE_RES_NULL, "");
            return;
        }
        if (ErrorUtil.CODE_SUCCESS == result.getErrorCode()) {
            l.onSuccess(result);
        } else {
            l.onFailed(result.getErrorCode(), result.getErrorMsg());
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> ObservableTransformer<T, T> getTransformer() {
        return (ObservableTransformer<T, T>) RESULT_TRANSFORMER;
    }

    protected static final ObservableTransformer RESULT_TRANSFORMER = new ResultTransformer();

    @SuppressWarnings("unchecked")
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
                                LogUtil.e(TAG, "result == null");
                                throw new RuntimeException("response body is null");
                            }
                            if (result instanceof BaseResult) {
                                BaseResult r = (BaseResult) result;
                                int code = r.getErrorCode();
                                LogUtil.e(TAG, "result error code " + code);
                            }
                            return (R) result;
                        }
                    })
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtil.e(TAG, throwable.getMessage());
                        }
                    });
        }
    }

//    private <T, V> ObservableTransformer<T, V> getResultTransformer() {
//        return new ObservableTransformer<T, V>() {
//            @Override
//            public ObservableSource<V> apply(Observable<T> upstream) {
//                return upstream
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.io())
//                        .map(new Function<T, V>() {
//                            @Override
//                            public V apply(T result) throws Exception {
//                                if (result == null) {
//                                    LogUtil.e(TAG, "result == null");
//                                    throw new RuntimeException("response body is null");
//                                }
//                                if (result instanceof BaseResult) {
//                                    BaseResult r = (BaseResult) result;
//                                    int code = r.getErrorCode();
//                                    LogUtil.e(TAG, "result error code " + code);
//                                }
//                                return (V) result;
//                            }
//                        })
//                        .doOnError(new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) throws Exception {
//                                LogUtil.e(TAG, throwable.getMessage());
//                            }
//                        });
//            }
//        };
//    }
}
