package com.yanyl.baijia.news.atys;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.util.Util;

/**
 * Created by yanyl on 2016/10/23.
 * 详细新闻
 */
public class DetailNewsActivity  extends BaseActivity{
    //WebView 控件
    private WebView webView;
    private ProgressDialog dialog ;

   /* LoadingDialog dialoga;*/
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //具体新闻的url
        String url=getIntent().getStringExtra("detail");

        webView= (WebView) findViewById(R.id.id_news_detail);

        //初始化webView
        webViewInit();
        //判断url 是否为空
        if (url!=null){
            //加载
            loadUrl(url);
            
        }
    }

    private void webViewInit() {

        WebSettings settings = webView.getSettings();
        // 支持js语言
        settings.setJavaScriptEnabled(true);
        // 设置支持页面缩放
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        // 自动将网页设置为一个合理的比例 显示出来
        settings.setUseWideViewPort(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLoadWithOverviewMode(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //关闭dialog
                dialog.dismiss();

            }
        });

    }

    private void loadUrl(String url) {
        if(webView != null)
        {
            webView.loadUrl(url);
            dialog = ProgressDialog.show(this, null, "页面正在加载，请稍后..");
            //重新加载
            webView.reload();
        }
    }

    public void back(View v){
        Util.backIntent(DetailNewsActivity.this);
    }
}
