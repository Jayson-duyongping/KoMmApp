package com.jayson.komm.common.net

import com.jayson.komm.common.util.LogUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @Author: Jayson
 * @CreateDate: 2023/4/2 17:03
 * @Version: 1.0
 * @Description:
 */
object RetrofitClient {
    private const val TAG = "RetrofitClient"
    private const val CALL_TIME_OUT = 30L

    private val client: OkHttpClient by lazy { newClient() }

    private val loggingInterceptor: Interceptor by lazy {
        HttpLoggingInterceptor { message ->
            LogUtils.d(TAG, message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    /**
     * OkHttpClient客户端
     */
    private fun newClient(): OkHttpClient = OkHttpClient.Builder().apply {
        callTimeout(CALL_TIME_OUT, TimeUnit.SECONDS)
        addInterceptor(loggingInterceptor)// 仅debug模式启用日志过滤器
    }.build()

    /**
     * 创建API Service接口实例
     */
    fun <T> createService(baseUrl: String, clazz: Class<T>): T =
        Retrofit.Builder().baseUrl(baseUrl).client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(clazz)
}