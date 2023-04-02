package com.jayson.komm.mvvm

import android.content.Intent
import android.view.View
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableArrayMap
import androidx.lifecycle.lifecycleScope
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.util.LogUtils
import com.jayson.komm.mvvm.data.User
import com.jayson.komm.mvvm.data.UserOne
import com.jayson.komm.mvvm.databinding.MvvmBinding
import com.jayson.komm.mvvm.listener.MyLocationObserver
import com.jayson.komm.mvvm.listener.MyLocationOwner
import com.jayson.komm.mvvm.model.DiceRollViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * 1.dataBinding true:
 * https://developer.android.google.cn/topic/libraries/data-binding/start?hl=zh-cn
 *
 * 2.lifecycle:
 * https://developer.android.google.cn/topic/libraries/architecture/lifecycle?hl=zh-cn
 *
 * 3.ViewModel:
 * https://developer.android.google.cn/topic/libraries/architecture/viewmodel?hl=zh-cn
 */
class MvvmActivity : BaseActivity() {

    companion object {
        private const val TAG = "MvvmActivity"
    }

    // 执行data类名MvvmBinding
    private var binding: MvvmBinding? = null

    private var myLocationObserver: MyLocationObserver? = null
    private var myLocationOwner: MyLocationOwner? = null

    override fun initView() {
        super.initView()
        // 初始化binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm)
        binding?.apply {
            textStr = "填充数据"
            onClickListener = View.OnClickListener {
                textStr = "数据：One"
                user = User("张三", 27)
                list = listOf("苹果", "桃子", "香蕉")
                index = 1
                map = mapOf("1" to "ONE", "2" to "TWO", "3" to "THREE")
                key = "2"
                userInfoMap?.set("firstName", "Baidu")
                userInfoList?.set(0, "Sina")
                userOne?.firstName = "℃"
                userOne?.lastName = "先生"
            }
            onMvpClickListener = View.OnClickListener {
                Intent(this@MvvmActivity, MvpActivity::class.java).apply {
                    startActivity(this)
                }
            }
            onKMClickListener = View.OnClickListener {
                Intent(this@MvvmActivity, KMActivity::class.java).apply {
                    startActivity(this)
                }
            }
            onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
                LogUtils.d(TAG, "initView, OnCheckedChangeListener $isChecked")
            }
        }
    }

    override fun initConfig() {
        super.initConfig()
        lifeCycleDemo()
        viewModelDemo()
    }

    override fun initData() {
        super.initData()
        ObservableArrayMap<String, Any>().apply {
            put("firstName", "Google")
            put("lastName", "Inc")
            put("age", 17)
        }.let {
            binding?.userInfoMap = it
        }
        ObservableArrayList<Any>().apply {
            add("GoogleOne")
            add("Inc.")
            add(17)
        }.let {
            binding?.userInfoList = it
        }
        binding?.apply {
            userOne = UserOne()
            userOne?.firstName = "Y"
            userOne?.lastName = "小姐"
        }
    }

    private fun lifeCycleDemo() {
        myLocationOwner = MyLocationOwner()
        myLocationObserver = MyLocationObserver(this, lifecycle) {
            LogUtils.d(TAG, "lifeCycleDemo, MyLocationObserver update UI")
        }.apply {
            myLocationOwner?.lifecycle?.addObserver(this)
        }
        myLocationOwner?.create()
    }

    private fun viewModelDemo() {
        val viewModel = DiceRollViewModel()
        lifecycleScope.launch {
            viewModel.uiState.collect {
                LogUtils.d(TAG, "viewModelDemo, $it")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myLocationOwner?.destroy()
    }
}