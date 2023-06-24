package com.jayson.komm.common.bean

data class Card(
    val title: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val imageResource: Int = 0,
    val images: List<Int>? = null
)
