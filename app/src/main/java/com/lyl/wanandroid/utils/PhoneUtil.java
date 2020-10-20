package com.lyl.wanandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.lyl.wanandroid.ui.activity.WebActivity;


public class PhoneUtil {
    private static final String TAG = "Utils";

    public static int getScreenW(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenH(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
    /**
    * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
    */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
    * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
    */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * WebView打开url
     */

    public static void openInWebView(Context context, String url){
        Log.d(TAG, "onItemClicked: url = " + url);
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(ConstUtil.WEB_VIEW_URL, url);
        context.startActivity(intent);
    }
}
