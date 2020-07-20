package com.lyl.wanandroid.retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.lyl.wanandroid.constant.Const;
import com.lyl.wanandroid.constant.PreferenceConst;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by AxeChen on 2018/4/21.
 * 添加cookie做持久化
 */
public class AddCookieInterceptor implements Interceptor {
    private static final String TAG = "AddCookieInterceptor";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String domain = request.url().host();
        Request.Builder builder = request.newBuilder();
        int userId = PreferenceConst.instance().getUserId();
//        Log.d(TAG, "domain:" + domain + ", userId:" + userId);
        if (!TextUtils.isEmpty(domain) && 0 != userId) {
            String cookies = PreferenceConst.instance().getDomainName();

            HashSet<String> cookieSet = (HashSet<String>)PreferenceConst.instance().getCookieSet();
            for (String cookie : cookieSet) {
                builder.addHeader("Cookie", cookie);
                Log.v(TAG, "Adding Header  Cookie: " + cookie);
                // This is done so I know which headers are being added;
                // this interceptor is used after the normal logging of OkHttp
            }

//            Log.d(TAG, "cookies:" + cookies);
//            if (!TextUtils.isEmpty(cookies)) {
//                builder.addHeader(Const.COOKIE_NAME, cookies);
//            }
        }
        return chain.proceed(builder.build());
    }
}