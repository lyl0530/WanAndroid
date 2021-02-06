package com.lyl.wanandroid.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.lyl.wanandroid.service.RetrofitHelper;
import com.lyl.wanandroid.utils.ConstUtil;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by lym on 2020/12/19
 * Describe :启动时间优化，初始化Application中的第三方库
 */
public class InitIntentService extends IntentService {
    public static final String ACTION = "com.lyl.service.init";

    public InitIntentService() {
        super("InitIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(null != intent && ACTION.equalsIgnoreCase(intent.getAction())){
            RetrofitHelper.init();
            initThirdSdk();
        }
    }

    public static void start(Context context){
        Intent intent = new Intent(context, InitIntentService.class);
        intent.setAction(ACTION);
        context.startService(intent);
    }

    private void initThirdSdk(){
        // add bugly
        // 为了保证运营数据的准确性，建议不要在异步线程初始化Bugly。
        // 第三个参数为SDK调试模式开关，调试模式的行为特性如下：
        // 输出详细的Bugly SDK的Log；
        // 每一条Crash都会被立即上报；
        // 自定义日志将会在Logcat中输出。
        // 建议在测试阶段建议设置成true，发布时设置为false。
        CrashReport.initCrashReport(getApplicationContext(), ConstUtil.BUGLY_ID, false);

        //LeakCanary
        if (LeakCanary.isInAnalyzerProcess(getApplicationContext())) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(getApplication());
    }
}
