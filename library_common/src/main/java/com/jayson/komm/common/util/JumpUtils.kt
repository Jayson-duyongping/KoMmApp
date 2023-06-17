package com.jayson.komm.common.util

import android.app.Activity
import android.content.Intent
import com.jayson.komm.common.R

/**
 * @Author: Jayson
 * @CreateDate: 2023/4/8 19:09
 * @Version: 1.0
 * @Description:
 */
object JumpUtils {

    private const val TAG = "JumpUtils"

    fun startGoAction(activity: Activity?, action: String) {
        kotlin.runCatching {
            activity?.apply {
                startActivity(Intent(action))
                overridePendingTransition(R.anim.slide_enter_in_right, R.anim.slide_enter_out_left)
            }
        }.onFailure {
            LogUtils.d(TAG, "startGoAction, e:${it.message}")
        }
    }

    fun startGoActivity(activity: Activity?, cls: Class<*>) {
        kotlin.runCatching {
            activity?.apply {
                startActivity(Intent(activity, cls))
                overridePendingTransition(R.anim.slide_enter_in_right, R.anim.slide_enter_out_left)
            }
        }.onFailure {
            LogUtils.d(TAG, "startGoActivity, e:${it.message}")
        }
    }
}