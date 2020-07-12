package com.lyl.wanandroid.base;


import com.lyl.wanandroid.mvp.model.Model;
import com.lyl.wanandroid.util.LogUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Created by lym on 2020/4/2
 * Describe :
 */
public abstract class BasePresenter<V extends BaseView> {
    private static final String TAG = BasePresenter.class.getSimpleName();
    private WeakReference<V> mWeakReference; //弱引用, 防止内存泄漏
    private V mProxyView;
    private final Model mModel;

    protected BasePresenter() {
        mModel = new Model();
    }

    protected Model getModel() {
        return mModel;
    }
    /**
     * 关联V层和P层
     */
    public void attach(V view){
        mWeakReference = new WeakReference<>(view);
        MvpViewHandler h = new MvpViewHandler(view);
        //使用代理模式，防止控制指针异常
        //Proxy（库类）中的newProxyInstance方法被调用，该方法返回一个被代理对象的实例:代理view，然后向上转型为其对应的接口
        mProxyView = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(), h);

//        //这里可以通过运行结果证明mProxyView是Proxy的一个实例，这个实例实现了V的接口
//        Log.d(TAG, "mProxyView instanceof Proxy: " + (mProxyView instanceof Proxy));
//
//
//        //这里可以看出mProxyView的Class类是$Proxy0, 这个$Proxy0类继承了Proxy，实现了Subject接口
//        Log.d(TAG, "mProxyView的Class类是："+ mProxyView.getClass().toString());
//
//        Log.d(TAG, "mProxyView中的属性有：");
//        Field[] field= mProxyView.getClass().getDeclaredFields();
//        for(Field f:field){
//            Log.d(TAG, f.getName()+", ");
//        }
//
//        Log.d(TAG, "\n"+"mProxyView中的方法有：");
//        Method[] method= mProxyView.getClass().getDeclaredMethods();
//        for(Method m:method){
//            Log.d(TAG, m.getName()+", ");
//        }
//
//        Log.d(TAG, "\n"+"mProxyView的父类是："+ mProxyView.getClass().getSuperclass());
//
//        Log.d(TAG, "\n"+"mProxyView实现的接口是：");
//        Class<?>[] interfaces= mProxyView.getClass().getInterfaces();
//        for(Class<?> i:interfaces){
//            Log.d(TAG, i.getName()+", ");
//        }

    }

    /**
     * 断开V层和P层
     */
    public void detach(){
        if (isViewAttached()){
            mWeakReference.clear();
            mWeakReference = null;
        }
    }

    /**
     * @return P层和V层是否关联.
     */
    private boolean isViewAttached(){
        return null != mWeakReference && null != mWeakReference.get();
    }

    protected V getView(){
        return mProxyView;
    }
    private class MvpViewHandler implements InvocationHandler {
        private final V mView;
        private MvpViewHandler(V view){
            mView = view;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            LogUtils.d(TAG, "invoke: method = " + method.getName() + ", args = " + Arrays.toString(args));
            // mView.hideProgressDialog()方法时，就调用了$Proxy0类中的hideProgressDialog()方法，
            // 进而调用父类Proxy中的h的invoke()方法.即InvocationHandler.invoke()。
            if (isViewAttached()){ //如果V层没被销毁, 执行V层的方法.
                return method.invoke(mView, args);
            }
            return null;//P层不需要关注V层的返回值
        }
    }
}
