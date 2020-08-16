package com.example.androiod_quick_starter_kit

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat


class AndroidJSInterface(applicationContext: Context) {
    private val context = applicationContext;

    @JavascriptInterface
    fun getAppVersion(): String {
        Log.d("[Bridge]", "onClicked is called");
        val pInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return pInfo.versionName;
    }

    @JavascriptInterface
    fun goNotificationCenter() {
        val intent = Intent();
        //for Android 5-7
        intent.putExtra("app_package", context.packageName);
        intent.putExtra("app_uid", context.applicationInfo.uid);
        // for Android 8 and above
        intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)

        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS";
        context.startActivity(intent);
    }

    @JavascriptInterface
    fun isEnableNotification(): Boolean{
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}

// adb connect 127.0.0.1:62001
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.settings.javaScriptEnabled = true
        WebView.setWebContentsDebuggingEnabled(true)
        myWebView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url.toString())
                return true
            }
        }
        myWebView.loadUrl("https://www.naver.com");
        myWebView.addJavascriptInterface(AndroidJSInterface(applicationContext), "WebViewTest")
    }
}