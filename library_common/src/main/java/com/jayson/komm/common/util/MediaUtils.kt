package com.jayson.komm.common.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.jayson.komm.common.bean.FileInfo
import java.io.File

object MediaUtils {

    private const val TAG = "MediaUtils"

    /**
     * 查询媒体库所有图片并分组
     */
    fun queryImages(context: Context?): Map<String, ArrayList<FileInfo>> {
        val fileMap: MutableMap<String, ArrayList<FileInfo>> = mutableMapOf()
        return kotlin.runCatching {
            LogUtils.d(TAG, "queryImages")
            val projection = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Files.FileColumns.MIME_TYPE
            )
            val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val cursor: Cursor? =
                context?.contentResolver?.query(
                    uri, projection, null, null, null
                )

            cursor?.use {
                val dataColumn: Int = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val bucketIndex: Int =
                    it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                val widthColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
                val heightColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)
                val mimeTypeColumn =
                    it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
                while (it.moveToNext()) {
                    val imagePath: String = it.getString(dataColumn)
                    val bucketName: String = it.getString(bucketIndex)
                    val fileName = it.getString(nameColumn)
                    val fileSize = it.getLong(sizeColumn)
                    val width = it.getInt(widthColumn)
                    val height = it.getInt(heightColumn)
                    val directory = File(it.getString(dataColumn)).parent ?: ""
                    val mimeType = it.getString(mimeTypeColumn)
                    val imageInfo = FileInfo(
                        fileName = fileName,
                        fileSize = fileSize,
                        width = width,
                        height = height,
                        directory = directory,
                        bucket = bucketName,
                        path = imagePath,
                        fileType = mimeType
                    )
                    if (fileMap.containsKey(bucketName)) {
                        fileMap[bucketName]?.add(imageInfo)
                    } else {
                        val imageList: ArrayList<FileInfo> = ArrayList()
                        imageList.add(imageInfo)
                        fileMap[bucketName] = imageList
                    }
                }
            }
            fileMap
        }.getOrElse {
            LogUtils.e(TAG, "queryImages, e:$it")
            fileMap
        }
    }

    /**
     * 查询媒体库所有视频并分组
     */
    fun queryVideos(context: Context?): Map<String, ArrayList<FileInfo>> {
        val fileMap: MutableMap<String, ArrayList<FileInfo>> = mutableMapOf()
        return kotlin.runCatching {
            LogUtils.d(TAG, "queryVideo")
            val projection = arrayOf(
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Files.FileColumns.MIME_TYPE
            )
            val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val cursor: Cursor? =
                context?.contentResolver?.query(
                    uri, projection, null, null, null
                )

            cursor?.use {
                val dataColumn: Int = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                val bucketIndex: Int =
                    it.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                val widthColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)
                val heightColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
                val mimeTypeColumn =
                    it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
                while (it.moveToNext()) {
                    val imagePath: String = it.getString(dataColumn)
                    val bucketName: String = it.getString(bucketIndex)
                    val fileName = it.getString(nameColumn)
                    val fileSize = it.getLong(sizeColumn)
                    val width = it.getInt(widthColumn)
                    val height = it.getInt(heightColumn)
                    val directory = File(it.getString(dataColumn)).parent ?: ""
                    val mimeType = it.getString(mimeTypeColumn)
                    val imageInfo = FileInfo(
                        fileName = fileName,
                        fileSize = fileSize,
                        width = width,
                        height = height,
                        directory = directory,
                        bucket = bucketName,
                        path = imagePath,
                        fileType = mimeType
                    )
                    if (fileMap.containsKey(bucketName)) {
                        fileMap[bucketName]?.add(imageInfo)
                    } else {
                        val imageList: ArrayList<FileInfo> = ArrayList()
                        imageList.add(imageInfo)
                        fileMap[bucketName] = imageList
                    }
                }
            }
            fileMap
        }.getOrElse {
            LogUtils.e(TAG, "queryVideo, e:$it")
            fileMap
        }
    }

    /**
     * 查询媒体库所有音频并分组
     */
    fun queryAudios(context: Context?): Map<String, ArrayList<FileInfo>> {
        val fileMap: MutableMap<String, ArrayList<FileInfo>> = mutableMapOf()
        return kotlin.runCatching {
            LogUtils.d(TAG, "queryAudios")
            val projection = arrayOf(
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.WIDTH,
                MediaStore.Audio.Media.HEIGHT,
                MediaStore.Files.FileColumns.MIME_TYPE
            )
            val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val cursor: Cursor? =
                context?.contentResolver?.query(
                    uri, projection, null, null, null
                )

            cursor?.use {
                val dataColumn: Int = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val bucketIndex: Int =
                    it.getColumnIndexOrThrow(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val widthColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.WIDTH)
                val heightColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.HEIGHT)
                val mimeTypeColumn =
                    it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
                while (it.moveToNext()) {
                    val imagePath: String = it.getString(dataColumn)
                    val bucketName: String = it.getString(bucketIndex)
                    val fileName = it.getString(nameColumn)
                    val fileSize = it.getLong(sizeColumn)
                    val width = it.getInt(widthColumn)
                    val height = it.getInt(heightColumn)
                    val directory = File(it.getString(dataColumn)).parent ?: ""
                    val mimeType = it.getString(mimeTypeColumn)
                    val imageInfo = FileInfo(
                        fileName = fileName,
                        fileSize = fileSize,
                        width = width,
                        height = height,
                        directory = directory,
                        bucket = bucketName,
                        path = imagePath,
                        fileType = mimeType
                    )
                    if (fileMap.containsKey(bucketName)) {
                        fileMap[bucketName]?.add(imageInfo)
                    } else {
                        val imageList: ArrayList<FileInfo> = ArrayList()
                        imageList.add(imageInfo)
                        fileMap[bucketName] = imageList
                    }
                }
            }
            fileMap
        }.getOrElse {
            LogUtils.e(TAG, "queryAudios, e:$it")
            fileMap
        }
    }
}