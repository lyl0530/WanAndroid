package com.lyl.wanandroid.ui.adapter.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lyl.wanandroid.R;
import com.lyl.wanandroid.app.BaseApplication;
import com.lyl.wanandroid.listener.OnArticleListListener;
import com.lyl.wanandroid.service.entity.ArticleBean;
import com.lyl.wanandroid.service.entity.BannerResult;
import com.lyl.wanandroid.service.entity.DataBean;
import com.lyl.wanandroid.utils.ConstUtil;
import com.lyl.wanandroid.utils.LogUtil;
import com.lyl.wanandroid.utils.PhoneUtil;
import com.lyl.wanandroid.widget.ViewPagerScroller;
import com.lyl.wanandroid.widget.ViewPagerTransformer;

import org.apache.commons.text.StringEscapeUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lym on 2020/10/21
 * Describe :
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "HomeAdapter";

    private Context mContext;
    private List<ImageView> mImageViewList = new ArrayList<>();//图片视图
    private List<String> mImgPathList = new ArrayList<>();//真正的path
    private List<String> mUrlList = new ArrayList<>();//点击之后的跳转地址
    private List<String> mTitleList = new ArrayList<>();//对应的标题

    private int oriImgCnt;//从server端获取的图片数量
    private int curImgCnt;//图DABCDA,补头补尾后的可以轮播的数量

    private RequestOptions requestOptions;
    private MyPagerAdapter mPagerAdapter;
    private List<DataBean> mResList;
    private int topArticleCnt;

    private final int MSG_BANNER = 1;
    private final int DELAY = 4 * 1000;

    public HomeAdapter(Context context, List<DataBean> resList, int cnt) {
        mContext = context;
        mResList = resList;
        topArticleCnt = cnt;
        initData();
    }

    private void initData() {
        //https://blog.csdn.net/lpCrazyBoy/article/details/85296285
        requestOptions = new RequestOptions()
                //.circleCrop()//如果需要裁剪图片，比如圆形，椭圆形等等
                .placeholder(R.drawable.banner_empty)
                .error(R.drawable.banner_empty)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        //分离出banner的数据
        getBannerData();
    }

    private void getBannerData() {
        DataBean bean = mResList.get(0);
        if (ConstUtil.TYPE_BANNER != bean.getType() ||
                !(bean.getContent() instanceof BannerResult)) return;
        try {
            BannerResult mBannerData = (BannerResult)bean.getContent();
            if (null == mBannerData) return;
            List<BannerResult.DataBean> dataList = mBannerData.getData();
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

//            LogUtil.d(TAG, "Success: mImgPathList \n" + mImgPathList.toString() + "\n mUrlList \n" +
//                    mUrlList.toString() + "\n mTitleList \n" + mTitleList.toString() +
//                    "\n mImageViewList.size = " + mImageViewList.size());
        } catch (ClassCastException e){
            Log.e(TAG, "类型转换失败 ");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == ConstUtil.TYPE_BANNER){
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_banner, viewGroup, false);
            return new BannerHolder(view);
        }
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_article_list, viewGroup, false);
        return new ArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof  BannerHolder){
            bindBannerHolder((BannerHolder)viewHolder, position);
            Log.d(TAG, "onBindViewHolder: 111");
        } else if (viewHolder instanceof ArticleHolder){
            bindArticleHolder((ArticleHolder)viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mResList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ConstUtil.TYPE_BANNER : ConstUtil.TYPE_ARTICLE;
    }

    private ViewPager mViewPager;
    @SuppressLint("SetTextI18n")
    private void bindBannerHolder(BannerHolder holder, int position){
        LogUtil.e(TAG, "bindBannerHolder begin : position = " + position);
        mViewPager = holder.vpBanner;
        mPagerAdapter = new MyPagerAdapter(holder.vpBanner);
        holder.vpBanner.setAdapter(mPagerAdapter);
        holder.vpBanner.setCurrentItem(1);

        setViewPagerScroller(holder.vpBanner);
        holder.vpBanner.setPageTransformer(true, new ViewPagerTransformer());

        holder.tvTitle.setText(mTitleList.get(position));
        holder.tvNumber.setText((position + 1) + "/" + oriImgCnt);

        holder.vpBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                int currentItem = holder.vpBanner.getCurrentItem();
                if (currentItem == 0) {//第一张图
                    titleIndex = oriImgCnt - 1;//curImgCnt - 3;
                } else if (currentItem == curImgCnt - 1) {//最后一张图
                    titleIndex = 0;
                } else {
                    titleIndex = i - 1;
                }
                holder.tvTitle.setText(mTitleList.get(titleIndex));
                holder.tvNumber.setText((titleIndex + 1) + "/" + oriImgCnt);

//            LogUtils.d(TAG, "title = " + mTitleList.get(titleIndex) +
//                    ", num = " + (titleIndex + 1) + "/" + oriImgCnt);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int currentItem = holder.vpBanner.getCurrentItem();
//            LogUtils.d(TAG, "onPageScrollStateChanged: currentItem = " + currentItem);
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
//                    LogUtils.i(TAG, "---->onPageScrollStateChanged 无动作");
                        if (oriImgCnt > 1) {
                            if (currentItem == 0) {
                                holder.vpBanner.setCurrentItem(curImgCnt - 2, false);
                            } else if (currentItem == curImgCnt - 1) {
                                holder.vpBanner.setCurrentItem(1, false);
                            }
                        }
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
//                    LogUtils.i(TAG, "---->onPageScrollStateChanged 点击、滑屏");
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
//                    LogUtils.i(TAG, "---->onPageScrollStateChanged 释放");
                        break;
                }
            }
        });
        LogUtil.e(TAG, "bindBannerHolder end");
    }

    private void bindArticleHolder(ArticleHolder holder, int position){
        LogUtil.e(TAG, "bindArticleHolder begin, position = " + position);
        Object content = mResList.get(position).getContent();
        if (!(content instanceof ArticleBean)) return;
        ArticleBean bean = (ArticleBean)content;
        int drawableResId = bean.isCollect() && BaseApplication.isLogin() ?
                R.drawable.icon_collected :
                R.drawable.icon_collecte;

        String title = bean.getTitle();
        String newTitle = StringEscapeUtils.unescapeHtml4(title);
        Log.d(TAG, "getView: " + newTitle + ", " + bean.isCollect());

        String author = bean.getAuthor();
        if (TextUtils.isEmpty(author)) {
            author = bean.getShareUser();
        }
        String time = bean.getNiceDate();

        holder.ibtnCollect.setBackground(mContext.getResources().getDrawable(drawableResId));
        holder.ibtnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 20200729: " + bean.getId());
                if (null != mListener){
                    mListener.onItemCollect(bean.getId(), position, bean.isCollect());
                }
            }
        });

        holder.tvTitle.setText(StringEscapeUtils.unescapeHtml4(newTitle));
        holder.tvAuthor.setText(author);
        holder.tvTime.setText(time);

        if(1 == position || topArticleCnt+1 == position) {
            holder.imgNew.setVisibility(View.VISIBLE);
        } else {
            holder.imgNew.setVisibility(View.GONE);
        }
        if (position <= topArticleCnt) {
            holder.imgTop.setVisibility(View.VISIBLE);
        } else {
            holder.imgTop.setVisibility(View.GONE);
        }

        //RecycleView item的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onItemClick(bean);
                }
            }
        });

        LogUtil.e(TAG, "bindArticleHolder end");
    }

    static class BannerHolder extends RecyclerView.ViewHolder{
        ViewPager vpBanner;
        TextView tvTitle, tvNumber;
        public BannerHolder(@NonNull View itemView) {
            super(itemView);

            vpBanner = itemView.findViewById(R.id.vp_banner);
            tvTitle = itemView.findViewById(R.id.tv_banner_title);
            tvNumber = itemView.findViewById(R.id.tv_banner_number);
        }
    }

    static class ArticleHolder extends RecyclerView.ViewHolder{
        ImageButton ibtnCollect;
        TextView tvTitle, tvAuthor, tvTime;
        ImageView imgNew, imgTop;
        public ArticleHolder(@NonNull View itemView) {
            super(itemView);
            ibtnCollect = itemView.findViewById(R.id.ibtn_collect);
            tvTitle = itemView.findViewById(R.id.tv_title);
            imgNew = itemView.findViewById(R.id.img_new);
            imgTop = itemView.findViewById(R.id.img_top);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        private ViewPager mVp;
        public MyPagerAdapter(ViewPager vp) {
            mVp = vp;
        }

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
                    int currentItem = mVp.getCurrentItem();
                    LogUtil.d(TAG, "onClick: " + (v instanceof ImageView) + ", " + currentItem);
                    //open it's url
                    PhoneUtil.openInWebView(mContext, mUrlList.get(currentItem-1));
                }
            });
            container.addView(imageView);
            return imageView;
        }
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

    private OnArticleListListener mListener;
    public void setOnArticleListListener(OnArticleListListener l){
        mListener = l;
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
