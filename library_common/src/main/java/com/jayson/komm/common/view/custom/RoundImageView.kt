package com.jayson.komm.common.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.jayson.komm.common.R

/**
 * 圆角ImageView
 */
class RoundImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {
    private var cornerRadius = 0f
    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyle, 0)
        cornerRadius = a.getDimension(R.styleable.RoundImageView_cornerRadius, 0f)
        a.recycle()
    }
    fun setCornerRadius(radius: Float) {
        cornerRadius = radius
        invalidate()
    }
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (cornerRadius > 0) {
            canvas.saveLayer(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
            val path = Path()
            path.addRoundRect(RectF(0f, 0f, width.toFloat(), height.toFloat()), cornerRadius, cornerRadius, Path.Direction.CW)
            canvas.clipPath(path)
            super.onDraw(canvas)
            canvas.restore()
        } else {
            super.onDraw(canvas)
        }
    }
}