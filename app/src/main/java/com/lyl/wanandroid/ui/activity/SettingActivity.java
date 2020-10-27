package com.lyl.wanandroid.ui.activity;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.app.BaseApplication;
import com.lyl.wanandroid.base.BaseActivity;
import com.lyl.wanandroid.service.entity.LogoutResult;
import com.lyl.wanandroid.utils.ConstUtil;
import com.lyl.wanandroid.utils.PreferenceUtil;
import com.lyl.wanandroid.service.present.LogoutPresenter;
import com.lyl.wanandroid.service.view.LogoutView;
import com.lyl.wanandroid.utils.LogUtil;
import com.lyl.wanandroid.widget.DialogHelper;

import org.greenrobot.eventbus.EventBus;

public class SettingActivity extends BaseActivity implements LogoutView {
    private static final String TAG = SettingActivity.class.getSimpleName();

    private TextView mLogout;
    private LogoutPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
        initData();
    }

    private void initView() {
        mLogout = findViewById(R.id.tv_logout);
    }

    private void initData() {
        mPresenter = new LogoutPresenter();
        mPresenter.attach(this);
        if (BaseApplication.isLogin()){
            mLogout.setVisibility(View.VISIBLE);
        }
    }

    private Dialog dialog;

    public void onLogout(View v) {
        dialog = new DialogHelper(this)
                .bindHintDialog("", getString(R.string.are_you_sure_to_logout),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (null != dialog) dialog.dismiss();
                                //logout
                                mPresenter.logout();
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (null != dialog) {
                                    dialog.dismiss();
                                    dialog = null;
                                }
                            }
                        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

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
    public void Failed(String msg) {

    }

    @Override
    protected void onDestroy() {
        if (null != mPresenter) {
            mPresenter.detach();
        }
        super.onDestroy();
    }
}
