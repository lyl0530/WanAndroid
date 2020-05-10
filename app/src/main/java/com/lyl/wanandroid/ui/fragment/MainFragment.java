package com.lyl.wanandroid.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lyl.wanandroid.R;
import com.lyl.wanandroid.base.BaseFragment;
import com.lyl.wanandroid.bean.BannerResult;
import com.lyl.wanandroid.present.BannerPresenter;
import com.lyl.wanandroid.ui.view.LoadingDialog;
import com.lyl.wanandroid.util.LogUtils;
import com.lyl.wanandroid.view.BannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lym on 2020/4/9
 * Describe : 主页
 * 引入BaseFragment！！！
 */
public class MainFragment extends BaseFragment implements BannerView {
    private static final String TAG = MainFragment.class.getSimpleName();

    private View mView;
    private Context mContext;
    private ViewPager mViewPager;
    private MyAdapter mAdapter;

    private List<Integer> mImages = new ArrayList<>();//图片对应的id
    private List<ImageView> mImageViewList = new ArrayList<>();//图片视图
    private List<String> mImgPathList = new ArrayList<>();//真正的path
    private List<String> mUrlList = new ArrayList<>();//点击之后的跳转地址
    private List<String> mTitleList = new ArrayList<>();//对应的标题

    private TextView mBannerTitle, mBannerNumber;

    private BannerPresenter mPresenter;
    private LoadingDialog mLoadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        mContext = getContext();

        initView();

        mPresenter = new BannerPresenter();
        mPresenter.attach(this);

        //获取Banner信息，得到Banner图张数
        mPresenter.getBanner();
        return mView;
    }

    private void initView() {
        mViewPager = mView.findViewById(R.id.vp_banner);
        mBannerTitle = mView.findViewById(R.id.tv_banner_title);
        mBannerNumber = mView.findViewById(R.id.tv_banner_number);
    }

    @Override
    public void showProgressDialog() {
        if (null == mLoadingDialog) {
            mLoadingDialog = LoadingDialog.with(mContext);
        }
        mLoadingDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (null != mLoadingDialog) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void Success(BannerResult res) {
        LogUtils.d(TAG, "loginSuccess: " + res);
        if (null == res || null == res.getData()) return;

        //1 set data
        for (int i = 0; i < res.getData().size(); i++) {
            BannerResult.DataBean banner = res.getData().get(i);
            if (null == banner) continue;
            mImgPathList.add(banner.getImagePath());
            mUrlList.add(banner.getUrl());
            mTitleList.add(banner.getTitle());
        }
        Log.d(TAG, "Success: " + mImgPathList.toString() + ", " +
                mUrlList.toString() + ", " + mTitleList.toString());

        //2 获取Banner图片
        for (int i = 0; i < mImgPathList.size(); i++) {
            mImageViewList.add(new ImageView(mContext));
            displayBannerWithGlide(i);
        }

        //3 display banner
        //mImages.add(R.drawable.ic_default_avatar);
        mAdapter = new MyAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener);

        mBannerTitle.setText(mTitleList.get(0));
        mBannerNumber.setText(1 + "/" + mTitleList.size());
    }

    private void displayBannerWithGlide(int position) {
        //https://blog.csdn.net/lpCrazyBoy/article/details/85296285
        RequestOptions requestOptions = new RequestOptions()
                //.circleCrop()//如果需要裁剪图片，比如圆形，椭圆形等等
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        //DiskCacheStrategy.NONE： 表示关闭Glide的硬盘缓存机制,不缓存任何内容。
        //DiskCacheStrategy.SOURCE： 表示只缓存原始图片。
        //DiskCacheStrategy.RESULT： 表示只缓存转换过后的图片（默认选项）。
        //DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。

        Glide.with(this).load(mImgPathList.get(position))
                .apply(requestOptions)
                .into(mImageViewList.get(position));
    }

    private ViewPager.OnPageChangeListener mPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            Log.d(TAG, "onPageSelected: " + i + ", title = " +
                    mTitleList.get(i) + ", num = " + (i + 1) + "/" + mTitleList.size());
            mBannerTitle.setText(mTitleList.get(i));
            mBannerNumber.setText((i + 1) + "/" + mTitleList.size());
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    @Override
    public void Failed(String msg) {
        LogUtils.e(TAG, msg);
    }

    private class MyAdapter extends PagerAdapter {
        /**
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
            Log.d(TAG, "instantiateItem: " + position);
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
                    int currentItem = mViewPager.getCurrentItem();
                    Log.d(TAG, "onClick: " + (v instanceof ImageView) + ", " + currentItem);
                    //open it's url

                }
            });
            container.addView(imageView);
            return imageView;
        }
    }


    @Override
    public void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }
}
