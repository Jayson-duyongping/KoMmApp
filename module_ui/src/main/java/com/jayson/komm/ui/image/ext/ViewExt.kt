package com.jayson.komm.ui.image.ext

import android.animation.Animator
import android.view.View
import android.view.animation.PathInterpolator

fun View.rotateAndScaleView(
    rotateDegree: Float,
    scaleRatio: Float,
    duration: Long,
    stopAction: (() -> Unit)? = null
) {
    animate().cancel()
    // 获取动画开始前的初始旋转角度
    val initialRotation = rotation
    val animator = animate()
        .rotationBy(rotateDegree)
        .scaleX(scaleRatio)
        .scaleY(scaleRatio)
        .setDuration(duration)
        .setInterpolator(PathInterpolator(0.3f, 0f, 0.1f, 1f))
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {}

            override fun onAnimationEnd(p0: Animator) {
                stopAction?.invoke()
            }

            override fun onAnimationCancel(p0: Animator) {
                // 手动设置旋转角度为预期的最终角度
                rotation = initialRotation + rotateDegree
                stopAction?.invoke()
            }

            override fun onAnimationRepeat(p0: Animator) {}
        })
    animator.start()
}