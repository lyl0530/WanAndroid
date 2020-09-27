package com.lyl.wanandroid.ui.fragment;

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
import com.lyl.wanandroid.bean.RegisterResult;
import com.lyl.wanandroid.constant.PreferenceConst;
import com.lyl.wanandroid.mvp.present.RegisterPresenter;
import com.lyl.wanandroid.mvp.view.RegisterView;
import com.lyl.wanandroid.ui.activity.LoginActivity;
import com.lyl.wanandroid.util.LogUtils;

import java.util.Objects;

/**
 * Created by lym on 2020/4/9
 * Describe : 注册
 *
 */
public class FragmentRegister extends BaseFragment implements View.OnClickListener, RegisterView {
    private static final String TAG = FragmentRegister.class.getSimpleName();

    //private Context mContext;
    private View mView;
    private RegisterPresenter mPresenter;
    private EditText mUserName, mPwd, mPwd2;
    private ImageButton mCancel;
    private CheckBox mSeePwd, mSeePwd2;
    private boolean seePwd = false;
    private boolean seePwd2 = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_register, container, false);

        //mContext = getActivity();
        mPresenter = new RegisterPresenter();
        mPresenter.attach(this);

        mUserName = mView.findViewById(R.id.et_username);
        mUserName.addTextChangedListener(textWatcher);
        mCancel = mView.findViewById(R.id.btn_cancel);
        mCancel.setOnClickListener(this);
        mPwd = mView.findViewById(R.id.et_pwd);
        mPwd.addTextChangedListener(textWatcher);
        mSeePwd = mView.findViewById(R.id.see_pwd);
        mSeePwd.setOnClickListener(this);

        mPwd2 = mView.findViewById(R.id.et_pwd2);
        mPwd2.addTextChangedListener(textWatcher);
        mSeePwd2 = mView.findViewById(R.id.see_pwd2);
        mSeePwd2.setOnClickListener(this);

        mView.findViewById(R.id.btn_register).setOnClickListener(this);
        return mView;
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
            visible = TextUtils.isEmpty(mPwd2.getText().toString())
                    ? View.INVISIBLE
                    : View.VISIBLE;
            mSeePwd2.setVisibility(visible);
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
                mPwd.setTransformationMethod(seePwd ? HideReturnsTransformationMethod.getInstance()
                        : PasswordTransformationMethod.getInstance()); //密码可见 : 密码不可见
                break;
            case R.id.see_pwd2:
                seePwd2 = !seePwd2;
                mSeePwd2.setSelected(seePwd2);
                mPwd2.setTransformationMethod(seePwd2 ? HideReturnsTransformationMethod.getInstance()
                        : PasswordTransformationMethod.getInstance()); //密码可见 : 密码不可见
                break;
            case R.id.btn_register:
                String userName = mUserName.getText().toString();
                String pwd = mPwd.getText().toString();
                String pwd2 = mPwd2.getText().toString();
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(pwd2)) {
                    LogUtils.d(TAG, "onClick: register");
                    mPresenter.register(userName, pwd, pwd2);
                } else { //for test
                    LogUtils.d(TAG, "onClick: register1");
                    mPresenter.register("987789123", "345543", "345543");
                }
                break;
        }
    }

//    @Override
//    public void showProgressDialog() {
//        super.showProgressDialog();
//    }
//
//
//    @Override
//    public void hideProgressDialog() {
//        super.hideProgressDialog();
//    }

    @Override
    public void Success(RegisterResult result) {
        LogUtils.d(TAG, "success: " + result);
        Toast.makeText(getContext(), getString(R.string.register_success), Toast.LENGTH_SHORT).show();
        //preference
        PreferenceConst.instance().setAccount(mUserName.getText().toString());
        ((LoginActivity) Objects.requireNonNull(getActivity())).getViewPager().setCurrentItem(0);
    }

    @Override
    public void Failed(String msg) {
        LogUtils.d(TAG, "failed: " + msg);
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
