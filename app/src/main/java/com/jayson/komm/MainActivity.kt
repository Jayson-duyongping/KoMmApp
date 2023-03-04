package com.jayson.komm

import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.databinding.ActivityMainBinding
import com.jayson.komm.girls.GirlsFragment
import com.jayson.komm.home.HomeFragment
import com.jayson.komm.me.MeFragment
import com.jayson.komm.ui.adapter.MainViewPagerAdapter

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val fragmentList: List<Fragment> by lazy {
        listOf(HomeFragment(), GirlsFragment(), MeFragment())
    }

    private val mainVpAdapter: MainViewPagerAdapter by lazy {
        MainViewPagerAdapter(
            this,
            fragmentList
        )
    }

    private val pagerChangeCallback: OnPageChangeCallback by lazy {
        object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> binding.mainBottomNav.selectedItemId = R.id.home_item
                    1 -> binding.mainBottomNav.selectedItemId = R.id.girls_item
                    2 -> binding.mainBottomNav.selectedItemId = R.id.me_item
                }
            }
        }
    }

    override fun setContentView() {
        super.setContentView()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView() {
        super.initView()
        // ViewPager2 和 BottomNavigationView联动
        binding.mainVp.adapter = mainVpAdapter
        binding.mainVp.registerOnPageChangeCallback(pagerChangeCallback)
        // 使彩色svg图标可显示
        binding.mainBottomNav.itemIconTintList = null
        binding.mainBottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_item -> binding.mainVp.currentItem = 0
                R.id.girls_item -> binding.mainVp.currentItem = 1
                R.id.me_item -> binding.mainVp.currentItem = 2
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mainBottomNav.setBackgroundColor(window.navigationBarColor)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mainVp.unregisterOnPageChangeCallback(pagerChangeCallback)
    }
}
