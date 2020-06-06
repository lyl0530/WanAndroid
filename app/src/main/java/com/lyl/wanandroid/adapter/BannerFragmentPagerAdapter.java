package com.lyl.wanandroid.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.lyl.wanandroid.bean.BannerResult;
import com.lyl.wanandroid.ui.fragment.BannerFragment;

/**
 * Created by lym on 2020/6/5
 * Describe :
 */
public class BannerFragmentPagerAdapter extends FragmentPagerAdapter {

    private BannerResult mRes;
    private SparseArray<Fragment> mFragmentList = new SparseArray<>();

    public BannerFragmentPagerAdapter(FragmentManager fm, BannerResult res) {
        super(fm);
        mRes = res;
    }

    @Override
    public Fragment getItem(int i) {
        return BannerFragment.newInstance((i + 1) + "/" + getCount(), mRes.getData().get(i));
    }

    @Override
    public int getCount() {
        return mRes.getData().size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        BannerFragment fm = (BannerFragment) super.instantiateItem(container, position);
        mFragmentList.put(position, fm);
        return fm;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        mFragmentList.remove(position);
        super.destroyItem(container, position, object);
    }
}
