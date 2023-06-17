package com.jayson.komm.common.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast

object DownLoadUtils {

    /**
     * DownloadManager下载图片
     */
    fun downloadImage(context: Context, imageUrl: String) {
        if (imageUrl.isEmpty()) {
            Toast.makeText(context, "图片路径为空", Toast.LENGTH_SHORT).show()
            return
        }
        val downloadRequest = DownloadManager.Request(Uri.parse(imageUrl))
            .setTitle("图片下载")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "${System.currentTimeMillis()}.jpg"
            )
        val downloadManager =
            (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager)
        downloadManager.enqueue(downloadRequest)
        Toast.makeText(context, "图片开始下载", Toast.LENGTH_SHORT).show()
    }
}