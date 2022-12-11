package com.jayson.komm.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @Author: Jayson
 * @CreateDate: 2022/12/10 17:19
 * @Version: 1.0
 * @Description: 主页ViewPagerAdapter
 */
class MainViewPagerAdapter(activity: FragmentActivity, list: List<Fragment>) :
    FragmentStateAdapter(activity) {

    private val fragmentList = list

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}