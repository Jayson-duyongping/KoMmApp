package com.jayson.komm.common.ext

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

/**
 * 点击view缩放效果
 */
@SuppressLint("ClickableViewAccessibility")
fun View.addClickScale(scale: Float = 0.95f, duration: Long = 100, onClick: () -> Unit) {
    this.setOnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                this.animate().scaleX(scale).scaleY(scale).setDuration(duration).start()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                this.animate().scaleX(1f).scaleY(1f).setDuration(duration).start()
                onClick()
            }
        }
        true
    }
}
