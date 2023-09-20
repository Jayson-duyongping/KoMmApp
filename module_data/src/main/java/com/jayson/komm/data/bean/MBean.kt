package com.jayson.komm.data.bean

import androidx.annotation.Keep

@Keep
data class Classify(
    val fileType: String? = "",
    val parentName: String? = "",
    val name: String? = "",
    val path: String? = "",
    val dataList: List<Classify> = listOf(),
    var isChecked: Boolean = false
)