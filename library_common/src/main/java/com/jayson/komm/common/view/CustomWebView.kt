package com.jayson.komm.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.webkit.*

/**
 * 自定义WebView
 */
class CustomWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    init {
        setupWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        // 透明背景会增加 WebView 的渲染复杂度，从而降低加载速度，可以设置不透明的背景色。
        setBackgroundColor(Color.WHITE)
        // 开启 WebView 调试模式
        setWebContentsDebuggingEnabled(false)
        // 硬件加速
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        settings.apply {
            // 启用JavaScript
            javaScriptEnabled = true
            // 启用DOM Storage
            domStorageEnabled = true
            // 是否支持ViewPort标签
            useWideViewPort = true
            // 使用内置的缩放机制
            builtInZoomControls = true
            // 使用内置的缩放机制是否展示缩放控件
            displayZoomControls = false
            // 缓存模式
            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            // 视频相关
            allowFileAccess = true
            allowContentAccess = true
            mediaPlaybackRequiresUserGesture = false
        }
        // 通过设置WebViewClient来控制WebView的行为
        webViewClient = CustomWebViewClient()
        // 通过设置WebChromeClient来控制WebView的UI交互
        webChromeClient = CustomWebChromeClient()

    }

    inner class CustomWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            // 加载页面
            view?.loadUrl(request?.url.toString())
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            // 页面开始加载
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            // 页面加载完成
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            // 页面加载出错
        }
    }

    inner class CustomWebChromeClient : WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            // 页面加载进度变化,这里可以自定义WebView加载进度的显示方式
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            // 页面标题变化
        }

        override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
            // 页面图标变化
        }
    }
}