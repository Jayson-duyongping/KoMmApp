package com.jayson.komm.api.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.jayson.komm.api.paging.PicturePagingSource

/**
 * @Author: Jayson
 * @CreateDate: 2023/4/5 15:44
 * @Version: 1.0
 * @Description:
 */
class PictureViewModel : ViewModel() {

    var msg: String = ""

    // 创建Pager对象
    val pager = Pager(
        // 配置分页参数
        PagingConfig(pageSize = 20, enablePlaceholders = false),
        // 传入数据源
        pagingSourceFactory = { PicturePagingSource(msg) }
    ).flow

    // 通过Pager对象获取数据流
    val data = pager.cachedIn(viewModelScope)
}