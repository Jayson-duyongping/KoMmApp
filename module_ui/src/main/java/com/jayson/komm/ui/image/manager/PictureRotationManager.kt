package com.jayson.komm.ui.image.manager

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.jayson.komm.common.util.LogUtils
import com.jayson.komm.ui.R

object PictureRotationManager {

    private const val TAG = "PictureRotationManager"
    private const val ROTATION_DEGREE = -90f

    @JvmStatic
    fun View.getRotatedWidthAndHeight(): Pair<Int, Int> {
        val rotationRadians = Math.toRadians(rotation.toDouble())
        val originalWidth = measuredWidth
        val originalHeight = measuredHeight
        val absCos = Math.abs(Math.cos(rotationRadians))
        val absSin = Math.abs(Math.sin(rotationRadians))
        val rotatedWidth = (originalWidth * absCos + originalHeight * absSin).toInt()
        val rotatedHeight = (originalWidth * absSin + originalHeight * absCos).toInt()
        return Pair(rotatedWidth, rotatedHeight)
    }

    /**
     * 通过图片计算ImageView的展示宽高（包括缩放）
     */
    @JvmStatic
    fun getImageViewScaleRatio(
        viewGroup: ViewGroup,
        imageView: ImageView
    ): Float? {
        // 有个内间距
        val padding =
            viewGroup.context.resources.getDimension(R.dimen.padding)
        val paddingValue = (2 * padding).toInt()
        // 父View宽高 - 内间距，才应该是ImageView显示的最大宽高
        val parentWidth = viewGroup.width
        val parentHeight = viewGroup.height

        // ImageView旋转之后的实际宽高
        var imageWidth = imageView.getRotatedWidthAndHeight().second
        var imageHeight = imageView.getRotatedWidthAndHeight().first

        // 获取之前的ImageView的显示宽高
        val originWidth = imageView.measuredWidth
        val originHeight = imageView.measuredHeight

        var scaleRatio = 1f
        if (imageWidth >= imageHeight) {
            // （图片宽度大于图片高度 && 图片宽度大于父View宽） 则 （以宽度缩放比例为准，计算图片应该展示的宽高）
            if (imageWidth > parentWidth) {
                scaleRatio = parentWidth.toFloat() / imageWidth
            }
        } else {
            // （图片高度大于图片宽度 && 图片高度大于父View高） 则 （以高度缩放比例为准）
            if ((imageHeight > parentHeight)) {
                scaleRatio = parentHeight.toFloat() / imageHeight
            }

        }
        return scaleRatio.also {
            LogUtils.d(TAG, "getImageViewWidthAndHeightByBitmap,$it")
        }
    }

    /**
     * 从文件获取旋转后的bitmap
     */
    @JvmStatic
    fun getRotateBitmapFromFile(filePath: String): Bitmap? {
        val rotationDegrees = ROTATION_DEGREE
        // 从文件加载Bitmap
        val bitmap = BitmapFactory.decodeFile(filePath)
        // 创建Matrix对象并进行旋转
        val matrix = Matrix()
        matrix.postRotate(rotationDegrees)
        // 应用Matrix变换到Bitmap
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}