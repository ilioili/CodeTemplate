package com.taihe.template.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.taihe.template.app.base.AppBaseActivity;
import com.taihe.template.base.injection.Id;
import com.taihe.template.base.injection.Layout;

import java.util.LinkedList;


@Layout(R.layout.activity_web)
public class BaseWebActivity extends AppBaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private static final String INTENT_KEY_URL = "URL";
    private static final String INTENT_KEY_CAN_NAVITATE = "CanNavitate";
    public static final String INTENT_KEY_USER_CACHE = "UseCache";

    @Id(R.id.webView)
    private WebView webView;
    @Id(R.id.swipeRefreshLayout)
    private SwipeRefreshLayout swipeRefreshLayout;
    @Id(R.id.toolbar)
    private Toolbar toolbar;

    private LinkedList<String> urls = new LinkedList<>();

    public static Intent newIntent(Context context, String url, boolean canNavigateBackInWebView, boolean userCache){
        Intent it = new Intent(context, BaseWebActivity.class);
        it.putExtra(INTENT_KEY_URL, url);
        it.putExtra(INTENT_KEY_CAN_NAVITATE, canNavigateBackInWebView);
        it.putExtra(INTENT_KEY_USER_CACHE, userCache);
        return it;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        swipeRefreshLayout.setOnRefreshListener(this);
        setUpWebView();
        webView.loadUrl("http://bbs.360taihe.com");
    }

    @Override
    public void onRefresh() {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpWebView() {
        CookieManager cookieManager = CookieManager.getInstance();
        System.out.println(cookieManager.getCookie("http://bbs.360taihe.com"));
        webView.clearCache(true);
        webView.clearMatches();
        webView.clearFormData();
        webView.clearHistory();
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUserAgentString("Android 4.4.4)");
    }
}
