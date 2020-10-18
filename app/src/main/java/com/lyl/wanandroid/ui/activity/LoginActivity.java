package com.lyl.wanandroid.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.base.BaseActivity;
import com.lyl.wanandroid.constant.Const;
import com.lyl.wanandroid.ui.fragment.FragmentLogin;
import com.lyl.wanandroid.ui.fragment.FragmentRegister;
import com.lyl.wanandroid.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends BaseActivity implements /*LoginView, */View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final int FM_CNT = 2;//fragment的数量
    private FrameLayout mFrameLayout;
    public static final int TYPE_ADD = 1;
    public static final int TYPE_REPLACE = 2;

    private TextView mLogin, mRegister;
    private ViewPager mViewPager;
    //activity和fragment交互，即调用fragment的方法时，要先判断空
    // 若设置的是默认缓存，即mViewPager.setOffscreenPageLimit(size);的参数为0或1时
    // 左边一个fragment，右边一个fragment，滑动到第3个fragment时，
    // 第0个fragment已经被remove了，此时tabFragment为null
    private /*List<Fragment>*/ SparseArray<Fragment> mFragmentList = new SparseArray<>();
    private FragmentPagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        articleId = getIntent().getIntExtra(Const.ARTICLE_ID, -1);
        initView();
        initData();

        EventBus.getDefault().register(this);//这句话要放在初始化组件(findViewById)之后，不然页面接收不到参数
    }

    private void initView() {
        mViewPager = findViewById(R.id.login_register);
        mLogin = findViewById(R.id.tv_login);
        mRegister = findViewById(R.id.tv_register);
    }

    private void initData() {
        initAdapter();
//        mFragmentList = new ArrayList<>();
//        mFragmentList.add(new LoginFragment());
//        mFragmentList.add(new RegisterFragment());

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        //mViewPager.setOffscreenPageLimit();共2个fragment，不需要设置预加载页面的数量，默认会预加载下一页
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }

    private void initAdapter() {
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return /*mFragmentList.size();*/FM_CNT;
            }

            @Override
            public Fragment getItem(int i) {
                if (1 == i) {
                    return new FragmentRegister();
                }
                return new FragmentLogin();
                //return mFragmentList.get(i);
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                Fragment fm = (Fragment) super.instantiateItem(container, position);
                mFragmentList.put(position, fm);
                return fm;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                mFragmentList.remove(position);
                super.destroyItem(container, position, object);
            }
        };
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

    private int articleId = -1;//登录成功后，把要收藏的文章索引传回去
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setRefreshMain(String str){
        Log.d(TAG, "setRefreshMain: 20200729  " + str + ", " + articleId);
        if (Const.REFRESH_MAIN.equals(str) && articleId > 0){
            Intent intent = new Intent();
            intent.putExtra(Const.ARTICLE_ID, articleId);
            setResult(Const.RESULT_CODE_LOGIN, intent);
        }
    }

    @Override
    protected void onDestroy() {
//        Log.d(TAG, "onDestroy: 32121 ");
        //setResult(Const.RESULT_CODE_LOGIN);

        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
