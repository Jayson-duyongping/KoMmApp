package com.jayson.komm.dev.view

import android.animation.*
import android.annotation.SuppressLint
import android.os.Handler
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.core.widget.NestedScrollView
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.dev.databinding.ActivityWebSlide2Binding


class WebSlide2Activity : BaseActivity() {

    companion object {
        private const val TAG = "WebSlide2Activity"

    }

    private var isScrolling = false
    private var isInfoFullyVisible = false
    private var isInfoFullyHidden = false

    private lateinit var binding: ActivityWebSlide2Binding

    override fun initView() {
        super.initView()
        // 初始化binding
        binding = ActivityWebSlide2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(){
        val webSettings: WebSettings = binding.webContentWb.settings
        webSettings.javaScriptEnabled = true
        binding.webContentWb.webViewClient = WebViewClient()
        binding.webContentWb.loadUrl("https://www.baidu.com")

        // 初始2秒后自动隐藏
        binding.webContentWb.postDelayed(::scrollToInitialPosition, 2000)

        // 为 NestedScrollView 添加滚动监听器
        binding.webScrollView.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, oldScrollY ->
            if (isScrolling) return@setOnScrollChangeListener

            val infoTextView = binding.webInfoTv
            val top = infoTextView.top
            val bottom = top + infoTextView.height

            when {
                scrollY < oldScrollY && !isInfoFullyVisible && scrollY < bottom -> {
                    // 下滑且信息栏未完全显示
                    scrollToShowFullInfo()
                }
                scrollY > oldScrollY && !isInfoFullyHidden && scrollY > top -> {
                    // 上滑且信息栏未完全隐藏
                    scrollToHideFullInfo()
                }
            }
        }
    }

    private fun scrollToInitialPosition() {
        val scrollView: NestedScrollView = binding.webScrollView
        val infoTextView: View = binding.webInfoTv

        // 计算要滚动的目标位置
        val targetScrollY = infoTextView.height + dpToPx(50)
        scrollView.smoothScrollTo(0, targetScrollY)
        isInfoFullyHidden = true
        isInfoFullyVisible = false
    }

    private fun scrollToShowFullInfo() {
        isScrolling = true
        val scrollView: NestedScrollView = binding.webScrollView
        val infoTextView: View = binding.webInfoTv
        val targetScrollY = infoTextView.top

        // 使用平滑滚动
        scrollView.smoothScrollTo(0, targetScrollY)

        // 滚动完成后恢复状态（使用 postDelayed 模拟滚动完成事件）
        Handler().postDelayed({
            isScrolling = false
            isInfoFullyVisible = true
            isInfoFullyHidden = false
        }, 250) // 与系统默认动画时长保持一致
    }

    private fun scrollToHideFullInfo() {
        isScrolling = true
        val scrollView: NestedScrollView = binding.webScrollView
        val infoTextView = binding.webInfoTv
        val targetScrollY = infoTextView.top + infoTextView.height

        // 使用平滑滚动
        scrollView.smoothScrollTo(0, targetScrollY)

        // 滚动完成后恢复状态
        Handler().postDelayed({
            isScrolling = false
            isInfoFullyHidden = true
            isInfoFullyVisible = false
        }, 250)
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}