package com.lyl.wanandroid.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.util.Utils;

import java.util.ArrayList;

/**
 * Created by lym on 2020/4/9
 * Describe :
 */
public class FragmentProject extends Fragment {
    private static final String TAG = "FragmentProject ";
    private Context mContext;
    private View mRootView;

    private HorizontalScrollView mScrollView;
    private LinearLayout mLineLayout;

    private int curTvCenterX, preTvCenterX;
    private boolean isFirst = true;
    private int SCREEN_CENTER_X, LINEAR_LAYOUT_W;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_project, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();

        initView();
        initDate();
    }

    private void initView() {
        mLineLayout = mRootView.findViewById(R.id.ll_text);
        mScrollView = mRootView.findViewById(R.id.scrollView);
    }

    private void initDate() {
        preTvCenterX = SCREEN_CENTER_X = Utils.getScreenW(mContext)/2;
        Log.d(TAG, "initDate: SCREEN_CENTER_X = " + SCREEN_CENTER_X);

        ArrayList<String> list = new ArrayList<>();
        list.add("项目1");
        list.add("项目2");
        list.add("项目3");
        list.add("项目1333");
        list.add("项目14444");
        list.add("项目15556");
        list.add("项目15555555577777");
        list.add("项目1555555558888");
        list.add("项目");
        list.add("项目1");

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(Utils.dp2px(mContext,15),0,Utils.dp2px(mContext,15),0);//4个参数按顺序分别是左上右下

        for (int i = 0; i < list.size(); i++){
            TextView tv = new TextView(mContext);
            tv.setText(list.get(i));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            tv.setTextColor(i == 0 ? Color.WHITE : getResources().getColor(R.color.little_dark));
            tv.setLayoutParams(layoutParams);
            tv.setTag(i);
            mLineLayout.addView(tv);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (int)((TextView)view).getTag();
                    TextView curTv = (TextView)mLineLayout.getChildAt(index);
                    //getLeft 和onLayout之后的getWidth的关系？都是在布局之后，可以得到其值的吗？
                    Log.d(TAG, "onClick: index = "
                            + index + ", l = " + curTv.getLeft() + ", r = " + curTv.getRight()
//                            + ", all_l = " + mScrollView.getLeft() + ", all_r = " + mScrollView.getRight()
                            + ", l_l = " + mLineLayout.getLeft() + ", l_r = " + mLineLayout.getRight()
                            +", l_w = " + mLineLayout.getWidth() + ", margin_l/r = " + Utils.dp2px(mContext,30));

                    curTvCenterX = (curTv.getRight()+curTv.getLeft())/2;

                    //第一次：当前tv可动，则位于正中
                    if (isFirst){
                        isFirst = false;
                        //当前textView的中轴c1，距离屏幕的中轴c2的距离，若c1在c2右侧，c1c2重合后，c1后边tv不会受影响，则
                        Log.d(TAG, "onClick: curTvCenterX = " + curTvCenterX + ", preTvCenterX = " + preTvCenterX);
                        if (curTvCenterX > preTvCenterX){
                            mScrollView.smoothScrollBy(curTvCenterX-preTvCenterX, 0);
                            preTvCenterX = curTvCenterX;
                        }
                    } else { //第二次：当前tv可动，则tv也滑向正中
                        Log.d(TAG, "onClick: curTvCenterX = " + curTvCenterX + ", preTvCenterX = " + preTvCenterX);
                        if (curTvCenterX > SCREEN_CENTER_X) {
                            mScrollView.smoothScrollBy(curTvCenterX - preTvCenterX, 0);
                            if (curTvCenterX > LINEAR_LAYOUT_W-SCREEN_CENTER_X){
                                preTvCenterX = LINEAR_LAYOUT_W-SCREEN_CENTER_X;
                            } else {
                                preTvCenterX = curTvCenterX;
                            }
                        } else {
                            mScrollView.smoothScrollBy(curTvCenterX - preTvCenterX, 0);
                            preTvCenterX = SCREEN_CENTER_X;
                        }
                    }
                    for (int i = 0; i < mLineLayout.getChildCount(); i++){
                        TextView temTv = (TextView) mLineLayout.getChildAt(i);
                        temTv.setTextColor(i == index ? Color.WHITE : getResources().getColor(R.color.little_dark));
                    }
                }
            });
        }

        //只写mLineLayout.getRight()，获取的值为0，此时mLineLayout还没绘制完成，得不到值
        //https://www.cnblogs.com/ouyangping/p/7398753.html
        mLineLayout.post(new Runnable() {
            @Override
            public void run() {
                LINEAR_LAYOUT_W = mLineLayout.getRight();
                Log.d(TAG, "initDate: LINEAR_LAYOUT_W = " + LINEAR_LAYOUT_W + ", " + mLineLayout.getWidth());
            }
        });
    }
}
