package com.example.srbus.model.search

import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.local.recentSearchStation.RecentSearchStation
import com.example.srbus.data.remote.searchStation.SearchStationItem
import com.example.srbus.data.remote.searchStation.SearchStation

interface SearchStationSource {

    interface LoadDataCallBack {
        fun onLoadData(searchStation: SearchStation?)
    }

    fun getSearchStation(callback: LoadDataCallBack, stSrch: String)

    interface LoadRecentSearchStationCallBack {
        fun onLoadData(stations: List<RecentSearchStation>)
    }

    fun insertRecentSearchStation(station: SearchStationItem)
    fun deleteAllRecentSearchStation()
    fun deleteRecentSearchStation(arsId: String)
    fun getRecentSearchStation(callback: LoadRecentSearchStationCallBack)

    fun getAllFavoriteStations(): List<FavoriteStation>
    fun addFavoriteStation(station: SearchStationItem)
    fun removeFavoriteStation(station: SearchStationItem)
}