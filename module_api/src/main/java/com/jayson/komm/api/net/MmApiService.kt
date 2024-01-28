package com.jayson.komm.api.net

import com.jayson.komm.api.bean.BaseResult
import com.jayson.komm.api.bean.Mm
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Author: Jayson
 * @CreateDate: 2023/4/2 18:16
 * @Version: 1.0
 * @Description:
 */
interface MmApiService {
    @GET("/api/image/girl/list/random")
    fun getMmData(
        @Query("app_id") appId: String,
        @Query("app_secret") appSecret: String
    ): Call<BaseResult<Mm>>
}