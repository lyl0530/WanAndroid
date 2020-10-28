package com.lyl.wanandroid.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.listener.OnCollectListListener;
import com.lyl.wanandroid.service.entity.Article1Bean;

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lym on 2020/9/27
 * Describe :项目中文章列表适配器
 */
public class CollectListAdapter extends RecyclerView.Adapter<CollectListAdapter.ViewHolder> {
    private static final String TAG = "pal adapter";

    private Context mContext;
    private ArrayList<Article1Bean> mList;

    private OnCollectListListener mListener;
    public void setOnCollectListListener(OnCollectListListener l) {
        mListener = l;
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvAuthor, mTvTime, mTvTitle;
        ImageButton ibtnCollect;
        ImageView imgNew, imgTop;

        public ViewHolder(@NonNull View itemView, final OnCollectListListener listener) {
            super(itemView);
            mTvAuthor = itemView.findViewById(R.id.tv_author);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            ibtnCollect = itemView.findViewById(R.id.ibtn_collect);
            imgNew= itemView.findViewById(R.id.img_new);
            imgTop= itemView.findViewById(R.id.img_top);
        }
    }

    public CollectListAdapter(Context context, List<Article1Bean> list) {
        mContext = context;
        Log.d(TAG, "ProjectArticleListAdapter: list = "+ list.size());
        this.mList = (ArrayList<Article1Bean>)list;
        Log.d(TAG, "ProjectArticleListAdapter: mList = " + mList.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_article_list, viewGroup, false);
        return new ViewHolder(itemView, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: i = " + i + ", " + mList.get(i));
        String author = TextUtils.isEmpty(mList.get(i).getAuthor()) ?
                mList.get(i).getChapterName() : mList.get(i).getAuthor();
        viewHolder.mTvAuthor.setText(author);
        viewHolder.mTvTime.setText(mList.get(i).getNiceDate());
        viewHolder.mTvTitle.setText(Html.fromHtml(StringEscapeUtils.unescapeHtml4(mList.get(i).getTitle())));
        int drawableResId = R.drawable.icon_collected;
        viewHolder.ibtnCollect.setBackground(mContext.getResources().getDrawable(drawableResId));
        final int pos = i;
        //收藏
        viewHolder.ibtnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 20200729: " + pos);
                if (null != mListener){
                    mListener.onItemUnCollect(mList.get(pos).getId(),
                            mList.get(pos).getOriginId(), pos);
                }
            }
        });
        //item点击
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener){
                    mListener.onItemClick(mList.get(pos));
                }
            }
        });
        viewHolder.imgNew.setVisibility(View.GONE);
        viewHolder.imgTop.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: mList.size() = " + mList.size());
        return mList.size();
    }
}
