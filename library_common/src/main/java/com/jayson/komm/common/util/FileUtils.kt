package com.jayson.komm.common.util

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object FileUtils {

    private const val TAG = "FileUtils"

    /**
     * ContentResolver-uri复制操作，可复制上传所有文件
     */
    @JvmStatic
    suspend fun copyContentResolverUriToFileDir(
        contentResolver: ContentResolver,
        uri: Uri,
        destinationDir: String
    ) {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                // 获取后缀格式
                val mimeType = contentResolver.getType(uri)
                val extension =
                    MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                // 获取并修改名字
                var name = uri.path ?: ""
                if (name.contains("/")) {
                    val names = name.split("/")
                    name = "${names[names.size - 1]}.$extension"
                    // :必须转成其他符号，否则对文件的操作会有问题
                    name = name.replace(":", "_")
                }
                LogUtils.d(TAG, "copyContentResolverUriToFileDir, name: $name")
                // 使用FileOutputStream将文件复制到指定位置
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    FileOutputStream(File(destinationDir, name)).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
        }.onFailure {
            LogUtils.e(TAG, "copyContentResolverUriToFileDir, e: $it")
        }
    }

    /**
     * ContentResolver-uri移动操作，先复制，再删除，“最近文件”中是删除不了的，只能复制
     */
    @JvmStatic
    suspend fun moveContentResolverUriToFileDir(
        contentResolver: ContentResolver,
        uri: Uri,
        destinationDir: String
    ) {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                LogUtils.d(TAG, "moveContentResolverUriToFileDir, uri: $uri")
                // 获取后缀格式
                val mimeType = contentResolver.getType(uri)
                val extension =
                    MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                // 获取并修改名字
                var name = uri.path ?: ""
                if (name.contains("/")) {
                    val names = name.split("/")
                    name = "${names[names.size - 1]}.$extension"
                    // :必须转成其他符号，否则对文件的操作会有问题
                    name = name.replace(":", "_")
                }
                LogUtils.d(TAG, "moveContentResolverUriToFileDir, name: $name")
                // 使用FileOutputStream将文件复制到指定位置
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    FileOutputStream(File(destinationDir, name)).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                // 删除原文件，达到移动效果
                contentResolver.delete(uri,null,null)
            }
        }.onFailure {
            LogUtils.e(TAG, "moveContentResolverUriToFileDir, e: $it")
        }
    }

    /**
     * 复制文件
     */
    @JvmStatic
    suspend fun copyFile(sourcePath: String, destinationPath: String) {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                val sourceFile = File(sourcePath)
                val destinationFile = File(destinationPath)
                FileInputStream(sourceFile).use { inputStream ->
                    FileOutputStream(destinationFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
        }.onFailure {
            LogUtils.e(TAG, "copyFile, e:$it")
        }
    }

    /**
     * 移动文件
     */
    @JvmStatic
    suspend fun moveFile(sourcePath: String, destinationPath: String) {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                val sourceFile = File(sourcePath)
                val destinationFile = File(destinationPath, sourceFile.name)
                val success = sourceFile.renameTo(destinationFile)
                if (!success) {
                    LogUtils.e(
                        TAG,
                        "moveFile, Failed to move file from $sourcePath to $destinationPath"
                    )
                }
            }
        }.onFailure {
            LogUtils.e(TAG, "moveFile, e:$it")
        }
    }

    /**
     * 删除文件
     */
    @JvmStatic
    suspend fun deleteFile(filePath: String) {
        kotlin.runCatching {
            LogUtils.d(TAG, "deleteFile, filePath:$filePath")
            withContext(Dispatchers.IO) {
                File(filePath).delete()
            }
        }.onFailure {
            LogUtils.e(TAG, "deleteFile, e:$it")
        }
    }
}