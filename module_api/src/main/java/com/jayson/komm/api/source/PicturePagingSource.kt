package com.jayson.komm.api.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jayson.komm.api.bean.Picture
import com.jayson.komm.api.repo.PictureHttpService
import com.jayson.komm.common.util.LogUtils
import kotlinx.coroutines.*

/**
 * @Author: Jayson
 * @CreateDate: 2023/4/5 15:04
 * @Version: 1.0·
 * @Description:一个数据源，用于加载数据
 */
class PicturePagingSource(
    private val msg: String
) : PagingSource<Int, Picture>() {

    companion object {
        private const val TAG = "PicturePagingSource"
    }

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    override fun getRefreshKey(state: PagingState<Int, Picture>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Picture> {
        try {
            // 获取请求的页码
            val page = params.key ?: 1
            // 在协程中调用网络请求
            val response = withContext(Dispatchers.IO) { PictureHttpService.getPictureList(msg, page) }
            scope.cancel()
            // 计算下一页的页码
            val nextPage = if (response?.size!! > page * params.loadSize) {
                page + 1
            } else {
                null
            }
            // 返回数据
            return LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LogUtils.d(TAG, "load, e:${e.message}")
            scope.cancel()
            // 网络请求失败，返回错误信息
            return LoadResult.Error(e)
        }
    }
}