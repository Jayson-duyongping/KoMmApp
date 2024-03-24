package com.jayson.komm.ui.viewpager2.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ZoomOutPageTransformer : ViewPager2.PageTransformer {
    companion object {
        private const val MIN_SCALE = 0.9f
    }

    override fun transformPage(page: View, position: Float) {
        if (position < -1) { // 左侧非焦点页
            page.alpha = 0f
        } else if (position <= 0) { // 当前焦点页
            page.alpha = 1f
            page.scaleX = 1f
            page.scaleY = 1f
        } else if (position <= 1) { // 右侧非焦点页
            page.alpha = 1 - position
            page.scaleX = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position))
            page.scaleY = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position))
        } else { // 右侧超出的页
            page.alpha = 0f
        }
    }
}