package com.jayson.komm.dev.view.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.jayson.komm.dev.R

class RadiusTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var backgroundBitmap: Bitmap? = null
    private val paint = Paint()
    private var cornerRadius: Float = 10f
    private var backgroundColor: Int = Color.parseColor("#80FFFFFF")

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.RadiusTextView)
        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {
            updateBackground()
        }
    }

    private fun updateBackground() {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val rect = Rect(0, 0, width, height)
        val bgPaint = Paint()
        bgPaint.color = backgroundColor
        bgPaint.style = Paint.Style.FILL
        bgPaint.isAntiAlias = true
        canvas.drawRoundRect(
            rect.left.toFloat(),
            rect.top.toFloat(),
            rect.right.toFloat(),
            rect.bottom.toFloat(),
            cornerRadius,
            cornerRadius,
            bgPaint
        )
    }

    override fun onDraw(canvas: Canvas) {
        backgroundBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, paint)
        }
        super.onDraw(canvas)
    }

    fun setCornerRadius(radius: Float) {
        this.cornerRadius = radius
        invalidate()
    }
}