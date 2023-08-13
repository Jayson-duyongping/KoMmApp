package com.jayson.komm

import android.widget.FrameLayout
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
        // 主页不统一设置padding状态栏，里面有fragment需要沉浸式
        findViewById<FrameLayout>(android.R.id.content).apply {
            setPadding(0, 0, 0, 0);
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView() {
        super.initView()
        // ViewPager2 和 BottomNavigationView联动
        binding.mainVp.apply {
            isUserInputEnabled = false // 禁止viewpager2左右滑动
            adapter = mainVpAdapter
            registerOnPageChangeCallback(pagerChangeCallback)
        }
        binding.mainBottomNav.apply {
            itemIconTintList = null // 使彩色svg图标可显示
            setOnItemSelectedListener {
                // setCurrentItem(item,false)
                when (it.itemId) {
                    R.id.home_item -> binding.mainVp.setCurrentItem(0, false) // 禁止viewpager2滑动效果
                    R.id.girls_item -> binding.mainVp.setCurrentItem(1, false)
                    R.id.me_item -> binding.mainVp.setCurrentItem(2, false)
                }
                return@setOnItemSelectedListener true
            }
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
