package fr.ikallali.fdjtest.news;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import fr.ikallali.fdjtest.R;
import fr.ikallali.fdjtest.base.BaseActivity;


public class NewsWebViewActivity extends BaseActivity {

    static final String TAG = "NewsWebViewActivity";

    private WebView webView;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG,"onSaveInstanceState");
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_webview);

        toolbar.setTitle("Open URL");

        // get the post url from indent
        String url = getIntent().getStringExtra("fdj.url");

        webView = findViewById(R.id.webview);


        if(savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(true);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    dismissLoadingDialog();
                }
            });
            showLoadingDialog();
            webView.loadUrl(url);
        }


    }

}
