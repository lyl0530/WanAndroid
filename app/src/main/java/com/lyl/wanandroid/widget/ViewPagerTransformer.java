package com.lyl.wanandroid.widget;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by lym on 2020/5/23
 * Describe : ViewPager移动动画
 * 1.position=0,静止状态下当前显示的page的位置
 * 2.position=1,静止状态下,当前page右边的第一个page的位置,依此类推,逐渐+1
 * 3.position=-1,静止状态下,当前page左边的第一个page的位置,此次类推,逐渐+1
 *
 * 其他特效：
 * https://www.jianshu.com/p/3a199fbe1f7f
 * https://blog.csdn.net/sclgxt/article/details/75572229
 *
 */
public class ViewPagerTransformer implements ViewPager.PageTransformer {
    private static final String TAG = ViewPagerTransformer.class.getSimpleName();

    private static final float MIN_SCALE = 0.25f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        onPreTransform(page, position);
        onTransform(page, position);
        onPostTransform(page, position);
    }

    private void onPreTransform(View page, float position) {
        Log.d(TAG, "onPreTransform: position = " + position);

        //不旋转
        page.setRotationX(0);//绕着水平中心线旋转
        page.setRotationY(0);//绕着竖直中心线旋转
        page.setRotation(0);//绕着中心点平面旋转

        //不缩放
        page.setScaleX(1);// 横向缩放
        page.setScaleY(1);// 纵向缩放

        //把view的左上角 设置为缩放轴点
        page.setPivotX(0);//设置缩放轴点X轴的坐标
        page.setPivotY(0);//设置缩放轴点Y轴的坐标

        //设置view相对原始位置的偏移量
        page.setTranslationY(0);
        page.setTranslationX(0);

        //只有当前位置的view可见，其余都不可见：0f-透明,1f-可见
        page.setAlpha(position <= -1f || position >= 1f ? 0f : 1f);
    }

    private void onTransform(View view, float position) {
        if (position <= 0f) {//左侧的view
            view.setTranslationX(0f);
            view.setScaleX(1f);
            view.setScaleY(1f);
        } else if (position <= 1f) {//当前view
            view.setAlpha(1 - position);//pos变大，透明度越低，越不可见

            //把view的左边中间 设置为缩放轴点
            view.setPivotY(0.5f * view.getHeight());

            //设置view向左的偏移量
            view.setTranslationX(view.getWidth() * -position);

            //根据pos来缩放，pos越大，越接近缩放比例
            final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }
    }

    private void onPostTransform(View page, float position) {
    }


}
