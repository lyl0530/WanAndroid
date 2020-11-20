package com.lyl.wanandroid.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyl.wanandroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lym on 2020/7/6
 * Describe : 流式布局测试版本
 */
public class FlowLayoutTemp extends ViewGroup {
    private static final String TAG = "FlowLayout";

    private int paddingT, paddingB, paddingL, paddingR;//上下左右的内边距
    private int distanceH, distanceV;//纵、横轴上每个元素中间的间距
    private int lineH;//行高

    public FlowLayoutTemp(Context context) {
        this(context, null);
    }

    public FlowLayoutTemp(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayoutTemp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        distanceV = distanceH = 20;
    }

    private ArrayList<Integer> arr = new ArrayList<>(); //每行中第一个字符串对应的index

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        arr.clear();

        paddingL = getPaddingStart();
        paddingR = getPaddingEnd();
        paddingT = getPaddingTop();
        paddingB = getPaddingBottom();
        Log.d(TAG, "onMeasure: [" + paddingL + "," + paddingT + "," + paddingR + "," + paddingB+"]");

        int w = getMeasuredWidth();

        int cnt = getChildCount();
        int lineNum = 1;
        int sumW = 0;//当前行所占总宽度
        for (int i = 0; i < cnt; i++) {
            View child = getChildAt(i);
            // 测量子View的宽和高：先调用measureChild，child.getMeasuredHeight();才能获取到值
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //LayoutParams lp = child.getLayoutParams();

            lineH = child.getMeasuredHeight();
            int tempW = child.getMeasuredWidth();

            if (0 == i) {//这一行的第一个元素
                sumW = paddingL + paddingR + tempW;
                arr.add(i);//把第一个元素对应的index存入arr
                Log.d(TAG, "onMeasure: line" + lineNum);
            } else {
                sumW += tempW + distanceH;
            }

            if (sumW > w) {
                sumW = paddingL + paddingR + tempW;
                lineNum++;
                arr.add(i);//换行时，把该元素对应的index存入arr
                Log.d(TAG, "onMeasure: line" + lineNum + ", index = " + i);
            }
        }

        int h = lineH*lineNum + paddingT + paddingB + (lineNum-1)*distanceV;//所有元素所占宽高，包含padding
        Log.d(TAG, "onMeasure: w = " + w + ", h = " + h);
        setMeasuredDimension(w, h)  ;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cnt = getChildCount();
        int curL, curR, curT, curB ,preL;
        int preT = 0;

        for (int i = 0; i < arr.size(); i++) {
            //当前行第一个元素对应的index
            int curLineFirstEleIndex = arr.get(i);

            //当前行最后一个元素对应的index
            boolean lastLine = i+1 >= arr.size();//最后一行
            int curLineLastEleIndex = lastLine ? cnt-1 : arr.get(i + 1) - 1;//下一行的前一个元素，为当前行的最后一个元素
//            Log.d(TAG, "onLayout: line" + i + "=[" + curLineFirstEleIndex + "," + curLineLastEleIndex + "]");

            preL = 0;//每进入新的一行，preL要置0，curL才会从最左侧开始计算
            curT = (preT == 0) ? paddingT : (preT+distanceV);
            curB = curT + lineH;

            //针对当前行做处理
            for (int j = curLineFirstEleIndex; j <= curLineLastEleIndex; j++){
                View child = getChildAt(j);
                //若是第一个元素，则其左边距为paddingL；否则为上个元素的左边界加元素横向间距
                curL = (preL==0) ? paddingL : (preL + distanceH);
                curR = curL + child.getMeasuredWidth();
                //curT = paddingT;
                //curB = curT + lineH;
                child.layout(curL, curT, curR, curB);

                preL = curR;//把当前元素的右边界 赋值给 preL
            }
            preT = curB;//把当前行的底边界 赋值给 preT
        }
    }

    public void addItem(List<String> itemList/*, List<String> list, boolean hierarchy*/) {
        if (null != itemList) {
//            Log.d(TAG, "addItem: cnt1 = " + getChildCount());
            if (getChildCount() != 0) {
                removeAllViews();
                Log.d(TAG, "addItem: cnt2 = " + getChildCount());
            }

            for (int i = 0; i < itemList.size(); i++) {
                TextView tv = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.item, null);
                tv.setText(itemList.get(i));
                int pos = i;
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String url = hierarchy ? "https://www.wanandroid.com/article/list/0/json?cid=" + list.get(pos) :
//                                list.get(pos);
                        Log.d(TAG, "onClick: pos = " + pos);
                        if (null != mListener){
                            mListener.clickItem(pos);
                        }
                    }
                });
                addView(tv);
            }
        }
    }

    public interface ItemClickListener{
        void clickItem(int position);
    }
    private ItemClickListener mListener;
    public void setItemClickListener(ItemClickListener listener){
        mListener = listener;
    }

}