package com.example.srbus.presentation.search

import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.local.searchStationHistory.SearchStationHistory
import com.example.srbus.data.remote.searchStation.SearchStationItem
import com.example.srbus.data.remote.searchStation.SearchStation

interface SearchStationContract {

    interface View {
        var searchKeyword: String?

        fun showSearchStationHistory(stations: List<SearchStationHistory>)
        fun showSearchStation(searchStation: SearchStation)
        fun hideRecyclerView()

        fun showNoSearchHistoryMessage()
        fun hideNoSearchHistoryMessage()

        fun showNoSearchResultMessage()
        fun hideNoSearchResultMessage()

        fun startStationActivity(station: SearchStationItem)
    }

    interface Presenter {
        fun getSearchStation(keyword: String)

        fun insertSearchStationHistory(station: SearchStationItem)
        fun deleteAllSearchStationHistories()
        fun deleteSearchStationHistory(arsId: String)
        fun getSearchStationHistories()

        fun getAllFavoriteStations(): List<FavoriteStation>
        fun addFavoriteStation(station: SearchStationHistory)
        fun removeFavoriteStation(station: SearchStationHistory)
    }
}