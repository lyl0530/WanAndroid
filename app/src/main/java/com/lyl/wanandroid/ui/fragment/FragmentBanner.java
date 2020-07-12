package com.lyl.wanandroid.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.lyl.wanandroid.bean.BannerResult;

/**
 * Created by lym on 2020/6/5
 * Describe :
 */
public class FragmentBanner extends Fragment {
    private static final String TAG = "BannerFragment";

    private static final String BUNDLE_BANNER_INDEX = "bundle_banner_index";
    private static final String BUNDLE_BANNER_DATA = "bundle_banner_data";

    private View mRootView;
    private ImageView mIvBg;
    private TextView mTvTitle, mTvNum;
    private String mIndex;
    private BannerResult.DataBean mData;

    public static FragmentBanner newInstance(String index, BannerResult.DataBean data) {
        FragmentBanner fragment = new FragmentBanner();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_BANNER_INDEX, index);
        bundle.putSerializable(BUNDLE_BANNER_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    //在onCreate中获取Arguments
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (null == arguments) return;
        mIndex = arguments.getString(BUNDLE_BANNER_INDEX, "");
        mData = (BannerResult.DataBean) arguments.getSerializable(BUNDLE_BANNER_DATA);
        Log.d(TAG, "initData: [" + mIndex + "]: " + mData);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //参数3 false:表示布局为独立的
        mRootView = inflater.inflate(R.layout.fragment_banner, container, false);
        return mRootView;
    }

    //onViewCreated-布局加载完后，会调用该方法，在其内获取控件，进行赋值
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initView() {
        mIvBg = mRootView.findViewById(R.id.iv_banner);
        mTvTitle = mRootView.findViewById(R.id.tv_banner_title);
        mTvNum = mRootView.findViewById(R.id.tv_banner_number);
    }

    private void initData() {
        if (null == mData) return;
        Log.d(TAG, "initData: [" + mIndex + "]: " + mData);

        RequestOptions options = new RequestOptions()
                //.circleCrop()//如果需要裁剪图片，比如圆形，椭圆形等等
                .placeholder(R.drawable.banner_empty)
                .error(R.drawable.banner_empty)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(this).load(mData.getImagePath())
                .apply(options)
                .into(mIvBg);

        mTvTitle.setText(mData.getTitle());
        mTvNum.setText(mIndex);
    }
}