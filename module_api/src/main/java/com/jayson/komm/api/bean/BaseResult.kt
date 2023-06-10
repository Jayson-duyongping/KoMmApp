package com.jayson.komm.api.bean

/**
 * @Author: Jayson
 * @CreateDate: 2023/4/9 7:56
 * @Version: 1.0
 * @Description:
 */
open class BaseResult<T>(
    val code: Int? = -1,
    val msg: String? = "",
    val `data`: List<T>? = null
)
