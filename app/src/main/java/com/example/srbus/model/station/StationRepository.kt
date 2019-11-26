package com.example.srbus.model.station

import android.content.Context
import com.example.srbus.data.local.favorite.FavoriteStationInBus
import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.data.remote.arrBus.ArrBusItem
import com.example.srbus.data.remote.searchStation.SearchStationItem

class StationRepository(context: Context) : StationSource {

    private val dataSource = StationDataSource(context)

    override fun getBus(callBack: StationSource.LoadDataCallBack, arsId: String) {
        dataSource.getBus(object : StationSource.LoadDataCallBack {
            override fun onLoadData(arrBus: ArrBus?) {
                callBack.onLoadData(arrBus)
            }
        }, arsId)
    }

    override fun isFavoriteStation(arsId: String): Boolean {
        return dataSource.isFavoriteStation(arsId)
    }

    override fun addFavoriteStation(station: SearchStationItem, nextStation: String) {
        dataSource.addFavoriteStation(station, nextStation)
    }

    override fun removeFavoriteStation(station: SearchStationItem) {
        dataSource.removeFavoriteStation(station)
    }

    override fun getFavoriteBusList(arsId: String): List<FavoriteStationInBus>
            = dataSource.getFavoriteBusList(arsId)

    override fun addFavoriteBus(bus: ArrBusItem) {
        dataSource.addFavoriteBus(bus)
    }

    override fun removeFavoriteBus(bus: ArrBusItem) {
        dataSource.removeFavoriteBus(bus)
    }
}