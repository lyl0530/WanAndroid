package com.lyl.wanandroid.ui.adapter.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lyl.wanandroid.utils.LogUtil;
import com.lyl.wanandroid.utils.PhoneUtil;

import java.util.List;

/**
 * Created by lym on 2020/10/30
 * Describe :
 * ViewPager里面对每个页面的管理是key-value形式的，也就是说每个page都有个对应的id（id是object类型），
 * 需要对page操作的时候都是通过id来完成的
 * <p>
 * public Object instantiateItem(ViewGroupcontainer,intposition)；
 * 功能就是往PageView里添加自己需要的page。同时注意它还有个返回值object，这就是那个id。
 * <p>
 * public abstract boolean isViewFromObject(Viewview,Objectobject)
 * 这个函数就是用来告诉框架，这个view的id是不是这个object。
 * <p>
 * 谷歌官方推荐把view当id用，所以常规的instantiateItem()函数的返回值是你自己定义的view，
 * 而isViewFromObject()的返回值是view==object。
 * ps：感觉这个机制应该是历史遗留问题，属于改bug改出来的机制。
 * 否则官方不会推荐这种把view当id的做法。
 * https://segmentfault.com/q/1010000000484617
 */
public class BannerPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ViewPager mVp;
    private List<ImageView> mImageViewList;
    private List<String> mUrlList;
    public BannerPagerAdapter(Context context, ViewPager vp, List<ImageView> imageViewList,
                              List<String> urlList) {
        mContext = context;
        mVp = vp;
        mImageViewList = imageViewList;
        mUrlList = urlList;
    }

    @Override
    public int getCount() {
        return mImageViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //LogUtils.d(TAG, "instantiateItem: " + position);
        //https://blog.csdn.net/liangcaiyun2013/article/details/45100903
        //始终存在2个view，左滑或者右滑都会预先加载下一个view，销毁多余的view。
        //参数position非当前item的index，而是预先加载的下个view的index。

        //ImageView imageView = new ImageView(mContext);//mImageViewList.get(position);//
        //imageView.setBackgroundResource(mImages.get(position));
        //container.addView(imageView);
        //return imageView;

        ImageView imageView = mImageViewList.get(position);
        //ViewPager的Item的onClick监听 https://blog.csdn.net/lebron_wei/article/details/51463110
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mVp.getCurrentItem();
//                LogUtil.d(TAG, "onClick: " + (v instanceof ImageView) + ", " + currentItem);
                //open it's url
                PhoneUtil.openInWebView(mContext, mUrlList.get(currentItem-1));
            }
        });
        container.addView(imageView);
        return imageView;
    }
}
