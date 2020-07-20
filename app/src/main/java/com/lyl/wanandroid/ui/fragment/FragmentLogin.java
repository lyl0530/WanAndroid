package com.lyl.wanandroid.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.base.BaseFragment;
import com.lyl.wanandroid.bean.LoginResult;
import com.lyl.wanandroid.constant.Const;
import com.lyl.wanandroid.constant.PreferenceConst;
import com.lyl.wanandroid.mvp.present.LoginPresenter;
import com.lyl.wanandroid.mvp.view.LoginView;
import com.lyl.wanandroid.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

/**
 * Created by lym on 2020/4/9
 * Describe : 登录
 *
 */
public class FragmentLogin extends BaseFragment implements LoginView, View.OnClickListener {
    private static final String TAG = FragmentLogin.class.getSimpleName();

    private Context mContext;
    private View mView;
    private LoginPresenter mPresenter;
    private EditText mUserName, mPwd;
    private ImageButton mCancel;
    private CheckBox mRememberPwd, mSeePwd;
    private boolean seePwd = false;//密码可见
    private boolean remember = false;//记住密码的勾选状态

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);

        mContext = getActivity();
        mPresenter = new LoginPresenter();
        mPresenter.attach(this);

        mView.findViewById(R.id.btn_login).setOnClickListener(this);
        mUserName = mView.findViewById(R.id.et_username);
        mUserName.addTextChangedListener(textWatcher);
        mCancel = mView.findViewById(R.id.btn_cancel);
        mCancel.setOnClickListener(this);
        mPwd = mView.findViewById(R.id.et_pwd);
        mPwd.addTextChangedListener(textWatcher);
        mSeePwd = mView.findViewById(R.id.see_pwd);
        mSeePwd.setOnClickListener(this);
        mRememberPwd = mView.findViewById(R.id.remember_pwd);
        mRememberPwd.setOnClickListener(this);

        return mView;
    }

    private boolean isFirstShow = true;
    private String mAccount;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mAccount = PreferenceConst.instance().getAccount();
            LogUtils.d(TAG, "setUserVisibleHint: " + mAccount);
            if (!isFirstShow) {
                setUserNamePwd();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstShow) {
            isFirstShow = false;
            LogUtils.d(TAG, "onResume: ");
            setUserNamePwd();
        }
    }

    //进入login界面时，从sp中读
    private void setUserNamePwd() {
        mUserName.setText(mAccount);

        //进入，显示上次的勾选状态。若勾选，则从sp中读取显示密码；否则，不显示
        remember = PreferenceConst.instance().getCheck();
        LogUtils.d(TAG, "setUserNamePwd: remember = " + remember);
        mRememberPwd.setChecked(remember);
        if (remember) {
            mPwd.setText(PreferenceConst.instance().getPwd());
        }
    }

    //退出login界面时，往sp中写
    private void saveUserNamePwd() {
        PreferenceConst.instance().setAccount(mUserName.getText().toString());

        //记住密码：若勾选，则写入pwd；否则，清空
        PreferenceConst.instance().setPwd(
                PreferenceConst.instance().getCheck()
                        ? mPwd.getText().toString()
                        : "");
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int visible = TextUtils.isEmpty(mUserName.getText().toString())
                    ? View.INVISIBLE
                    : View.VISIBLE;
            mCancel.setVisibility(visible);
            visible = TextUtils.isEmpty(mPwd.getText().toString())
                    ? View.INVISIBLE
                    : View.VISIBLE;
            mSeePwd.setVisibility(visible);
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
            case R.id.see_pwd:
                seePwd = !seePwd;
                mSeePwd.setSelected(seePwd);
//                mSeePwd.setBackgroundResource(seePwd ? R.drawable.login_see_pwd
//                        : R.drawable.login_not_see_pwd);
                mPwd.setTransformationMethod(seePwd ? HideReturnsTransformationMethod.getInstance()
                        : PasswordTransformationMethod.getInstance()); //密码可见 : 密码不可见
                break;
            case R.id.remember_pwd:
                remember = !remember;//从 sp中获取初始值
                mRememberPwd.setSelected(remember);
                PreferenceConst.instance().setCheck(remember);
                if (!remember) {
                    PreferenceConst.instance().setPwd("");
                }
//                Drawable drawable= getResources().getDrawable(R.drawable.login_check_checked);
//                /// 这一步必须要做,否则不会显示.
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                mRememberPwd.setCompoundDrawables(drawable, null, null, null);
                break;
            case R.id.btn_login:
                String userName = mUserName.getText().toString();
                String pwd = mPwd.getText().toString();
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(pwd)) {
                    mPresenter.login(userName, pwd);
                }/* else { //for test
                    mPresenter.login("123147258", "123456");
                }*/
                break;
        }
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
    public void Success(LoginResult result) {
        LogUtils.d(TAG, "loginSuccess: " + result);
        if (null != result && null != result.getData()) {
            PreferenceConst.instance().setUserId(result.getData().getId());
        }
        saveUserNamePwd();
        EventBus.getDefault().post(Const.LOGIN);
        Toast.makeText(getContext(), getString(R.string.login_success), Toast.LENGTH_SHORT).show();
        Objects.requireNonNull(getActivity()).finish();
    }

    @Override
    public void Failed(String msg) {
        LogUtils.e(TAG, "loginFailed: " + msg);
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        if (null != mPresenter) {
            mPresenter.detach();
        }
        super.onDestroy();
    }

}
