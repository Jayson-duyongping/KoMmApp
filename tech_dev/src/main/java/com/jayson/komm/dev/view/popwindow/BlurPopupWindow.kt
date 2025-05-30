package com.jayson.komm.dev.view.popwindow

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.*
import android.os.Build
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.jayson.komm.common.util.LogUtils
import com.jayson.komm.dev.R

class BlurPopupWindow(private val activity: Activity) : PopupWindow() {

    companion object {
        private const val TAG = "BlurPopupWindow"
    }

    private lateinit var rootView: View
    private lateinit var contentLayout: LinearLayout
    private lateinit var contentTv: TextView
    private lateinit var blurView: ImageView

    init {
        setupPopup()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupPopup() {
        // 初始化布局
        rootView = LayoutInflater.from(activity).inflate(R.layout.popup_layout, null)
        contentLayout = rootView.findViewById(R.id.contentLayout)
        contentTv = rootView.findViewById(R.id.content_tv)
        blurView = rootView.findViewById(R.id.blurBackground)

        // PopupWindow 基本设置
        contentView = rootView
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isFocusable = false
        isOutsideTouchable = true

        // 设置背景和阴影
        val drawableBg =
            rootView.resources.getDrawable(R.drawable.popup_background, rootView.context.theme)
        setBackgroundDrawable(drawableBg)
        elevation = 20F

        // 处理键盘模式
        inputMethodMode = INPUT_METHOD_NEEDED
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE

        applyCornerRadius()

        // 添加布局监听器
        rootView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                applyBlurEffect()
            }
        }
    }

    /**
     * 只对模糊图层进行裁剪分割圆角，如果对其他View这样貌似会有冲突，效果很不好看
     */
    private fun applyCornerRadius() {
        blurView.apply {
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(0, 0, view.width, view.height, 48F)
                }
            }
            clipToOutline = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun applyBlurEffect() {
        // 创建 PopupWindow 下方区域的截图
        val screenshot = captureAreaBelowPopup()

        // 设置截图到背景视图
        blurView.setImageBitmap(screenshot)

        // 应用毛玻璃效果
        val blurEffect = RenderEffect.createBlurEffect(
            25f, // 模糊半径 X
            25f, // 模糊半径 Y
            Shader.TileMode.MIRROR // 边缘处理模式
        )
        blurView.setRenderEffect(blurEffect)
    }

    /**
     * 只截取 PopupWindow 下方的区域
     */
    private fun captureAreaBelowPopup(): Bitmap {

        val decorView = activity.window.decorView as ViewGroup

        // 获取 PopupWindow 的位置
        val location = IntArray(2)
        contentLayout.getLocationOnScreen(location)
        val popupX = location[0]
        val popupY = location[1]

        val contentWidth = contentLayout.measuredWidth
        val contentHeight = contentLayout.measuredHeight

        LogUtils.d(
            TAG,
            "captureAreaBelowPopup, contentWidth:$contentWidth, contentHeight:$contentHeight"
        )
        // 截取的屏幕的位置
        val captureRect = Rect(
            popupX,
            popupY,
            popupX + contentWidth,
            popupY + contentHeight
        )

        // 创建对应区域的 Bitmap
        val bitmap = Bitmap.createBitmap(
            captureRect.width(),
            captureRect.height(),
            Bitmap.Config.ARGB_8888
        )

        // 创建 Canvas 并绘制指定区域
        val canvas = Canvas(bitmap)
        // 必须平移绘制
        canvas.translate(-captureRect.left.toFloat(), -captureRect.top.toFloat())
        decorView.draw(canvas)

        return bitmap
    }

    // 显示在目标视图下方
    fun showBelow(anchor: View) {
        showAsDropDown(anchor, 0, 0)
    }

    // 清理资源
    override fun dismiss() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            blurView.setRenderEffect(null)
        }
        super.dismiss()
    }

    fun setContent(content: String) {
        contentTv.text = content
    }
}