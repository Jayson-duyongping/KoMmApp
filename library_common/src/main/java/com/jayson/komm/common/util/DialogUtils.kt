package com.jayson.komm.common.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jayson.komm.common.R


object DialogUtils {

    /**
     * 自定义PopWindow
     *
     * @param context     上下文对象
     * @param anchorView  锚点视图，用于确定PopWindow的显示位置
     * @param layoutResId PopWindow的内容资源ID
     * @param buttonListeners 事件组
     * @param dialogWidth PopWindow的宽度 - dialogWidth = ScreenUtil.dp2px(context, 150f),
     */
    @JvmStatic
    fun showPopWindow(
        context: Activity?,
        anchorView: View,
        layoutResId: Int,
        vararg buttonListeners: Pair<Int, () -> Unit>,
        dialogWidth: Int? = null
    ) {
        val popupWindow = PopupWindow(context)
        val view = LayoutInflater.from(context).inflate(layoutResId, null)
        // 按钮监听
        for (buttonListener in buttonListeners) {
            val button = view.findViewById<View>(buttonListener.first)
            button.setOnClickListener {
                buttonListener.second.invoke()
                popupWindow.dismiss()
            }
        }
        // PopWindow设置
        popupWindow.apply {
            contentView = view;
            // 设置PopWindow的宽度和高度
            width = dialogWidth ?: ViewGroup.LayoutParams.WRAP_CONTENT
            // 设置PopWindow的背景
            elevation = 10F
            setBackgroundDrawable(ColorDrawable(Color.WHITE))
            // 设置PopWindow的外观和行为
            isFocusable = true
            isOutsideTouchable = true
        }

        // 获取PopWindow的宽度和高度
        val popupWindowWidth = dialogWidth ?: let {
            // 手动测量和布局PopupWindow的内容视图（WRAP_CONTENT自动获取不到width）
            view.apply {
                val widthMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                val heightMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                measure(widthMeasureSpec, heightMeasureSpec)
                layout(0, 0, view.measuredWidth, view.measuredHeight)
            }
            view.measuredWidth
        }

        // 显示PopWindow（带偏移量）
        anchorView.apply {
            // 获取控件在屏幕上的坐标位置以及控件的宽度
            val anchorPos = IntArray(2)
            anchorView.getLocationOnScreen(anchorPos)
            val anchorWidth = this.width
            val anchorHeight = this.height
            val y = anchorPos[1] - anchorHeight
            // 获取屏幕的宽度
            val displayMetrics = DisplayMetrics()
            context?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            val screenWidth = displayMetrics.widthPixels
            // 判断控件在屏幕的中心位置是在屏幕的左侧还是右侧
            val isLeft = anchorPos[0] + anchorWidth / 2 < screenWidth / 2
            // 设置PopupWindow的偏移量和显示位置
            if (isLeft) {
                val offsetX = 0
                popupWindow.showAsDropDown(this, offsetX, y)
            } else {
                val offsetX = popupWindowWidth - anchorWidth
                popupWindow.showAsDropDown(this, -offsetX, y)
            }
        }
    }

    /**
     * 自定义AlertDialog
     */
    @JvmStatic
    fun showAlertDialog(
        context: Context,
        layoutResId: Int,
        vararg buttonListeners: Pair<Int, () -> Unit>,
        dialogWidth: Int? = null,
        gravity: Int? = null,
        windowAlpha: Float? = 0.3F
    ) {
        val dialogView = LayoutInflater.from(context).inflate(layoutResId, null)
        val dialogBuilder = AlertDialog.Builder(context).setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.show()

        // 自定义设置（dialog宽度、右上角跟手）
        dialog.window?.let { window ->
            val layoutParams = window.attributes
            // 设置dialog宽度
            dialogWidth?.let {
                layoutParams.width = it
            }
            // 设置位置
            gravity?.let {
                layoutParams.gravity = Gravity.TOP or Gravity.START
            }
            // 设置window蒙版透明度(一般0.7以下更好)
            windowAlpha?.let {
                window.setDimAmount(it)
            }
            window.attributes = layoutParams
        }

        // 按钮监听
        for (buttonListener in buttonListeners) {
            val button = dialogView.findViewById<View>(buttonListener.first)
            button.setOnClickListener {
                buttonListener.second.invoke()
                // 关闭弹框
                dialog.dismiss()
            }
        }
    }

    /**
     * 居中AlertDialog
     */
    @JvmStatic
    fun showAlertDialogCenter(
        context: Context,
        title: String,
        content: String,
        onCancelClickListener: () -> Unit,
        onConfirmClickListener: () -> Unit,
        isCancelable: Boolean = false
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_alert, null)
        val dialogBuilder =
            AlertDialog.Builder(context).setView(dialogView).setCancelable(isCancelable)
        val dialog = dialogBuilder.create()
        dialog.show()

        // window设置
        dialog.window?.apply {
            attributes.gravity = Gravity.CENTER
            setDimAmount(0.3F)
            //这一句消除白块(才可显示布局中圆角)
            setBackgroundDrawable(BitmapDrawable())
        }

        // view设置
        val titleTv = dialogView.findViewById<TextView>(R.id.title_tv)
        val contentTv = dialogView.findViewById<TextView>(R.id.content_tv)
        val cancelBtn = dialogView.findViewById<Button>(R.id.cancel_btn)
        val confirmBtn = dialogView.findViewById<Button>(R.id.confirm_btn)
        titleTv.text = title
        contentTv.text = content
        cancelBtn.setOnClickListener {
            // 关闭弹框
            dialog.dismiss()
            onCancelClickListener.invoke()
        }
        confirmBtn.setOnClickListener {
            // 关闭弹框
            dialog.dismiss()
            onConfirmClickListener.invoke()
        }
    }

    /**
     * 自定义Menu-AlertDialog，目前只支持右上或居中
     */
    @JvmStatic
    fun showInputDialog(
        context: Context,
        title: String? = null,
        onConfirmClickListener: (String) -> Unit,
        windowAlpha: Float? = 0.3F
    ) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.layout_dialog_input, null)
        val dialogBuilder = AlertDialog.Builder(context).setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.show()

        // 自定义设置
        dialog.window?.let { window ->
            val layoutParams = window.attributes
            // 设置window蒙版透明度(一般0.7以下更好)
            windowAlpha?.let {
                window.setDimAmount(it)
            }
            // 设置位置
            layoutParams.gravity = Gravity.BOTTOM
            window.attributes = layoutParams
        }

        // 设置View
        val titleTv = dialogView.findViewById<TextView>(R.id.title_tv)
        val contentEt = dialogView.findViewById<EditText>(R.id.content_et)
        val cancelBtn = dialogView.findViewById<Button>(R.id.cancel_btn)
        val confirmBtn = dialogView.findViewById<Button>(R.id.confirm_btn)

        // 请求焦点并弹出键盘
        contentEt?.apply {
            isFocusable = true
            isFocusableInTouchMode = true;
            requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            postDelayed({
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }, 200)
        }

        // 设置标题
        title?.let {
            titleTv.text = it
        }

        // 按钮监听
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        confirmBtn.setOnClickListener {
            onConfirmClickListener.invoke(contentEt.text.toString())
            dialog.dismiss()
        }
    }


    /**
     * 底部面板
     */
    @JvmStatic
    fun showBottomSheetDialog(
        context: Context,
        layoutResId: Int,
        windowAlpha: Float? = 0.3F,
    ) {
        val view = LayoutInflater.from(context).inflate(layoutResId, null)
        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialogStyle)
        dialog.apply {
            setContentView(view)
            show()
        }
        // 自定义设置
        dialog.window?.let { window ->
            // 设置window蒙版透明度(一般0.7以下更好)
            windowAlpha?.let {
                window.setDimAmount(it)
            }
            // 设置为之前的颜色
            val originalNavigationBarColor = window.navigationBarColor
            dialog.setOnDismissListener {
                dialog.window?.navigationBarColor = originalNavigationBarColor
            }
        }
    }
}