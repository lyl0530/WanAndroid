package com.lyl.wanandroid.ui.activity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.base.BaseActivity;
import com.lyl.wanandroid.utils.ConstUtil;

public class WebActivity extends BaseActivity{
    private static final String TAG = WebActivity.class.getSimpleName();

    private WebView mWebView;
    private String mUrl;
    private final String mErrorUrl = "file:///android_asset/test.html";//-> assets目录下放置文件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent intent = getIntent();
        if (null != intent){
            mUrl = intent.getStringExtra(ConstUtil.WEB_VIEW_URL);
        }
        Log.d(TAG, "onCreate: url = " + mUrl);
        initView();
    }

    private ProgressBar mProgress;

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        mProgress = findViewById(R.id.pb);
        mWebView = findViewById(R.id.wv);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setWebChromeClient(chromeClient);
        mWebView.loadUrl(null == mUrl ? mErrorUrl : mUrl);

        WebSettings webSetting = mWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);// -> 是否开启JS支持
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);// -> 是否允许JS打开新窗口
        webSetting.setAllowFileAccess(true);//-> 是否允许访问文件
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//-> 设定WebView的HTML布局方式
        webSetting.setSupportZoom(true);//-> 是否支持缩放
        webSetting.setBuiltInZoomControls(true);// -> 是否支持缩放变焦，前提是支持缩放
        webSetting.setUseWideViewPort(true);// -> 缩放至屏幕大小
        webSetting.setLoadWithOverviewMode(true);//-> 缩放至屏幕大小
        webSetting.setAppCacheEnabled(true);//-> 是否应用缓存
        webSetting.setDatabaseEnabled(true); //-> 是否数据缓存
        webSetting.setDomStorageEnabled(true);//-> 是否节点缓存
        webSetting.setGeolocationEnabled(true);//-> 设置开启定位功能
//        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
//        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);//（默认）根据cache-control决定是否从网络上取数据。

        //当WebView加载的链接为Https开头，但是链接里面的内容，比如图片为Http链接，这时候，图片就会加载不出来
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }
    
    private WebViewClient mWebViewClient = new WebViewClient(){
        //页面开始加载时调用
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgress.setVisibility(View.VISIBLE);
            Log.d(TAG, "onPageStarted: ");
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            startTimer();
            super.onLoadResource(view, url);
        }

        //页面完成加载时调用
        @Override
        public void onPageFinished(WebView view, String url) {
            stopTimer();
            mProgress.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

//            /**
//             * 若没有设置 WebViewClient 则由系统（Activity Manager）处理该 url，通常是使用浏览器打开或弹出浏览器选择对话框。
//             * 若设置 WebViewClient 且该方法返回 true ，则说明由应用的代码处理该 url，WebView 不处理，也就是程序员自己做处理。
//             * 若设置 WebViewClient 且该方法返回 false，则说明由 WebView 处理该 url，即用 WebView 加载该 url。
//             *
//             * 链接：https://www.jianshu.com/p/3474cb8096da
//             */
            if (null != request && null != request.getUrl()){
                Log.d(TAG, "shouldOverrideUrlLoading: "+ request.getUrl());
                String url = request.getUrl().toString();
                if (url.startsWith("http://") || url.startsWith("https://")) { //加载的url是http/https协议地址
//                    view.loadUrl(url);
//                    return true;//返回true，说明不让WebView处理，自己在代码里处理，即自己调用了WebView的loadUrl方法
                    return false; //返回false表示由WebView处理该url。和上边两句的功能是一样的。
                } else { //加载的url是自定义协议地址，如jianshu:// alipays:// 等
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;//返回 true，说明不让WebView处理，自己在代码里处理
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            stopTimer();
            super.onReceivedError(view, request, error);
            Log.e(TAG, "onReceivedError: " + request.getUrl() + ", " + error.getDescription());
//            showToast(request.getUrl() + ", " + error.getDescription());
//            view.loadUrl(mErrorUrl);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            stopTimer();
            super.onReceivedHttpError(view, request, errorResponse);
            Log.e(TAG, "onReceivedHttpError: " + request.getUrl() + ", " + errorResponse.toString());
//            view.loadUrl(mErrorUrl);

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
            builder.setMessage("lalalal");
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    //超时处理
    private final static int TIMEOUT_TIMER_ID = 1;
    private final static int TIMEOUT_TIME = 10*1000;
    private void startTimer() {
        mHandler.sendEmptyMessageDelayed(TIMEOUT_TIMER_ID, TIMEOUT_TIME);
    }

    private void stopTimer() {
        mHandler.removeCallbacksAndMessages(null);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (TIMEOUT_TIMER_ID == msg.what){
                stopTimer();
                if (mProgress.getProgress() < 100 && null != mWebView){
//                    showToast("响应超时");
//                    mWebView.loadUrl(mErrorUrl);
                }
            }
        }
    };

    WebChromeClient chromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgress.setProgress(newProgress);
        }
    };

    public void onBack(View v){
        if (mWebView.canGoBack() && !mErrorUrl.equals(mWebView.getUrl())){//当前不处于error界面时
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWebView.canGoBack()
                && !mErrorUrl.equals(mWebView.getUrl())
                && keyCode == KeyEvent.KEYCODE_BACK){//点击返回按钮的时候判断有没有上一页
            mWebView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        //释放资源
        mWebView.destroy();
        mWebView = null;
        super.onDestroy();
    }
}
