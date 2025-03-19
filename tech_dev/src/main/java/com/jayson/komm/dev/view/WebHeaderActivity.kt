package com.jayson.komm.dev.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.MotionEvent
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.util.LogUtils


class WebHeaderActivity : BaseActivity() {

    companion object {
        private const val TAG = "WebHeaderActivity"
        private const val HIDE_DELAY = 2000L // 2 秒延迟
    }

    private lateinit var headerView: LinearLayout
    private var isHeaderVisible = true
    private var lastY = 0f
    private var currentAnimator: ValueAnimator? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        super.initView()

        // 创建垂直的 LinearLayout 作为根布局
        val rootLayout = LinearLayout(this)
        rootLayout.orientation = LinearLayout.VERTICAL
        setContentView(rootLayout)

        // 创建 WebView
        val webView = WebView(this)
        webView.webViewClient = WebViewClient()

        // 为 WebView 设置布局参数
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        webView.layoutParams = layoutParams

        // 创建 HeaderView
        headerView = createHeaderView()
        webView.addView(headerView)

        // 加载 URL
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        // 设置 WebViewClient，使得页面在 WebView 内打开
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // 注入 padding-top 样式
                val paddingTop = 40 // 可根据需求调整这个值
                LogUtils.d(TAG, "onPageFinished, paddingTop:$paddingTop")
                view?.loadUrl(
                    "javascript:(function() {" +
                        "document.body.style.paddingTop = '" + paddingTop + "px';" +
                        "})()"
                )
            }
        }
        webView.loadUrl("file:///android_asset/sample.html")

        // 将 WebView 添加到根布局
        rootLayout.addView(webView)

        // 2 秒后隐藏 HeaderView
        webView.postDelayed({
            if (isHeaderVisible) {
                smoothScrollWebView(
                    webView,
                    0,
                    webView.scrollY,
                    0,
                    webView.scrollY + headerView.height
                )
                isHeaderVisible = false
            }
        }, HIDE_DELAY)

        webView.viewTreeObserver.addOnScrollChangedListener {
            checkHeaderVisibility()
        }

        // 添加触摸事件监听器
        webView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastY = event.y
                }
                MotionEvent.ACTION_UP -> {
                    val deltaY = event.y - lastY
                    LogUtils.d(TAG, "setOnTouchListener,deltaY：$deltaY")
                    if (deltaY < 0) { // 上滑
                        if (isHeaderVisible) {
                            LogUtils.d(TAG, "setOnTouchListener,ACTION_UP 上滑隐藏")
                            // 可以定个阈值,超过50可多滑动，不超过则最大只能是headerView.height
                            val finalTargetY = if(Math.abs(deltaY.toInt()) > 50){
                                webView.scrollY + headerView.height
                            }else{
                                headerView.height
                            }
                            smoothScrollWebView(webView, 0, webView.scrollY, 0, finalTargetY)
                            isHeaderVisible = false
                        }
                    } else if (deltaY > 0) { // 下滑
                        if (isHeaderVisible) {
                            LogUtils.d(TAG, "setOnTouchListener,ACTION_UP 下滑显示")
                            val targetY = webView.scrollY - headerView.height
                            // 确保滚动不会超出边界
                            val finalTargetY = if (targetY < 0) 0 else targetY
                            smoothScrollWebView(webView, 0, webView.scrollY, 0, finalTargetY)
                            isHeaderVisible = true
                        }
                    }
                }
            }
            false
        }
    }

    private fun checkHeaderVisibility() {
        val rect = Rect()
        val isVisible = headerView.getGlobalVisibleRect(rect)
        isHeaderVisible = isVisible
        LogUtils.d(TAG, "HeaderView $isVisible visible on the screen.")
    }

    private fun createHeaderView(): LinearLayout {
        val headerLayout = LinearLayout(this)
        headerLayout.orientation = LinearLayout.HORIZONTAL
        headerLayout.setBackgroundColor(resources.getColor(android.R.color.holo_blue_dark))

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(50)
        )
        headerLayout.layoutParams = layoutParams

        return headerLayout
    }


    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun smoothScrollWebView(
        webView: WebView,
        startX: Int,
        startY: Int,
        endX: Int,
        endY: Int
    ) {
        currentAnimator?.cancel() // 取消之前的动画

        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 300 // 动画时长，可根据需要调整
        animator.addUpdateListener { animation ->
            val fraction = animation.animatedFraction
            val currentX = (startX + fraction * (endX - startX)).toInt()
            val currentY = (startY + fraction * (endY - startY)).toInt()
            webView.scrollTo(currentX, currentY)
        }
        animator.start()
        currentAnimator = animator
    }
}