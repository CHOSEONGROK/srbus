package com.example.srbus.retrofit

import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.data.remote.searchStation.SearchStation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface RetrofitService {
//    @GET("getStationByUid?serviceKey/{serviceKey}/arsId/{arsId}")
//    fun getListRespos(@Path("serviceKey") serviceKey: String, @Path("arsId") arsId: String): Call<ArrayList<JsonObject>>

//    @GET("stationinfo/getStationByUid")
//    fun getListRespos(@Query("serviceKey") serviceKey: String, @Query("arsId") arsId: String): Call<ArrayList<JsonObject>>

//    @GET("stationinfo/getStationByUid")
//    fun getListRespos(@Query("serviceKey") serviceKey: String, @Query("arsId") arsId: String): Call<ArrBusApi>

//    @GET("stationinfo/getStationByUid")
//    fun getListRespos(@Query("serviceKey", encoded = false) serviceKey: String, @Query("arsId") arsId: String): Call<ArrBus>

    @GET
    fun getArrBusByUidItem(@Url serviceKey: String): Call<ArrBus>

    @GET
    fun getStationByNameList(@Url serviceKey: String): Call<SearchStation>

}