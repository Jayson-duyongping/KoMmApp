package com.jayson.komm.api.net

import com.jayson.komm.api.bean.BaseResult
import com.jayson.komm.api.bean.Mm
import com.jayson.komm.common.net.RetrofitClient
import com.jayson.komm.common.util.LogUtils
import java.net.HttpURLConnection

/**
 * @Author: Jayson
 * @CreateDate: 2023/4/2 18:19
 * @Version: 1.0
 * @Description:
 */
object MmHttpService {
    private const val TAG = "MmHttpService"

    private const val BASE_URL = "https://www.mxnzp.com"
    private const val APP_ID = "isymrxqtqksdffjk"
    private const val APP_SECRET = "L3JVcEFJcUlzdFlhamk0L2JzcnJIdz09"

    private val api by lazy {
        RetrofitClient.createService(BASE_URL, MmApiService::class.java)
    }

    fun getMmList(): List<Mm>? {
        kotlin.runCatching {
            val response = api.getMmData(APP_ID, APP_SECRET).execute()
            if (response.code() == HttpURLConnection.HTTP_OK) {
                val result = response.body() as BaseResult<Mm>
                LogUtils.d(TAG, "getMmList, ${result.code},${result.msg}")
                return result.data
            }
        }.onFailure {
            LogUtils.e(TAG, "getMmList, e:${it.message}")
        }
        return null
    }
}