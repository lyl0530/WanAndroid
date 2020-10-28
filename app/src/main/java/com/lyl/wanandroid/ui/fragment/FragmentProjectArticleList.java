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
import com.lyl.wanandroid.base.BaseFragment;
import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.listener.OnArticleListListener;
import com.lyl.wanandroid.service.entity.ArticleBean;
import com.lyl.wanandroid.service.entity.ProjectArticleListResult;
import com.lyl.wanandroid.service.present.CollectPresenter;
import com.lyl.wanandroid.service.present.ProjectArticleListPresenter;
import com.lyl.wanandroid.service.view.CollectView;
import com.lyl.wanandroid.service.view.ProjectArticleListView;
import com.lyl.wanandroid.ui.activity.LoginActivity;
import com.lyl.wanandroid.ui.adapter.ArticleListAdapter;
import com.lyl.wanandroid.ui.view.SpacesItemDecoration;
import com.lyl.wanandroid.utils.ConstUtil;
import com.lyl.wanandroid.utils.ErrorUtil;
import com.lyl.wanandroid.utils.PhoneUtil;
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
public class FragmentProjectArticleList extends BaseFragment implements ProjectArticleListView, CollectView {
    private static final String TAG = "fpal lyl12345";
    private Context mContext;
    private View mRootView;

    private int cId;
    private String name;

    private int curPageId = 0;//当前页id
    private int allPage = 0;//总页数
    private boolean loadMore = false;//是否可以加载更多

    private ProjectArticleListPresenter mPresenter;
    private CollectPresenter mCollectPresenter;
    private RecyclerView mRv;
    private ArticleListAdapter mAdapter;
    private List<ArticleBean> dataList = new ArrayList<>();

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

        mCollectPresenter = new CollectPresenter();
        mCollectPresenter.attach(this);
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

            List<ArticleBean> tempList =
                res.getData().getDatas();
            if (loadMore){
                loadMore = false;
                dataList.addAll(tempList);
                mAdapter.notifyDataSetChanged();
            } else {
                dataList = tempList;
                mAdapter = new ArticleListAdapter(mContext, dataList);
                mAdapter.setOnArticleListListener(new OnArticleListListener() {
                    @Override
                    public void onItemClick(ArticleBean bean) {
                        PhoneUtil.openInWebView(mContext, bean);
                    }

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
            for(int i = 0; i < dataList.size(); i++){//从第0个开始
                ArticleBean d = dataList.get(i);
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
        ArticleBean bean = dataList.get(position);
        bean.setCollect(true);
        dataList.set(position, bean);
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
        ArticleBean bean = dataList.get(position);
        bean.setCollect(false);
        dataList.set(position, bean);
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
