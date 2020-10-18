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
import com.lyl.wanandroid.bean.HierarchyResult;
import com.lyl.wanandroid.mvp.present.HierarchyPresenter;
import com.lyl.wanandroid.mvp.view.HierarchyView;
import com.lyl.wanandroid.util.LogUtils;
import com.lyl.wanandroid.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lym on 2020/4/9
 * Describe :
 */
public class FrgmHierarchy extends BaseFragment implements HierarchyView {
    private static final String TAG = "FrgmHierarchy";

    private Activity mActivity;
    private View mRootView;
    private ListView mListView;
    private MyAdapter mAdapter;

    private HierarchyPresenter mPresenter;

    private HierarchyResult mRes;
    private List<HierarchyResult.DataBean> mDataList;
    public static FrgmHierarchy newInstance(){
        FrgmHierarchy fragment = new FrgmHierarchy();
//        new
//        fragment.setArguments();
        return  fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.frgm_hierarchy, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = mRootView.findViewById(R.id.lv);
        mActivity = getActivity();
        mPresenter = new HierarchyPresenter();
        mPresenter.attach(this);

        mPresenter.getHierarchy();
    }


    @Override
    public void Success(HierarchyResult res) {
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

            HierarchyResult.DataBean data = mDataList.get(position);
            List<HierarchyResult.DataBean.ChildrenBean> list = data.getChildren();
//            if (null == list) return
            //StringBuilder sb = new StringBuilder();
            ArrayList<String> itemList = new ArrayList<>();
            ArrayList<String> cidList = new ArrayList<>();

            for (HierarchyResult.DataBean.ChildrenBean bean : list){
                //sb.append(bean.getName()).append(" ");
                itemList.add(bean.getName());
                cidList.add(bean.getId()+"");
            }
            Log.d(TAG, "getView: " + data.getName() + ", " + itemList);
            viewHolder.mTvTitle.setText(data.getName());
                viewHolder.mLayoutContent.addItem(itemList/*, cidList, true*/);

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
