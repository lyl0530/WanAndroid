package com.lyl.wanandroid.ui.view;
 
import android.content.Context;
import android.content.res.TypedArray;
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
 * Describe :流式布局优化正式版本
 */
public class FlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";
 
    private List<View> mViewList = new ArrayList<>();;
    //每行行高
    private List<Integer> mLineHList = new ArrayList<>();
    //所有view
    private List<List<View>> mAllViews = new ArrayList<>();

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
 
    private int[] ll = new int[]{android.R.attr.maxLines};
    private int mMaxLines = 0;
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, ll);
        mMaxLines = ta.getInt(0, Integer.MAX_VALUE);
        Log.d("lyl12345", "FlowLayout1: mMaxLines = " + mMaxLines);
        ta.recycle();
    }
 
    /**
    * 先考虑child_margin
    * 再考虑parent_padding
    */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
 
        mAllViews.clear();
        mLineHList.clear();
        int height = 0;   //FlowLayout的高
        int mLineH = 0; //当前行高
        int mLineW = 0; //当前行宽
        /**
         * 容器的使用场景
         * 宽度是确定的
         * 高度 wrap_content /  exactly / unspecified
         *
         * widthMeasureSpec
         *  建议宽度 + mode
         *  1. 300dp + exactly
         *  2. parent_width + at_most
         *  3. 0/parent_width + unspecified ：unspecified模式在scrollView ListView中用到
         *
         */
        //此控件的使用场景，宽度是确定的，modeWidth则是exactly。此处的sizeWidth可以直接使用。
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        //但是高度需要根据传入的view去测量
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);
 
        //根据子view来确定自己的高度
        int cnt = getChildCount();
        mViewList.clear();
        for (int i = 0; i < cnt; i++){
          View child = getChildAt(i);
 
          //child gone时，不做处理
          if (child.getVisibility() == GONE) {
            continue;
          }
                    //child也要确定宽高
          //测量child的宽高，其宽高 受父控件的mode 和 xml中的大小限制
          //mode: MeasureSpec.EXACTLY /AT_MOST / UNSPECIFIED
          //xml中的大小 : 30dp / LayoutParams.MATCH_PARENT / WRAP_CONTENT
          measureChild(child, widthMeasureSpec, heightMeasureSpec);
          //子控件的模式依赖于：
          // 1.xml中所写的值 - 30dp match_parent wrap_content
          // 2.父控件的mode
          //先考虑child_margin - 此处使用了强转
          MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
          int cW = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
          int cH = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
 
          //布局设置了padding,影响到了child的布局 摆放
          //padding不影响控件的固定宽度，会影响子view可用宽度
        //        if (lineW + w > sizeWidth){//换行 : 当前宽度>sizeWidth
          if (mLineW + cW > (sizeW - getPaddingLeft() - getPaddingRight())){//换行
            mLineHList.add(cH);
            //每行的view
            mAllViews.add(mViewList);
            mViewList = new ArrayList<>();
            mViewList.add(child);//换行，把上一行的view加进来，当前行的view也不能丢
 
            height += mLineH;
 
            //新行，重置lineW
            mLineH = cH;
            mLineW = cW;
          } else {//不换行
            mViewList.add(child);
 
            mLineH = Math.max(mLineH, cH);
            mLineW += cW;
          }
          //最后一行
          if (i == cnt-1){
            mLineHList.add(cH);
            mAllViews.add(mViewList);
 
            height += mLineH;
          }
        }
 
        //使用mMaxLines -  mMaxLines小于最大的值 ： 可优化
        if (mMaxLines < mLineHList.size()){
          height = getCurHeightLine();
        }
 
        //padding - 对高度的影响
        //EXACTLY 设置了30dp的高，padding 为20dp,对高度无影响 ： padding不影响明确的高度值
        //AT_MOST padding 为20dp,对高度有影响
        if (MeasureSpec.EXACTLY == modeH){//可以前移优化
          Log.d("lyl12345", "onMeasure: EXACTLY");//EXACTLY：height以传入的建议值为标准
          height = sizeH;
        } else if (MeasureSpec.AT_MOST == modeH) {//AT_MOST:最终高度不能超过建议值
          Log.d("lyl12345", "onMeasure: AT_MOST");
          height = Math.min(height, sizeH);//高度不能超过设置的高度
          height += getPaddingTop() + getPaddingBottom();
        } else if (MeasureSpec.UNSPECIFIED == modeH){ //MeasureSpec.UNSPECIFIED - 无限制，不做处理，height测得的值是多少，就是多少。直接用
          Log.d("lyl12345", "onMeasure: UNSPECIFIED - scrollView等用得到");
          height += getPaddingTop() + getPaddingBottom();
        }
        Log.d("lyl12345", "onMeasure: " + sizeW + "," + height);
        setMeasuredDimension(sizeW, height);
    }
 
    private int getCurHeightLine() {
        int h = 0;
        for (int i = 0; i < mMaxLines; i++){
            h += mLineHList.get(i);
        }
        return h;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /**
          * 摆放view
          * 1.lineH
          * 2.每行的view
          */
        //从左上开始
        int left = getPaddingLeft();//0;
        int top = getPaddingTop();//0;//布局设置了padding,影响到了child的布局 摆放
 
        for (int i = 0; i < mAllViews.size(); i++){
          List<View> viewList = mAllViews.get(i);
          for (int j = 0; j < viewList.size(); j++){
            View child = viewList.get(j);
            MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
            int cl = left + lp.leftMargin;
            int ct = top + lp.topMargin;
            int cr = cl + child.getMeasuredWidth();
            int cb = ct + child.getMeasuredHeight();
            Log.d("lyl12345", "onLayout: " + cl + "," + ct + "," + cr + "," + cb);
            child.layout(cl, ct, cr, cb);
            left = cr + lp.rightMargin;
          }
          left = getPaddingLeft();
          top += mLineHList.get(i);
        }
    }
    //new view后，动态add view时使用，给child设置一个默认的LayoutParams
    //Adds a child view. If no layout parameters are already set on the child, the
    //default parameters for this ViewGroup are set on the child.
    //generateDefaultLayoutParams - child的margin关系
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
 
    /**
     * AttributeSet ： 读取属性中的信息
     inflater
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
     return new MarginLayoutParams(getContext(), attrs);
    }
 
    /**
     addView
     */
    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
    //addView
    //     Override to allow type-checking of LayoutParams.
    //    查看params类型是否正确
    // LayoutParams能用返回true，不能用返回false
    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }
 
    public void addItem(List<String> itemList) {
        if (null != itemList) {
//            Log.d(TAG, "addItem: cnt1 = " + getChildCount());
            if (getChildCount() != 0) {
                removeAllViews();
                Log.d(TAG, "addItem: cnt2 = " + getChildCount());
            }
 
            for (int i = 0; i < itemList.size(); i++) {
                TextView tv = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.item, this, false);
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