package com.lyl.wanandroid.ui.adapter.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lyl.wanandroid.R;
import com.lyl.wanandroid.service.entity.BannerResult;
import com.lyl.wanandroid.service.entity.DataBean;
import com.lyl.wanandroid.utils.ConstUtil;
import com.lyl.wanandroid.utils.LogUtil;
import com.lyl.wanandroid.widget.ViewPagerScroller;
import com.lyl.wanandroid.widget.ViewPagerTransformer;

import org.apache.commons.text.StringEscapeUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BannerAdapter implements IDelegateAdapter {
    private static final String TAG = "BannerAdapter";

    private List<ImageView> mImageViewList = new ArrayList<>();//图片视图
    private List<String> mImgPathList = new ArrayList<>();//真正的path
    private List<String> mUrlList = new ArrayList<>();//点击之后的跳转地址
    private List<String> mTitleList = new ArrayList<>();//对应的标题

    private int oriImgCnt;//从server端获取的图片数量
    private int curImgCnt;//图DABCDA,补头补尾后的可以轮播的数量

    private RequestOptions requestOptions;
    private BannerPagerAdapter mPagerAdapter;

    private ViewPager mViewPager;
    private TextView mTvTitle, mTvNum;

    private final int MSG_BANNER = 1;
    private final int DELAY = 4 * 1000;

    private Context mContext;
    public BannerAdapter(Context context) {
        mContext = context;

        //https://blog.csdn.net/lpCrazyBoy/article/details/85296285
        requestOptions = new RequestOptions()
                //.circleCrop()//如果需要裁剪图片，比如圆形，椭圆形等等
                .placeholder(R.drawable.banner_empty)
                .error(R.drawable.banner_empty)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }


    @Override
    public boolean isForViewType(DataBean bean) {
        // 我能处理一张图片
        return bean.getType() == ConstUtil.TYPE_BANNER;
    }
 
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_banner, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int i, DataBean bean, int cnt) {
        LogUtil.d(TAG, "onBindViewHolder: i = " + i + ", " + bean);
        ViewHolder holder = (ViewHolder) vh;
        if (!(bean.getContent() instanceof BannerResult)) return;
        BannerResult res = (BannerResult) bean.getContent();
        handlerBannerData(res);

        mTvTitle = holder.tvTitle;
        mTvNum = holder.tvNumber;

        mTvTitle.setText(mTitleList.get(0));
        mTvNum.setText("1/" + oriImgCnt);

        setBannerViewPager(holder);
    }

    private void handlerBannerData(BannerResult res) {
        if (null == res) return;
        List<BannerResult.DataBean> dataList = res.getData();
        mImgPathList.clear();
        mUrlList.clear();
        mTitleList.clear();
        mImageViewList.clear();
        for (int i = 0; i < dataList.size(); i++) {
            BannerResult.DataBean banner = dataList.get(i);
            if (null == banner) continue;
            mImgPathList.add(banner.getImagePath());
            mUrlList.add(banner.getUrl());
            String title = StringEscapeUtils.unescapeHtml4(banner.getTitle());
            mTitleList.add(title);
        }

        oriImgCnt = mImgPathList.size();
        curImgCnt = oriImgCnt + 2;
        //2 获取Banner图片
        for (int i = 0; i < curImgCnt; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageViewList.add(imageView);
            String imgPath;
            if (0 == i) {
                imgPath = mImgPathList.get(oriImgCnt - 1);
            } else if (curImgCnt - 1 == i) {
                imgPath = mImgPathList.get(0);
            } else {
                imgPath = mImgPathList.get(i - 1);
            }

            Glide.with(mContext).load(imgPath)
                    .apply(requestOptions)
                    .into(mImageViewList.get(i));
        }
    }

    private void setBannerViewPager(ViewHolder holder){
        mViewPager = holder.vpBanner;
        mPagerAdapter = new BannerPagerAdapter(mContext, mViewPager, mImageViewList, mUrlList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(1);

        setViewPagerScroller(mViewPager);
        mViewPager.setPageTransformer(true, new ViewPagerTransformer());
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    //设置ViewPager滑动的速度
    private void setViewPagerScroller(ViewPager vp) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(mContext);
            mScroller.set(vp, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
            //position 当前所在页面
            //positionOffset 当前所在页面偏移百分比
            //positionOffsetPixels 当前所在页面偏移量
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onPageSelected(int i) {
            int titleIndex;//Banner图title对应的下标
            int currentItem = mViewPager.getCurrentItem();
            if (currentItem == 0) {//第一张图
                titleIndex = oriImgCnt - 1;//curImgCnt - 3;
            } else if (currentItem == curImgCnt - 1) {//最后一张图
                titleIndex = 0;
            } else {
                titleIndex = i - 1;
            }
            mTvTitle.setText(mTitleList.get(titleIndex));
            mTvNum.setText((titleIndex + 1) + "/" + oriImgCnt);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            int currentItem = mViewPager.getCurrentItem();
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE:
//                    LogUtils.i(TAG, "---->onPageScrollStateChanged 无动作");
                    if (oriImgCnt > 1) {
                        if (currentItem == 0) {
                            mViewPager.setCurrentItem(curImgCnt - 2, false);
                        } else if (currentItem == curImgCnt - 1) {
                            mViewPager.setCurrentItem(1, false);
                        }
                    }
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:
//                    LogUtils.i(TAG, "---->onPageScrollStateChanged 点击、滑屏");
//                        mHandler.removeCallbacksAndMessages(null);
//                        mHandler.sendEmptyMessageDelayed(MSG_BANNER, DELAY);
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
//                    LogUtils.i(TAG, "---->onPageScrollStateChanged 释放");
                    break;
            }
        }
    };

    static class ViewHolder extends RecyclerView.ViewHolder{
        ViewPager vpBanner;
        TextView tvTitle, tvNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vpBanner = itemView.findViewById(R.id.vp_banner);
            tvTitle = itemView.findViewById(R.id.tv_banner_title);
            tvNumber = itemView.findViewById(R.id.tv_banner_number);
        }
    }

    public void restartHandler(boolean reset){
        mHandler.removeCallbacksAndMessages(null);
        if (null != mViewPager){
            mViewPager.setCurrentItem(1);
        }
        if (reset) {
            mHandler.sendEmptyMessageDelayed(MSG_BANNER, DELAY);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_BANNER && null != mViewPager) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                mHandler.sendEmptyMessageDelayed(MSG_BANNER, DELAY);
            }
        }
    };
}