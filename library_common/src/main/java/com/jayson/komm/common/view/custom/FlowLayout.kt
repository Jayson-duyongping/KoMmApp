package com.jayson.komm.common.view.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jayson.komm.common.R
import kotlin.random.Random

/**
 * 自定义流式标签
 */
class FlowLayout(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {
    private var horizontalSpacing = 20
    private var verticalSpacing = 20

    // 记录上一次选中的View
    private var lastSelectedView: TextView? = null
    private var lastTagColor: Int? = null

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout)
        horizontalSpacing = ta.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpacing, 0)
        verticalSpacing = ta.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpacing, 0)
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var x = paddingLeft
        var y = paddingTop
        var rowHeight = 0
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val widthWithoutPadding = width - paddingLeft - paddingRight
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                child.measure(
                    MeasureSpec.UNSPECIFIED,
                    MeasureSpec.UNSPECIFIED
                )
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight
                rowHeight = Math.max(rowHeight, childHeight + verticalSpacing)
                if (x + childWidth > widthWithoutPadding) {
                    x = paddingLeft
                    y += rowHeight
                }
                x += childWidth + horizontalSpacing
            }
        }
        val height = y + rowHeight + paddingBottom
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var x = paddingLeft
        var y = paddingTop
        var rowHeight = 0
        val widthWithoutPadding = r - l - paddingLeft - paddingRight
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight
                rowHeight = Math.max(rowHeight, childHeight + verticalSpacing)
                if (x + childWidth > widthWithoutPadding) {
                    x = paddingLeft
                    y += rowHeight
                }
                child.layout(x, y, x + childWidth, y + childHeight)
                x += childWidth + horizontalSpacing
            }
        }
    }

    fun addTags(tags: List<Tag>, onTagClickListener: OnTagClickListener?) {
        for (tag in tags) {
            val randomColor = getRandomColor()
            val shape = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 50F
                setColor(Color.TRANSPARENT)
                setStroke(2, randomColor)
            }
            val textView = TextView(context).apply {
                background = shape
                text = tag.name
                setTextColor(randomColor)
                setPadding(20, 0, 20, 10)
                setOnClickListener {
                    // 如果选中的不是上一次选中的View
                    if (this != lastSelectedView) {
                        // 将上一次选中的View设为未选中状态
                        lastSelectedView?.apply {
                            isSelected = false
                            (background as GradientDrawable).setColor(Color.TRANSPARENT)
                            lastTagColor?.let { setTextColor(it) }
                        }
                    }
                    // 记录选中的View
                    lastSelectedView = this
                    lastTagColor = currentTextColor
                    // 选择时颜色
                    isSelected = !isSelected
                    if (isSelected) {
                        shape.setColor(randomColor)
                        setTextColor(Color.WHITE)
                    }
                    onTagClickListener?.onTagClick(tag)
                }
            }
            addView(textView)
        }
    }

    private fun getRandomColor(): Int {
        val rnd = Random
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    interface OnTagClickListener {
        fun onTagClick(tag: Tag)
    }

    data class Tag(
        val tagId: Long = 0,
        val name: String,
        val linkUrl: String
    )
}