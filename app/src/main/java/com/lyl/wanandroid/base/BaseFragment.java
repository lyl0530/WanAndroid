package com.lyl.wanandroid.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.lyl.wanandroid.widget.LoadingDialog;

/**
 * Created by lym on 2020/5/6
 * Describe :
 */
public abstract class BaseFragment extends Fragment implements BaseView{
    private static final String TAG = BaseFragment.class.getSimpleName();

    private Context mContext;
    private LoadingDialog mLoadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void showProgressDialog() {
        if (null == mLoadingDialog) {
            mLoadingDialog = LoadingDialog.with(mContext);
        }
        if(!mLoadingDialog.isShow()) {
            mLoadingDialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (null != mLoadingDialog) {
            mLoadingDialog.dismiss();
        }
    }
}
