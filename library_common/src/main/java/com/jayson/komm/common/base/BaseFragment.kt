package com.jayson.komm.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * @Author: Jayson
 * @CreateDate: 2022/12/10 18:35
 * @Version: 1.0
 * @Description: BaseFragment
 */
abstract class BaseFragment : Fragment {
    /**
     * 无参构造函数
     */
    constructor() : super()

    /**
     * 可以填入layout布局的构造函数，使用viewBinding的方便
     * [layout] layout布局文件的id
     */
    constructor(@LayoutRes layout: Int) : super(layout)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutRes(), container, false)
        initView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initConfig()
        initData()
    }

    @LayoutRes
    abstract fun getLayoutRes(): Int

    /**
     * view初始化后的必要配置
     */
    open fun initView(view: View) {
    }

    /**
     * view初始化后的必要配置
     */
    open fun initConfig() {
    }

    /**
     * view初始化后的必要数据
     */
    open fun initData() {
    }
}