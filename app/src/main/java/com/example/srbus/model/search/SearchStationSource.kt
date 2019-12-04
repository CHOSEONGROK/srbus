package com.example.srbus.model.search

import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.local.searchStationHistory.SearchStationHistory
import com.example.srbus.data.remote.searchStation.SearchStationItem
import com.example.srbus.data.remote.searchStation.SearchStation

interface SearchStationSource {

    interface LoadDataCallBack {
        fun onLoadData(searchStation: SearchStation?)
    }

    fun getSearchStation(callback: LoadDataCallBack, stSrch: String)

    interface LoadRecentSearchStationCallBack {
        fun onLoadData(stations: List<SearchStationHistory>)
    }

    fun insertRecentSearchStation(station: SearchStationItem)
    fun deleteAllSearchStationHistories()
    fun deleteSearchStationHistory(arsId: String)
    fun getSearchStationHistories(callback: LoadRecentSearchStationCallBack)

    fun getAllFavoriteStations(): List<FavoriteStation>
    fun addFavoriteStation(station: SearchStationHistory)
    fun removeFavoriteStation(station: SearchStationHistory)
}