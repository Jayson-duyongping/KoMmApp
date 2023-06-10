package com.jayson.komm.common.util

import android.content.Context
import android.content.Intent

/**
 * @Author: Jayson
 * @CreateDate: 2023/4/8 19:09
 * @Version: 1.0
 * @Description:
 */
object JumpUtils {

    private const val TAG = "JumpUtils"

    fun startGoAction(context: Context?, action: String) {
        kotlin.runCatching {
            val intent = Intent(action)
            context?.startActivity(intent)
        }.onFailure {
            LogUtils.d(TAG, "startGoAction, e:${it.message}")
        }
    }

    fun startGoActivity(context: Context?, cls: Class<*>) {
        kotlin.runCatching {
            val intent = Intent(context, cls)
            context?.startActivity(intent)
        }.onFailure {
            LogUtils.d(TAG, "startGoActivity, e:${it.message}")
        }
    }
}