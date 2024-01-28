package com.jayson.komm.api.net

import com.jayson.komm.api.bean.Film
import com.jayson.komm.common.net.RetrofitClient
import com.jayson.komm.common.util.LogUtils
import java.net.HttpURLConnection

object FilmHttpService {

    private const val TAG = "FilmHttpService"

    private const val BASE_URL = "https://api.wmdb.tv"

    val api by lazy {
        RetrofitClient.createService(BASE_URL, FilmApiService::class.java)
    }

    /**
     * 根据id获取影视数据
     */
    fun getFilmInfoByDoubanId(doubanId: String): Film? {
        kotlin.runCatching {
            val response = api.getFilmInfoById(doubanId).execute()
            if (response.code() == HttpURLConnection.HTTP_OK) {
                return response.body() as Film
            }
        }.onFailure {
            LogUtils.e(TAG, "getFilmInfoById, e:${it.message}")
        }
        return null
    }

    /**
     * 获取搜索列表
     */
    fun getSearchFilmListByKeywords(keywords: String): List<Film>? {
        kotlin.runCatching {
            val response = api.getSearchFilmListByKeywords(keywords, 10).execute()
            if (response.code() == HttpURLConnection.HTTP_OK) {
                return response.body()
            }
        }.onFailure {
            LogUtils.e(TAG, "getSearchFilmListByKeywords, e:${it.message}")
        }
        return null
    }
}