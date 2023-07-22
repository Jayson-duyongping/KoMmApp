package com.jayson.komm.dev.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jayson.komm.common.util.LogUtils

class DevBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "DevBroadcastReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // 打印日志
        LogUtils.d(TAG, "onReceive")
    }
}