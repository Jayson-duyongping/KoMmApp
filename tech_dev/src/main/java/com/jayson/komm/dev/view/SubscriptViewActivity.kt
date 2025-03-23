package com.jayson.komm.dev.view

import android.annotation.SuppressLint
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.dev.databinding.ActivitySubscriptBinding


class SubscriptViewActivity : BaseActivity() {

    companion object {
        private const val TAG = "SubscriptViewActivity"

    }
    private lateinit var binding: ActivitySubscriptBinding


    @SuppressLint("RestrictedApi")
    override fun initView() {
        super.initView()
        // 初始化binding
        binding = ActivitySubscriptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        binding.increaseHeightBtn.setOnClickListener {
            val layoutParams = binding.toolbar.layoutParams
            layoutParams.height += dpToPx(50)
            binding.toolbar.layoutParams = layoutParams
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}