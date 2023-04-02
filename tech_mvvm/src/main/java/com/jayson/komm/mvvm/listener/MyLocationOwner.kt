package com.jayson.komm.mvvm.listener

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.jayson.komm.common.util.LogUtils

/**
 * @Author: Jayson
 * @CreateDate: 2023/3/18 13:31
 * @Version: 1.0
 * @Description:
 */
class MyLocationOwner : LifecycleOwner {

    companion object {
        private const val TAG = "MyLocationOwner"
    }

    private var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    fun create() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        LogUtils.d(TAG, "create")
    }

    fun destroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        LogUtils.d(TAG, "destroy")
    }
}