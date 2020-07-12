package com.lyl.wanandroid.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lyl.wanandroid.R;
import com.lyl.wanandroid.base.BaseFragment;
import com.lyl.wanandroid.bean.BannerResult;
import com.lyl.wanandroid.bean.TopArticleResult;
import com.lyl.wanandroid.mvp.present.MainPresenter;
import com.lyl.wanandroid.mvp.view.MainView;
import com.lyl.wanandroid.ui.activity.MainActivity;
import com.lyl.wanandroid.ui.activity.Test;
import com.lyl.wanandroid.util.LogUtils;
import com.lyl.wanandroid.view.CircleView;
import com.lyl.wanandroid.widget.ViewPagerScroller;
import com.lyl.wanandroid.widget.ViewPagerTransformer;

import org.apache.commons.text.StringEscapeUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//import org.apache.commons.lang3.StringEscapeUtils;


/**
 * Created by lym on 2020/4/9
 * Describe : 主页
 *
 */
public class FragmentMain extends BaseFragment implements MainView, View.OnClickListener {
    private static final String TAG = FragmentMain.class.getSimpleName();

    private View mView;
    private Context mContext;

    private AppBarLayout mTitleBar;
    private /*ImageView*/ CircleView mAvatar;

    private ViewPager mViewPager;
    private MyAdapter mAdapter;
//    private BannerFragmentPagerAdapter mAdapter;

    private List<Integer> mImages = new ArrayList<>();//图片对应的id
    private List<ImageView> mImageViewList = new ArrayList<>();//图片视图
    private List<String> mImgPathList = new ArrayList<>();//真正的path
    private List<String> mUrlList = new ArrayList<>();//点击之后的跳转地址
    private List<String> mTitleList = new ArrayList<>();//对应的标题

    private TextView mBannerTitle, mBannerNumber;

    private MainPresenter mPresenter;

    private int oriImgCnt;//从server端获取的图片数量
    private int curImgCnt;//图DABCDA,补头补尾后的可以轮播的数量

    private RequestOptions requestOptions;

    private final int MSG_BANNER = 1;
    private final int DELAY = 2 * 1000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        mContext = getContext();

        initView();
        initData();
        mPresenter = new MainPresenter();
        mPresenter.attach(this);

        //获取Banner信息，得到Banner图张数
        mPresenter.getBanner();

        return mView;
    }

    private void initData() {
        //https://blog.csdn.net/lpCrazyBoy/article/details/85296285
        requestOptions = new RequestOptions()
                //.circleCrop()//如果需要裁剪图片，比如圆形，椭圆形等等
                .placeholder(R.drawable.banner_empty)
                .error(R.drawable.banner_empty)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        //DiskCacheStrategy.NONE： 表示关闭Glide的硬盘缓存机制,不缓存任何内容。
        //DiskCacheStrategy.SOURCE： 表示只缓存原始图片。
        //DiskCacheStrategy.RESULT： 表示只缓存转换过后的图片（默认选项）。
        //DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。

    }

    private void initView() {
        //titleBar
        mTitleBar = mView.findViewById(R.id.title_bar);
        mAvatar = mTitleBar.findViewById(R.id.avatar);
        mAvatar.setOnClickListener(this);
        //new Thread(mAvatar).start();

        mViewPager = mView.findViewById(R.id.vp_banner);
        mBannerTitle = mView.findViewById(R.id.tv_banner_title);
        mBannerNumber = mView.findViewById(R.id.tv_banner_number);
    }

    @Override
    public void Success(BannerResult res) {
        LogUtils.d(TAG, "banner Success: " + res);
        if (null == res || null == res.getData()) return;

        //1 set data
        for (int i = 0; i < res.getData().size(); i++) {
            BannerResult.DataBean banner = res.getData().get(i);
            if (null == banner) continue;
            mImgPathList.add(banner.getImagePath());
            mUrlList.add(banner.getUrl());
            String title = banner.getTitle();
            String newTitle = StringEscapeUtils.unescapeHtml4(title);
            LogUtils.d(TAG, "Success: " + title + ", " + newTitle);
            mTitleList.add(newTitle);
        }
        LogUtils.d(TAG, "Success: " + mImgPathList.toString() + ", " +
                mUrlList.toString() + ", " + mTitleList.toString());

        oriImgCnt = mImgPathList.size();
        curImgCnt = oriImgCnt + 2;
        //2 获取Banner图片
        for (int i = 0; i < curImgCnt; i++) {
            mImageViewList.add(new ImageView(mContext));
            String imgPath;
            if (0 == i) {
                imgPath = mImgPathList.get(oriImgCnt - 1);
            } else if (curImgCnt - 1 == i) {
                imgPath = mImgPathList.get(0);
            } else {
                imgPath = mImgPathList.get(i - 1);
            }

            Glide.with(this).load(imgPath)
                    .apply(requestOptions)
                    .into(mImageViewList.get(i));
        }

        //3 display banner
        //mImages.add(R.drawable.ic_default_avatar);
        mAdapter = new MyAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mViewPager.setCurrentItem(1);

        setViewPagerScroller();
        mViewPager.setPageTransformer(true, new ViewPagerTransformer());

        mHandler.sendEmptyMessageDelayed(MSG_BANNER, DELAY);
//        FragmentActivity activity = getActivity();
//        if (null == activity) return;
//        mAdapter = new BannerFragmentPagerAdapter(activity.getSupportFragmentManager(), res);
//        mViewPager.setAdapter(mAdapter);
//        mViewPager.setOffscreenPageLimit(res.getData().size());
    }

    @Override
    public void showProgressDialog() {
        super.showProgressDialog();
    }

    @Override
    public void hideProgressDialog() {
        super.hideProgressDialog();

        //获取置顶文章列表
        if (null != mPresenter && !sExecute) {
            sExecute = true;
            mPresenter.getTopArticle();
        }
    }

    private static boolean sExecute = false;
    @Override
    public void Success(TopArticleResult res) {
        LogUtils.d(TAG, "top article Success: " + res);
        if (null == res || null == res.getData()) return;
    }


    private ViewPager.OnPageChangeListener mPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //position 当前所在页面
            //positionOffset 当前所在页面偏移百分比
            //positionOffsetPixels 当前所在页面偏移量
        }

        @Override
        public void onPageSelected(int i) {
            LogUtils.d(TAG, "onPageSelected: " + i);

            int titleIndex;//Banner图title对应的下标
            int currentItem = mViewPager.getCurrentItem();
            if (currentItem == 0) {//第一张图
                titleIndex = oriImgCnt - 1;//curImgCnt - 3;
            } else if (currentItem == curImgCnt - 1) {//最后一张图
                titleIndex = 0;
            } else {
                titleIndex = i - 1;
            }
            mBannerTitle.setText(mTitleList.get(titleIndex));
            mBannerNumber.setText((titleIndex + 1) + "/" + oriImgCnt);

            LogUtils.d(TAG, "title = " + mTitleList.get(titleIndex) +
                    ", num = " + (titleIndex + 1) + "/" + oriImgCnt);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            int currentItem = mViewPager.getCurrentItem();
            LogUtils.d(TAG, "onPageScrollStateChanged: currentItem = " + currentItem);
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE:
                    LogUtils.i(TAG, "---->onPageScrollStateChanged 无动作");
                    if (oriImgCnt > 1) {
                        if (currentItem == 0) {
                            mViewPager.setCurrentItem(curImgCnt - 2, false);
                        } else if (currentItem == curImgCnt - 1) {
                            mViewPager.setCurrentItem(1, false);
                        }
                    }
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:
                    LogUtils.i(TAG, "---->onPageScrollStateChanged 点击、滑屏");
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.sendEmptyMessageDelayed(MSG_BANNER, DELAY);
                    //https://blog.csdn.net/oweixiao123/article/details/23459041
                    //图     D A B C D A
                    //index  0 1 2 3 4 5
                    // 图D划向图A的时候，currentItem为4,当手指抬起，动作释放，划动完成后，
                    // 划动状态会变为SCROLL_STATE_IDLE，此时currentItem为5，进入第二个if
                    // 设置当前Item为1，即显示图片A
                    // setCurrentItem参数2为false:不显示跳转过程的动画
                    // 若在onPageSelected中执行setCurrentItem，从图D到图A切换时，会突然没有动画，很突兀。

                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    LogUtils.i(TAG, "---->onPageScrollStateChanged 释放");
                    break;
            }
        }
    };

    @Override
    public void Failed(String msg) {
        LogUtils.e(TAG, msg);
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    //设置ViewPager滑动的速度
    private void setViewPagerScroller() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(getContext());
            mScroller.set(mViewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_BANNER:
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    mHandler.sendEmptyMessageDelayed(MSG_BANNER, DELAY);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar:
                ((MainActivity) Objects.requireNonNull(getActivity())).clickMainAvatar();
                break;
        }
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
                    int currentItem = mViewPager.getCurrentItem();
                    LogUtils.d(TAG, "onClick: " + (v instanceof ImageView) + ", " + currentItem);
                    //open it's url

                }
            });
            container.addView(imageView);
            return imageView;
        }
    }

    @Override
    public void onDestroy() {
        if (null != mPresenter) {
            mPresenter.detach();
        }
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}