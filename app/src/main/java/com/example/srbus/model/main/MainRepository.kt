package com.example.srbus.model.main

import android.content.Context
import android.util.Log
import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.local.favorite.FavoriteStationInBus
import com.example.srbus.data.remote.arrBus.ArrBus

class MainRepository(
    private val context: Context
) : MainSource {
    private val TAG = javaClass.simpleName

    private val dataSource = MainDataSource(context)

    override fun getArrBus(callback: MainSource.LoadArrBusCallBack, arsIds: List<String>) {
        dataSource.getArrBus(object : MainSource.LoadArrBusCallBack {
            override fun onLoadArrBus(arrBus: ArrBus?, arsId: String) {
                callback.onLoadArrBus(arrBus.also {
                    for (i in 0 until it?.msgBody!!.itemList!!.size) {
                        it.msgBody.itemList[i].arrmsg1 = it.msgBody.itemList[i].arrmsg1
                        it.msgBody.itemList[i].arrmsg2 = it.msgBody.itemList[i].arrmsg2
                    }
                }, arsId)
            }
        }, arsIds)
    }

    override fun getAllFavoriteStations(): List<FavoriteStation>
            = dataSource.getAllFavoriteStations()

    override fun getAllFavoriteStationInBuses(): List<FavoriteStationInBus>
            = dataSource.getAllFavoriteStationInBuses()
}