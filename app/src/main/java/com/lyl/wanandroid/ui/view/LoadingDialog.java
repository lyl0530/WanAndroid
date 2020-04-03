package com.lyl.wanandroid.ui.view;


import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.lyl.wanandroid.R;

/**
 * Created by lym on 2020/4/3
 * Describe :
 */
public class LoadingDialog{
    private static final String TAG = "lym123 LoadingDialog";
    private final Context mContext;
    private Dialog mDialog;

    private LoadingDialog(Context context){
        mContext = context;
    }

    public static LoadingDialog with(Context context){
        return new LoadingDialog(context);
    }
    public void show(){
        Log.d(TAG, "show: ");
        mDialog = new Dialog(mContext, R.style.loadingDialog);
        mDialog.setContentView(R.layout.dialog_loading);
        mDialog.show();
    }

    public void dismiss(){
        Log.d(TAG, "dismiss: ");
        if (null != mDialog && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }
}
