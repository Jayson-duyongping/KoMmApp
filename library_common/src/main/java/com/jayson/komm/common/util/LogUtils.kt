package com.jayson.komm.common.util

import android.util.Log

/**
 * @Author: Jayson
 * @CreateDate: 2022/12/11 21:57
 * @Version: 1.0
 * @Description: LogUtils日志类
 */
object LogUtils {
    private const val isDebug: Boolean = true
    private const val BASE_TAG = "KoMm_"

    fun d(tag: String, msg: String) {
        if (isDebug) {
            Log.d(BASE_TAG + tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (isDebug) {
            Log.e(BASE_TAG + tag, msg)
        }
    }

    fun v(tag: String, msg: String) {
        if (isDebug) {
            Log.v(BASE_TAG + tag, msg)
        }
    }
}