package com.example.srbus.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

object NetRetrofit {

    const val BASE_URL = "http://ws.bus.go.kr/api/rest/"
    const val SERVICE_KEY = "y%2FOnGVM1gOI5vdueI0B%2FpXijRrFGgMc%2FUccGCdwxl14gBQjTJ%2FTujbvUTLkpaIKxWb2xLW828iFFGO2Y4fxOKg%3D%3D"

    private const val TIME_OUT = 10000L

    private var _service: RetrofitService? = null

    val service: RetrofitService
        get() = _service ?: createService().also { _service = it }

    private fun createService(): RetrofitService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .client(createOkHttpClient())
            .build()
            .create(RetrofitService::class.java)

    private fun createOkHttpClient() =
        OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .addInterceptor(createLoggingInterceptor())
            .build()

    private fun createLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }



    fun getURLArrBusByUidItem(arsId: String) = "stationinfo/getStationByUid?serviceKey=$SERVICE_KEY&arsId=$arsId"
    fun getURLStationByNameList(stSrch: String) = "stationinfo/getStationByName?serviceKey=$SERVICE_KEY&stSrch=$stSrch"
}