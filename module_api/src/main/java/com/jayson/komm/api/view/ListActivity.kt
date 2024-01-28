package com.jayson.komm.api.view

import com.jayson.komm.api.R
import com.jayson.komm.api.databinding.ActivityListBinding
import com.jayson.komm.api.view.fragment.ListFragment
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.ext.replaceFragment

class ListActivity : BaseActivity() {

    private lateinit var binding: ActivityListBinding

    override fun initView() {
        super.initView()
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFragment()
    }

    private fun setFragment() {
        replaceFragment(R.id.fragment_container, ListFragment())
    }
}