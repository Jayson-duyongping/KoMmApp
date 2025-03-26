package com.jayson.komm.dev.view

import android.animation.*
import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.dev.databinding.ActivityWebSlideBehavior2Binding
import com.jayson.komm.dev.view.behavior.HeaderBehavior


class WebSlide3Activity : BaseActivity() {

    companion object {
        private const val TAG = "WebSlide3Activity"

    }

    private lateinit var binding: ActivityWebSlideBehavior2Binding

    override fun initView() {
        super.initView()
        // 初始化binding
        binding = ActivityWebSlideBehavior2Binding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.webView.postDelayed({
            autoScrollViews()
        },2000)
    }

    private fun autoScrollViews() {
        val params = binding.header.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as? HeaderBehavior
        val scrollTargetHeight = binding.header.height + 100.dpToPx(this)

        // WebView会自动跟随Header移动，因为WebViewBehavior已经实现了依赖关系
        behavior?.smoothScroll(binding.header, -scrollTargetHeight)

    }

    // dp 转 px 扩展函数
    fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}