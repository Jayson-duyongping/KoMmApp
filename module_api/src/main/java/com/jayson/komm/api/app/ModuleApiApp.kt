package com.jayson.komm.api.app

import android.content.Context
import com.jayson.komm.api.db.ApiDatabase

class ModuleApiApp(applicationContext: Context) {

    private val context = applicationContext

    // 在这里可以初始化模块独有的组件和资源，在宿主中调用
    fun setUp() {
        // 初始化操作
        ApiDatabase.initDB(context)
    }
}
