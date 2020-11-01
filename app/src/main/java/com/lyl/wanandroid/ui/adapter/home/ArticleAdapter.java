package com.lyl.wanandroid.ui.adapter.home;

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
import com.lyl.wanandroid.app.BaseApplication;
import com.lyl.wanandroid.listener.OnArticleListListener;
import com.lyl.wanandroid.service.entity.ArticleBean;
import com.lyl.wanandroid.service.entity.DataBean;
import com.lyl.wanandroid.utils.ConstUtil;

import org.apache.commons.text.StringEscapeUtils;

public class ArticleAdapter implements IDelegateAdapter {
    private static final String TAG = "ArticleList1Adapter";

    private Context mContext;
    public ArticleAdapter(Context context) {
        mContext = context;
    }

    @Override
    public boolean isForViewType(DataBean bean) {
        // 我能处理一张图片
        return bean.getType() == ConstUtil.TYPE_ARTICLE;
    }
 
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_article_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, DataBean bean, int topArticleCnt) {
        Log.d(TAG, "onBindViewHolder: position = " + position + ", topArticleCnt = " + topArticleCnt);
        ViewHolder viewHolder = (ViewHolder)holder;
        ArticleBean b = (ArticleBean)bean.getContent();

        String author = TextUtils.isEmpty(b.getAuthor()) ? b.getChapterName() : b.getAuthor();
        viewHolder.mTvAuthor.setText(author);
        viewHolder.mTvTime.setText(b.getNiceDate());
        viewHolder.mTvTitle.setText(Html.fromHtml(StringEscapeUtils.unescapeHtml4(b.getTitle())));
        viewHolder.mTvNum.setText(position+"");
        int drawableResId = b.isCollect() && BaseApplication.isLogin() ?
                R.drawable.icon_collected :
                R.drawable.icon_collecte;
        viewHolder.ibtnCollect.setBackground(mContext.getResources().getDrawable(drawableResId));

        if(1 == position || topArticleCnt+1 == position) {
            viewHolder.imgNew.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgNew.setVisibility(View.GONE);
        }
        if (position <= topArticleCnt) {
            viewHolder.imgTop.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgTop.setVisibility(View.GONE);
        }

        final int pos = position;
        //收藏
        viewHolder.ibtnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 20200729: " + pos);
//                Toast.makeText(mContext, "点击了"+pos, Toast.LENGTH_SHORT).show();
                if (null != mListener){
                    mListener.onItemCollect(b.getId(), pos, b.isCollect());
                }
            }
        });
        //item点击
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener){
                    mListener.onItemClick(b);
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvAuthor, mTvTime, mTvTitle, mTvNum;
        ImageButton ibtnCollect;
        ImageView imgNew, imgTop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvAuthor = itemView.findViewById(R.id.tv_author);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvNum = itemView.findViewById(R.id.tv_num);
            ibtnCollect = itemView.findViewById(R.id.ibtn_collect);
            imgNew= itemView.findViewById(R.id.img_new);
            imgTop= itemView.findViewById(R.id.img_top);
        }
    }

    private OnArticleListListener mListener;
    public void setOnArticleListListener(OnArticleListListener l){
        mListener = l;
    }
}