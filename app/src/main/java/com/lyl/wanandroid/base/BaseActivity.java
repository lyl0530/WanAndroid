package com.lyl.wanandroid.base;

import android.support.v7.app.AppCompatActivity;

import com.lyl.wanandroid.widget.LoadingDialog;

/**
 * Created by lym on 2020/4/13
 * Describe :
 */
public abstract class BaseActivity extends AppCompatActivity {

    private LoadingDialog mLoadingDialog;

    public void showProgressDialog() {
        if (null == mLoadingDialog) {
            mLoadingDialog = LoadingDialog.with(this);
        }
        mLoadingDialog.show();
    }

    public void hideProgressDialog() {
        if (null != mLoadingDialog) {
            mLoadingDialog.dismiss();
        }
    }

}
