package com.jayson.komm.common.util

object ClickUtils {

    private const val MIN_CLICK_DELAY_TIME = 500
    private var lastClickTime: Long = 0

    fun isFastClick(): Boolean {
        val currentTime = System.currentTimeMillis()
        val flag = (currentTime - lastClickTime <= MIN_CLICK_DELAY_TIME)
        lastClickTime = currentTime
        return flag
    }
}