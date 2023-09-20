package com.jayson.komm

import android.content.Intent
import android.provider.Settings
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.util.DialogUtils
import com.jayson.komm.common.util.PermissionUtils
import com.jayson.komm.databinding.ActivityMainBinding
import com.jayson.komm.ent.EntFragment
import com.jayson.komm.home.HomeFragment
import com.jayson.komm.me.MeFragment
import com.jayson.komm.ui.adapter.MainViewPagerAdapter

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val fragmentList: List<Fragment> by lazy {
        listOf(HomeFragment(), EntFragment(), MeFragment())
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

    /**
     * 请求管理所有文件权限返回结果
     */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            if (PermissionUtils.isManageAllFilesAccessPermission()) {
                initPageView()
            } else {
                finish()
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
        // 需要判断是否已经授权管理所有文件
        if (PermissionUtils.isManageAllFilesAccessPermission()) {
            initPageView()
        } else {
            DialogUtils.showAlertDialogCenter(
                this,
                "设置管理所有文件权限",
                "需要申请管理所有权限，才可以对文件进行管理、读写，是否去设置",
                onCancelClickListener = {
                    finish()
                },
                onConfirmClickListener = {
                    // 跳转到授权管理所有文件权限（SDK大于30可用）
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    requestPermissionLauncher.launch(intent)
                }
            )
        }
    }

    private fun initPageView() {
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
