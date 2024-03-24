package com.jayson.komm.ui.viewpager2

import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.ui.databinding.ActivityViewpager2Binding
import com.jayson.komm.ui.viewpager2.adapter.PageAdapter
import com.jayson.komm.ui.viewpager2.transformer.ZoomOutPageTransformer

class ViewPager2Activity : BaseActivity() {

    companion object {
        private const val TAG = "ViewPager2Activity"
    }

    private lateinit var binding: ActivityViewpager2Binding

    private var pageList = mutableListOf("1", "2", "3", "4", "5", "6")

    override fun initView() {
        super.initView()
        // 初始化binding
        binding = ActivityViewpager2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager2.apply {
            setPageTransformer(ZoomOutPageTransformer())
            adapter = PageAdapter(pageList)
        }
    }
}