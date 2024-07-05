package com.jayson.komm.ui.image

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import com.google.android.material.animation.MatrixEvaluator
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.util.ClickUtils
import com.jayson.komm.common.util.LogUtils
import com.jayson.komm.ui.R
import com.jayson.komm.ui.databinding.ActivityImageRotationBinding
import kotlin.math.max
import kotlin.math.min

class ImageRotationActivity : BaseActivity() {

    companion object {
        private const val TAG = "ImageRotationActivity"
        private const val DEGREE_90 = 90F
        private const val DEGREE_360 = 360F
        private const val DURATION_TIME = 200L
    }

    private lateinit var binding: ActivityImageRotationBinding

    private val originBitmap by lazy {
        (getDrawable(R.drawable.test2) as BitmapDrawable).bitmap
    }

    private var imgDegree = 0f

    override fun initView() {
        super.initView()
        binding = ActivityImageRotationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageIv.setImageBitmap(originBitmap)
        binding.rotationBtn.setOnClickListener {
            if (ClickUtils.isFastClick()) return@setOnClickListener
            binding.imageIv.scaleType = ImageView.ScaleType.MATRIX
            // 旋转90度，缩小到原来的0.5倍 - (建议用animateImage2效果更好)
            animateImage2(binding.imageIv, DEGREE_90, getScale(DEGREE_90))
        }
    }

    private fun getScale(degree: Float): Float {
        imgDegree += degree
        val width = binding.imageIv.width
        val height = binding.imageIv.height
        LogUtils.d(TAG, "getScale, width:$width, height:$height")
        val shortSize = min(binding.imageIv.width, binding.imageIv.height)
        val longSize = max(binding.imageIv.width, binding.imageIv.height)
        return if ((imgDegree % DEGREE_360) % 180f == 0f) {
            // 旋转 0 180 360
            longSize / shortSize.toFloat()
        } else {
            // 旋转 90 270
            shortSize / longSize.toFloat()
        }.also {
            LogUtils.d(TAG, "getScale, scale:$it")
        }
    }

    private fun animateImage1(
        imageView: ImageView,
        toDegrees: Float,
        toScale: Float
    ) {
        val startRotation = imageView.rotation
        val startScale = imageView.scaleX
        val animation: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                imageView.rotation = startRotation + (toDegrees * interpolatedTime)
                val scale = startScale + ((toScale - startScale) * interpolatedTime)
                LogUtils.d(TAG, "animateImage1, scale:$scale")
                imageView.scaleX = scale
                imageView.scaleY = scale
            }
        }
        animation.duration = DURATION_TIME // 动画持续时间
        animation.fillAfter = true // 动画结束后保持结束状态
        animation.interpolator = AccelerateDecelerateInterpolator() // 动画插入器
        imageView.startAnimation(animation)
    }

    private fun animateImage2(
        imageView: ImageView,
        toDegrees: Float,
        toScale: Float
    ) {
        val pivotX = imageView.width / 2f
        val pivotY = imageView.height / 2f
        val initMatrix = imageView.imageMatrix
        val endMatrix = Matrix(initMatrix).apply {
            postRotate(toDegrees, pivotX, pivotY)
            postScale(toScale, toScale, pivotX, pivotY)
        }
        val evaluator = MatrixEvaluator()
        val animation: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                val matrix =
                    evaluator.evaluate(interpolatedTime, initMatrix, endMatrix)
                imageView.imageMatrix = matrix
            }
        }
        animation.duration = DURATION_TIME // 动画持续时间
        animation.fillAfter = true // 动画结束后保持结束状态
        animation.interpolator = AccelerateDecelerateInterpolator() // 动画插入器
        imageView.startAnimation(animation)
    }

    private fun animateImage3(
        imageView: ImageView,
        toDegrees: Float,
        toScale: Float
    ) {
        val pivotX = imageView.width / 2f
        val pivotY = imageView.height / 2f
        val initMatrix = imageView.imageMatrix
        val endMatrix = Matrix(initMatrix).apply {
            postRotate(toDegrees, pivotX, pivotY)
            postScale(toScale, toScale, pivotX, pivotY)
        }

        // 构建动画
        val evaluator = MatrixEvaluator()
        val rotationScaleAnimator = ValueAnimator.ofFloat(0f, 1f)
        rotationScaleAnimator.addUpdateListener {
            val matrix =
                evaluator.evaluate(it.animatedFraction, initMatrix, endMatrix)
            imageView.imageMatrix = matrix
        }
        val set = AnimatorSet()
        set.playTogether(rotationScaleAnimator)
        set.interpolator = AccelerateDecelerateInterpolator()
        set.duration = DURATION_TIME
        set.start()
    }
}