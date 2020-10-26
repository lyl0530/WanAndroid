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
import android.widget.Toast;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.ui.adapter.ProjectArticleListAdapter;
import com.lyl.wanandroid.base.BaseFragment;
import com.lyl.wanandroid.service.entity.ProjectArticleListResult;
import com.lyl.wanandroid.service.present.ProjectArticleListPresenter;
import com.lyl.wanandroid.service.view.ProjectArticleListView;
import com.lyl.wanandroid.utils.PhoneUtil;
import com.lyl.wanandroid.ui.view.SpacesItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lym on 2020/4/9
 * Describe :
 */
public class FragmentProjectArticleList extends BaseFragment implements ProjectArticleListView {
    private static final String TAG = "fpal lyl12345";
    private Context mContext;
    private View mRootView;

    private int cId;
    private String name;

    private int curPageId = 0;//当前页id
    private int allPage = 0;//总页数
    private boolean loadMore = false;//是否可以加载更多

    private ProjectArticleListPresenter mPresenter;
    private RecyclerView mRv;
    private ProjectArticleListAdapter mAdapter;
    private List<ProjectArticleListResult.DataBean.DatasBean> dataList = new ArrayList<>();

    private SmartRefreshLayout mRefreshLayout;

    public static FragmentProjectArticleList newInstance(int cid, String name){
        FragmentProjectArticleList fr = new FragmentProjectArticleList();
        Bundle bundle = new Bundle();
//        Log.d(TAG, "newInstance: set argument : " + cid + ", " + name);
        bundle.putInt("PROJECT_ARTICLE_CID", cid);
        bundle.putString("PROJECT_ARTICLE_TITLE", name);
        fr.setArguments(bundle);
        return fr;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //从argument中获取cId
        Bundle arguments = getArguments();
        if (null != arguments){
            cId = arguments.getInt("PROJECT_ARTICLE_CID");
            name = arguments.getString("PROJECT_ARTICLE_TITLE");
            Log.d(TAG, "onCreate: get argument: " + cId + ", " + name);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_project_article_list, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        initView();
        initDate();
    }

    private void initView() {
        mRefreshLayout = mRootView.findViewById(R.id.article_list_refresh);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                curPageId = 0;
                if (null != mPresenter) {
                    Log.d(TAG, "onRefresh: curPageId = " + curPageId + ", cId =" + cId);
                    mPresenter.getProjectArticleList(curPageId, cId);
                }
            }
        }).setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Log.d(TAG, "onLoadMore: curPageId = " + curPageId + ", allPage = " + allPage);
                if (null != mPresenter && curPageId < allPage) {
                    loadMore = true;
                    Log.d(TAG, "onLoadMore: curPageId = " + curPageId + ", cId =" + cId);
                    mPresenter.getProjectArticleList(curPageId, cId);
                } else {
                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                }
            }
        }).setRefreshHeader(new ClassicsHeader(mContext).setEnableLastTime(true))
          .setRefreshFooter(new ClassicsFooter(mContext));

        mRv = mRootView.findViewById(R.id.project_article_rv);
        LinearLayoutManager lm = new LinearLayoutManager(mContext);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(lm);
        mRv.addItemDecoration(new SpacesItemDecoration(20));
    }

    private void initDate() {
        //https://www.wanandroid.com/project/list/1/json?cid=294
        mPresenter = new ProjectArticleListPresenter();
        mPresenter.attach(this);
        Log.d(TAG, "initDate: curPageId = " + curPageId + ", cId =" + cId);
        mPresenter.getProjectArticleList(curPageId, cId);
    }

    @Override
    public void Failed(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        if (loadMore){
            loadMore = false;
            mRefreshLayout.finishLoadMore();
        } else  {
            mRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void Success(ProjectArticleListResult res) {
        if (null != res && null != res.getData() && null != res.getData().getDatas()){
            curPageId = res.getData().getCurPage();
            allPage = res.getData().getPageCount();
            Log.d(TAG, "Success: curPageId = " + curPageId + ", allPage = " + allPage);
            Log.d(TAG, "Success: res.getData() = " + res.getData());
            if (curPageId < allPage){
                mRefreshLayout.finishLoadMore();
            } else {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }

            List<ProjectArticleListResult.DataBean.DatasBean> tempList =
                res.getData().getDatas();
            if (loadMore){
                loadMore = false;
                dataList.addAll(tempList);
                mAdapter.notifyDataSetChanged();
            } else {
                dataList = tempList;
                mAdapter = new ProjectArticleListAdapter(mContext, dataList);
                mAdapter.setOnItemClickListener(new ProjectArticleListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClicked(View view, int position) {
                        ProjectArticleListResult.DataBean.DatasBean bean = dataList.get(position);
                        if (null != bean){
                            PhoneUtil.openInWebView(mContext, bean.getLink());
                        }
                    }
                });
                mRv.setAdapter(mAdapter);

                mRefreshLayout.finishRefresh();
            }
        }
    }
}
