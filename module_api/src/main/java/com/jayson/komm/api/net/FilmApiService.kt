package com.jayson.komm.api.net

import com.jayson.komm.api.bean.Film
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 获取影视数据
 * Get https://api.wmdb.tv/movie/api?id=doubanid
 * 1292052
 * 全文模糊搜索，根据匹配分数排序
 * Get https://api.wmdb.tv/api/v1/movie/search?q=英雄本色&limit=10&skip=0&lang=Cn&year=2002
 * TOP250接口
 * Get https://api.wmdb.tv/api/v1/top?type=Imdb&skip=0&limit=50&lang=Cn
 */
interface FilmApiService {

    @GET("/movie/api")
    fun getFilmInfoById(@Query("id") doubanId: String): Call<Film>

    @GET("/api/v1/movie/search")
    fun getSearchFilmListByKeywords(
        @Query("q") keywords: String,
        @Query("limit") limit: Int
    ): Call<List<Film>>
}