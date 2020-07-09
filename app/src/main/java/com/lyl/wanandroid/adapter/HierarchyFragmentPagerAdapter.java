package com.lyl.wanandroid.adapter;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.lyl.wanandroid.ui.fragment.FrgmHierarchy;
import com.lyl.wanandroid.ui.fragment.FrgmNavigation;

/**
 * Created by lym on 2020/7/5
 * Describe : 体系Tab中的第一个子tab
 */
public class HierarchyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final static int FM_CNT = 2;
    private SparseArray<Fragment> fragmentList = new SparseArray<>();
    public HierarchyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        return 1==pos ? FrgmNavigation.newInstance() : FrgmHierarchy.newInstance();
    }

    @Override
    public int getCount() {
        return FM_CNT;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //super方法里会调用getItem()方法！
        Fragment fm = (Fragment)super.instantiateItem(container, position);
        fragmentList.put(position, fm);
        return fm;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        fragmentList.remove(position);
        super.destroyItem(container, position, object);
    }
}
