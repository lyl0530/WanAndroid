package com.lyl.wanandroid.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.base.BaseActivity;
import com.lyl.wanandroid.constant.Const;
import com.lyl.wanandroid.listener.ScrollViewListener;
import com.lyl.wanandroid.ui.fragment.FragmentProjectArticleList;
import com.lyl.wanandroid.util.Utils;
import com.lyl.wanandroid.view.TitleScrollView;

import java.util.ArrayList;

public class ArticleListActivity extends BaseActivity {
    private static final String TAG = "lym123";

    private TitleScrollView mTitleScrollView;
    private LinearLayout mLinearLayout;
    private Context mContext;

    //scrCx : 屏幕的中轴线
    //scrollOverX : 滑动后，偏移的x坐标
    private int scrCx, scrollOverX;

    private ArrayList<String> mTitleList;
    private ArrayList<Integer> mCidList;
    private int mCurPos = -1;
    private ViewPager mVp;
    private FragmentStatePagerAdapter mAdapter;
    private SparseArray<FragmentProjectArticleList> mFrList = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        mContext = this;

        Intent intent = getIntent();
        if (null != intent){
            mTitleList = intent.getStringArrayListExtra(Const.ARTICLE_TITLE_LIST);
            mCidList = intent.getIntegerArrayListExtra(Const.ARTICLE_CID_LIST);
            mCurPos = intent.getIntExtra(Const.ARTICLE_POSITION, -1);
        }

        initView();
        initData();
    }

    private void initView() {
        mTitleScrollView = findViewById(R.id.tsv);
        mLinearLayout = findViewById(R.id.ll_tsv);
        mTitleScrollView.setListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(HorizontalScrollView scrollView, int l, int t, int oldl, int oldt) {
//                Log.d(TAG, "onScrollChanged: oldl =" + oldl + ", l = " + l);
                scrollOverX = l;
            }
        });
        mVp = findViewById(R.id.vp);

    }

    private void initData() {
        addView(mTitleList);
        showContent();
    }

    private void addView(ArrayList<String> list){
        if (null == list || 0 == list.size()) return;
        Log.d(TAG, "showHScrollTextView: list = " + list);

        scrCx = Utils.getScreenW(mContext)/2;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(Utils.dp2px(mContext,15),0,Utils.dp2px(mContext,15),0);//4个参数按顺序分别是左上右下
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        for (int i = 0; i < list.size(); i++){
            TextView tv = new TextView(mContext);
            tv.setText(list.get(i));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, i == 0 ? 20 : 17);

            tv.setTextColor(i == 0 ? Color.WHITE : getResources().getColor(R.color.little_dark));
            tv.setLayoutParams(layoutParams);
            tv.setTag(i);
            mLinearLayout.addView(tv);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (int)((TextView)view).getTag();
                    mVp.setCurrentItem(index);
                    onItemClick(index);
                }
            });
        }

        //只写mLineLayout.getRight()，获取的值为0，此时mLineLayout还没绘制完成，得不到值
        //https://www.cnblogs.com/ouyangping/p/7398753.html
        mLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                int w = mLinearLayout.getRight();
                Log.d(TAG, "initDate: w = " + w + ", " + mLinearLayout.getWidth());
                onItemClick(mCurPos);
            }
        });
    }

    private void onItemClick(int index){
        TextView curTv = (TextView)mLinearLayout.getChildAt(index);
        //getLeft 和onLayout之后的getWidth的关系？都是在布局之后，可以得到其值的吗？
        Log.d(TAG, "onClick: index = " + index + ", " + curTv.getText()
                        + ", 范围[" + curTv.getLeft() + "," + curTv.getRight() + "]"
//                + ", all_l = " + mTitleScrollView.getLeft() + ", all_r = " + mTitleScrollView.getRight()
//                + ", l_l = " + mLinearLayout.getLeft() + ", l_r = " + mLinearLayout.getRight()
//                + ", l_w = " + mLinearLayout.getWidth() + ", margin_l/r = " + Utils.dp2px(mContext,30)
        );

        //textAbsCx : text的中轴线到scrollView的绝对距离
        int textAbsCx = (curTv.getRight()+curTv.getLeft())/2 + mTitleScrollView.getPaddingLeft();
        Log.d(TAG, "onItemClick: textAbsCx = " + textAbsCx + ", scrollOverX = " + scrollOverX);

        //当前text的中心轴线距离屏幕左边界的距离，也就是text中轴线的值
        int textCx2ScrLDis = textAbsCx - scrollOverX;
        Log.d(TAG, "textCx2ScrLDis = " + textCx2ScrLDis + ", scrCx = " + scrCx);
//        if (textCx2ScrLDis > scrCx){//text中轴线在屏幕中轴线右侧
//            //text是否可以向左移动到屏幕中轴线处：都可以
//            mTitleScrollView.smoothScrollBy(textCx2ScrLDis-scrCx, 0);
//        } else {//左侧
//            //text是否可以向右移动到屏幕中轴线处
//            if (scrollOverX < scrCx) {
//                if (textAbsCx > scrCx) {//可以
//                    mTitleScrollView.smoothScrollBy(textCx2ScrLDis-scrCx, 0);
//                } else {//回到最开始
//                    mTitleScrollView.smoothScrollTo(0, 0);
//                }
//            } else {//可以
//                mTitleScrollView.smoothScrollBy(textCx2ScrLDis-scrCx, 0);
//            }
//        }
        if (textCx2ScrLDis < scrCx && scrollOverX < scrCx && textAbsCx < scrCx) {
            mTitleScrollView.smoothScrollTo(0, 0);
        } else {
            mTitleScrollView.smoothScrollBy(textCx2ScrLDis-scrCx, 0);
        }

        for (int i = 0; i < mLinearLayout.getChildCount(); i++){
            TextView temTv = (TextView) mLinearLayout.getChildAt(i);
            temTv.setTextColor(i == index ? Color.WHITE : getResources().getColor(R.color.little_dark));
            temTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, i == index ? 20 : 17);
        }
    }

    private void showContent() {
        mAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
//                Log.d(TAG, "getItem: i = " + i + "," + mDataList.get(i).getId());
                return FragmentProjectArticleList.newInstance(mCidList.get(i), mTitleList.get(i));
            }

            @Override
            public int getCount() {
//                Log.d(TAG, "getCount: size = " + mDataList.size());
                return mTitleList.size();
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
//                Log.d(TAG, "instantiateItem: position = " + position);
                FragmentProjectArticleList fr = (FragmentProjectArticleList) super.instantiateItem(container, position);
                mFrList.put(position, fr);
                return fr;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//                Log.d(TAG, "destroyItem: pos = " + position);
                mFrList.delete(position);
                super.destroyItem(container, position, object);
            }
        };
        //默认缓存就近的左右两页，若setOffscreenPageLimit的参数num大于1，则左右各缓存num页
        mVp.setOffscreenPageLimit(4);
        mVp.setAdapter(mAdapter);
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                onItemClick(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mVp.setCurrentItem(mCurPos);
    }

    public void onBack(View v){
        finish();
    }
}
