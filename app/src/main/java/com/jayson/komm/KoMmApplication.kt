package com.jayson.komm

import android.app.Application
import com.jayson.komm.api.app.ModuleApiApp

class KoMmApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化不同的模块Application
        ModuleApiApp(this).setUp()
    }
}