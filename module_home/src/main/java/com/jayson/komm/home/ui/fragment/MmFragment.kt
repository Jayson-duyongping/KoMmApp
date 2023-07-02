package com.jayson.komm.home.ui.fragment

import android.view.View
import com.jayson.komm.common.base.BaseFragment
import com.jayson.komm.common.ext.replaceFragment
import com.jayson.komm.home.R
import com.jayson.komm.home.databinding.FragmentMmBinding

class MmFragment : BaseFragment() {

    private lateinit var binding: FragmentMmBinding

    override fun getLayoutRes(): Int {
        return R.layout.fragment_mm
    }

    override fun initView(view: View) {
        super.initView(view)
        binding = FragmentMmBinding.bind(view)
        replaceFragment(R.id.mm_container, MmListFrag())
    }
}