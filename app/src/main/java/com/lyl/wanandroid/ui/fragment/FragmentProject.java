package com.lyl.wanandroid.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.base.BaseFragment;
import com.lyl.wanandroid.service.entity.ProjectResult;
import com.lyl.wanandroid.listener.ScrollViewListener;
import com.lyl.wanandroid.service.present.ProjectPresenter;
import com.lyl.wanandroid.service.view.ProjectView;
import com.lyl.wanandroid.utils.PhoneUtil;
import com.lyl.wanandroid.ui.view.ProjectTitleScrollView;

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;

/**
 * Created by lym on 2020/4/9
 * Describe :
 */
public class FragmentProject extends BaseFragment implements ProjectView {
    private static final String TAG = "FragmentProject";
    private Context mContext;
    private View mRootView;
    private FragmentActivity mActivity;

    private ProjectPresenter mPresenter;
    private ArrayList<ProjectResult.DataBean> mDataList = new ArrayList<>();

    private ProjectTitleScrollView mScrollView;
    private LinearLayout mLineLayout;
    private ViewPager mVp;
    //FragmentStatePagerAdapter会回收fragment，会执行onDestroyView、onDestroy方法，可很好的控制内存
    private FragmentStatePagerAdapter mAdapter;
    private SparseArray<FragmentProjectArticleList> mFrList = new SparseArray<>();

    //scrCx : 屏幕的中轴线
    //scrollOverX : 滑动后，偏移的x坐标
    private int scrCx, scrollOverX;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mRootView = inflater.inflate(R.layout.fragment_project, container, false);
//        return mRootView;
        //返回的view就是onViewCreated参数中的view
        return inflater.inflate(R.layout.fragment_project, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRootView = view;
        mContext = getContext();
        mActivity = getActivity();

        initView();
        initDate();
    }

    private void initView() {
        mLineLayout = mRootView.findViewById(R.id.ll_text);
        mScrollView = mRootView.findViewById(R.id.scrollView);
        mVp = mRootView.findViewById(R.id.project_vp);

        mScrollView.setListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(HorizontalScrollView scrollView, int l, int t, int oldl, int oldt) {
//                Log.d(TAG, "onScrollChanged: oldl =" + oldl + ", l = " + l);
                scrollOverX = l;
            }
        });
    }

    private void initDate() {
        mPresenter = new ProjectPresenter();
        mPresenter.attach(this);
        mPresenter.getProject();
    }

    @Override
    public void Success(ProjectResult res) {
        if(null != res.getData()){
            mDataList.clear();
            ArrayList<String> textList = new ArrayList<>();
            for (ProjectResult.DataBean bean : res.getData()){
                if (null == bean) continue;
                mDataList.add(bean);
                textList.add(StringEscapeUtils.unescapeHtml4(bean.getName()));
            }
            showHScrollTextView(textList);
            showContent();
        }
    }

    private void showContent() {
        mAdapter = new FragmentStatePagerAdapter(mActivity.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
//                Log.d(TAG, "getItem: i = " + i + "," + mDataList.get(i).getId());
                return FragmentProjectArticleList.newInstance(mDataList.get(i).getId(), mDataList.get(i).getName());
            }

            @Override
            public int getCount() {
//                Log.d(TAG, "getCount: size = " + mDataList.size());
                return mDataList.size();
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
    }

    @Override
    public void Failed(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    private void showHScrollTextView(ArrayList<String> list){
        if (null == list || 0 == list.size()) return;
        Log.d(TAG, "showHScrollTextView: list = " + list);

        scrCx = PhoneUtil.getScreenW(mContext)/2;
//        Log.d(TAG, "initDate: scrCx = " + scrCx);
//
//        ArrayList<String> list = new ArrayList<>();
//        list = null == list ? new ArrayList<String>() : list;
//
//        list.add("项目1");
//        list.add("项目2");
//        list.add("项目3");
//        list.add("项目1333");
//        list.add("项目14444");
//        list.add("项目15556");
//        list.add("项目15555555577777");
//        list.add("项目1555555558888");
//        list.add("项目");
//        list.add("项目1");

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(PhoneUtil.dp2px(mContext,15),0, PhoneUtil.dp2px(mContext,15),0);//4个参数按顺序分别是左上右下

        for (int i = 0; i < list.size(); i++){
            TextView tv = new TextView(mContext);
            tv.setText(list.get(i));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            tv.setTextColor(i == 0 ? Color.WHITE : getResources().getColor(R.color.little_dark));
            tv.setLayoutParams(layoutParams);
            tv.setTag(i);
            mLineLayout.addView(tv);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (int) view.getTag();
                    mVp.setCurrentItem(index);
                    onItemClick(index);
                }
            });
        }

        //只写mLineLayout.getRight()，获取的值为0，此时mLineLayout还没绘制完成，得不到值
        //https://www.cnblogs.com/ouyangping/p/7398753.html
//        mLineLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                LINEAR_LAYOUT_W = mLineLayout.getRight();
////                Log.d(TAG, "initDate: LINEAR_LAYOUT_W = " + LINEAR_LAYOUT_W + ", " + mLineLayout.getWidth());
//            }
//        });
    }

    private void onItemClick(int index){
        TextView curTv = (TextView)mLineLayout.getChildAt(index);
        //getLeft 和onLayout之后的getWidth的关系？都是在布局之后，可以得到其值的吗？
        Log.d(TAG, "onClick: index = " + index + ", " + curTv.getText()
                + ", 范围[" + curTv.getLeft() + "," + curTv.getRight() + "]"
//                + ", all_l = " + mScrollView.getLeft() + ", all_r = " + mScrollView.getRight()
//                + ", l_l = " + mLineLayout.getLeft() + ", l_r = " + mLineLayout.getRight()
//                + ", l_w = " + mLineLayout.getWidth() + ", margin_l/r = " + Utils.dp2px(mContext,30)
        );

        //textAbsCx : text的中轴线到scrollView的绝对距离
        int textAbsCx = (curTv.getRight()+curTv.getLeft())/2;
        Log.d(TAG, "onItemClick: textAbsCx = " + textAbsCx + ", scrollOverX = " + scrollOverX);

        //当前text的中心轴线距离屏幕左边界的距离，也就是text中轴线的值
        int textCx2ScrLDis = textAbsCx - scrollOverX;
        Log.d(TAG, "textCx2ScrLDis = " + textCx2ScrLDis + ", scrCx = " + scrCx);
//        if (textCx2ScrLDis > scrCx){//text中轴线在屏幕中轴线右侧
//            //text是否可以向左移动到屏幕中轴线处：都可以
//            mScrollView.smoothScrollBy(textCx2ScrLDis-scrCx, 0);
//        } else {//左侧
//            //text是否可以向右移动到屏幕中轴线处
//            if (scrollOverX < scrCx) {
//                if (textAbsCx > scrCx) {//可以
//                    mScrollView.smoothScrollBy(textCx2ScrLDis-scrCx, 0);
//                } else {//回到最开始
//                    mScrollView.smoothScrollTo(0, 0);
//                }
//            } else {//可以
//                mScrollView.smoothScrollBy(textCx2ScrLDis-scrCx, 0);
//            }
//        }
        if (textCx2ScrLDis < scrCx && scrollOverX < scrCx && textAbsCx < scrCx) {
            mScrollView.smoothScrollTo(0, 0);
        } else {
            mScrollView.smoothScrollBy(textCx2ScrLDis-scrCx, 0);
        }

        for (int i = 0; i < mLineLayout.getChildCount(); i++){
            TextView temTv = (TextView) mLineLayout.getChildAt(i);
            temTv.setTextColor(i == index ? Color.WHITE : getResources().getColor(R.color.little_dark));
        }


    }
}
