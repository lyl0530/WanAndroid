package com.lyl.wanandroid.view;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lym on 2020/10/10
 * Describe :RecycleView设置行间距
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    public SpacesItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = outRect.right = outRect.top = mSpace;
    }
}
