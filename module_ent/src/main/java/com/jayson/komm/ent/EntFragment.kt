package com.jayson.komm.ent

import android.view.View
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.base.BaseFragment
import com.jayson.komm.ent.databinding.FragmentEntBinding

class EntFragment : BaseFragment() {

    private lateinit var binding: FragmentEntBinding
    private val dataList = listOf("Page 1", "Page 2", "Page 3", "Page 4", "Page 5")

    override fun getLayoutRes(): Int {
        return R.layout.fragment_ent
    }

    override fun initView(view: View) {
        super.initView(view)
        binding = FragmentEntBinding.bind(view)
        (activity as BaseActivity).setPaddingStatusBar(view)
        binding.verticalPv.initVerticalPager(dataList)
    }
}