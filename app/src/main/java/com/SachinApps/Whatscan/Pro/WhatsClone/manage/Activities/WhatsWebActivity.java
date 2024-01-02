package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.core.view.PointerIconCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.FileChooserParams;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.SachinApps.Whatscan.Pro.WhatsClone.R;

import java.io.InputStream;
import java.util.Locale;

public class WhatsWebActivity extends AppCompatActivity {
    private static ValueCallback<Uri[]> mUploadMessageArr;
    String TAG = WhatsWebActivity.class.getSimpleName();
    AlertDialog alertDialogue;
    boolean doubleBack = false;
    boolean doubleBackToExitPressedOnce = false;
    final Activity mActivity = this;
    private ValueCallback<Uri> mUploadMessage = null;
    WebView webView;


    private class MyWebViewClient extends WebViewClient {
        public void onPageFinished(WebView webView, String str) {
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        }

        /* synthetic */ MyWebViewClient(WhatsWebActivity whatzweb, WhatsWebActivity anonymousClass1) {
            this();
        }

        private MyWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            String str2 = WhatsWebActivity.this.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("shouldOverrideUrlLoading: ");
            stringBuilder.append(str);
            Log.e(str2, stringBuilder.toString());
            if (Uri.parse(str).getHost().contains(".whatsapp.com")) {
                return true;
            }
            WhatsWebActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
            return true;
        }
    }

    public class chromeView extends WebChromeClient {
        @SuppressLint({"NewApi"})
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            return WhatsWebActivity.this.startFileChooserIntent(valueCallback, fileChooserParams.createIntent());
        }

        @RequiresApi(api = 21)
        public void onPermissionRequest(PermissionRequest permissionRequest) {
            permissionRequest.grant(permissionRequest.getResources());
        }

        public void onProgressChanged(WebView webView, int i) {
            WhatsWebActivity.this.mActivity.setTitle("  Wait ...");
            WhatsWebActivity.this.mActivity.setProgress(i * 100);
            if (i == 100) {
                WhatsWebActivity.this.mActivity.setTitle("Whatscan Pro");
            }
            WhatsWebActivity.this.addcss();
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.whatsweb_main);
        if (VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE", "android.permission.ACCESS_COARSE_LOCATION"}, 123);
        }
        ShowInterstitialAds();
        RelativeLayout adLayout = findViewById(R.id.adLayout);

        this.webView = (WebView) findViewById(R.id.WebView);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.getSettings().setBuiltInZoomControls(true);
        this.webView.getSettings().setDisplayZoomControls(false);
        this.webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/41.0");
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new MyWebViewClient(this, null));
        this.webView.getSettings().setSaveFormData(true);
        this.webView.getSettings().setLoadsImagesAutomatically(true);
        this.webView.getSettings().setUseWideViewPort(true);
        this.webView.getSettings().setAllowFileAccessFromFileURLs(true);
        this.webView.getSettings().setBlockNetworkImage(false);
        this.webView.getSettings().setBlockNetworkLoads(false);
        this.webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        this.webView.getSettings().setSupportMultipleWindows(true);
        this.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setNeedInitialFocus(false);
//        this.webView.getSettings().setAppCacheEnabled(true);
        this.webView.getSettings().setDatabaseEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.getSettings().setGeolocationEnabled(true);
        this.webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        this.webView.getSettings().setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://web.whatsapp.com/🌐/");
        stringBuilder.append(Locale.getDefault().getLanguage());
        this.webView.loadUrl(stringBuilder.toString());
        this.webView.setWebChromeClient(new chromeView());


    }

    private void LoadInterstitialAds() {



    }

    public void ShowInterstitialAds(){



    }


    public void addcss() {
        try {
            InputStream open = getAssets().open("s.css");
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            String encodeToString = Base64.encodeToString(bArr, 2);
            WebView webView = this.webView;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("javascript:(function() {var parent = document.getElementsByTagName('head').item(0);var style = document.createElement('style');style.type = 'text/css';style.innerHTML = window.atob('");
            stringBuilder.append(encodeToString);
            stringBuilder.append("');parent.appendChild(style)})();");
            webView.loadUrl(stringBuilder.toString(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4 && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }
        finish();
        return super.onKeyDown(i, keyEvent);
    }

    public void onBackPressed() {
        if (this.doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please press again to exit", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                WhatsWebActivity.this.doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @SuppressLint({"NewApi", "RestrictedApi"})
    public boolean startFileChooserIntent(ValueCallback<Uri[]> valueCallback, Intent intent) {
        if (mUploadMessageArr != null) {
            mUploadMessageArr.onReceiveValue(null);
            mUploadMessageArr = null;
        }
        mUploadMessageArr = valueCallback;
        try {
            startActivityForResult(intent, PointerIconCompat.TYPE_CONTEXT_MENU, new Bundle());
            return true;
        } catch (Throwable th) {
            th.printStackTrace();
            if (mUploadMessageArr != null) {
                mUploadMessageArr.onReceiveValue(null);
                mUploadMessageArr = null;
            }
            return Boolean.parseBoolean(null);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == PointerIconCompat.TYPE_CONTEXT_MENU && VERSION.SDK_INT >= 21) {
            mUploadMessageArr.onReceiveValue(FileChooserParams.parseResult(i2, intent));
            mUploadMessageArr = null;
        }
    }


    protected void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
        this.webView.clearCache(true);
    }

    public void onDestroy() {
        super.onDestroy();
        this.webView.clearCache(true);
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        this.webView.clearCache(true);
        super.onStop();
    }
}
