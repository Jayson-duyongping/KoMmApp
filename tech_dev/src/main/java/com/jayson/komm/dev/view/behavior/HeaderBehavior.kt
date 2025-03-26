package com.jayson.komm.dev.view.behavior

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import kotlin.math.abs

class HeaderBehavior(
    context: Context,
    attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<View>(context, attrs) {

    private var initialHeight = 0
    // ValueAnimator（更轻量）
    private var animator: ValueAnimator? = null

    // 新增用户松开时判断header的显示
    private var isScrollingUp = false
    private var lastDy = 0

    // 新增动画状态锁
    private var isAnimating = false

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return !isAnimating && axes == ViewCompat.SCROLL_AXIS_VERTICAL // 动画期间禁用滑动
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (initialHeight == 0) {
            initialHeight = child.height
        }

        // 快速滑动时禁用动画
        if (abs(dy) > 20) { // 速度阈值
            animator?.cancel()
        }

        // 记录滑动方向
        lastDy = dy
        isScrollingUp = dy > 0

        // 仅在 WebView 滚动到顶部且下拉时（dy < 0）才展开 Header
        if (dy < 0 && target.canScrollVertically(-1).not()) {
            val newTranslationY = child.translationY - dy
            child.translationY = newTranslationY.coerceAtMost(0f)
            consumed[1] = dy // 消耗下拉事件
        }
        // 上滑时直接折叠 Header（原逻辑）
        else if (dy > 0 && child.translationY > -initialHeight) {
            val newTranslationY = child.translationY - dy
            child.translationY = newTranslationY.coerceAtLeast(-initialHeight.toFloat())
            consumed[1] = dy
        }
    }

    // 新增方法：处理滑动停止时的平滑动画
    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)

        // 如果Header部分显示（既不是完全显示也不是完全隐藏）
        if (child.translationY < 0 && child.translationY > -initialHeight) {
            val threshold = initialHeight * 0.5f // 50%作为阈值

            if (isScrollingUp) {
                // 向上滑动且松手 - 如果显示部分小于阈值，完全隐藏
                if (abs(child.translationY) > threshold) {
                    smoothScrollTo(child, -initialHeight.toFloat())
                } else {
                    smoothScrollTo(child, 0f)
                }
            } else {
                // 向下滑动且松手 - 如果隐藏部分小于阈值，完全显示
                if (abs(child.translationY) < threshold) {
                    smoothScrollTo(child, 0f)
                } else {
                    smoothScrollTo(child, -initialHeight.toFloat())
                }
            }
        }
    }

    // 平滑滚动到指定位置
    private fun smoothScrollTo(child: View, targetY: Float) {
        if (isAnimating) return // 防止重复触发
        animator?.cancel()
        isAnimating = true // 加锁
        animator = ValueAnimator.ofFloat(child.translationY, targetY).apply {
            duration = 250 // 缩短动画时间（250ms）
            interpolator = DecelerateInterpolator() // 减速曲线更自然
            addUpdateListener { animation ->
                child.translationY = animation.animatedValue as Float
                child.postInvalidateOnAnimation() // 使用垂直同步信号刷新
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator) {
                    isAnimating = false // 确保异常时解锁
                }
                override fun onAnimationEnd(animation: Animator) {
                    child.translationY = targetY // 确保最终位置准确
                    isAnimating = false // 解锁
                }
            })
            start()
        }
    }

    // 平滑滚动方法
    fun smoothScroll(child: View, dy: Int) {
        val targetY = child.translationY + dy
        smoothScrollTo(child, targetY)
    }

    // 平滑滚动方法 - 确保不会滚动超过Header自身高度
    fun smoothScrollAtMostHeight(child: View, dy: Int) {
        val targetY = child.translationY + dy
        // 确保不会滚动超过Header自身高度
        val finalY = targetY.coerceAtLeast(-child.height.toFloat())
        smoothScrollTo(child, finalY)
    }
}