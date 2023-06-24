package com.jayson.komm.common.util

import android.annotation.SuppressLint
import android.content.Context
import com.jayson.komm.common.R

object ResourceUtil {

    /**
     * 获取通用色彩搭配
     */
    @SuppressLint("Recycle")
    @JvmStatic
    fun getCommonColors(context: Context): List<Int> {
        val colorList = mutableListOf<Int>()
        val colorArray =
            context.resources.obtainTypedArray(R.array.card_colors)
        for (i in 0 until colorArray.length()) {
            val color = colorArray.getColor(i, 0)
            colorList.add(color)
        }
        return colorList
    }
}