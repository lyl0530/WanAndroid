package com.lyl.wanandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import com.lyl.wanandroid.listener.ScrollViewListener;

/**
 * Created by lym on 2020/9/27
 * Describe :
 */
public class ProjectTitleScrollView extends HorizontalScrollView {

    private ScrollViewListener mListener;
    public ProjectTitleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProjectTitleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ProjectTitleScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ScrollViewListener getListener() {
        return mListener;
    }

    public void setListener(ScrollViewListener l) {
        this.mListener = l;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != mListener){
            mListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
}
