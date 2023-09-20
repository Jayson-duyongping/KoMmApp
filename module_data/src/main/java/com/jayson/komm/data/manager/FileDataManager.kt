package com.jayson.komm.data.manager

import android.content.Context
import android.os.Environment
import com.jayson.komm.common.util.FileTypeChecker
import com.jayson.komm.common.util.LogUtils
import com.jayson.komm.data.bean.Classify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object FileDataManager {

    private const val TAG = "FileDataManager"

    private const val RESOURCE_FILE_NAME = "KommResource"
    private const val RESOURCE_HIDE_FILE_NAME = ".KommResource"
    private const val SEPARATOR = "/"
    private const val NOME_DIA = ".nomedia"

    private const val RESOURCE_MA_FILE = "KommResource/Ma"
    private const val RESOURCE_MS_FILE = "KommResource/Ms"
    private const val RESOURCE_HIDE_MA_FILE = ".KommResource/Ma"
    private const val RESOURCE_HIDE_Ms_FILE = ".KommResource/Ms"

    const val INTENT_TAG = "scanFileDir"

    @JvmStatic
    val resourcePath =
        Environment.getExternalStorageDirectory().path + SEPARATOR + RESOURCE_FILE_NAME

    @JvmStatic
    val resourceHidePath =
        Environment.getExternalStorageDirectory().path + SEPARATOR + RESOURCE_HIDE_FILE_NAME

    @JvmStatic
    val resourceMaHidePath =
        Environment.getExternalStorageDirectory().path + SEPARATOR + RESOURCE_HIDE_MA_FILE

    @JvmStatic
    val resourceMsHidePath =
        Environment.getExternalStorageDirectory().path + SEPARATOR + RESOURCE_HIDE_Ms_FILE

    /**
     * 显示KommResource库
     */
    @JvmStatic
    suspend fun showResourceFile(dispatcherMain: () -> Unit) {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                File(resourceHidePath).let {
                    // 删除之前的名为".nomedia"的空文件
                    File(it, NOME_DIA).delete()
                    // 从之前的.XXX改回XXX
                    val newName: String = it.name.substring(1)
                    val newFile = File(it.parentFile, newName)
                    it.renameTo(newFile)
                }
                withContext(Dispatchers.Main) {
                    dispatcherMain()
                }
            }
        }.onFailure {
            LogUtils.e(TAG, "showFile, e:$it")
        }
    }

    /**
     * 隐藏KommResource库
     */
    @JvmStatic
    suspend fun hideResourceFile(dispatcherMain: () -> Unit) {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                File(resourcePath).let {
                    // 指定文件夹中创建一个名为".nomedia"的空文件，这会告诉系统该文件夹中不应该被媒体库扫描
                    File(it, NOME_DIA).createNewFile()
                    // 改名为.XXX实现隐藏
                    val newName = "." + it.name
                    val newFile = File(it.parentFile, newName)
                    it.renameTo(newFile)
                }
            }
            withContext(Dispatchers.Main) {
                dispatcherMain()
            }
        }.onFailure {
            LogUtils.e(TAG, "hideFile, e:$it")
        }
    }

    /**
     * 扫描当前目录文件
     */
    @JvmStatic
    suspend fun scanCurrentFiles(folder: File): Pair<ArrayList<Classify>, ArrayList<Classify>> {
        val folderList = arrayListOf<Classify>()
        val fileList = arrayListOf<Classify>()
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                val files = folder.listFiles() ?: return@withContext
                for (file in files) {
                    if (file.isDirectory) {
                        LogUtils.d(TAG, "scanCurrentMaFiles, isDirectory: ${file.name}")
                        val classify = Classify(
                            fileType = "directory",
                            name = file.name,
                            parentName = folder.name,
                            path = file.path,
                        )
                        LogUtils.d(TAG, "scanCurrentMaFiles, folder classify: $classify")
                        folderList.add(classify)
                    } else {
                        LogUtils.d(TAG, "scanCurrentMaFiles, isFile: ${file.name}")
                        val classify = Classify(
                            fileType = FileTypeChecker.getFileType(file),
                            name = file.name,
                            parentName = folder.name,
                            path = file.path,
                        )
                        LogUtils.d(TAG, "scanCurrentMaFiles, file classify: $classify")
                        fileList.add(classify)
                    }
                }
            }
        }.onFailure {
            LogUtils.e(TAG, "hideFile, e:$it")
        }
        return Pair(folderList, fileList)
    }

    @JvmStatic
    suspend fun scanImageFiles(context: Context, folder: File) {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                scanImages(folder)
            }
        }.onFailure {
            LogUtils.e(TAG, "hideFile, e:$it")
        }
    }

    private fun scanImages(folder: File) {
        val files = folder.listFiles() ?: return
        for (file in files) {
            if (file.isDirectory) {
                // 递归遍历子文件夹
                scanImages(file)
                LogUtils.d(TAG, "scanImageFiles, isDirectory:${file.name}")
            } else {
                // 判断文件是否为图片文件
                if (isImageFile(file)) {
                    // 扫描到图片文件
                    LogUtils.d(
                        TAG,
                        "scanImageFiles, Image File:${file.parentFile?.name},${file.absolutePath}"
                    )
                    // 进行相应操作
                }
            }
        }
    }

    private fun isImageFile(file: File): Boolean {
        val name = file.name
        val extension = name.substringAfterLast(".", "")
        val imageExtensions = arrayOf("jpg", "jpeg", "png", "gif", "bmp")
        for (imageExtension in imageExtensions) {
            if (extension.equals(imageExtension, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    /**
     * 删除文件
     */
    @JvmStatic
    suspend fun deleteFiles(deleteFiles: List<Classify>, block: () -> Unit) {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                LogUtils.d(TAG, "deleteFiles, deleteFiles: $deleteFiles")
                for (data in deleteFiles) {
                    data.path?.let { File(it).delete() }
                }
                withContext(Dispatchers.Main) {
                    block()
                }
            }.onFailure {
                LogUtils.e(TAG, "deleteFiles, e:$it")
            }
        }
    }

    /**
     * 移动文件
     */
    @JvmStatic
    suspend fun removeFiles(removeFiles: List<Classify>, targetPath: String, block: () -> Unit) {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                LogUtils.d(TAG, "removeFiles, removeFiles: $removeFiles")
                for (data in removeFiles) {
                    if (data.path == null) {
                        return@withContext
                    }
                    val sourceFile = File(data.path)
                    val targetFile =
                        File(targetPath, data.name ?: System.currentTimeMillis().toString())
                    LogUtils.d(TAG, "removeFiles, $sourceFile: $targetFile")
                    sourceFile.renameTo(targetFile)
                }
                withContext(Dispatchers.Main) {
                    block()
                }
            }.onFailure {
                LogUtils.e(TAG, "removeFiles, e:$it")
            }
        }
    }
}