package com.lyl.wanandroid.service;

import com.lyl.wanandroid.service.entity.BannerResult;
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


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lym on 2020/3/29
 * Describe : wan android's api
 */
public interface RetrofitService {

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

    /**
     * https://www.wanandroid.com/article/list/0/json
     * 首页的置顶文章
     */
    @GET("article/list/{pageIndex}/json")
    Observable<MainArticleResult> getMainArticle(@Path("pageIndex") int pageIndex);

    /**
     * https://www.wanandroid.com/lg/collect/1165/json
     * 收藏站内文章
     */
    @POST("lg/collect/{id}/json")
    Observable<MainArticleResult> collectArticle(@Path("id") int id);


    /**
     * https://www.wanandroid.com/lg/uncollect_originId/2333/json
     * 文章列表处取消收藏
     */
    @POST("lg/uncollect_originId/{id}/json")
    Observable<MainArticleResult> unCollectArticle(@Path("id") int id);


    /**
     * https://www.wanandroid.com/project/tree/json
     * 获取项目的整个分类
     */
    @GET("project/tree/json")
    Observable<ProjectResult> getProject();

    /**
     * https://www.wanandroid.com/project/list/1/json?cid=249 - 完整项目
     * https://www.wanandroid.com/article/list/0/json?cid=15 - 体系里的分类
     * 获取单个项目的文章列表
     * 去掉url中问号后面的字段，只使用@Query注解
     * https://blog.csdn.net/gao511147456/article/details/108262617
     */
    @GET("article/list/{curPageId}/json")
    Observable<ProjectArticleListResult> getProjectArticleList(@Path("curPageId") int curPageId,
                                                               @Query("cid") int cid);

    /**
     * https://www.wanandroid.com/hotkey/json
     * 搜索热词
     */
    @GET("hotkey/json")
    Observable<HotKeyResult> getHotKey();


    /**
     * https://www.wanandroid.com/article/query/0/json?k=%s
     * 搜索
     * 方法：POST
     * 参数：
     * 页码：拼接在链接上，从0开始。
     * k ： 搜索关键词
     * 支持多个关键词，用空格隔开
     */
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    Observable<ProjectArticleListResult> search(@Path("page") int page,
                                         @Field("k") String k);
}
