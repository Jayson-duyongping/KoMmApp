package com.jayson.komm.common.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * 图片处理工具类
 */
object BitmapUtil {
    /**
     * 加载高斯模糊
     * @param url 网络url
     * @param radius 半径值radius越大，模糊效果越明显
     */
    @JvmStatic
    fun loadBlurImageUseUrl(context: Context?, url: String, imageView: ImageView, radius: Int) {
        if (context == null) {
            return
        }
        Glide.with(context)
            .asBitmap()
            .load(url)
            .transform(BlurTransformation(radius))
            .into(imageView)
    }

    /**
     * 加载高斯模糊
     * @param res 本地资源
     * @param radius 半径值radius越大，模糊效果越明显
     */
    @JvmStatic
    fun loadBlurImageUseRes(context: Context?, res: Int, imageView: ImageView, radius: Int) {
        if (context == null) {
            return
        }
        Glide.with(context)
            .asBitmap()
            .load(res)
            .transform(BlurTransformation(radius))
            .into(imageView)
    }
}