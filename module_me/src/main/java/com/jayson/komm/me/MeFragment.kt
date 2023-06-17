package com.jayson.komm.me

import android.view.View
import com.jayson.komm.common.base.BaseFragment
import com.jayson.komm.common.util.JumpUtils.startGoAction
import com.jayson.komm.me.databinding.FragmentMeBinding

class MeFragment : BaseFragment() {

    companion object {
        private const val ACTION_MVVM = "com.jayson.komm.mvvm.Main"
        private const val ACTION_MM = "com.jayson.komm.api.Main"
    }

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
            startGoAction(activity, ACTION_MVVM)
        }
        binding.apiBtn.setOnClickListener {
            startGoAction(activity, ACTION_MM)
        }
    }
}