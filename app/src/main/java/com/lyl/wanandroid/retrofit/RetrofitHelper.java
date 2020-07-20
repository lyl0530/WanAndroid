package com.lyl.wanandroid.retrofit;

import android.util.Log;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lyl.wanandroid.app.BaseApplication;
import com.lyl.wanandroid.constant.Const;
import com.lyl.wanandroid.constant.PreferenceConst;
import com.lyl.wanandroid.util.LogUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
    private static WanApi mWanApi;

    private RetrofitHelper(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(
                msg -> LogUtils.d(TAG, "log: " + msg));
        if (LogUtils.isDebug()) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }

        // 每个client对象都有自己的线程池和连接池，如果为每个请求都创建一个client对象，自然会出现内存溢出。
        // 所以官方建议OkHttpClient应该单例化，重用连接和线程能降低延迟和减少内存消耗。
        // https://blog.csdn.net/sinat_36553913/article/details/104054028
        // 此处是在application中初始化一次
        PersistentCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(BaseApplication.getContext()));
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(8000, TimeUnit.SECONDS)
//                .cookieJar(cookieJar)//增加 cookieJar，登录后使用cookie，可获取用户的收藏列表等信息
//                .addInterceptor(new AddCookiesInterceptor())
//                .addInterceptor(new ReceivedCookiesInterceptor())
                .addInterceptor(new AddCookieInterceptor())
                .addInterceptor(new GetCookieInterceptor())
                .build();

        mWanApi = new Retrofit.Builder()
                .baseUrl(Const.WAN_ANDROID_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(WanApi.class);
    }

    //面对高并发下的懒汉模式，最安全、高效的选择就是双重if判断加同步锁的方式。
    //https://blog.csdn.net/lyl0530/article/details/81348318
    public static void init() {
        if (null == instance) {//提高执行效率
            synchronized (RetrofitHelper.class) {
                if (null == instance) {//出于线程安全的考虑
                    instance = new RetrofitHelper();
                }
            }
        }
    }

    public static WanApi getWanApi() {
        return mWanApi;
    }


    private static class ReceivedCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            //Log.d(TAG, "lll123 intercept: " + originalResponse);
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();

                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }
                Log.d(TAG, "lll123 intercept: " + cookies);
                PreferenceConst.instance().setCookieSet(cookies);
//                Preferences.getDefaultPreferences().edit()
//                        .putStringSet(Preferences.PREF_COOKIES, cookies)
//                        .apply();
            }

            return originalResponse;
        }
    }

    private static class AddCookiesInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            HashSet<String> preferences = (HashSet)PreferenceConst.instance().getCookieSet();
//            HashSet<String> preferences = (HashSet) Preferences.getDefaultPreferences().getStringSet(Preferences.PREF_COOKIES, new HashSet<>());
            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
                Log.v("lll123", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
            }

            return chain.proceed(builder.build());
        }
    }
}
