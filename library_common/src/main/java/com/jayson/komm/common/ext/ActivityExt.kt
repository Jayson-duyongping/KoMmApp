package com.jayson.komm.common.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner


/**
 * Activity中使用DataBinding时setContentView的简化
 * [layout] 布局文件
 * @return 返回一个Binding的对象实例
 */
fun <T : ViewDataBinding> Activity.bindView(@LayoutRes layout: Int): T {
    return DataBindingUtil.setContentView(this, layout)
}

/**
 * Activity中使用DataBinding时setContentView的简化
 * [layout] 布局文件
 * @return 返回一个Binding的对象实例 T 类型的 可null的
 */
fun <T : ViewDataBinding> Activity.bindView(view: View): T? {
    return DataBindingUtil.bind<T>(view)
}

/**
 * 沉浸式状态栏
 * 注意点，需要在setContentView之前调用
 */
@Suppress("DEPRECATION")
fun Activity.immediateStatusBar() {
    window.apply {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = Color.TRANSPARENT
        val fullscreen = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        val navigationBar = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        val statusBar = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        var flag = decorView.systemUiVisibility or fullscreen or navigationBar
        if (!isNightMode()) {
            flag = flag or statusBar
        }
        decorView.systemUiVisibility = flag
    }
}

/**
 * 是否是暗色模式（应用）
 */
fun Activity.isNightMode(): Boolean {
    val uiMode = context.resources.configuration.uiMode
    if ((uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
        return true
    }
    return false
}

/**
 * 是否是暗色模式（系统）
 */
fun Activity.isSysNightMode(): Boolean {
    val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    if (uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES) {
        return true
    }
    return false
}


/**
 * 状态栏高度
 */
@SuppressLint("InternalInsetResource")
fun Context.getStatusBarHeight(): Int {
    var result = 0;
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId);
    }
    return result;
}

/**
 * 虚拟按键高度
 */
@SuppressLint("InternalInsetResource")
fun Context.getNavigationBarHeight(): Int {
    var result = 0;
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId);
    }
    return result;
}

/**
 * 软键盘的隐藏
 * [view] 事件控件View
 */
fun Activity.dismissKeyBoard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * 扩展lifeCycleOwner属性，便于和Fragment之间使用lifeCycleOwner 一致性
 */
val ComponentActivity.viewLifeCycleOwner: LifecycleOwner
    get() = this

/**
 * Activity的扩展字段，便于和Fragment中使用liveData之类的时候，参数一致性
 */
val Activity.context: Context
    get() = this