package com.jayson.komm.dev.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.core.widget.NestedScrollView
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.dev.databinding.ActivityWebSlideBinding


class WebSlideActivity : BaseActivity() {

    companion object {
        private const val TAG = "WebSlideActivity"
    }

    private lateinit var binding: ActivityWebSlideBinding

    private var currentAnimator: ObjectAnimator? = null
    private var isScrolling = false
    private var isInfoFullyVisible = false
    private var isInfoFullyHidden = false

    override fun initView() {
        super.initView()
        // 初始化binding
        binding = ActivityWebSlideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(){
        val webSettings: WebSettings = binding.webContentWb.settings
        webSettings.javaScriptEnabled = true
        binding.webContentWb.webViewClient = WebViewClient()
        binding.webContentWb.loadUrl("https://www.baidu.com")

        // 2 秒后隐藏信息栏和 WebView 顶部 50dp
        Handler(Looper.getMainLooper()).postDelayed({
            scrollToInitialPosition()
        }, 2000)


        // 为 NestedScrollView 添加滚动监听器
        binding.webScrollView.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, oldScrollY ->
            if (isScrolling) return@setOnScrollChangeListener

            val infoTextView = binding.webInfoTv
            val top = infoTextView.top
            val bottom = top + infoTextView.height

            when {
                scrollY < oldScrollY && !isInfoFullyVisible && scrollY < bottom -> {
                    // 下滑且信息栏未完全显示
                    cancelCurrentAnimation()
                    scrollToShowFullInfo()
                }
                scrollY > oldScrollY && !isInfoFullyHidden && scrollY > top -> {
                    // 上滑且信息栏未完全隐藏
                    cancelCurrentAnimation()
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

        // 创建平滑滚动的动画
        startScrollAnimation(scrollView, scrollView.scrollY, targetScrollY, 500) {
            isInfoFullyHidden = true
            isInfoFullyVisible = false
        }
    }

    private fun scrollToShowFullInfo() {
        val scrollView: NestedScrollView = binding.webScrollView
        val infoTextView: View = binding.webInfoTv
        val targetScrollY = infoTextView.top

        // 创建平滑滚动的动画
        startScrollAnimation(scrollView, scrollView.scrollY, targetScrollY, 300) {
            isInfoFullyVisible = true
            isInfoFullyHidden = false
        }
    }

    private fun scrollToHideFullInfo() {
        val scrollView: NestedScrollView = binding.webScrollView
        val infoTextView = binding.webInfoTv
        val targetScrollY = infoTextView.top + infoTextView.height

        // 创建平滑滚动的动画
        startScrollAnimation(scrollView, scrollView.scrollY, targetScrollY, 300) {
            isInfoFullyHidden = true
            isInfoFullyVisible = false
        }
    }

    private fun startScrollAnimation(
        scrollView: NestedScrollView,
        startY: Int,
        endY: Int,
        duration: Long,
        onAnimationEnd: () -> Unit
    ) {
        isScrolling = true
        currentAnimator = ObjectAnimator.ofInt(scrollView, "scrollY", startY, endY)
        currentAnimator?.apply {
            this.duration = duration
            interpolator = DecelerateInterpolator()
            addListener(object : android.animation.Animator.AnimatorListener {
                override fun onAnimationStart(animation: android.animation.Animator) {}
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    isScrolling = false
                    onAnimationEnd()
                }
                override fun onAnimationCancel(animation: android.animation.Animator) {
                    isScrolling = false
                }
                override fun onAnimationRepeat(animation: android.animation.Animator) {}
            })
            start()
        }
    }

    private fun cancelCurrentAnimation() {
        currentAnimator?.cancel()
        currentAnimator = null
    }


    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}