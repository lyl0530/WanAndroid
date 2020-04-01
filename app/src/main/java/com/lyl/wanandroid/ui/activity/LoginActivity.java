package com.lyl.wanandroid.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.present.LoginPresent;
import com.lyl.wanandroid.view.LoginView;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private LoginPresent mPresent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPresent = new LoginPresent(this);
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void loginSuccess() {

    }

    @Override
    public void loginFailed(String msg) {

    }

    public void onLogin(View view) {
        mPresent.login("123147258", "123456");
    }
}
