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

    private static Application application = null;
    public static Application getApp() {
        if (application == null) {
            throw new NullPointerException("App is not registered in the manifest");
        } else {
            return application;
        }
    }

    public static Context getAppContext() {
        return getApp().getApplicationContext();
    }

    //application的生命周期
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
//        RetrofitHelper.init();
//        initThirdSdk();
        new Thread(new Runnable() {
            @Override
            public void run() {
                RetrofitHelper.init();
                initThirdSdk();
            }
        }).start();

//        InitIntentService.start(this.getApplicationContext());
    }

    private void initThirdSdk(){
        // add bugly
        // 为了保证运营数据的准确性，建议不要在异步线程初始化Bugly。
        // 第三个参数为SDK调试模式开关，调试模式的行为特性如下：
        // 输出详细的Bugly SDK的Log；
        // 每一条Crash都会被立即上报；
        // 自定义日志将会在Logcat中输出。
        // 建议在测试阶段建议设置成true，发布时设置为false。
        CrashReport.initCrashReport(getAppContext(), ConstUtil.BUGLY_ID, true);

        //LeakCanary
        if (LeakCanary.isInAnalyzerProcess(getAppContext())) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(application);
    }

    public static boolean isLogin() {
        return PreferenceUtil.instance().getUserId() > 0;
    }

}
