package com.lyl.wanandroid.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lyl.wanandroid.constant.Const;
import com.lyl.wanandroid.util.LogUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lym on 2020/3/29
 * Describe :
 */
public class RetrofitHelper {
    private static final String TAG = RetrofitHelper.class.getSimpleName();

    private volatile static RetrofitHelper instance;
    private RetrofitHelper(){

    }

    //面对高并发下的懒汉模式，最安全、高效的选择就是双重if判断加同步锁的方式。
    //https://blog.csdn.net/lyl0530/article/details/81348318
    private static RetrofitHelper getInstance() {
        if (null == instance) {//提高执行效率
            synchronized (RetrofitHelper.class) {
                if (null == instance) {//出于线程安全的考虑
                    instance = new RetrofitHelper();
                }
            }
        }
        return instance;
    }

    private Retrofit getRetrofit(String baseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(
                msg -> LogUtils.d(TAG, "log: " + msg));
        if (LogUtils.isDebug()) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(8000, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static WanApi getWanApi() {
        return RetrofitHelper.getInstance().getRetrofit(Const.WAN_ANDROID_BASE_URL)
                .create(WanApi.class);
    }
}
