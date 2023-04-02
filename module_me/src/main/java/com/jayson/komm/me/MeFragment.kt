package com.jayson.komm.me

import android.content.Intent
import android.view.View
import com.jayson.komm.common.base.BaseFragment
import com.jayson.komm.me.databinding.FragmentMeBinding

class MeFragment : BaseFragment() {

    private lateinit var binding: FragmentMeBinding

    override fun getLayoutRes(): Int {
        return R.layout.fragment_me
    }

    override fun initView(view: View) {
        super.initView(view)
        binding = FragmentMeBinding.bind(view)
        view.apply {
            postDelayed({
                binding.logoIv.visibility = View.GONE
                binding.contentLl.visibility = View.VISIBLE
            }, 500)
        }
        binding.mvvmBtn.setOnClickListener {
            val className = Class.forName("com.jayson.komm.mvvm.MvvmActivity")
            //val intent = Intent(activity,className)
            val intent = Intent("com.jayson.komm.mvvm.Main")
            activity?.startActivity(intent)
        }
    }
}