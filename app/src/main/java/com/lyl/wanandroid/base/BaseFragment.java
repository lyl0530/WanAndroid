package com.lyl.wanandroid.base;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by lym on 2020/5/6
 * Describe :
 */
public class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }
}
