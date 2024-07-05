package com.jayson.komm.ui.image

import android.graphics.PointF
import android.view.View
import androidx.core.view.doOnLayout
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.util.LogUtils
import com.jayson.komm.ui.databinding.ActivityImageBinding
import com.jayson.komm.ui.image.ext.rotateAndScaleView
import com.jayson.komm.ui.image.manager.PictureRotationUtils
import kotlin.math.abs


class ImageActivity : BaseActivity() {

    companion object {
        private const val TAG = "ImageActivity"
    }

    private val iconWidth by lazy {
        resources.getDimension(com.jayson.komm.ui.R.dimen.iconWidth)
    }

    private var imageViewTopEndX = 0f
    private var imageViewTopEndY = 0f

    private lateinit var binding: ActivityImageBinding

    override fun initView() {
        super.initView()
        // 初始化binding
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.picPreIv.doOnLayout {
            imageViewTopEndX = it.x
            imageViewTopEndY = it.y
            closeLocation()
        }
        binding.rotationBtn.setOnClickListener {
            rotation()
        }
    }

    private fun rotation() {
        binding.picCloseIv.visibility = View.GONE
        // 1.计算imageViewGroup的缩放比例
        PictureRotationUtils.getImageViewScaleRatio(
            binding.picParent,
            binding.picPreIv
        )?.let {
            // 2.旋转imageViewGroup
            binding.picPreIv.rotateAndScaleView(-90f, it, 200) {
                closeLocation(it)
            }
        }
    }

    private fun closeLocation(scale: Float = 1f) {
        // 在调用处使用该函数
        val viewWidth = binding.picPreIv.measuredWidth.toFloat()
        val viewHeight = binding.picPreIv.measuredHeight.toFloat()
        val rotatedRect = PictureRotationUtils.rotateAndScaleRectangle(
            imageViewTopEndX,
            imageViewTopEndY,
            viewWidth,
            viewHeight,
            binding.picPreIv.rotation,
            scale
        )
        //
        LogUtils.d(TAG, "closeLocation:rotation: ${binding.picPreIv.rotation}}")
        for (point in rotatedRect) {
            LogUtils.d(TAG, "closeLocation:Point: (${point.x}, ${point.y})")
        }

        val degree = binding.picPreIv.rotation.toInt() % 360
        val degreeAbs = abs(degree)
        LogUtils.d(TAG, "closeLocation:degree：$degree")
        val topEndPoint: PointF = when (degreeAbs) {
            0 -> rotatedRect[1]
            270 -> rotatedRect[0]
            180 -> rotatedRect[2]
            90 -> rotatedRect[3]
            else -> rotatedRect[1]
        }

        binding.picCloseIv.apply {
            x = topEndPoint.x - iconWidth
            y = topEndPoint.y
            LogUtils.d(TAG, "closeLocation:x:$x,y:$y")
            requestLayout()
            // 计算删除按钮位置
            visibility = View.VISIBLE
        }
    }
}