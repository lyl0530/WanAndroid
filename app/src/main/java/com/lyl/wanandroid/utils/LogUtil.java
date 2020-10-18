package com.lyl.wanandroid.utils;

import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.lyl.wanandroid.app.BaseApplication;

/**
 * Created by lym on 2020/4/3
 * Describe : log util
 */
public class LogUtil {
    //便捷，但安全性还是不高，反编译后，修改代码，还是可以看到log
    //自己 App Module 中不能主动设置 android:debuggable，否则无论 Debug 还是 Release 版会始终是设置的值。当然本身就没有自动设置的必要。
    //推荐使用live template的功能
    //参考：
    //https://www.cnblogs.com/zhujiabin/p/6874508.html
    //https://www.jb51.net/article/129634.htm
    //https://www.jb51.net/article/129636.htm
    //https://www.jianshu.com/p/658eb18838df
    public static boolean isDebug(){
        return 0 != (BaseApplication.getContext().getApplicationInfo().flags
                        & ApplicationInfo.FLAG_DEBUGGABLE);
    }

    public static void v(String tag, String msg) {
        if (isDebug()) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug()) {
            Log.d(tag + " lyl123", msg);
        }
    }
    public static void i(String tag, String msg) {
        if (isDebug()) {
            Log.i(tag, msg);
        }
    }
    public static void w(String tag, String msg) {
        if (isDebug()) {
            Log.w(tag, msg);
        }
    }
    public static void e(String tag, String msg) {
        if (isDebug()) {
            Log.e(tag + " lyl123", msg);
        }
    }
}
