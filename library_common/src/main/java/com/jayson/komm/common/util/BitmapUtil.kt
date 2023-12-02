package com.jayson.komm.common.util

import android.content.Context
import android.graphics.*
import android.media.MediaMetadataRetriever
import android.widget.ImageView
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 图片处理工具类
 */
object BitmapUtil {

    private const val TAG = "BitmapUtil"

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

    /**
     * 创建带文字的图片
     */
    @JvmStatic
    fun makeBitmapWithText(text: String): Bitmap? {
        return kotlin.runCatching {
            val backgroundColor = Color.WHITE
            val textColor = Color.BLACK
            val textSize = 48f
            val width = 200
            val height = 200
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.textSize = textSize
            paint.color = textColor
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) // 自定义字体样式
            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)
            val bitmap = Bitmap.createBitmap(
                width, height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            canvas.drawColor(backgroundColor)
            val x = (bitmap.width - bounds.width()) / 2f
            val y = (bitmap.height + bounds.height()) / 2f
            canvas.drawText(text, x, y, paint)
            bitmap
        }.getOrElse {
            LogUtils.e(TAG, "makeBitmapWithText, e: $it")
            null
        }
    }

    /**
     * 获取视频缩缩略图(耗时，需要开线程)
     * -本以为获取视频缩略图很麻烦，需要这样获取，但实际上Glide直接加载路径就可以，NB
     */
    @JvmStatic
    suspend fun getVideoThumbnail(videoPath: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            return@withContext kotlin.runCatching {
                if (videoPath.isEmpty()) {
                    return@withContext null
                }
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(videoPath)
                val bitmap = retriever.frameAtTime
                retriever.release()
                bitmap
            }.getOrElse {
                null
            }
        }
    }
}