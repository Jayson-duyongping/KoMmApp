package com.jayson.komm.common.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.jayson.komm.common.ext.getStatusBarHeight
import com.jayson.komm.common.ext.immediateStatusBar
import com.jayson.komm.common.util.LogUtils

/**
 * @Author: Jayson
 * @CreateDate: 2022/12/10 18:30
 * @Version: 1.0
 * @Description: BaseActivity
 */
abstract class BaseActivity : AppCompatActivity {
    /**
     * 无参构造函数
     */
    constructor() : super()

    /**
     * 可以填入layout布局的构造函数，使用viewBinding的方便
     * [layout] layout布局文件的id
     */
    constructor(@LayoutRes layout: Int) : super(layout)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immediateStatusBar()
        setContentView()
        initView()
        initConfig()
        initData()
    }

    /**
     * 设置跟布局android:fitsSystemWindows="true"，或加载View之后设置
     */
    protected fun setPaddingStatusBar(view: View) {
        view.setPadding(0, getStatusBarHeight(), 0, 0);
    }

    /**
     * 加载布局或布局之前的一些设置
     */
    open fun setContentView() {}

    /**
     * 必要的view初始化
     */
    open fun initView() {
        LogUtils.d(this.javaClass.simpleName, "初始化 initView")
    }

    /**
     * 必要的配置初始化
     */
    open fun initConfig() {
        LogUtils.d(this.javaClass.simpleName, "初始化 initConfig")
    }

    /**
     * 必要的数据初始化
     */
    open fun initData() {
        LogUtils.d(this.javaClass.simpleName, "初始化 initData")
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}