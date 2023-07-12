package com.jayson.komm.api

import android.content.Intent
import com.jayson.komm.api.databinding.ActivityApiBinding
import com.jayson.komm.api.ui.ListActivity
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
            startGoActivity(this, Intent(this, WebActivity::class.java))
        }
        binding.waterFlowBtn.setOnClickListener {
            startGoActivity(this, Intent(this, MmActivity::class.java))
        }
        binding.pagingBtn.setOnClickListener {
            startGoActivity(this, Intent(this, PagingActivity::class.java))
        }
        binding.newsBtn.setOnClickListener {
            startGoActivity(this, Intent(this, ListActivity::class.java))
        }
    }
}