package com.lyl.wanandroid.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.bean.LoginResult;
import com.lyl.wanandroid.present.LoginPresent;
import com.lyl.wanandroid.ui.view.LoadingDialog;
import com.lyl.wanandroid.util.LogUtils;
import com.lyl.wanandroid.view.LoginView;

/**
 * Created by lym on 2020/4/9
 * Describe : 主页
 * 引入BaseFragment！！！
 */
public class LoginFragment extends Fragment implements LoginView, View.OnClickListener {
    private static final String TAG = "lym123 LoginFragment";

    private Context mContext;
    private View mView;
    private LoginPresent mPresent;
    private LoadingDialog mLoadingDialog;
    private EditText mUserName, mPwd;
    private ImageButton mCancel, mSeePwd;
    private CheckBox mRememberPwd;
    private boolean seePwd = false;
    private boolean remember = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);

        mContext = getActivity();
        mPresent = new LoginPresent();
        mPresent.attach(this);

        mView.findViewById(R.id.btn_login).setOnClickListener(this);
        mUserName = mView.findViewById(R.id.et_username);
        mUserName.addTextChangedListener(userNameWatcher);
        mCancel = mView.findViewById(R.id.btn_cancel);
        mCancel.setOnClickListener(this);
        mPwd = mView.findViewById(R.id.et_pwd);
        mPwd.addTextChangedListener(pwdWatcher);
        mSeePwd = mView.findViewById(R.id.btn_see_pwd);
        mSeePwd.setOnClickListener(this);
        mRememberPwd = mView.findViewById(R.id.remember_pwd);
        mRememberPwd.setOnClickListener(this);

        return mView;
    }

    private TextWatcher userNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mCancel.setVisibility(s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher pwdWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mSeePwd.setVisibility(s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                mUserName.setText("");
                break;
            case R.id.btn_see_pwd:
                seePwd = !seePwd;
                mSeePwd.setSelected(seePwd);
                mSeePwd.setBackgroundResource(seePwd ? R.drawable.login_see_pwd
                        : R.drawable.login_not_see_pwd);
                mPwd.setTransformationMethod(seePwd ? HideReturnsTransformationMethod.getInstance()
                        : PasswordTransformationMethod.getInstance()); //密码可见 : 密码不可见
                break;
            case R.id.remember_pwd:
                remember = !remember;//从 sp中获取初始值
                mRememberPwd.setSelected(remember);
//                Drawable drawable= getResources().getDrawable(R.drawable.login_check_checked);
//                /// 这一步必须要做,否则不会显示.
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                mRememberPwd.setCompoundDrawables(drawable, null, null, null);
                break;
            case R.id.btn_login:
                mPresent.login("123147258", "123456");
                break;
        }
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

    @Override
    public void onDestroy() {
        mPresent.detach();
        super.onDestroy();
    }

}
