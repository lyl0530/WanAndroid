package com.lyl.wanandroid.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.ui.adapter.HierarchyFragmentPagerAdapter;
import com.lyl.wanandroid.base.BaseFragment;
import com.lyl.wanandroid.service.entity.HotKeyResult;
import com.lyl.wanandroid.service.present.HotKeyPresenter;
import com.lyl.wanandroid.service.view.HotKeyView;
import com.lyl.wanandroid.ui.view.FlowLayout;
import com.lyl.wanandroid.utils.ErrorUtil;
import com.lyl.wanandroid.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    private FlowLayout mHotKeyLayout, mHistoryLayout;
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
        mHistoryLayout = mRootView.findViewById(R.id.fl_search_history);
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

        mHistoryLayout.setItemClickListener(new FlowLayout.ItemClickListener() {
            @Override
            public void clickItem(int position) {
                Set<String> historySet = PreferenceUtil.instance().getSearchHistory();
                List<String> historyList = new ArrayList<>(historySet);
                String content = historyList.get(position);
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
    public void Failed(int code, String msg) {
        String str = ErrorUtil.getErrorInfo(mContext, code, msg);
        showToast(str);
    }

    @Override
    public void Success(HotKeyResult res) {
        Log.e(TAG, "Success: res = " + res);
        mHotKeyList.clear();
        List<HotKeyResult.DataBean> beanList = res.getData();
        for (int i = 0; i <beanList .size(); i++){
            mHotKeyList.add(beanList.get(i).getName());
        }
        if (mHotKeyList.isEmpty()) {
            mTvHotKey.setVisibility(View.INVISIBLE);
        } else {
            mTvHotKey.setVisibility(View.VISIBLE);
            mHotKeyLayout.addItem(mHotKeyList);
        }
        showHistory();
    }

    @Override
    public void onResume() {
        super.onResume();
        showHistory();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            showHistory();
        }
    }

    private void showHistory(){
        //显示搜索历史
        Set<String> historySet = PreferenceUtil.instance().getSearchHistory();
        if (!historySet.isEmpty() && View.VISIBLE == mTvHotKey.getVisibility()){
            List<String> historyList = new ArrayList<>(historySet);
            mHistoryLayout.addItem(historyList);
            mTvHistory.setVisibility(View.VISIBLE);
        } else {
            mTvHistory.setVisibility(View.INVISIBLE);
        }
    }
}
