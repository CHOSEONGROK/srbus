package com.example.srbus.presentation.station

import com.example.srbus.data.local.favorite.FavoriteStationInBus
import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.data.remote.arrBus.ArrBusItem
import com.example.srbus.data.remote.searchStation.SearchStationItem

interface StationContract {

    interface View {
        fun showArrBus(arrBus: ArrBus?)
        fun showNextStationDirection(nextDirection: String)
        fun decreaseRemainingTimeByOneSecond()

        fun setFavoriteStation()
        fun removeFavoriteStation()

        fun setFavoriteBus(position: Int)
        fun removeFavoriteBus(position: Int)

        fun showLoadingSpinner()
        fun hideLoadingSpinner()

        fun startAlarmActivity(arsId: String, stationName: String, busNumber: String)
    }

    interface Presenter {
        fun loadArrBus(arsId: String)

        fun isFavoriteStation(arsId: String): Boolean
        fun addFavoriteStation(station: SearchStationItem, nextStation: String)
        fun removeFavoriteStation(station: SearchStationItem)

        fun getFavoriteBusList(arsId: String): List<FavoriteStationInBus>
        fun addFavoriteBus(bus: ArrBusItem, position: Int)
        fun removeFavoriteBus(bus: ArrBusItem, position: Int)

        fun startAutoRequestTimer(arsId: String)
        fun finishAutoRequestTimer()
    }
}