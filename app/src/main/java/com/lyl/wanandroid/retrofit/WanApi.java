package com.lyl.wanandroid.retrofit;

import com.lyl.wanandroid.bean.BannerResult;
import com.lyl.wanandroid.bean.LoginResult;
import com.lyl.wanandroid.bean.RegisterResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by lym on 2020/3/29
 * Describe : wan android's api
 */
public interface WanApi {

    // Field FormUrlEncoded成对出现
    //https://www.wanandroid.com/user/login
    @POST("user/login")
    @FormUrlEncoded
    Observable<LoginResult> login(
            @Field("username") String username,
            @Field("password") String password);

    /**
     * https://www.wanandroid.com/user/register
     * 注册
     */
    @POST("user/register")
    @FormUrlEncoded
    Observable<RegisterResult> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("repassword") String repassword);

    /**
     * https://www.wanandroid.com/banner/json
     * 首页banner
     */
    @GET("banner/json")
    Observable<BannerResult> getBanner();


}
