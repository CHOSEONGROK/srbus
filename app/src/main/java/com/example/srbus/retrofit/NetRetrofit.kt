package com.example.srbus.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object NetRetrofit {

    const val BASE_URL = "http://ws.bus.go.kr/api/rest/"
    const val SERVICE_KEY = "y%2FOnGVM1gOI5vdueI0B%2FpXijRrFGgMc%2FUccGCdwxl14gBQjTJ%2FTujbvUTLkpaIKxWb2xLW828iFFGO2Y4fxOKg%3D%3D"

    init { }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()

    val service: RetrofitService = retrofit.create(RetrofitService::class.java)

    fun getURLArrBusByUidItem(arsId: String) = "stationinfo/getStationByUid?serviceKey=$SERVICE_KEY&arsId=$arsId"
    fun getURLStationByNameList(stSrch: String) = "stationinfo/getStationByName?serviceKey=$SERVICE_KEY&stSrch=$stSrch"
}