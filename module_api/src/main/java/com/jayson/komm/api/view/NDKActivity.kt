package com.jayson.komm.api.view

import android.os.Bundle
import com.jayson.komm.api.databinding.ActivityNdkBinding
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.util.LogUtils

class NDKActivity : BaseActivity() {

    // 声明native方法
    private external fun stringFromJNI(): String

    companion object {
        private const val TAG = "NDKActivity"

        init {
            // 静态加载native库
            System.loadLibrary("native")
        }
    }

    private lateinit var binding: ActivityNdkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNdkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.callNativeBtn.setOnClickListener {
            val nativeStr = stringFromJNI()
            LogUtils.d(TAG, "onCreate, nativeStr:$nativeStr")
        }
    }
}