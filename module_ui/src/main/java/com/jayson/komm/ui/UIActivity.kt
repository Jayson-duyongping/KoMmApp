package com.jayson.komm.ui

import android.content.Intent
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.util.JumpUtils
import com.jayson.komm.ui.databinding.ActivityUiBinding
import com.jayson.komm.ui.image.ImageActivity
import com.jayson.komm.ui.viewpager2.ViewPager2Activity

class UIActivity : BaseActivity() {

    companion object {
        private const val TAG = "UIActivity"
    }

    private lateinit var binding: ActivityUiBinding

    override fun initView() {
        super.initView()
        // 初始化binding
        binding = ActivityUiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewpager2Btn.setOnClickListener {
            JumpUtils.startGoActivity(this, Intent(this, ViewPager2Activity::class.java))
        }
        binding.imageBtn.setOnClickListener {
            JumpUtils.startGoActivity(this, Intent(this, ImageActivity::class.java))
        }
    }
}