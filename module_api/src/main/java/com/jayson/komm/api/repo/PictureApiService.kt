package com.jayson.komm.api.repo

import com.jayson.komm.api.bean.BaseResult
import com.jayson.komm.api.bean.Picture
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Author: Jayson
 * @CreateDate: 2023/4/2 18:16
 * @Version: 1.0
 * @Description:
 */
interface PictureApiService {
    @GET("/api/so-baidu-img/")
    fun getPictureData(
        @Query("msg") msg: String,
        @Query("page") page: Int
    ): Call<BaseResult<Picture>>
}