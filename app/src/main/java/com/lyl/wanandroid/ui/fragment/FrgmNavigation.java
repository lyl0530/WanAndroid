package com.lyl.wanandroid.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.base.BaseFragment;
import com.lyl.wanandroid.bean.NavigationResult;
import com.lyl.wanandroid.mvp.present.NavigationPresenter;
import com.lyl.wanandroid.mvp.view.NavigationView;
import com.lyl.wanandroid.util.LogUtils;
import com.lyl.wanandroid.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lym on 2020/7/5
 * Describe :体系Tab中的第二个子tab ： 导航
 */
public class FrgmNavigation extends BaseFragment implements NavigationView {
    private static final String TAG = "FrgmNavigation";
    private Activity mActivity;
    private View mRootView;
    private ListView mListView;
    private MyAdapter mAdapter;

    private NavigationPresenter mPresenter;

    private NavigationResult mRes;
    private List<NavigationResult.DataBean> mDataList;
    public static FrgmNavigation newInstance(){
        FrgmNavigation fragment = new FrgmNavigation();
//        new
//        fragment.setArguments();
        return  fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.frgm_navigation, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = mRootView.findViewById(R.id.lv);
        mActivity = getActivity();
        mPresenter = new NavigationPresenter();
        mPresenter.attach(this);

        mPresenter.getNavigation();
    }

    @Override
    public void Success(NavigationResult res) {
        Log.d(TAG, "Success: " + res);
        mRes = res;
        initData();
    }

    @Override
    public void Failed(String msg) {
        LogUtils.e(TAG, "loginFailed: " + msg);
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void initData() {
        if (null == mRes) return;
        mDataList = mRes.getData();

        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);

        //去掉listView的item之间的分隔线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);
    }


    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;

            if (null == convertView){
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.layout_normal_item, parent, false);
                viewHolder = new ViewHolder();
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();                 
            }

            viewHolder.mTvTitle = convertView.findViewById(R.id.tv_item_title);
            viewHolder.mLayoutContent = convertView.findViewById(R.id.layout_item_content);

            NavigationResult.DataBean data = mDataList.get(position);
            List<NavigationResult.DataBean.ArticlesBean> list = data.getArticles();

//            if (null == list) return
            //StringBuilder sb = new StringBuilder();
            ArrayList<String> itemList = new ArrayList<>();
            ArrayList<String> urlList = new ArrayList<>();

            for (NavigationResult.DataBean.ArticlesBean bean : list){
                //sb.append(bean.getTitle()).append(" ");
                itemList.add(bean.getTitle());
                urlList.add(bean.getLink());
            }
            Log.d(TAG, "getView: " + data.getName() + ", " + itemList);
            viewHolder.mTvTitle.setText(data.getName());
                viewHolder.mLayoutContent.addItem(itemList/*, urlList, false*/);

            return convertView;
        }

        private class ViewHolder{
            TextView mTvTitle;
            FlowLayout mLayoutContent;
        }
    }

    @Override
    public void onDestroyView() {
        if (null != mPresenter) {
            mPresenter.detach();
        }
        super.onDestroyView();
    }
}
