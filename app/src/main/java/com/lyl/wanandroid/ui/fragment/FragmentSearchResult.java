package com.lyl.wanandroid.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.adapter.ProjectArticleListAdapter;
import com.lyl.wanandroid.base.BaseFragment;
import com.lyl.wanandroid.bean.ProjectArticleListResult;
import com.lyl.wanandroid.mvp.present.SearchPresenter;
import com.lyl.wanandroid.mvp.view.SearchView;
import com.lyl.wanandroid.util.Utils;
import com.lyl.wanandroid.view.SpacesItemDecoration;

import java.util.List;

/**
 * Created by lym on 2020/4/9
 * Describe :
 */
public class FragmentSearchResult extends BaseFragment implements SearchView {
    private static final String TAG = "search lyl123";

    private View mRootView;
    private RecyclerView mRv;
    private SearchPresenter mPresenter;
    private int mCurPageIndex = 0;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_search_result, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        initView();
    }

    private void initView() {
        mPresenter = new SearchPresenter();
        mPresenter.attach(this);

        mRv = mRootView.findViewById(R.id.rv_search_result);
        LinearLayoutManager lm = new LinearLayoutManager(mContext);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(lm);
        mRv.addItemDecoration(new SpacesItemDecoration(20));
    }

    public void search(String key){
        if (isAdded() && null != mPresenter) {
            mPresenter.search(mCurPageIndex, key);
        }
    }

    @Override
    public void Failed(String msg) {

    }

    private ProjectArticleListAdapter mAdapter;

    @Override
    public void Success(ProjectArticleListResult res) {
//        Log.e(TAG, "Success: res = " + res);
        if (null != res && null != res.getData() && null != res.getData().getDatas()){
            List<ProjectArticleListResult.DataBean.DatasBean> dataList = res.getData().getDatas();
            Log.d(TAG, "dataList = " + dataList);
            if (null == dataList){
                //没有符合的内容
                return;
            }
            mAdapter = new ProjectArticleListAdapter(mContext, dataList);
            mAdapter.setOnItemClickListener(new ProjectArticleListAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(View view, int position) {
                    ProjectArticleListResult.DataBean.DatasBean bean = dataList.get(position);
                    if (null != bean){
                        Utils.openInWebView(mContext, bean.getLink());
                    }
                }
            });
            mRv.setAdapter(mAdapter);
        }
    }
}
