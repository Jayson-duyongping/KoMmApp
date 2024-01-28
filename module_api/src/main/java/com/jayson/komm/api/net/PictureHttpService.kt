package com.jayson.komm.api.net

import com.jayson.komm.api.bean.BaseResult
import com.jayson.komm.api.bean.Picture
import com.jayson.komm.common.net.RetrofitClient
import com.jayson.komm.common.util.LogUtils
import java.net.HttpURLConnection

/**
 * @Author: Jayson
 * @CreateDate: 2023/4/2 18:19
 * @Version: 1.0
 * @Description:
 */
object PictureHttpService {
    private const val TAG = "PictureHttpService"

    // 来自百度搜图
    private const val BASE_URL = "https://zj.v.api.aa1.cn"

    private val api by lazy {
        RetrofitClient.createService(BASE_URL, PictureApiService::class.java)
    }

    fun getPictureList(msg: String, page: Int): List<Picture>? {
        kotlin.runCatching {
            val response = api.getPictureData(msg, page).execute()
            if (response.code() == HttpURLConnection.HTTP_OK) {
                val result = response.body() as BaseResult<Picture>
                LogUtils.d(TAG, "getPictureList, ${result.code},${result.msg}")
                return result.data
            }
        }.onFailure {
            LogUtils.e(TAG, "getPictureList, e:${it.message}")
        }
        return null
    }
}