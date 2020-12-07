package com.lyl.wanandroid.app;

import android.app.Application;
import android.content.Context;

import com.lyl.wanandroid.utils.ConstUtil;
import com.lyl.wanandroid.utils.PreferenceUtil;
import com.lyl.wanandroid.service.RetrofitHelper;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by lym on 2020/4/1
 * Describe :
 */
public class BaseApplication extends Application {

    private static Context mAppContext;
    public static Context getContext() {
        return mAppContext;
    }

    //application的生命周期
    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                RetrofitHelper.init();
                initThirdSdk();
            }
        }).start();
    }

    private void initThirdSdk(){
        // add bugly
        // 为了保证运营数据的准确性，建议不要在异步线程初始化Bugly。
        // 第三个参数为SDK调试模式开关，调试模式的行为特性如下：
        // 输出详细的Bugly SDK的Log；
        // 每一条Crash都会被立即上报；
        // 自定义日志将会在Logcat中输出。
        // 建议在测试阶段建议设置成true，发布时设置为false。
        CrashReport.initCrashReport(mAppContext, ConstUtil.BUGLY_ID, false);

        //LeakCanary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    public static boolean isLogin() {
        return PreferenceUtil.instance().getUserId() > 0;
    }

}
