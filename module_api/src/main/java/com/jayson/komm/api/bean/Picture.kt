package com.jayson.komm.api.bean

/**
 * @Author: Jayson
 * @CreateDate: 2023/4/9 7:53
 * @Version: 1.0
 * @Description:
 */
data class Picture(
    val hoverUrl: String,
    val oriTitle: String,
    val thumbnailUrl: String,
    val width: Int,
    val height: Int
)