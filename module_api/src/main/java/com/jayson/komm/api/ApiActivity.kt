package com.jayson.komm.api

import android.content.Intent
import com.jayson.komm.api.databinding.ActivityApiBinding
import com.jayson.komm.api.view.*
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
        binding.filmApiBtn.setOnClickListener {
            startGoActivity(this, Intent(this, FilmActivity::class.java))
        }
        binding.pythonBtn.setOnClickListener {
            startGoActivity(this, Intent(this, PythonActivity::class.java))
        }
        binding.ndkBtn.setOnClickListener {
            startGoActivity(this, Intent(this, NDKActivity::class.java))
        }
    }
}