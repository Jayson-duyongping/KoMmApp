package com.jayson.komm.dev.view

import android.view.View
import android.view.ViewGroup
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.util.LogUtils
import com.jayson.komm.dev.databinding.ActivityAnimBinding
import com.jayson.komm.dev.utils.AnimatorUtils

class AnimActivity : BaseActivity() {

    companion object {
        private const val TAG = "AnimActivity"
    }

    private lateinit var binding: ActivityAnimBinding

    private var aCardParams: ViewGroup.LayoutParams? = null
    private var aCardFixedHeight: Int = 0

    override fun initView() {
        super.initView()
        // 初始化binding
        binding = ActivityAnimBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animationGo()
    }

    private fun animationGo() {
        aCardParams = binding.aCard.layoutParams
        aCardFixedHeight = if (aCardParams?.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            // 高度是wrap_content
            binding.aCard.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            binding.aCard.measuredHeight ?: 0
        } else {
            // 高度不是wrap_content
            binding.aCard.layoutParams?.height ?: 0
        }
        LogUtils.d(TAG, "animationGo, aCardFixedHeight: $aCardFixedHeight")

        binding.showBtn.setOnClickListener {
            LogUtils.d(TAG, "animationGo, show_aCardFixedHeight: $aCardFixedHeight")
            AnimatorUtils.showCardSelfByObjectAnimator(
                binding.aCard,
                aCardFixedHeight,
                onAnimationStart = {},
                onValueAnimator = {}
            )
        }
        binding.hideBtn.setOnClickListener {
            LogUtils.d(TAG, "animationGo, hide_aCardFixedHeight: $aCardFixedHeight")
            AnimatorUtils.hideCardSelfByObjectAnimator(
                binding.aCard,
                aCardFixedHeight,
                onAnimationEnd = {},
                onValueAnimator = {})
        }
    }
}