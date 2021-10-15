package com.juanpablo.eduky.Libro;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.juanpablo.eduky.R;

/**
 * Created by PERSONAL on 31/03/2018.
 */

public class WebViewLibro extends AppCompatActivity {

    private String url;
    private ProgressBar progressBar;
    WebView webView;
    ProgressDialog progressDialog;
    WebSettings settings;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        MobileAds.initialize(this,"ca-app-pub-3909964761084078~7936098913");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("a-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed(){
                super.onAdClosed();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        url = getIntent().getStringExtra("url");
        webView=(WebView)findViewById(R.id.webview1);
        startWebView(url);

    }

    private void startWebView(String url) {

        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }

        settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        progressDialog = new ProgressDialog(WebViewLibro.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebViewLibro.this, "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });
        webView.loadUrl("https://docs.google.com/viewer?url="+url);
    }

}
