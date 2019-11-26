package com.example.srbus.model.main

import android.content.Context
import android.os.Handler
import android.util.Log
import com.example.srbus.data.local.favorite.*
import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.retrofit.NetRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainDataSource(
    private val context: Context
) : MainSource {
    private val TAG = javaClass.simpleName

    private val favoriteStationDao = FavoriteStationDB
        .getInstance(context)
        .favoriteStationDao()

    private val favoriteStationInBusDao = FavoriteStationInBusDB
        .getInstance(context)
        .favoriteStationInBusDao()

    override fun getArrBus(callback: MainSource.LoadArrBusCallBack, arsIds: List<String>) {
        GlobalScope.launch(Dispatchers.Default) {

            arsIds.forEach {
                NetRetrofit
                    .service
                    .getArrBusByUidItem(NetRetrofit.getURLArrBusByUidItem(it))
                    .enqueue(object : Callback<ArrBus> {
                        override fun onResponse(call: Call<ArrBus>, response: Response<ArrBus>) {
                            Log.i(TAG, "onResponse() \ncall: $call \nresponse: $response \nbody: ${response.body().toString()}")
                            callback.onLoadArrBus(response.body(), it)
                        }

                        override fun onFailure(call: Call<ArrBus>, t: Throwable) {
                            Log.e(TAG, "onFailure() \ncall: $call \nt: $t")
                        }
                    })
            }
        }
    }

    override fun getAllFavoriteStations(): List<FavoriteStation>
            = favoriteStationDao.getAllFavoriteStation()

    override fun getAllFavoriteStationInBuses(): List<FavoriteStationInBus>
            = favoriteStationInBusDao.getAll()
}