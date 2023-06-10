package com.jayson.komm.mvvm.listener

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.jayson.komm.common.util.LogUtils

/**
 * @Author: Jayson
 * @CreateDate: 2023/3/18 13:31
 * @Version: 1.0
 * @Description:
 */
class MyLocationOwner(override val lifecycle: Lifecycle) : LifecycleOwner {

    companion object {
        private const val TAG = "MyLocationOwner"
    }

    fun create() {
        LogUtils.d(TAG, "create")
    }

    fun destroy() {
        LogUtils.d(TAG, "destroy")
    }
}