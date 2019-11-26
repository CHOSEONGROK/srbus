package com.example.srbus.model.main

import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.local.favorite.FavoriteStationInBus
import com.example.srbus.data.remote.arrBus.ArrBus

interface MainSource {

    interface LoadArrBusCallBack {
        fun onLoadArrBus(arrBus: ArrBus?, arsId: String)
    }

    fun getArrBus(callback: LoadArrBusCallBack, arsIds: List<String>)

    fun getAllFavoriteStations(): List<FavoriteStation>
    fun getAllFavoriteStationInBuses(): List<FavoriteStationInBus>



}