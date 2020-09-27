package com.lyl.wanandroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lyl.wanandroid.R;
import com.lyl.wanandroid.bean.ProjectArticleListResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lym on 2020/9/27
 * Describe :项目中文章列表适配器
 */
public class ProjectArticleListAdapter extends RecyclerView.Adapter<ProjectArticleListAdapter.ViewHolder> {
    private static final String TAG = "pal adapter";

    private Context mContext;
    private ArrayList<ProjectArticleListResult.DataBean.DatasBean> mList;
    private RequestOptions requestOptions;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvAuthor, mTvTime, mTvTitle, mTvSubTitle;
        ImageView mImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvAuthor = (TextView) itemView.findViewById(R.id.author);
            mTvTime = (TextView) itemView.findViewById(R.id.time);
            mTvTitle = (TextView) itemView.findViewById(R.id.title);
            mTvSubTitle = (TextView) itemView.findViewById(R.id.sub_title);
            mImg = (ImageView) itemView.findViewById(R.id.project_img);
        }
    }

    public ProjectArticleListAdapter(Context context, List<ProjectArticleListResult.DataBean.DatasBean> list) {
        mContext = context;
        Log.d(TAG, "ProjectArticleListAdapter: list = "+ list.size());
        this.mList = (ArrayList<ProjectArticleListResult.DataBean.DatasBean>)list;
        Log.d(TAG, "ProjectArticleListAdapter: mList = " + mList.size());

        //https://blog.csdn.net/qq_19269585/article/details/80968147
        requestOptions = new RequestOptions()
                //.circleCrop()//如果需要裁剪图片，比如圆形，椭圆形等等
                .transforms(new CenterCrop(), new RoundedCorners(30))//图片圆角为30
                .placeholder(R.drawable.temp)
                .error(R.drawable.temp)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_project_article_list, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: i = " + i + ", " + mList.get(i));
        viewHolder.mTvAuthor.setText(mList.get(i).getChapterName() + "/" + mList.get(i).getAuthor());
        viewHolder.mTvTime.setText(mList.get(i).getNiceDate());
        viewHolder.mTvTitle.setText(mList.get(i).getTitle());
        viewHolder.mTvSubTitle.setText(mList.get(i).getDesc());
        Glide.with(mContext).load(mList.get(i).getEnvelopePic())
                .apply(requestOptions)
                .into(viewHolder.mImg);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: mList.size() = " + mList.size());
        return mList.size();
    }
}
