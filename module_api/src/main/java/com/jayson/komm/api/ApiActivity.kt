package com.jayson.komm.api

import com.jayson.komm.api.databinding.ActivityApiBinding
import com.jayson.komm.api.ui.MmActivity
import com.jayson.komm.api.ui.PagingActivity
import com.jayson.komm.api.ui.WebActivity
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.util.JumpUtils.startGoActivity

class ApiActivity : BaseActivity() {

    private lateinit var binding: ActivityApiBinding

    override fun initView() {
        super.initView()
        // 初始化binding
        binding = ActivityApiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webBtn.setOnClickListener {
            startGoActivity(this, WebActivity::class.java)
        }
        binding.waterFlowBtn.setOnClickListener {
            startGoActivity(this, MmActivity::class.java)
        }
        binding.pagingBtn.setOnClickListener {
            startGoActivity(this, PagingActivity::class.java)
        }
    }
}