package com.jayson.komm.api.ui

import androidx.lifecycle.lifecycleScope
import com.jayson.komm.api.adapter.MmAdapter
import com.jayson.komm.api.databinding.ActivityMmBinding
import com.jayson.komm.api.repo.MmHttpService
import com.jayson.komm.common.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MmActivity : BaseActivity() {

    companion object {
        private const val TAG = "MmActivity"
    }

    private lateinit var binding: ActivityMmBinding
    private lateinit var adapter: MmAdapter

    override fun initView() {
        super.initView()
        // 初始化binding
        binding = ActivityMmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupSwipeRefreshLayout()
    }

    override fun initData() {
        super.initData()
        loadDada {}
    }

    private fun setupRecyclerView() {
        adapter = MmAdapter(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupSwipeRefreshLayout() {
        // 下拉刷新的实现
        binding.swipeRefreshLayout.setOnRefreshListener {
            // 设置为正在刷新状态
            adapter.isRefreshing = true
            loadDada {
                // 刷新完成后设置为非刷新状态
                adapter.isRefreshing = false
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun loadDada(block: () -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            // 网络请求获取数据
            val mmList = MmHttpService.getMmList()
            val data = mmList?.toMutableList()
            withContext(Dispatchers.Main) {
                data?.let {
                    adapter.setData(it)
                }
                block()
            }
        }
    }
}