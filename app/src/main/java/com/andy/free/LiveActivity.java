package com.andy.free;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.andy.free.widget.NestedScrollWebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/9/27.
 */

public class LiveActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.live_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.live_webView)
    NestedScrollWebView mWebView;

    @BindView(R.id.share_icon)
    ImageView shareBtn;

    Unbinder mUnbinder;

    private ProgressDialog mProgressDlg;
    private SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_live);
        mUnbinder=ButterKnife.bind(this);

        //mToolbar= (Toolbar) findViewById(R.id.live_toolbar);
        mToolbar.setTitle("直播");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mProgressDlg = new ProgressDialog(this);

        sweetAlertDialog=new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在努力加载中...");
        sweetAlertDialog.setCancelable(true);
        setWebView();

        shareBtn.setOnClickListener(this);
    }

    private void setWebView() {

        final String url="http://live.bianxianmao.com/redirect.htm?appKey=70e6374729e743118af31c3d6bb1828d&appType=app&appEntrance=1";

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        WebSettings webSettings=mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        int i = Build.VERSION.SDK_INT;
        try {
            webSettings.setAllowFileAccess(true);
            if (i>= 5) {
                webSettings.setDatabaseEnabled(true);
                webSettings.setGeolocationEnabled(true);
            }

            if (i>= 7) {
                webSettings.setAppCacheEnabled(true);
                webSettings.setDomStorageEnabled(true);
                webSettings.setUseWideViewPort(true);
                webSettings.setLoadWithOverviewMode(true);
            }
            if (i>= 8) {
                webSettings.setPluginState(WebSettings.PluginState.ON);
            }
            webSettings.setBuiltInZoomControls(false);
            webSettings.setSupportZoom(false);
            webSettings.setAppCachePath(this.getCacheDir().getAbsolutePath());
        } catch (Exception e) {
        }

        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                view.loadUrl(url);
                return true;

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if (!sweetAlertDialog.isShowing()){
                    sweetAlertDialog.show();
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (sweetAlertDialog.isShowing()){
                    sweetAlertDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                if (sweetAlertDialog.isShowing()){
                    sweetAlertDialog.dismiss();
                }
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (newProgress<=90){
                    //sweetAlertDialog.setProgress(newProgress);
                }else {
                    sweetAlertDialog.dismiss();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode==event.KEYCODE_BACK)&&mWebView.canGoBack()){

            mWebView.goBack();

            return true;
        }

        return super.onKeyDown(keyCode, event);

    }

    /*
    * 分享按钮点击事件监听
    * */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_icon:

/*                final ShareDialog dialog=new ShareDialog(mContext,R.style.Theme_share_dialog);
                dialog.show();
                dialog.setShareItemClickListener(new ShareDialog.OnShareItemClickListener() {
                    @Override
                    public void onItemClick(View view) {

                        switch (view.getId()){
                            case R.id.btn_share_cancel:
                                dialog.dismiss();
                                break;
                            default:
                                Toast.makeText(mContext,"暂时不支持分享！",Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });*/
                break;
            default:
                break;
        }
    }
}
