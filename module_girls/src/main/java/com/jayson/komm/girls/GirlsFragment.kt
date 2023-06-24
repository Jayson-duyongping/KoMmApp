package com.jayson.komm.girls

import android.view.View
import com.jayson.komm.common.base.BaseFragment
import com.jayson.komm.girls.databinding.FragmentGirlsBinding

class GirlsFragment : BaseFragment() {

    private lateinit var binding: FragmentGirlsBinding
    private val dataList = listOf("Page 1", "Page 2", "Page 3", "Page 4", "Page 5")

    override fun getLayoutRes(): Int {
        return R.layout.fragment_girls
    }

    override fun initView(view: View) {
        super.initView(view)
        binding = FragmentGirlsBinding.bind(view)
        binding.verticalPv.initVerticalPager(dataList)
    }
}