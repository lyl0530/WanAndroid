package com.lyl.wanandroid.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.app.BaseApplication;
import com.lyl.wanandroid.base.BaseFragment;
import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.listener.OnArticleItemListener;
import com.lyl.wanandroid.listener.OnItemCollectListener;
import com.lyl.wanandroid.service.entity.ArticleBean;
import com.lyl.wanandroid.service.entity.BannerResult;
import com.lyl.wanandroid.service.entity.HomeBean;
import com.lyl.wanandroid.service.entity.MainArticleResult;
import com.lyl.wanandroid.service.entity.TopArticleResult;
import com.lyl.wanandroid.service.present.CollectPresenter;
import com.lyl.wanandroid.service.present.MainPresenter;
import com.lyl.wanandroid.service.view.CollectView;
import com.lyl.wanandroid.service.view.MainView;
import com.lyl.wanandroid.ui.activity.LoginActivity;
import com.lyl.wanandroid.ui.activity.MainActivity;
import com.lyl.wanandroid.ui.activity.SearchActivity;
import com.lyl.wanandroid.ui.adapter.HomeAdapter;
import com.lyl.wanandroid.ui.view.CircleView;
import com.lyl.wanandroid.ui.view.SpacesItemDecoration;
import com.lyl.wanandroid.utils.ConstUtil;
import com.lyl.wanandroid.utils.LogUtil;
import com.lyl.wanandroid.utils.PhoneUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by lym on 2020/4/9
 * Describe : 主页
 *
 */
public class FragmentHome extends BaseFragment implements MainView, CollectView, View.OnClickListener {
    private static final String TAG = FragmentHome.class.getSimpleName();

    private View mView;
    private Context mContext;

    private AppBarLayout mTitleBar;
    private /*ImageView*/ CircleView mAvatar;

//    private SwipeRefreshLayout mRefreshLayout;
    private SmartRefreshLayout mRefreshLayout;

    private MainPresenter mPresenter;
    private CollectPresenter mCollectPresenter;
    private RecyclerView mRv;
    private HomeAdapter mHomeAdapter;
    private List<HomeBean> mResList = new ArrayList<>();
    private int articleId;//用于收藏和取消收藏
    private List<ArticleBean> dataList = new ArrayList<>();
    private int topArticleCnt = 0;
    private int curPage = 0;
    private int allPage = 0;
    private boolean loadMore = false;//加载更多：调用getMainArticle接口

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getContext();

        initView();
        EventBus.getDefault().register(this);//这句话要放在初始化组件(findViewById)之后，不然页面接收不到参数

        mPresenter = new MainPresenter();
        mPresenter.attach(this);
        mCollectPresenter = new CollectPresenter();
        mCollectPresenter.attach(this);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMainInfo();
    }

    private void initView() {
        //titleBar
        mTitleBar = mView.findViewById(R.id.title_bar);
        mAvatar = mTitleBar.findViewById(R.id.avatar);
        mAvatar.setOnClickListener(this);

        mTitleBar.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        mRefreshLayout = mView.findViewById(R.id.srl);
//        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getMainInfo();
//            }
//        });
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getMainInfo();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                mRefreshLayout.finishLoadMore();
//                mRefreshLayout.finishLoadMoreWithNoMoreData();  //全部加载完成,没有数据了调用此方法
                if (null != mPresenter && curPage < allPage) {
                    loadMore = true;
                    mPresenter.getMainArticle(curPage);
                } else {
                    mRefreshLayout.finishLoadMoreWithNoMoreData();  //全部加载完成,没有数据了调用此方法
                }
            }
        });
        //设置 Header 为 经典风格
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext).setEnableLastTime(true));
        //设置 Header 为 Material风格
//        mRefreshLayout.setRefreshHeader(new MaterialHeader(mContext).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
//        mRefreshLayout.setRefreshFooter(new BallPulseFooter(mContext).setSpinnerStyle(SpinnerStyle.Scale));
        //设置 Footer 为 经典风格
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));

        mRv = mView.findViewById(R.id.rv);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mRv.addItemDecoration(new SpacesItemDecoration(20));

        mRv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    private void getMainInfo(){
        if (null == mPresenter) return;
        Log.d(TAG, "getMainInfo");
        curPage = 0;
//        showProgressDialog();
        //获取Banner信息，得到Banner图张数
        mPresenter.getBanner();
        //获取置顶文章列表
        mPresenter.getTopArticle();
        mPresenter.getMainArticle(curPage);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "111234 setUserVisibleHint: " + isVisibleToUser);
        if (null != mHomeAdapter){
            mHomeAdapter.restartHandler(isVisibleToUser);//main fragment 不可见时，停止轮播图
        }
    }

    private boolean refreshMain = false;//登录、登出成功后，首页刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setRefreshMain(String str){
        if (ConstUtil.REFRESH_MAIN.equals(str)){
            refreshMain = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: loginRefresh = " + refreshMain);
        if (null != mHomeAdapter){
            mHomeAdapter.restartHandler(true);
        }
        if (refreshMain) {
            getMainInfo();
            refreshMain = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != mHomeAdapter){
            mHomeAdapter.restartHandler(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: 20200729 = " + requestCode + ", " + resultCode);
        if (ConstUtil.REQUEST_CODE_LOGIN == requestCode && ConstUtil.RESULT_CODE_LOGIN == resultCode){
            articleId = data.getIntExtra(ConstUtil.ARTICLE_ID, -1);
            Log.d(TAG, "onActivityResult: articleId = " + articleId);
        }
    }

    @Override
    public void getBannerSuccess(BannerResult res) {
//        LogUtils.d(TAG, "banner Success: " + res);
        if (null == res || null == res.getData()) return;

        mResList.clear();
        mResList.add(new HomeBean(ConstUtil.TYPE_BANNER, res.getData()));

        mHomeAdapter = new HomeAdapter(mContext, mResList, topArticleCnt);
        mHomeAdapter.restartHandler(true);
        mHomeAdapter.setOnItemCollectListener(new OnItemCollectListener() {
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
        mHomeAdapter.setOnArticleItemListener(new OnArticleItemListener() {
            @Override
            public void onItemClick(ArticleBean bean) {
                PhoneUtil.openInWebView(mContext, bean);
            }
        });
        mRv.setAdapter(mHomeAdapter);
    }

    @Override
    public void getBannerFailed(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getTopArticleSuccess(TopArticleResult res) {
//        LogUtils.d(TAG, "top_article Success: " + res);
        if (null == res || null == res.getData()) return;

        dataList = res.getData();
        topArticleCnt = dataList.size();
        for (int i = 0; i < topArticleCnt; i++) {
            mResList.add(new HomeBean(ConstUtil.TYPE_ARTICLE, dataList.get(i)));
        }
        mHomeAdapter.notifyDataSetChanged();
    }

    @Override
    public void getTopArticleFailed(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getMainArticleSuccess(MainArticleResult res) {
//        LogUtils.d(TAG, "main article Success: " + res);
        if (null == res || null == res.getData()) return;

        dataList = res.getData().getDatas();
        for (int i = 0; i < dataList.size(); i++) {
            mResList.add(new HomeBean(ConstUtil.TYPE_ARTICLE, dataList.get(i)));
        }
        mHomeAdapter.notifyDataSetChanged();

        curPage = res.getData().getCurPage();//成功后，得到curPage=1，下次则使用1作为下标，获取第二页的数据
        allPage = res.getData().getPageCount();
        Log.d(TAG, "getMainArticleSuccess: curPage = " + curPage + ", pageCount = " + allPage);
        if (loadMore){
            loadMore = false;
            if (curPage >= allPage){
                mRefreshLayout.finishLoadMoreWithNoMoreData();  //全部加载完成,没有数据了调用此方法
            } else {
                mRefreshLayout.finishLoadMore();
            }
        }
    }

    @Override
    public void getMainArticleFailed(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        if (loadMore){
            loadMore = false;
            mRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void Failed(String msg) {
//        hideProgressDialog();
        mRefreshLayout.finishRefresh();

        LogUtil.e(TAG, msg);
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Finish() {
//        hideProgressDialog();
        //关闭刷新
//        if (mRefreshLayout.isRefreshing()) {
//            mRefreshLayout.setRefreshing(false);
//        }
        mRefreshLayout.finishRefresh();

        Log.d(TAG, "Finish: 20200729 will enter collectArticleAfterLogin");
        //退出登录也会刷新首页
        if (BaseApplication.isLogin()) {
            collectArticleAfterLogin();
        }
    }

    private void collectArticleAfterLogin(){
        if (articleId > 0 ){
            //从list中获取当前是否为true，不为true的时候再调用collect方法
            for(int i = 1; i < mResList.size(); i++){//第0个是banner
                ArticleBean d = (ArticleBean)(mResList.get(i).getContent());
                if(null !=  d && articleId == d.getId()){
                    Log.d(TAG, "collectArticleAfterLogin: i = " + i +", collect = " + d.isCollect());
                    if(!d.isCollect()) {
                        Toast.makeText(mContext, "将要收藏文章！", Toast.LENGTH_SHORT).show();
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
        HomeBean bean = mResList.get(position);
        ((ArticleBean)bean.getContent()).setCollect(true);
        mResList.set(position, bean);
        mHomeAdapter.notifyDataSetChanged();
        Toast.makeText(mContext, "收藏成功！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void collectArticleFailed(String msg) {
        Log.e(TAG, "collectArticleFailed: 20200729 " + msg);
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
            Toast.makeText(mContext, "收藏失败：" + msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void unCollectArticleSuccess(BaseResult res, int position) {
        Log.d(TAG, "unCollectArticleSuccess: " + res);
        HomeBean bean = mResList.get(position);
        ((ArticleBean)bean.getContent()).setCollect(false);
        mResList.set(position, bean);
        mHomeAdapter.notifyDataSetChanged();
        Toast.makeText(mContext, "取消收藏成功！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unCollectArticleFailed(String msg) {
        Log.e(TAG, "collectArticleFailed: " + msg);
        Toast.makeText(mContext, "取消收藏失败："+msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.avatar) {
            ((MainActivity) Objects.requireNonNull(getActivity())).clickMainAvatar();
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (null != mHomeAdapter){
            mHomeAdapter.restartHandler(false);
        }
        if (null != mPresenter) {
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
