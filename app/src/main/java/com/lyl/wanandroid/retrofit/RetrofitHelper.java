package com.lyl.wanandroid.retrofit;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lyl.wanandroid.BuildConfig;

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
    private static final String TAG = "lym RetrofitHelper";

    private volatile static RetrofitHelper instance;
    private RetrofitHelper(){

    }

    //面对高并发下的懒汉模式，最安全、高效的选择就是双重if判断加同步锁的方式。
    //https://blog.csdn.net/lyl0530/article/details/81348318
    public static RetrofitHelper getInstance(){
        if (null == instance) {//提高执行效率
            synchronized (RetrofitHelper.class) {
                if (null == instance) {//出于线程安全的考虑
                    instance = new RetrofitHelper();
                }
            }
        }
        return instance;
    }

    public Retrofit getRetrofit(String baseUrl){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(
                msg -> Log.d(TAG, "log: " + msg));
        if (BuildConfig.DEBUG) {
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
}
