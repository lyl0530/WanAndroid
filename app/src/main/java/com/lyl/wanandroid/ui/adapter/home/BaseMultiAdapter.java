package com.lyl.wanandroid.ui.adapter.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.lyl.wanandroid.service.entity.DataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lym on 2020/10/21
 * Describe :
 */
public class BaseMultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "BaseMultiAdapter";

    private List<DataBean> mDataList = new ArrayList<>();
    private int topArticleCnt;

    public void setDataItems(List<DataBean> list, int cnt) {
        mDataList = list;
        topArticleCnt = cnt;
        notifyDataSetChanged();
    }

    List<IDelegateAdapter> delegateAdapters = new ArrayList<>();

    public void addDelegate(IDelegateAdapter delegateAdapter) {
        delegateAdapters.add(delegateAdapter);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // 找到对应的委托Adapter
        IDelegateAdapter delegateAdapter = delegateAdapters.get(viewType);
        // 把onCreateViewHolder交给委托Adapter去处理
        return delegateAdapter.onCreateViewHolder(viewGroup, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // 找到当前ViewHolder的ViewType，也就是委托Adapter在集合中的index
        int viewType = holder.getItemViewType();
        // 找到对应的委托Adapter
        IDelegateAdapter delegateAdapter = delegateAdapters.get(viewType);
        // 把onBindViewHolder交给委托Adapter去处理
        delegateAdapter.onBindViewHolder(holder, position, mDataList.get(position), topArticleCnt);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
//        return position == 0 ? ConstUtil.TYPE_BANNER : ConstUtil.TYPE_ARTICLE;
        // 找到当前位置的数据
        DataBean bean = mDataList.get(position);

        // 遍历所有的代理，问下他们谁能处理
        for (IDelegateAdapter delegateAdapter : delegateAdapters) {
            if (delegateAdapter.isForViewType(bean)) {
                // 谁能处理返回他的index
                return delegateAdapters.indexOf(delegateAdapter);
            }
        }
        throw new RuntimeException("没有找到可以处理的委托Adapter");
    }
}
