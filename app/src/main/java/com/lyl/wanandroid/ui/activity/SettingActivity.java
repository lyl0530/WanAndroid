package com.lyl.wanandroid.ui.activity;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.base.BaseActivity;
import com.lyl.wanandroid.bean.LogoutResult;
import com.lyl.wanandroid.constant.Const;
import com.lyl.wanandroid.constant.PreferenceConst;
import com.lyl.wanandroid.mvp.present.LogoutPresenter;
import com.lyl.wanandroid.mvp.view.LogoutView;
import com.lyl.wanandroid.util.LogUtils;
import com.lyl.wanandroid.widget.DialogHelper;

import org.greenrobot.eventbus.EventBus;

public class SettingActivity extends BaseActivity implements LogoutView {
    private static final String TAG = SettingActivity.class.getSimpleName();

    private LogoutPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {
        mPresenter = new LogoutPresenter();
        mPresenter.attach(this);
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
    public void showProgressDialog() {
        super.showProgressDialog();
    }

    @Override
    public void hideProgressDialog() {
        super.hideProgressDialog();
    }

    @Override
    public void Success(LogoutResult result) {
        LogUtils.d(TAG, "loginSuccess: " + result);
        EventBus.getDefault().post(Const.REFRESH_MAIN);//刷新首页
        //退出登录成功 - 清除缓存
        PreferenceConst.instance().setUserId(0);
        PreferenceConst.instance().setAccount("");
        PreferenceConst.instance().setPwd("");
        PreferenceConst.instance().setCookieSet(null);//清除本地cookie
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
