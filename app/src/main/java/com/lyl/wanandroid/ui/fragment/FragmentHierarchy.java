package com.lyl.wanandroid.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.ui.adapter.HierarchyFragmentPagerAdapter;

/**
 * Created by lym on 2020/4/9
 * Describe :
 */
public class FragmentHierarchy extends Fragment implements View.OnClickListener {
    private View mRootView;
    private ViewPager mVp;
    private FragmentActivity mActivity;
    private HierarchyFragmentPagerAdapter mAdapter;
    private TextView tvHierarchy, tvNavigation;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_hierarchy, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initDate();
    }

    private void initView() {
        tvHierarchy = mRootView.findViewById(R.id.tv_hierarchy);
        tvNavigation = mRootView.findViewById(R.id.tv_navigation);
        tvHierarchy.setOnClickListener(this);
        tvNavigation.setOnClickListener(this);

        mVp = mRootView.findViewById(R.id.vp_hierarchy);
    }

    private void initDate() {
        mActivity = getActivity();
        if (null == mActivity) return;
        mAdapter = new HierarchyFragmentPagerAdapter(mActivity.getSupportFragmentManager());
        mVp.setAdapter(mAdapter);

        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setPage(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setPage(int i) {
        if (i == 0) {
            tvHierarchy.setTextColor(getResources().getColor(R.color.white));
            tvNavigation.setTextColor(getResources().getColor(R.color.little_dark));
        } else {
            tvHierarchy.setTextColor(getResources().getColor(R.color.little_dark));
            tvNavigation.setTextColor(getResources().getColor(R.color.white));
        }
        mVp.setCurrentItem(i);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_hierarchy) {
            setPage(0);
        } else {
            setPage(1);
        }
    }
}
