package com.lyl.wanandroid.ui.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.ui.fragment.LoginFragment;
import com.lyl.wanandroid.ui.fragment.RegisterFragment;
import com.lyl.wanandroid.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements /*LoginView, */View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();


    private FrameLayout mFrameLayout;
    public static final int TYPE_ADD = 1;
    public static final int TYPE_REPLACE = 2;

    private TextView mLogin, mRegister;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initView() {
        mViewPager = findViewById(R.id.login_register);
        mLogin = findViewById(R.id.tv_login);
        mRegister = findViewById(R.id.tv_register);
    }

    private void initData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new LoginFragment());
        mFragmentList.add(new RegisterFragment());

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener);

        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }

    private final ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            setPosition(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private void setPosition(int i) {
        mRegister.setVisibility(0 == i ? View.VISIBLE : View.INVISIBLE);
        mLogin.setVisibility(0 == i ? View.INVISIBLE : View.VISIBLE);
        mViewPager.setCurrentItem(i);
    }

    private final FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.d(TAG, "onKeyDown: " + keyCode);
        if (KeyEvent.KEYCODE_BACK == keyCode){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBack(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                setPosition(0);
                break;
            case R.id.tv_register:
                setPosition(1);
                break;
        }
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }
}
