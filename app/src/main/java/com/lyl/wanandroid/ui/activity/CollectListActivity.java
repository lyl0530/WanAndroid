package com.lyl.wanandroid.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.base.BaseActivity;
import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.listener.OnCollectListListener;
import com.lyl.wanandroid.service.entity.Article1Bean;
import com.lyl.wanandroid.service.entity.CollectListResult;
import com.lyl.wanandroid.service.present.CollectListPresenter;
import com.lyl.wanandroid.service.view.CollectListView;
import com.lyl.wanandroid.ui.adapter.CollectListAdapter;
import com.lyl.wanandroid.ui.view.SpacesItemDecoration;
import com.lyl.wanandroid.utils.ErrorUtil;
import com.lyl.wanandroid.utils.LogUtil;
import com.lyl.wanandroid.utils.PhoneUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

public class CollectListActivity extends BaseActivity implements CollectListView {
    private static final String TAG = "lym321121";

    private Context mContext;
    private CollectListPresenter mPresenter;
    private int mCurPageId = 0;
    private int mAllPage = 0;

    private boolean loadMore = false;

    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRv;
    private CollectListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_list);
        mContext = this;
        initView();
        initData();
    }

    private void initView() {
        mRefreshLayout = findViewById(R.id.srl);
        mRv = findViewById(R.id.rv);
    }

    private void initData() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mCurPageId = 0;
                mPresenter.collectList(mCurPageId);
            }
        }).setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtil.d(TAG, "onLoadMore mCurPageId = " + mCurPageId + ", mAllPage = " + mAllPage);
                if (null != mPresenter && mCurPageId < mAllPage){
                    loadMore = true;
                    mPresenter.collectList(mCurPageId);
                } else {
                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                }

            }
        }).setRefreshHeader(new ClassicsHeader(mContext).setEnableLastTime(true))
        .setRefreshFooter(new ClassicsFooter(mContext));

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mRv.addItemDecoration(new SpacesItemDecoration(20));

        mPresenter = new CollectListPresenter();
        mPresenter.attach(this);
        mPresenter.collectList(mCurPageId);
    }

    private List<Article1Bean> mDataList;
    @Override
    public void getCollectListSuccess(CollectListResult res) {
        if (null != res && null != res.getData() && null != res.getData().getDatas()){
            mCurPageId = res.getData().getCurPage();
            mAllPage = res.getData().getPageCount();
            LogUtil.d(TAG, "Success mCurPageId = " + mCurPageId + ", mAllPage = " + mAllPage);
            List<Article1Bean> tempDataList = res.getData().getDatas();
            LogUtil.d(TAG, "Success mCurPageId tempDataList = " + tempDataList);
            if (loadMore){
                loadMore = false;
                mDataList.addAll(tempDataList);

                //mAdapter.notifyDataSetChanged();
                int beginIndex = mDataList.size()- tempDataList.size();
                int endIndex = mDataList.size();
                LogUtil.d(TAG, "beginIndex = " + beginIndex + ", endIndex = " + endIndex);
                for (int i = beginIndex; i < endIndex; i++) {
                    mAdapter.notifyItemChanged(i);
                }

                if (mCurPageId < mAllPage){
                    mRefreshLayout.finishLoadMore();
                } else {
                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                }
            } else {
                mDataList = tempDataList;
                mAdapter = new CollectListAdapter(mContext, mDataList);
                mAdapter.setOnCollectListListener(new OnCollectListListener() {
                    @Override
                    public void onItemClick(Article1Bean bean) {
                        PhoneUtil.openInWebView(mContext, bean);
                    }

                    @Override
                    public void onItemUnCollect(int articleId, int originId, int position) {
                        LogUtil.d(TAG, "articleId = " + articleId + ", originId = " + originId);
                        if (null != mPresenter){
                            mPresenter.unCollectArticle(articleId, originId, position);
                        }
                    }
                });
                mRv.setAdapter(mAdapter);
                mRefreshLayout.finishRefresh();
            }
        }
    }

    @Override
    public void unCollectSuccess(BaseResult res, int pos) {
        mDataList.remove(pos);
        //mAdapter.notifyDataSetChanged();
        mAdapter.notifyItemChanged(pos);
    }

    @Override
    public void Failed(int code, String msg) {
        String str = ErrorUtil.getErrorInfo(mContext, code, msg);
        showToast(str);

        if (loadMore){
            loadMore = false;
            mRefreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            mRefreshLayout.finishRefresh();
        }
    }

    public void onBack(View v){
        finish();
    }

    @Override
    protected void onDestroy() {
        if (null != mPresenter){
            mPresenter.detach();
        }
        super.onDestroy();
    }
}