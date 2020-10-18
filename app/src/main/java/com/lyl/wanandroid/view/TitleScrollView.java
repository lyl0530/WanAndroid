package com.lyl.wanandroid.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.listener.ScrollViewListener;
import com.lyl.wanandroid.util.Utils;

import java.util.ArrayList;

/**
 * Created by lym on 2020/9/27
 * Describe :
 */
public class TitleScrollView extends HorizontalScrollView {
    private static final String TAG = "TitleScrollView";

    private ScrollViewListener mListener;
    private Context mContext;
    public TitleScrollView(Context context){
        this(context, null);
    }
    public TitleScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
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
