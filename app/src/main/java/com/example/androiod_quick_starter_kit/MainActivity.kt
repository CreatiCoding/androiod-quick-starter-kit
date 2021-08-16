package com.example.androiod_quick_starter_kit

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import android.widget.Toast





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
    private var backBtnTime: Long = 0
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
        myWebView.settings.userAgentString += " JIJU-Android";

        myWebView.settings.domStorageEnabled = true
        myWebView.loadUrl("https://jiju.creative-service.xyz");

        myWebView.addJavascriptInterface(AndroidJSInterface(applicationContext), "JIJU")
    }
    override fun onBackPressed() {
        val myWebView: WebView = findViewById(R.id.webview)
        val curTime = System.currentTimeMillis()
        val gapTime = curTime - backBtnTime
        if (myWebView.canGoBack()) {
            myWebView.goBack()
        } else if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed()
        } else {
            backBtnTime = curTime
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }
}