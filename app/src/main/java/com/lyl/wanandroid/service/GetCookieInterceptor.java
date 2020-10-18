package com.lyl.wanandroid.service;

import android.text.TextUtils;
import android.util.Log;

import com.lyl.wanandroid.utils.ConstUtil;
import com.lyl.wanandroid.utils.PreferenceUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by AxeChen on 2018/4/21.
 * 登录时保存cookies
 */
public class GetCookieInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (null == request) return null;

        HttpUrl requestUrl = request.url();
        if (null == requestUrl) return null;

        String url = requestUrl.toString();
        String domain = requestUrl.host();

        Response response = chain.proceed(request);
        if (null == response) return null;
        List<String> cookies = response.headers(ConstUtil.SET_COOKIE_KEY);


//        if (!response.headers(Const.SET_COOKIE_KEY).isEmpty()) {
//            HashSet<String> cookies = new HashSet<>();
//            for (String header : response.headers(Const.SET_COOKIE_KEY)) {
//                cookies.add(header);
//            }
//        }

        if (!TextUtils.isEmpty(url) && url.contains(ConstUtil.SAVE_USER_LOGIN_KEY) &&
                !cookies.isEmpty()) {
//            String cookie = encodeCookie(cookies);
//            HashSet<String> cookieSet = new HashSet<>();
//            for (String cookie : cookies){
//                Log.d("lyl123", "encodeCookie: " + cookie);
//                //cookie.split(";")
//                cookieSet.add(cookie);
//            }
//
//            PreferenceConst.instance().setDomainName(cookie);

            HashSet<String> newCookies = new HashSet<>(cookies);
            Log.d("lyl123", "newCookies: " + newCookies);
            PreferenceUtil.instance().setCookieSet(newCookies);
        }
        return response;
    }

    private void saveCookie(String url, String domain, String cookie) {
        if (null == url) return;
        PreferenceUtil.instance().setDomainName(cookie);
    }

    private String encodeCookie(List<String> cookies) {
        HashSet<String> cookieSet = new HashSet<>();
        for (String cookie : cookies){
            Log.d("lyl123", "encodeCookie: " + cookie);
            //cookie.split(";")
            cookieSet.add(cookie);
        }
        return "";
    }
}