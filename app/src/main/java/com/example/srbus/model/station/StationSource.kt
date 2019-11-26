package com.example.srbus.model.station

import com.example.srbus.data.local.favorite.FavoriteStationInBus
import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.data.remote.arrBus.ArrBusItem
import com.example.srbus.data.remote.searchStation.SearchStationItem

interface StationSource {

    interface LoadDataCallBack {
        fun onLoadData(arrBus: ArrBus?)
    }

    fun getBus(callBack: LoadDataCallBack, arsId: String)

    fun isFavoriteStation(arsId: String): Boolean
    fun addFavoriteStation(station: SearchStationItem, nextStation: String)
    fun removeFavoriteStation(station: SearchStationItem)

    fun getFavoriteBusList(arsId: String): List<FavoriteStationInBus>
    fun addFavoriteBus(bus: ArrBusItem)
    fun removeFavoriteBus(bus: ArrBusItem)
}