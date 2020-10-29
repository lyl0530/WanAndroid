package com.lyl.wanandroid.ui.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.base.BaseActivity;
import com.lyl.wanandroid.ui.fragment.FragmentHotKey;
import com.lyl.wanandroid.ui.fragment.FragmentSearchResult;

public class SearchActivity extends BaseActivity{

    private FragmentManager mFragmentManager;
    private FragmentHotKey mFragmentHotKey;
    private FragmentSearchResult mFragmentSearchResult;
    private EditText mEtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();

        initData();
    }

    private void initView() {
        mEtSearch = findViewById(R.id.et_search);
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    onSearch(null);
                    return true;
                }
                return false;
            }
        });
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

}
