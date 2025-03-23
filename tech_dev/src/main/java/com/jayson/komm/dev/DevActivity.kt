package com.jayson.komm.dev

import android.content.Intent
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.util.JumpUtils
import com.jayson.komm.dev.databinding.ActivityDevBinding
import com.jayson.komm.dev.view.*

class DevActivity : BaseActivity() {

    companion object {
        private const val TAG = "DevActivity"
    }

    private lateinit var binding: ActivityDevBinding

    override fun initView() {
        super.initView()
        // 初始化binding
        binding = ActivityDevBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.serviceBtn.setOnClickListener {
            JumpUtils.startGoActivity(this, Intent(this, ServiceActivity::class.java))
        }
        binding.animBtn.setOnClickListener {
            JumpUtils.startGoActivity(this, Intent(this, AnimActivity::class.java))
        }
        binding.webSlideBtn.setOnClickListener {
            JumpUtils.startGoActivity(this, Intent(this, WebSlideActivity::class.java))
        }
        binding.webSlide2Btn.setOnClickListener {
            JumpUtils.startGoActivity(this, Intent(this, WebSlide2Activity::class.java))
        }
        binding.webSlide3Btn.setOnClickListener {
            JumpUtils.startGoActivity(this, Intent(this, WebSlide3Activity::class.java))
        }
        binding.subscriptBtn.setOnClickListener {
            JumpUtils.startGoActivity(this, Intent(this, SubscriptViewActivity::class.java))
        }

        binding.webHeaderBtn.setOnClickListener {
            JumpUtils.startGoActivity(this, Intent(this, WebHeaderActivity::class.java))
        }
    }
}