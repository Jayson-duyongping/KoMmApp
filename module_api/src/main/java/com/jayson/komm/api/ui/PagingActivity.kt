package com.jayson.komm.api.ui

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.jayson.komm.api.adapter.WaterFallAdapter
import com.jayson.komm.api.databinding.ActivityPagingBinding
import com.jayson.komm.api.model.PictureViewModel
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.ext.viewLifeCycleOwner
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PagingActivity : BaseActivity() {
    companion object {
        private const val TAG = "PagingActivity"
    }

    private lateinit var binding: ActivityPagingBinding

    private val viewModel: PictureViewModel by viewModels()
    private lateinit var adapter: WaterFallAdapter

    override fun initView() {
        super.initView()
        binding = ActivityPagingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = WaterFallAdapter(this)
        binding.apply {
            recyclerView.adapter = adapter
            searchBtn.setOnClickListener {
                viewModel.msg = searchEt.text.toString()
                // 通过ViewModel获取数据流
                viewLifeCycleOwner.lifecycleScope.launch {
                    viewModel.data.collectLatest {
                        adapter.submitData(it)
                    }
                }
            }
        }
    }
}