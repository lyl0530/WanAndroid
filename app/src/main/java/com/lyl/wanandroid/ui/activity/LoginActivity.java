package com.lyl.wanandroid.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.bean.LoginResult;
import com.lyl.wanandroid.present.LoginPresent;
import com.lyl.wanandroid.ui.view.LoadingDialog;
import com.lyl.wanandroid.util.LogUtils;
import com.lyl.wanandroid.view.LoginView;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private LoginPresent mPresent;
    private LoadingDialog mLoadingDialog;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        mPresent = new LoginPresent();
        mPresent.attach(this);
    }

    @Override
    protected void onDestroy() {
        mPresent.detach();
        super.onDestroy();
    }


    @Override
    public void showProgressDialog() {
        if (null == mLoadingDialog) {
            mLoadingDialog = LoadingDialog.with(mContext);
        }
        mLoadingDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (null != mLoadingDialog) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void loginSuccess(LoginResult result) {
        LogUtils.d(TAG, "loginSuccess: " + result);
    }

    @Override
    public void loginFailed(String msg) {
        LogUtils.d(TAG, "loginFailed: " + msg);
    }

    public void onLogin(View view) {
        mPresent.login("123147258", "123456");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.d(TAG, "onKeyDown: " + keyCode);
        if (KeyEvent.KEYCODE_BACK == keyCode){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
