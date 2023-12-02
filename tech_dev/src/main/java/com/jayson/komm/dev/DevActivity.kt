package com.jayson.komm.dev

import android.content.Intent
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.util.JumpUtils
import com.jayson.komm.dev.databinding.ActivityDevBinding
import com.jayson.komm.dev.view.AnimActivity
import com.jayson.komm.dev.view.ServiceActivity

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
    }
}