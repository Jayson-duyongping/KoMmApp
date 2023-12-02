package com.jayson.komm.dev.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.PathInterpolator

object AnimatorUtils {

    private const val TAG = "AnimatorUtils"

    private const val START_DURATION = 1000L
    private const val END_DURATION = 800L

    @JvmStatic
    private fun valueAnimatorSet(duration: Long, block: (ValueAnimator) -> Unit) {
        val layoutAnimator = ValueAnimator.ofFloat(0f, 1f)
        layoutAnimator.duration = duration
        layoutAnimator.interpolator = PathInterpolator(0.3f, 0f, 0.1f, 1f)
        layoutAnimator.start()
        layoutAnimator.addUpdateListener { animator ->
            block(animator)
        }
    }

    @JvmStatic
    fun showCardSelfByObjectAnimator(
        view: View?,
        viewFixedHeight: Int,
        onAnimationStart: () -> Unit,
        onValueAnimator: (ValueAnimator) -> Unit
    ) {
        if (view?.visibility == View.VISIBLE) {
            return
        }
        // 显示的动画参数
        val showScaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.92f, 1f)
        val showScaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.92f, 1f)
        val showAlpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        val showAnimatorSet = AnimatorSet().apply {
            playTogether(showScaleX, showScaleY, showAlpha)
            duration = START_DURATION
            interpolator = PathInterpolator(0.3f, 0f, 0.1f, 1f)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    // 动画开始时的处理
                    onAnimationStart.invoke()
                }

                override fun onAnimationEnd(animation: Animator) {
                    // 动画结束时的处理
                }

                override fun onAnimationCancel(animation: Animator) {
                    // 动画取消时的处理
                }

                override fun onAnimationRepeat(animation: Animator) {
                    // 动画重复时的处理
                }
            })
        }
        // 启动动画
        showAnimatorSet.start()
        // ValueAnimator更新监听
        var isVisibleView = false
        valueAnimatorSet(START_DURATION) {
            val value = it.animatedValue as Float
            // 根据比例计算卡片高度
            val aCardHeight = value * viewFixedHeight
            //LogUtils.d(TAG, "showCardSelfByObjectAnimator, value:$value, aCardHeight:$aCardHeight")
            val lp = view?.layoutParams
            if (lp != null && aCardHeight > 0) {
                lp.height = aCardHeight.toInt()
                view.layoutParams = lp
                if (!isVisibleView) {
                    view.visibility = View.VISIBLE
                    isVisibleView = true
                }
            }
            onValueAnimator.invoke(it)
        }
    }

    @JvmStatic
    fun hideCardSelfByObjectAnimator(
        view: View?,
        viewFixedHeight: Int,
        onAnimationEnd: () -> Unit,
        onValueAnimator: (ValueAnimator) -> Unit
    ) {
        if (view?.visibility == View.GONE) {
            return
        }
        // 隐藏的动画参数
        val hideScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.92f)
        val hideScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.92f)
        val hideAlpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
        val hideAnimatorSet = AnimatorSet().apply {
            playTogether(hideScaleX, hideScaleY, hideAlpha)
            duration = END_DURATION
            interpolator = PathInterpolator(0.3f, 0f, 0.1f, 1f)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    // 动画开始时的处理
                }

                override fun onAnimationEnd(animation: Animator) {
                    // 动画结束时的处理
                    view?.visibility = View.GONE
                    onAnimationEnd.invoke()
                }

                override fun onAnimationCancel(animation: Animator) {
                    // 动画取消时的处理
                }

                override fun onAnimationRepeat(animation: Animator) {
                    // 动画重复时的处理
                }
            })
        }
        // 启动动画
        hideAnimatorSet.start()
        // ValueAnimator更新监听
        valueAnimatorSet(END_DURATION) {
            val value = it.animatedValue as Float
            // 根据比例计算卡片高度
            val aCardHeight = (1 - value) * viewFixedHeight
            //LogUtils.d(TAG, "hideCardSelfByObjectAnimator, value2:$value, aCardHeight:$aCardHeight")
            val lp = view?.layoutParams
            if (lp != null && aCardHeight > 0) {
                lp.height = aCardHeight.toInt()
                view.layoutParams = lp
            }
            onValueAnimator.invoke(it)
        }
    }

}