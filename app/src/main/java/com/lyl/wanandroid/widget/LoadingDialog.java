package com.lyl.wanandroid.widget;


import android.app.Dialog;
import android.content.Context;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.util.LogUtils;

/**
 * Created by lym on 2020/4/3
 * Describe :
 */
public class LoadingDialog{
    private static final String TAG =  LoadingDialog.class.getSimpleName();
    private static Context sContext;
    private static volatile Dialog sDialog;

    private LoadingDialog(Context context){
        sContext = context;
    }

    public static LoadingDialog with(Context context){
        return new LoadingDialog(context);
    }
    
    private static Dialog newInstance(){
        if (null == sDialog) {
            synchronized (Dialog.class) {
                if (null == sDialog) {
                    sDialog = new Dialog(sContext, R.style.loadingDialog);
                }
            }
        }
        return sDialog;
    }
    
    public void show(){
        LogUtils.d(TAG, "show: ");
//        sDialog = new Dialog(sContext, R.style.loadingDialog);
        newInstance();
        sDialog.setContentView(R.layout.dialog_loading);
        sDialog.show();
    }

    public void dismiss(){
        LogUtils.d(TAG, "dismiss: ");
        if (null != sDialog && sDialog.isShowing()){
            sDialog.dismiss();
        }
    }

    public boolean isShow(){
        boolean b = null != sDialog && sDialog.isShowing();
        return null != sDialog && sDialog.isShowing();
    }
}
