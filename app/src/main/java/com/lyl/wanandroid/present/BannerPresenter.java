package com.lyl.wanandroid.present;

import com.lyl.wanandroid.base.BasePresenter;
import com.lyl.wanandroid.bean.BannerResult;
import com.lyl.wanandroid.listener.RequestListener;
import com.lyl.wanandroid.model.BannerModel;
import com.lyl.wanandroid.view.BannerView;

/**
 * Created by lym on 2020/5/6
 * Describe :
 */
public class BannerPresenter extends BasePresenter<BannerView> {
    private BannerView mView;
    private final BannerModel mModel;

    public BannerPresenter() {
        mModel = new BannerModel();
    }

    @Override
    public void attach(BannerView view) {
        super.attach(view);
        mView = view;
    }

    @Override
    public void detach() {
        if (null != getView()) {
            mView = null;
        }
        super.detach();
    }

    public void getBanner() {
        mModel.getBanner(new RequestListener<BannerResult>() {
            @Override
            public void onStart() {
                getView().showProgressDialog();
            }

            @Override
            public void onSuccess(BannerResult res) {
                getView().Success(res);
            }

            @Override
            public void onFailed(String msg) {
                getView().Failed(msg);
            }

            @Override
            public void onFinish() {
                getView().hideProgressDialog();
            }
        });
    }
}
