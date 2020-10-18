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
import com.lyl.wanandroid.mvp.present.ProjectArticleListPresenter;
import com.lyl.wanandroid.mvp.view.ProjectArticleListView;
import com.lyl.wanandroid.util.Utils;
import com.lyl.wanandroid.view.SpacesItemDecoration;

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

    private int curPageId = 0;

    private ProjectArticleListPresenter mPresenter;
    private RecyclerView mRv;
    private ProjectArticleListAdapter mAdapter;

    public static FragmentProjectArticleList newInstance(int cid, String name){
        FragmentProjectArticleList fr = new FragmentProjectArticleList();
        Bundle bundle = new Bundle();
        Log.d(TAG, "newInstance: set argument : " + cid + ", " + name);
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

//    private TextView tv;
    private void initView() {
        mRv = mRootView.findViewById(R.id.project_article_rv);
        LinearLayoutManager lm = new LinearLayoutManager(mContext);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(lm);
        mRv.addItemDecoration(new SpacesItemDecoration(20));
//        tv = mRootView.findViewById(R.id.fr_content);
//        tv.setText(name);
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

    }

    @Override
    public void Success(ProjectArticleListResult res) {
        if (null != res && null != res.getData() && null != res.getData().getDatas()){
            List<ProjectArticleListResult.DataBean.DatasBean> dataList = res.getData().getDatas();
            Log.d(TAG, "dataList = " + dataList);
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

//    @Override
//    public void showProgressDialog() {
//        super.showProgressDialog();
//    }

//    @Override
//    public void hideProgressDialog() {
//        super.hideProgressDialog();
//    }
}
