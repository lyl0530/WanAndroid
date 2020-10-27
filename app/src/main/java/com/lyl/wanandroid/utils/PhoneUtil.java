package com.lyl.wanandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.lyl.wanandroid.service.entity.ArticleBean;
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
    public static void openInWebView(Context context, ArticleBean bean){
        if (null == bean) return;
        openInWebView(context, bean.getLink());
    }

    public static void openInWebView(Context context, String url){
        Log.d(TAG, "onItemClicked: url = " + url);
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(context, "url is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(ConstUtil.WEB_VIEW_URL, url);
        context.startActivity(intent);
    }

    //https://www.cnblogs.com/jenson138/p/4342699.html
    //点击了可见不可见，EditText要重写获取焦点，设置内容 - 光标定位到内容最后
    public static void cursor2End(EditText et, String content){
        if (TextUtils.isEmpty(content)) {
            return;
        }
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        et.setText(content);
        et.setSelection(content.length());
    }
}
