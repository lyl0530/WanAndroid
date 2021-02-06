package com.lyl.wanandroid.base;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lym on 2020/5/6
 * Describe :
 */
public abstract class BaseFragment extends Fragment implements BaseView{
    private static final String TAG = BaseFragment.class.getSimpleName();

//    private Context mContext;
//    private LoadingDialog mLoadingDialog;

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        mContext = context;
//    }

//    public Context getContext() {
//        return mContext;
//    }

    @Override
    public void showProgressDialog() {
//        if (null == mLoadingDialog) {
//            mLoadingDialog = LoadingDialog.with(mContext);
//        }
//        if(!mLoadingDialog.isShow()) {
//            mLoadingDialog.show();
//        }
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity){
            ((BaseActivity)activity).showProgressDialog();
        }
    }

    @Override
    public void hideProgressDialog() {
//        if (null != mLoadingDialog && mLoadingDialog.isShow()) {
//            mLoadingDialog.dismiss();
//            mLoadingDialog = null;
//        }
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity){
            ((BaseActivity)activity).hideProgressDialog();
        }
    }

    protected Toast mToast;
    /**
     * 显示Toast
     *
     * @param messageId 提示信息资源ID
     */
    public void showToast(int messageId) {
        showToast(getString(messageId));
    }

    /**
     * 只显示一个toast的实现。
     * http://stackoverflow.com/questions/6925156/
     * @param message 提示信息
     */
    @SuppressLint("ShowToast")
    public void showToast(String message) {
        // 如果app不在前台，则不显示toast
//        if (!AirDeviceApplication.isRunningForeground()) {
//            return;
//        }
        try {
            mToast.getView().isShown();
        } catch (Exception e) {
            mToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        } finally {
            mToast.setText(message);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            //toast 多行内容居中显示 https://blog.csdn.net/ityangjun/article/details/68946273
            int textViewId = Resources.getSystem().getIdentifier("message", "id", "android");
            ((TextView) mToast.getView().findViewById(textViewId)).setGravity(Gravity.CENTER);
            mToast.show();
        }
    }
}
