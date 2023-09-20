package com.jayson.komm.common.bean

import kotlinx.serialization.Serializable

@Serializable
data class FileInfo(
    val fileName: String = "",
    val fileSize: Long = 0,
    val width: Int = 0,
    val height: Int = 0,
    val directory: String = "",
    val bucket: String = "",
    val path: String = "",
    val fileType: String = "",
)