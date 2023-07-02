package com.jayson.komm.home

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.base.BaseFragment
import com.jayson.komm.home.databinding.FragmentHomeBinding
import com.jayson.komm.home.ui.fragment.GameFragment
import com.jayson.komm.home.ui.fragment.MmFragment
import com.jayson.komm.home.ui.fragment.RecommendFragment

class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private val tabTitles = listOf("推荐", "美女", "游戏", "动漫", "文学")
    private val fragments =
        listOf<Fragment>(RecommendFragment(), MmFragment(), GameFragment())

    override fun getLayoutRes(): Int {
        return R.layout.fragment_home
    }

    override fun initView(view: View) {
        super.initView(view)
        binding = FragmentHomeBinding.bind(view)
        (activity as BaseActivity).setPaddingStatusBar(view)
        setupWithTabLayout(binding.tabLayout, binding.viewPager)
    }

    private fun setupWithTabLayout(tabLayout: TabLayout, viewPager2: ViewPager2) {
        // 设置ViewPager2的适配器
        binding.viewPager.apply {
            adapter = activity?.let { MyPagerAdapter(it) }
            // 设置ViewPager2为水平方向可滑动
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            // 预加载3
            offscreenPageLimit = 3
            // 禁止viewpager2左右滑动
            // isUserInputEnabled = false
        }
        // 添加TabLayout和ViewPager2的联动效果
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = tabTitles[position]
            viewPager2.setCurrentItem(tab.position, true)
        }.attach()
    }

    inner class MyPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
}