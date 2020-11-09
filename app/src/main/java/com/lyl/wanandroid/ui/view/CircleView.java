package com.lyl.wanandroid.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.lyl.wanandroid.R;

/**
 * Created by lym on 2020/6/27
 * Describe :
 */
public class CircleView extends View implements Runnable{
    private final Context mContext;

    private int mResId;
    //最终显示的圆的半径
    private float mR;
    //最终显示的圆心位置
    private float mX;
    private float mY;

    //圆所在屏幕中的宽高 -- 变量能定义成float，就不定义为int，在最后再类型转换
    private float realW;
    private float realH;

    //画圆的画笔
    private Paint mPaint = new Paint();
    private float mAngle;//旋转角度
    //自定义控件 默认找两个参数的构造
    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        //获取控件属性的值
        obtainStyledAttributes(attrs);
    }

    private void obtainStyledAttributes(AttributeSet attrs){
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mR = ta.getDimension(R.styleable.CircleView_r, 0);
        mResId = ta.getResourceId(R.styleable.CircleView_src, 0);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        realW = 2*mR;//getMeasuredWidth();//20200627
        realH = 2*mR;
        setMeasuredDimension((int)realW, (int)realH);
    }

    /**缩放bp
     * x/y bp的中心坐标
     * w/h bp的最终宽高
     * paint 画bp的画笔
     * */
    private void setBitmap(Bitmap bp, float x, float y, float w, float h, Paint paint){
        if (null == bp ){
            return;
        }

        float oriW = bp.getWidth();
        float oriH = bp.getHeight();
        if (oriW <= 0.0f || oriH <= 0.0f){
            return;
        }
        float scaleX = w/oriW;
        float scaleY = h/oriH;
        float scale = Math.max(scaleX, scaleY);//20200627

        Matrix matrix = new Matrix();
        matrix.reset();//重置矩阵为单位矩阵！不可少
        //先缩放，再平移
        matrix.postScale(scale, scale);
        //matrix.postTranslate(x - w/2, y - h/2);
        matrix.postTranslate(x - oriW*scale/2f, y - oriH*scale/2f);//20200627

//        /**
//         平铺模式有三种：
//         Shader.TileMode.CLAMP：如果着色器超出原始边界范围，会复制边缘颜色。
//         Shader.TileMode.MIRROR：图像不停翻转来平铺，直到平铺完毕。
//         Shader.TileMode.REPEAT：横向和纵向的重复着色器的图像。
//         一般来说，当Canvas的宽度（高度）小于等于BitmapShader中Bitmap的宽度（高度），我们会使用Shader.TileMode.CLAMP模式，
//         否则我们会使用Shader.TileMode.MIRROR或者Shader.TileMode.REPEAT模式。
//         **/
        BitmapShader shader = new BitmapShader(bp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        shader.setLocalMatrix(matrix);
        paint.setAntiAlias(true);
        paint.setShader(shader);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //有些变量初始化用到的值，要在测量以后才知道，这些变量可在布局方法中初始化
        //测量 - 布局 - 绘画：只有绘画会走多次，尽量少的在onDraw中计算/初始化变量等等
        mX = realW - mR;
        mY = realH - mR;

        Bitmap bp = BitmapFactory.decodeResource(mContext.getResources(), mResId/*R.drawable.temp1*/);//20200628
        setBitmap(bp, mX, mY, realW, realH, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

//        //1 使用图片
        canvas.save();//将已经绘制的图像保存起来，让后续的操作就好像在一个新图层上操作一样。
        //从自定义视图区域的左顶点(0, 0)到视图的(mX, mY)，但是圆的圆心在视图区域的边界处转
//        canvas.translate(mX, mY);
//        canvas.rotate(mAngle);//画布旋转 逆向为正值
//        canvas.translate(-mX, -mY);
        canvas.drawCircle(mX, mY, mR, mPaint);
        canvas.restore();//合并图层的操作，作用是将save之后绘制的图像和save之前的图像进行合并。save/restore成对出现
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            try {
                mAngle += 1;
                postInvalidate();// 刷新view
                Thread.sleep(10);// 每10ms刷新一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}