package com.lyl.wanandroid.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.adapter.HierarchyFragmentPagerAdapter;
import com.lyl.wanandroid.base.BaseFragment;
import com.lyl.wanandroid.bean.HotKeyResult;
import com.lyl.wanandroid.bean.ProjectArticleListResult;
import com.lyl.wanandroid.mvp.present.HotKeyPresenter;
import com.lyl.wanandroid.mvp.present.SearchPresenter;
import com.lyl.wanandroid.mvp.view.HotKeyView;
import com.lyl.wanandroid.mvp.view.SearchView;
import com.lyl.wanandroid.ui.activity.SearchActivity;
import com.lyl.wanandroid.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lym on 2020/4/9
 * Describe :
 */
public class FragmentHotKey extends BaseFragment implements HotKeyView{
    private static final String TAG = "FragmentHotKey";

    private View mRootView;
    private ViewPager mVp;
    private FragmentActivity mActivity;
    private HierarchyFragmentPagerAdapter mAdapter;
    private TextView tvHierarchy, tvNavigation;
    private HotKeyPresenter mPresenter;
    private List<String> mHotKeyList = new ArrayList<>();

    private FlowLayout mHotKeyLayout;
    private TextView mTvHotKey, mTvHistory;
    private Context mContext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_hot_key, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();
        initData();
    }

    private void initView() {
        mTvHotKey = mRootView.findViewById(R.id.tv_hot_key);
        mHotKeyLayout = mRootView.findViewById(R.id.fl_hot_key);
        mTvHistory = mRootView.findViewById(R.id.tv_search_history);

    }
    private void initData() {
        mPresenter = new HotKeyPresenter();
        mPresenter.attach(this);
        mPresenter.getHotKey();

        mHotKeyLayout.setItemClickListener(new FlowLayout.ItemClickListener() {
            @Override
            public void clickItem(int position) {
                String content = mHotKeyList.get(position);
                if (null != mListener){
                    mListener.onItemClick(content);
                }
            }
        });
    }

    public interface ItemClickListener{
        void onItemClick(String key);
    }
    private ItemClickListener mListener;
    public void setItemClickListener(ItemClickListener l){
        mListener = l;
    }
    @Override
    public void onDestroy() {
        if (null != mPresenter){
            mPresenter.detach();
            mPresenter  = null;
        }
        super.onDestroy();
    }

    @Override
    public void Failed(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Success(HotKeyResult res) {
        Log.e(TAG, "Success: res = " + res);
        mHotKeyList.clear();
        List<HotKeyResult.DataBean> beanList = res.getData();
        for (int i = 0; i <beanList .size(); i++){
            mHotKeyList.add(beanList.get(i).getName());
        }
        mTvHotKey.setVisibility(View.VISIBLE);
        mHotKeyLayout.addItem(mHotKeyList);
    }
}
