package com.example.srbus.presentation.search

import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.local.recentSearchStation.RecentSearchStation
import com.example.srbus.data.remote.searchStation.SearchStationItem
import com.example.srbus.data.remote.searchStation.SearchStation

interface SearchStationContract {

    interface View {
        var searchKeyword: String?

        fun showSearchStation(searchStation: SearchStation)
        fun showRecentSearchStation(stations: List<RecentSearchStation>)
        fun changeVisibleDeleteHistoryButton(isVisible: Boolean)

        fun startStationActivity(station: SearchStationItem)
    }

    interface Presenter {
        fun getSearchStation(keyword: String)

        fun insertRecentSearchStation(station: SearchStationItem)
        fun deleteAllRecentSearchStation()
        fun deleteRecentSearchStation(arsId: String)
        fun getRecentSearchStation()

        fun getAllFavoriteStations(): List<FavoriteStation>
        fun addFavoriteStation(station: SearchStationItem)
        fun removeFavoriteStation(station: SearchStationItem)
    }
}