package com.lyl.wanandroid.ui.fragment;

import android.content.Context;
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
import com.lyl.wanandroid.service.entity.ArticleBean;
import com.lyl.wanandroid.service.entity.NavigationResult;
import com.lyl.wanandroid.service.present.NavigationPresenter;
import com.lyl.wanandroid.service.view.NavigationView;
import com.lyl.wanandroid.utils.LogUtil;
import com.lyl.wanandroid.utils.PhoneUtil;
import com.lyl.wanandroid.ui.view.FlowLayout;

import org.apache.commons.text.StringEscapeUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lym on 2020/7/5
 * Describe :导航tab ：
 */
public class FrgmNavigation extends BaseFragment implements NavigationView {
    private static final String TAG = "FrgmNavigation";
//    private Activity mActivity;
    private Context mContext;
    private View mRootView;
    private ListView mListView;
    private MyAdapter mAdapter;

    private NavigationPresenter mPresenter;

    private NavigationResult mRes;
    private List<NavigationResult.DataBean> mDataList;
    public static FrgmNavigation newInstance(){
        return  new FrgmNavigation();
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
        mContext = getActivity();
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
        LogUtil.e(TAG, "loginFailed: " + msg);
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
            ViewHolder viewHolder;

            if (null == convertView){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_normal_item, parent, false);
                viewHolder = new ViewHolder();
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();                 
            }

            viewHolder.mTvTitle = convertView.findViewById(R.id.tv_item_title);
            viewHolder.mLayoutContent = convertView.findViewById(R.id.layout_item_content);

            NavigationResult.DataBean data = mDataList.get(position);
            List<ArticleBean> list = data.getArticles();

            ArrayList<String> itemList = new ArrayList<>();
            ArrayList<String> urlList = new ArrayList<>();

            for (ArticleBean bean : list){
                itemList.add(StringEscapeUtils.unescapeHtml4(bean.getTitle()));
                urlList.add(bean.getLink());
            }
            Log.d(TAG, "getView: " + data.getName() + ", " + itemList);
            if (itemList.size() > 0) {
                viewHolder.mTvTitle.setText(data.getName());
                viewHolder.mLayoutContent.addItem(itemList/*, urlList, false*/);
                viewHolder.mLayoutContent.setItemClickListener(new FlowLayout.ItemClickListener() {
                    @Override
                    public void clickItem(int index) {
                        Log.d(TAG, "clickItem: index = " + index);
                        //在webView中打开url
                        PhoneUtil.openInWebView(mContext, urlList.get(index));
                    }
                });
            }
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
