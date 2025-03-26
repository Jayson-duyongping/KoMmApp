package com.jayson.komm.dev.view.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import com.jayson.komm.dev.R

class WebViewBehavior(
    context: Context,
    attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<NestedScrollView>(context, attrs) {

    private var initialHeight = 0
    private var lastTranslationY = 0f

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: NestedScrollView,
        dependency: View
    ): Boolean {
        // 依赖 Header（假设 Header 的 ID 是 R.id.header）
        return dependency.id == R.id.header
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: NestedScrollView,
        dependency: View
    ): Boolean {
        val translationY = dependency.height + dependency.translationY
        if (translationY == lastTranslationY) return false // 避免重复计算

        lastTranslationY = translationY
        child.translationY = translationY

        // 调整WebView的高度以填充剩余空间
        if (initialHeight == 0) {
            initialHeight = parent.height - dependency.height
        }
        // 调整WebView的位置
        val newHeight = initialHeight - dependency.translationY.toInt()
        child.layoutParams.height = newHeight
        child.requestLayout()

        return true
    }
}