package com.lyl.wanandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lyl.wanandroid.BuildConfig;
import com.lyl.wanandroid.R;
import com.lyl.wanandroid.app.BaseApplication;
import com.lyl.wanandroid.base.BaseActivity;
import com.lyl.wanandroid.listener.DialogListener;
import com.lyl.wanandroid.service.entity.LogoutResult;
import com.lyl.wanandroid.service.present.LogoutPresenter;
import com.lyl.wanandroid.service.view.LogoutView;
import com.lyl.wanandroid.utils.ConstUtil;
import com.lyl.wanandroid.utils.ErrorUtil;
import com.lyl.wanandroid.utils.LogUtil;
import com.lyl.wanandroid.utils.PreferenceUtil;
import com.lyl.wanandroid.widget.ConfirmDialog;

import org.greenrobot.eventbus.EventBus;

public class SettingActivity extends BaseActivity implements LogoutView {
    private static final String TAG = SettingActivity.class.getSimpleName();

    private TextView mLogout, tvVerInfo;
    private LogoutPresenter mPresenter;
    private ConfirmDialog dialog;
    private static final String DIALOG_TAG = "dialog_tag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (null != savedInstanceState && null != getSupportFragmentManager()){
            dialog = (ConfirmDialog)getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
            if (null != dialog) {
                dialog.setOnDialogClickListener(mListener);
            }
        }
        initView();
        initData();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        mLogout = findViewById(R.id.tv_logout);
        tvVerInfo = findViewById(R.id.tv_version_info);

        //检查版本后，不需更新
        tvVerInfo.setText("v"+BuildConfig.VERSION_NAME);
    }

    private void initData() {
        mPresenter = new LogoutPresenter();
        mPresenter.attach(this);
        mLogout.setVisibility(BaseApplication.isLogin() ? View.VISIBLE : View.GONE);
    }

    public void onLogout(View v) {
        dialog = ConfirmDialog.getInstance(getString(R.string.are_you_sure_to_logout));
        dialog.setOnDialogClickListener(mListener);
        dialog.show(getSupportFragmentManager(), DIALOG_TAG);
    }

    private DialogListener mListener = new DialogListener() {
        @Override
        public void leftClick() {
            //do nothing
        }

        @Override
        public void rightClick() {
            if (null != mPresenter){
                mPresenter.logout();
            }
        }
    };

    public void onBack(View v) {
        finish();
    }

    @Override
    public void Success(LogoutResult result) {
        LogUtil.d(TAG, "loginSuccess: " + result);
        EventBus.getDefault().post(ConstUtil.REFRESH_MAIN);//刷新首页
        //退出登录成功 - 清除缓存
        PreferenceUtil.instance().setUserId(0);
//        PreferenceUtil.instance().setAccount("");
//        PreferenceUtil.instance().setPwd("");
        PreferenceUtil.instance().setCookieSet(null);//清除本地cookie
        finish();
    }

    @Override
    public void Failed(int code, String msg) {
        String str = ErrorUtil.getErrorInfo(SettingActivity.this, code, msg);
        showToast(str);
    }

    public void onAboutUs(View v){
        startActivity(new Intent(this, MeActivity.class));
    }
    @Override
    protected void onDestroy() {
        if (null != mPresenter) {
            mPresenter.detach();
        }
        super.onDestroy();
    }
}
