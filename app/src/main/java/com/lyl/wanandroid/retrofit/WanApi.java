package com.lyl.wanandroid.retrofit;

import com.lyl.wanandroid.bean.BannerResult;
import com.lyl.wanandroid.bean.HierarchyResult;
import com.lyl.wanandroid.bean.LoginResult;
import com.lyl.wanandroid.bean.LogoutResult;
import com.lyl.wanandroid.bean.NavigationResult;
import com.lyl.wanandroid.bean.RegisterResult;
import com.lyl.wanandroid.bean.TopArticleResult;

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

    /**
     * https://www.wanandroid.com/user/login
     * 登录
     * Field FormUrlEncoded成对出现
     */
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

    /**
     * https://www.wanandroid.com/user/logout/json
     * 退出登录
     */
    @GET("user/logout/json")
    Observable<LogoutResult> logout();


    /**
     * https://www.wanandroid.com/tree/json
     * 体系数据
     */
    @GET("tree/json")
    Observable<HierarchyResult> getHierarchy();

    /**
     * https://www.wanandroid.com/navi/json
     * 导航数据
     */
    @GET("navi/json")
    Observable<NavigationResult> getNavigation();

    /**
     * https://www.wanandroid.com/article/top/json
     * 首页的置顶文章
     */
    @GET("article/top/json")
    Observable<TopArticleResult> getTopArticle();
}
