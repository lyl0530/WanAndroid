package com.lyl.wanandroid.ui.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.base.BaseActivity;
import com.lyl.wanandroid.bean.HotKeyResult;
import com.lyl.wanandroid.bean.ProjectArticleListResult;
import com.lyl.wanandroid.mvp.present.SearchPresenter;
import com.lyl.wanandroid.mvp.view.SearchView;
import com.lyl.wanandroid.ui.fragment.FragmentHotKey;
import com.lyl.wanandroid.ui.fragment.FragmentSearchResult;
import com.lyl.wanandroid.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity{
    private static final String TAG = SearchActivity.class.getSimpleName();

    private Context mContext;

    private FragmentManager mFragmentManager;
    private FragmentHotKey mFragmentHotKey;
    private FragmentSearchResult mFragmentSearchResult;
    private EditText mEtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = this;
        initView();

        initData();
    }

    private void initView() {
        mEtSearch = findViewById(R.id.et_search);

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        Fragment fragment = mFragmentManager.findFragmentByTag(FragmentHotKey.class.getName());
        if (null == fragment){
            mFragmentHotKey = new FragmentHotKey();
            transaction.add(R.id.fl, mFragmentHotKey, FragmentHotKey.class.getName());
        } else {
            mFragmentHotKey = (FragmentHotKey)fragment;
        }

        fragment = mFragmentManager.findFragmentByTag(FragmentSearchResult.class.getName());
        if (null == fragment){
            mFragmentSearchResult = new FragmentSearchResult();
            transaction.add(R.id.fl, mFragmentSearchResult, FragmentSearchResult.class.getName());
        } else {
            mFragmentSearchResult = (FragmentSearchResult)fragment;
        }
        transaction.hide(mFragmentSearchResult);
        transaction.show(mFragmentHotKey);
        transaction.commit();
    }

    private void initData() {
        mFragmentHotKey.setItemClickListener(new FragmentHotKey.ItemClickListener() {
            @Override
            public void onItemClick(String key) {
                showHotKeyFragment(false);
                mFragmentSearchResult.search(key);
                mEtSearch.setText(key);
            }
        });
    }

    private boolean mShowHotKeyFragment = true;
    private void showHotKeyFragment(boolean show){
        mShowHotKeyFragment = show;
        FragmentTransaction t = mFragmentManager.beginTransaction();
        t.show(show ? mFragmentHotKey : mFragmentSearchResult);
        t.hide(show ? mFragmentSearchResult : mFragmentHotKey);
        t.commit();
    }

    public void onSearch(View v){
        String content = mEtSearch.getText().toString();
        if (!TextUtils.isEmpty(content)){
            showHotKeyFragment(false);
            mFragmentSearchResult.search(content);
        }
    }

    public void onBack(View v){
        if (mShowHotKeyFragment) {
            finish();
        } else {
            showHotKeyFragment(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
