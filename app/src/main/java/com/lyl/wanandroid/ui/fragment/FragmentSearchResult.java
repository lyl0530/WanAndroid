package com.lyl.wanandroid.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.app.BaseApplication;
import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.listener.OnArticleItemListener;
import com.lyl.wanandroid.listener.OnItemCollectListener;
import com.lyl.wanandroid.service.entity.ArticleBean;
import com.lyl.wanandroid.service.present.CollectPresenter;
import com.lyl.wanandroid.service.view.CollectView;
import com.lyl.wanandroid.ui.activity.LoginActivity;
import com.lyl.wanandroid.ui.adapter.ArticleListAdapter;
import com.lyl.wanandroid.base.BaseFragment;
import com.lyl.wanandroid.service.entity.ProjectArticleListResult;
import com.lyl.wanandroid.service.present.SearchPresenter;
import com.lyl.wanandroid.service.view.SearchView;
import com.lyl.wanandroid.utils.ConstUtil;
import com.lyl.wanandroid.utils.ErrorUtil;
import com.lyl.wanandroid.utils.PhoneUtil;
import com.lyl.wanandroid.ui.view.SpacesItemDecoration;
import com.lyl.wanandroid.utils.PreferenceUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lym on 2020/4/9
 * Describe :
 */
public class FragmentSearchResult extends BaseFragment implements SearchView, CollectView {
    private static final String TAG = "search lyl123";

    private View mRootView;
    private RecyclerView mRv;
    private SearchPresenter mPresenter;
    private CollectPresenter mCollectPresenter;
    private ArticleListAdapter mAdapter;
    private List<ArticleBean> mDataList;

    private int mCurPageIndex = 0;
    private int pageCnt = 0;//总页数
    private String mKey;//搜索关键字

    private Context mContext;
    private SmartRefreshLayout mRefreshLayout;
    private boolean loadMore = false;//加载更多
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
        mCollectPresenter = new CollectPresenter();
        mCollectPresenter.attach(this);

        mRefreshLayout = mRootView.findViewById(R.id.srl);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (null != mPresenter){
                    mCurPageIndex = 0;
                    mPresenter.search(mCurPageIndex, mKey);
                }
            }
        }).setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Log.d(TAG, "onLoadMore: mCurPageIndex = " + mCurPageIndex +
                        ", pageCnt = " + pageCnt);
                if (null != mPresenter && mCurPageIndex < pageCnt) {
                    loadMore = true;
                    mPresenter.search(mCurPageIndex, mKey);
                } else {
                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                }
            }
        }).setRefreshHeader(new ClassicsHeader(mContext).setEnableLastTime(true))
        .setRefreshFooter(new ClassicsFooter(mContext));

        mRv = mRootView.findViewById(R.id.rv_search_result);
        LinearLayoutManager lm = new LinearLayoutManager(mContext);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(lm);
        mRv.addItemDecoration(new SpacesItemDecoration(20));
    }

    public void search(String key){
        if (isAdded() && null != mPresenter) {
            mKey = key;
            mPresenter.search(mCurPageIndex, mKey);

            //https://blog.csdn.net/crazyman2010/article/details/51187817
            //https://blog.csdn.net/x635981012/article/details/50373173
            //存储搜索历史
            Set<String> historySet = new HashSet<>(PreferenceUtil.instance().getSearchHistory());
                //PreferenceUtil.instance().getSearchHistory()
            if (!historySet.contains(key)){
                historySet.add(key);
                Log.d(TAG, "will write in :" + historySet.toString());
                PreferenceUtil.instance().setSearchHistory(historySet);
            }
        }
    }

    @Override
    public void Failed(int code, String msg) {
        String str = ErrorUtil.getErrorInfo(mContext, code, msg);
        showToast(str);
        if (loadMore){
            loadMore = false;
            mRefreshLayout.finishLoadMore();
        } else  {
            mRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void Success(ProjectArticleListResult res) {
//        Log.e(TAG, "Success: res = " + res);
        if (null != res && null != res.getData() && null != res.getData().getDatas()){
            mCurPageIndex = res.getData().getCurPage();
            pageCnt = res.getData().getPageCount();

            List<ArticleBean> dataList = res.getData().getDatas();
            Log.d(TAG, "dataList = " + dataList + ", mCurPageIndex = " + mCurPageIndex  +
                    ", pageCnt = " + pageCnt);

            if (null == dataList){
                //没有符合的内容
                return;
            }
            if (loadMore){
                loadMore = false;
                mDataList.addAll(dataList);
                mAdapter.notifyDataSetChanged();
                if (mCurPageIndex < pageCnt){
                    mRefreshLayout.finishLoadMore();
                } else {
                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                }
            } else {
                mDataList = dataList;
                mAdapter = new ArticleListAdapter(mContext, mDataList);
                mAdapter.setOnArticleItemListener(new OnArticleItemListener() {
                    @Override
                    public void onItemClick(ArticleBean bean) {
                        PhoneUtil.openInWebView(mContext, bean);
                    }
                });
                mAdapter.setOnItemCollectListener(new OnItemCollectListener(){
                    @Override
                    public void onItemCollect(int articleId, int position, boolean isCollect) {
                        if (null == mCollectPresenter) return;
                        if (isCollect){//取消收藏
                            mCollectPresenter.unCollectArticle(articleId, position);
                        } else { //收藏
                            mCollectPresenter.collectArticle(articleId, position);
                        }
                    }
                });
                mRv.setAdapter(mAdapter);
                mRefreshLayout.finishRefresh();
            }
        }
    }

    private int articleId;//用于收藏和取消收藏
    private void collectArticleAfterLogin(){
        if (articleId > 0 ){
            //从list中获取当前是否为true，不为true的时候再调用collect方法
            for(int i = 0; i < mDataList.size(); i++){//从第0个开始
                ArticleBean d = mDataList.get(i);
                if(null !=  d && articleId == d.getId()){
                    Log.d(TAG, "collectArticleAfterLogin: i = " + i +", collect = " + d.isCollect());
                    if(!d.isCollect()) {
//                        showToast("将要收藏文章！");
                        mCollectPresenter.collectArticle(articleId, i);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void collectArticleSuccess(BaseResult res, int position) {
        Log.d(TAG, "collectArticleSuccess: " + res);
        ArticleBean bean = mDataList.get(position);
        bean.setCollect(true);
        mDataList.set(position, bean);
        mAdapter.notifyDataSetChanged();
        showToast(R.string.collect_success);
    }

    @Override
    public void collectArticleFailed(int code, String msg) {
        if (!TextUtils.isEmpty(msg) && msg.startsWith(ConstUtil.LOGIN_MSG)){
            try {
                int articleId = Integer.parseInt(msg.substring(ConstUtil.LOGIN_MSG.length()));
                Log.d(TAG, "collectArticleFailed: 20200729 = " + articleId);
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.putExtra(ConstUtil.ARTICLE_ID, articleId);
                startActivityForResult(intent, ConstUtil.REQUEST_CODE_LOGIN);
            } catch (NumberFormatException e){
                Log.e(TAG, "collectArticleFailed: articleId获取失败");
                e.printStackTrace();
            }
        } else {
            String str = ErrorUtil.getErrorInfo(mContext, code, msg);
            showToast(str);
        }
    }

    @Override
    public void unCollectArticleSuccess(BaseResult res, int position) {
        Log.d(TAG, "unCollectArticleSuccess: " + res);
        ArticleBean bean = mDataList.get(position);
        bean.setCollect(false);
        mDataList.set(position, bean);
        mAdapter.notifyDataSetChanged();
        showToast(R.string.un_collect_success);
    }

    @Override
    public void unCollectArticleFailed(int code, String msg) {
        String str = ErrorUtil.getErrorInfo(mContext, code, msg);
        showToast(str);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: 20200729 = " + requestCode + ", " + resultCode);
        if (ConstUtil.REQUEST_CODE_LOGIN == requestCode && ConstUtil.RESULT_CODE_LOGIN == resultCode){
            articleId = data.getIntExtra(ConstUtil.ARTICLE_ID, -1);
            Log.d(TAG, "onActivityResult: articleId = " + articleId);
            //退出登录也会刷新首页
            if (BaseApplication.isLogin()) {
                collectArticleAfterLogin();
            }
        }
    }


    @Override
    public void onDestroy() {
        if (null != mPresenter){
            mPresenter.detach();
            mPresenter = null;
        }
        if (null != mCollectPresenter){
            mCollectPresenter.detach();
            mCollectPresenter = null;
        }
        super.onDestroy();
    }
}
