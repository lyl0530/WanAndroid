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
import com.lyl.wanandroid.app.BaseApplication;
import com.lyl.wanandroid.listener.OnArticleListListener;
import com.lyl.wanandroid.service.entity.ArticleBean;

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lym on 2020/9/27
 * Describe :项目中文章列表适配器
 */
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {
    private static final String TAG = "pal adapter";

    private Context mContext;
    private ArrayList<ArticleBean> mList;

    private OnArticleListListener mListener;
    public void setOnArticleListListener(OnArticleListListener l){
        mListener = l;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvAuthor, mTvTime, mTvTitle;
        ImageButton ibtnCollect;
        ImageView imgNew, imgTop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvAuthor = itemView.findViewById(R.id.tv_author);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            ibtnCollect = itemView.findViewById(R.id.ibtn_collect);
            imgNew= itemView.findViewById(R.id.img_new);
            imgTop= itemView.findViewById(R.id.img_top);
        }
    }

    public ArticleListAdapter(Context context, List<ArticleBean> list) {
        mContext = context;
        Log.d(TAG, "ProjectArticleListAdapter: list = "+ list.size());
        this.mList = (ArrayList<ArticleBean>)list;
        Log.d(TAG, "ProjectArticleListAdapter: mList = " + mList.size());

        //https://blog.csdn.net/qq_19269585/article/details/80968147
//        requestOptions = new RequestOptions()
//                //.circleCrop()//如果需要裁剪图片，比如圆形，椭圆形等等
//                .transforms(new CenterCrop(), new RoundedCorners(30))//图片圆角为30
//                .placeholder(R.drawable.temp)
//                .error(R.drawable.temp)
//                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_article_list, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: i = " + i + ", " + mList.get(i));
        String author = TextUtils.isEmpty(mList.get(i).getAuthor()) ?
                mList.get(i).getChapterName() :
                /*mList.get(i).getChapterName() + "/" + */mList.get(i).getAuthor();
        viewHolder.mTvAuthor.setText(author);
        viewHolder.mTvTime.setText(mList.get(i).getNiceDate());
        viewHolder.mTvTitle.setText(Html.fromHtml(StringEscapeUtils.unescapeHtml4(mList.get(i).getTitle())));
//        viewHolder.mTvSubTitle.setText(mList.get(i).getDesc());
//        Glide.with(mContext).load(mList.get(i).getEnvelopePic())
//                .apply(requestOptions)
//                .into(viewHolder.mImg);
        int drawableResId = mList.get(i).isCollect() && BaseApplication.isLogin() ?
                R.drawable.icon_collected :
                R.drawable.icon_collecte;
        viewHolder.ibtnCollect.setBackground(mContext.getResources().getDrawable(drawableResId));
        final int pos = i;
        //收藏
        viewHolder.ibtnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 20200729: " + pos);
//                Toast.makeText(mContext, "点击了"+pos, Toast.LENGTH_SHORT).show();
                if (null != mListener){
                    mListener.onItemCollect(mList.get(pos).getId(), pos, mList.get(pos).isCollect());
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
