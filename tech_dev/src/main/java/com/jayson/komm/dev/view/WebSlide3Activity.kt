package com.jayson.komm.dev.view

import android.animation.*
import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.dev.R
import com.jayson.komm.dev.databinding.ActivityWebSlide3Binding


class WebSlide3Activity : BaseActivity() {

    companion object {
        private const val TAG = "WebSlide3Activity"

    }

    private lateinit var binding: ActivityWebSlide3Binding

    override fun initView() {
        super.initView()
        // 初始化binding
        binding = ActivityWebSlide3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // 确保Toolbar在最上层（XML中应最后声明）
        findViewById<View>(R.id.tool_bar).apply {
            bringToFront() // 确保位于视图最上层
        }

        initWebView()
        initListener()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        val webSettings: WebSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl("https://www.baidu.com")
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}