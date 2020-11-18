package com.lyl.wanandroid.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.base.BaseActivity;
import com.lyl.wanandroid.service.present.LogoutPresenter;
import com.lyl.wanandroid.utils.PhoneUtil;
import com.lyl.wanandroid.widget.ConfirmDialog;

public class MeActivity extends BaseActivity{
    private static final String TAG = MeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
    }

    public void onBlog(View v){
        PhoneUtil.openInWebView(this, "https://blog.csdn.net/lyl0530");
    }

    public void onGithub(View v){
        PhoneUtil.openInWebView(this, "https://github.com/lyl0530/WanAndroid");
    }

    public void onBack(View v) {
        finish();
    }
}
