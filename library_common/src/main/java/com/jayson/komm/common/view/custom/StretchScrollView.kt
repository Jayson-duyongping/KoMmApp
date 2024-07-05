package com.jayson.komm.common.view.custom

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import android.view.animation.TranslateAnimation
import androidx.core.widget.NestedScrollView

/**
 * 阻尼回弹 ScrollView
 */
class StretchScrollView(context: Context, attrs: AttributeSet?) :
    NestedScrollView(context, attrs) {
    // 子View
    private var innerView: View? = null

    // 上次手势事件的y坐标
    private var mLastY = 0f

    // 记录子View的正常位置
    private val normal: Rect = Rect()
    protected override fun onFinishInflate() {
        initView()
        super.onFinishInflate()
    }

    /**
     * 获取ScrollView的子布局
     */
    private fun initView() {
        // 去除原本ScrollView滚动到边界时的阴影效果
        overScrollMode = OVER_SCROLL_NEVER
        if (getChildAt(0) != null) {
            innerView = getChildAt(0)
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            // 手指松开恢复
            MotionEvent.ACTION_UP ->
                if (!normal.isEmpty) {
                    planAnimation()
                    normal.setEmpty()
                    mLastY = 0f
                }
            MotionEvent.ACTION_MOVE -> {
                val currentY = ev.y
                // 滑动距离
                val distanceY = (mLastY - currentY).toInt()
                innerView?.let {
                    // 处理Y轴的滚动事件，当滚动到最上或者最下时需要移动布局
                    // 手指刚触及屏幕时，也会触发此事件，此时mLastY的值还是0，会立即触发一个比较大的移动。这里过滤掉这种情况
                    if (isNeedTranslate && mLastY != 0f) {
                        if (normal.isEmpty) {
                            // 保存正常的布局位置
                            normal.set(
                                it.left,
                                it.top,
                                it.right,
                                it.bottom
                            )
                        }
                        // 移动布局， 使distance / 2 防止平移过快
                        it.layout(
                            it.left, it.top - distanceY / 2,
                            it.right, it.bottom - distanceY / 2
                        )
                    }
                }
                mLastY = currentY
            }
        }
        return super.onTouchEvent(ev)
    }

    /**
     * 回缩动画
     */
    private fun planAnimation() {
        // 开启移动动画
        val animation = TranslateAnimation(0F, 0F, innerView?.top?.toFloat()?:0f, normal.top.toFloat())
        animation.duration = 500
        animation.interpolator = OvershootInterpolator()
        innerView?.startAnimation(animation)
        // 补间动画并不会真正修改innerView的位置，这里需要设置使得innerView回到正常的布局位置
        innerView?.layout(normal.left, normal.top, normal.right, normal.bottom)
    }

    /**
     * 是否需要Y移动布局
     */
    private val isNeedTranslate: Boolean
        get() {
            val offset: Int = innerView?.measuredHeight?.minus(height) ?: 0
            val scrollY: Int = scrollY
            // 顶部或者底部
            return scrollY == 0 || scrollY == offset
        }
}