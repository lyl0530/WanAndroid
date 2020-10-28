package com.lyl.wanandroid.widget;


import android.app.Dialog;
import android.content.Context;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.utils.LogUtil;

/**
 * Created by lym on 2020/4/3
 * Describe :
 */
public class LoadingDialog{
    private static final String TAG =  LoadingDialog.class.getSimpleName();

    private final Context mContext;
    private Dialog mDialog;
    private LoadingDialog(Context context){
        mContext = context;
    }

    public static LoadingDialog with(Context context){
        return new LoadingDialog(context);
    }
    
    public void show(){
        LogUtil.d(TAG, "show: mContext = " + mContext);
        mDialog = new Dialog(mContext, R.style.loadingDialog);
        mDialog.setContentView(R.layout.dialog_loading);
        mDialog.show();
    }

    public void dismiss() {
        if (null != mDialog && mDialog.isShowing()){
            mDialog.dismiss();
            mDialog = null;
        }
        LogUtil.d(TAG, "mDialog: " + mDialog + ", mContext = " + mContext);
    }

    public boolean isShow(){
//        boolean b = null != mDialog && mDialog.isShowing();
        return null != mDialog && mDialog.isShowing();
    }
}
