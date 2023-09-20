package com.jayson.komm.common.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object ShareUtils {

    private const val TAG = "ShareUtils"
    private const val FILE_AUTHORITY = "com.jayson.komm.fileProvider"

    /**
     * 分享文件（类型自解析）
     */
    fun shareFile(context: Context, filePath: String?) {
        kotlin.runCatching {
            if (filePath == null || filePath == "") {
                return
            }
            val file = File(filePath)
            val uri = FileProvider.getUriForFile(
                context,
                FILE_AUTHORITY,
                file
            )
            // 根据文件后缀名获取 MIME 类型
            val mimeType = FileTypeChecker.getMimeTypeFromExtension(file.extension)
            LogUtils.d(TAG, "shareFile, uri:$uri, mimeType:$mimeType")
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = mimeType
                putExtra(Intent.EXTRA_STREAM, uri)
            }
            context.startActivity(Intent.createChooser(intent, "分享到"))
        }.getOrElse {
            LogUtils.e(TAG, "shareFile, e:$it")
        }
    }

    /**
     * 分享文本
     */
    fun shareText(context: Context, text: String) {
        kotlin.runCatching {
            LogUtils.d(TAG, "shareText, text:$text")
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(Intent.createChooser(intent, "分享到"))
        }.getOrElse {
            LogUtils.e(TAG, "shareText, e:$it")
        }
    }

    /**
     * 分享图片
     */
    fun shareImage(context: Context, imageUri: Uri?) {
        kotlin.runCatching {
            LogUtils.d(TAG, "shareImage, imageUri:$imageUri")
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, imageUri)
            }
            context.startActivity(Intent.createChooser(intent, "分享到"))
        }.getOrElse {
            LogUtils.e(TAG, "shareImage, e:$it")
        }
    }

    /**
     * 分享视频
     */
    fun shareVideo(context: Context, videoUri: Uri?) {
        kotlin.runCatching {
            LogUtils.d(TAG, "shareVideo, videoUri:$videoUri")
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "video/*"
                putExtra(Intent.EXTRA_STREAM, videoUri)
            }
            context.startActivity(Intent.createChooser(intent, "分享到"))
        }.getOrElse {
            LogUtils.e(TAG, "shareVideo, e:$it")
        }
    }

    /**
     * 分享音频
     */
    fun shareAudio(context: Context, audioUri: Uri?) {
        kotlin.runCatching {
            LogUtils.d(TAG, "shareAudio, audioUri:$audioUri")
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "audio/*"
                putExtra(Intent.EXTRA_STREAM, audioUri)
            }
            context.startActivity(Intent.createChooser(intent, "分享到"))
        }.getOrElse {
            LogUtils.e(TAG, "shareAudio, e:$it")
        }
    }
}