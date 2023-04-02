package com.jayson.komm.mvvm.listener

import android.content.Context
import android.location.Location
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.jayson.komm.common.util.LogUtils

/**
 * @Author: Jayson
 * @CreateDate: 2023/3/18 13:08
 * @Version: 1.0
 * @Description:
 */
class MyLocationObserver(
    private val context: Context,
    private val lifecycle: Lifecycle,
    private val callback: (Location) -> Unit
) : DefaultLifecycleObserver {

    companion object {
        private const val TAG = "MyLocationObserver"
    }

    private var enabled = false

    override fun onCreate(owner: LifecycleOwner) {
        LogUtils.d(TAG, "onCreate")
    }

    override fun onStart(owner: LifecycleOwner) {
        LogUtils.d(TAG, "onStart")
    }

    override fun onStop(owner: LifecycleOwner) {
        LogUtils.d(TAG, "onStop")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        LogUtils.d(TAG, "onDestroy")
    }

    fun enable() {
        enabled = true
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            LogUtils.d(TAG, "connect if not connected")
        }
    }

}