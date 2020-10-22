package com.lyl.wanandroid.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lyl.wanandroid.R;
import com.lyl.wanandroid.app.BaseApplication;
import com.lyl.wanandroid.base.BaseFragment;
import com.lyl.wanandroid.base.BaseResult;
import com.lyl.wanandroid.service.entity.ArticleBean;
import com.lyl.wanandroid.service.entity.BannerResult;
import com.lyl.wanandroid.service.entity.HomeBean;
import com.lyl.wanandroid.service.entity.MainArticleResult;
import com.lyl.wanandroid.service.entity.TopArticleResult;
import com.lyl.wanandroid.service.present.MainPresenter;
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
import com.lyl.wanandroid.widget.ViewPagerScroller;
import com.lyl.wanandroid.widget.ViewPagerTransformer;

import org.apache.commons.text.StringEscapeUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by lym on 2020/4/9
 * Describe : 主页
 *
 */
public class FragmentHome extends BaseFragment implements MainView, View.OnClickListener {
    private static final String TAG = FragmentHome.class.getSimpleName();

    private View mView;
    private Context mContext;

    private AppBarLayout mTitleBar;
    private /*ImageView*/ CircleView mAvatar;

//    private ViewPager mViewPager;
//    private MyAdapter mAdapter;
//    private BannerFragmentPagerAdapter mAdapter;

    private List<Integer> mImages = new ArrayList<>();//图片对应的id
    private List<ImageView> mImageViewList = new ArrayList<>();//图片视图
    private List<String> mImgPathList = new ArrayList<>();//真正的path
    private List<String> mUrlList = new ArrayList<>();//点击之后的跳转地址
    private List<String> mTitleList = new ArrayList<>();//对应的标题

    private TextView mBannerTitle, mBannerNumber;

    private MainPresenter mPresenter;





    private ListView mListView;
    private static boolean sExecute = false;
    private List<ArticleBean> dataList = new ArrayList<>();
    private int topArticleCnt = 0;
    private int curPage = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getContext();

        initView();
//        initData();

        EventBus.getDefault().register(this);//这句话要放在初始化组件(findViewById)之后，不然页面接收不到参数

        mPresenter = new MainPresenter();
        mPresenter.attach(this);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMainInfo();
    }

//    private ArticleAdapter mArticleAdapter;


    private RecyclerView mRv;
    private HomeAdapter mHomeAdapter;

    private void initView() {
        //titleBar
        mTitleBar = mView.findViewById(R.id.title_bar);
        mAvatar = mTitleBar.findViewById(R.id.avatar);
        mAvatar.setOnClickListener(this);
        //new Thread(mAvatar).start();

        mTitleBar.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        mRv = mView.findViewById(R.id.rv);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mRv.addItemDecoration(new SpacesItemDecoration(20));

//
//        mViewPager = mView.findViewById(R.id.vp_banner);
//        mBannerTitle = mView.findViewById(R.id.tv_banner_title);
//        mBannerNumber = mView.findViewById(R.id.tv_banner_number);
//
//        mListView = mView.findViewById(R.id.lv_top_article);
//        mListView.setVerticalScrollBarEnabled(false);
//        mListView.getLayoutParams().height =
//                PhoneUtil.getScreenH(mContext);// -  PhoneUtil.dp2px(mContext, 450);
//        mArticleAdapter = new ArticleAdapter();
//        mListView.setAdapter(mArticleAdapter);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                PhoneUtil.openInWebView(mContext, dataList.get(position).getLink());
//            }
//        });
    }




    private void getMainInfo(){
        if (null == mPresenter) return;
        Log.d(TAG, "getMainInfo");

        showProgressDialog();
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
        if (!isVisibleToUser){//main fragment 不可见时，停止轮播图
//            mHandler.removeCallbacksAndMessages(null);
        } else {
//            mHandler.sendEmptyMessageDelayed(MSG_BANNER, DELAY);
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
        if (refreshMain) {
            getMainInfo();
//            mArticleAdapter.notifyDataSetChanged();
            refreshMain = false;
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

    private List<HomeBean> mResList = new ArrayList<>();

    private int articleId;
    private void collectArticleAfterLogin(){
        if (articleId > 0 ){
            //从list中获取当前是否为true，不为true的时候再调用collect方法
            for(int i = 1; i < mResList.size(); i++){//第0个是banner
                ArticleBean d = (ArticleBean)(mResList.get(i).getContent());
                if(null !=  d && articleId == d.getId()){
                    Log.d(TAG, "collectArticleAfterLogin: i = " + i +", collect = " + d.isCollect());
                    if(!d.isCollect()) {
                        Toast.makeText(mContext, "将要收藏文章！", Toast.LENGTH_SHORT).show();
                        mPresenter.collectArticle(articleId, i);
                    }
                    break;
                }
            }
        }
    }
    @Override
    public void getBannerSuccess(BannerResult res) {
//        LogUtils.d(TAG, "banner Success: " + res);
        if (null == res || null == res.getData()) return;
//        mHandler.removeCallbacksAndMessages(null);
        mResList.clear();
        mResList.add(new HomeBean(ConstUtil.TYPE_BANNER, res.getData()));

        mHomeAdapter = new HomeAdapter(mContext, mResList, topArticleCnt);
        mHomeAdapter.setOnItemCollectListener(new HomeAdapter.OnItemCollectListener() {
            @Override
            public void onItemCollect(int articleId, int position, boolean isCollect) {
                if (null == mPresenter) return;
                if (isCollect){//取消收藏
                    mPresenter.unCollectArticle(articleId, position);
                } else { //收藏
                    mPresenter.collectArticle(articleId, position);
                }
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
        for (int i = 0; i < dataList.size(); i++) {
            mResList.add(new HomeBean(ConstUtil.TYPE_ARTICLE, dataList.get(i)));
        }
        topArticleCnt = dataList.size();
        mHomeAdapter.notifyDataSetChanged();
//
//        LogUtil.d(TAG, "top_article Success, data size = topArticleCnt =" + dataList.size());
//        mArticleAdapter.notifyDataSetChanged();
    }

    @Override
    public void getTopArticleFailed(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getMainArticleSuccess(MainArticleResult res) {
//        LogUtils.d(TAG, "main article Success: " + res);
        if (null == res || null == res.getData()) return;

//        dataList.addAll(res.getData().getDatas());
        dataList = res.getData().getDatas();
        for (int i = 0; i < dataList.size(); i++) {
            mResList.add(new HomeBean(ConstUtil.TYPE_ARTICLE, dataList.get(i)));
        }
        mHomeAdapter.notifyDataSetChanged();

//        curPage = res.getData().getCurPage();//成功后，得到curPage=1，下次则使用1作为下标，获取第二页的数据
//
//
//        LogUtil.d(TAG, "main_article Success, data size = " + res.getData().getDatas().size() +
//                ", dataList size = " + dataList.size());

//        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void getMainArticleFailed(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Failed(String msg) {
        LogUtil.e(TAG, msg);
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Finish() {
        hideProgressDialog();
        Log.d(TAG, "Finish: 20200729 will enter collectArticleAfterLogin");
        //退出登录也会刷新首页
        if (BaseApplication.isLogin()) {
            collectArticleAfterLogin();
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

    private static class ViewHolder {
        ImageButton ibtnCollect;
        TextView tvTitle, tvAuthor, tvTime;
        ImageView imgNew, imgTop;
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
        if (null != mPresenter) {
            mPresenter.detach();
        }
//        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}
