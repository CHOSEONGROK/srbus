package com.example.srbus.presentation.main

import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.remote.arrBus.ArrBus

interface MainContract {

    interface View {
        fun showArrBus(arrBus: ArrBus?, arsId: String)

        fun showLoadingSpinner()

        fun decreaseRemainingTimeByOneSecond()

        fun showAddBusDialog(favoriteStation: FavoriteStation, arrBus: ArrBus)

        fun startStationActivity(favoriteStation: FavoriteStation)
        fun startAlarmActivity(arsId: String, stationName: String, busNumber: String)
    }

    interface Presenter {
        fun getFavoriteStationAndBus(): ArrayList<MainRvAdapter.Favorite>

        fun getAllArrBus(favorites: ArrayList<MainRvAdapter.Favorite>)
        fun getArrBus(arsId: String)

        fun startAutoRequestTimer(favorites: ArrayList<MainRvAdapter.Favorite>)
        fun finishAutoRequestTimer()
    }
}