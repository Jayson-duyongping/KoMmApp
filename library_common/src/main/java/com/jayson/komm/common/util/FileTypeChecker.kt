package com.jayson.komm.common.util

import android.webkit.MimeTypeMap
import java.io.File

object FileTypeChecker {

    @JvmStatic
    fun getFileType(file: File): String? {
        val extension = getFileExtension(file)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }

    @JvmStatic
    private fun getFileExtension(file: File): String {
        val fileName = file.name
        var extension = ""
        val dotIndex = fileName.lastIndexOf(".")
        if (dotIndex != -1) {
            extension = fileName.substring(dotIndex + 1)
        }
        return extension
    }

    @JvmStatic
    fun getMimeTypeFromExtension(extension: String): String {
        var mimeType: String? = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        if (mimeType == null) {
            // 处理未知类型的文件
            mimeType = "*/*"
        }
        return mimeType
    }
}