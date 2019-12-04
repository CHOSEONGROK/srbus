package com.example.srbus.model.search

import android.content.Context
import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.local.searchStationHistory.SearchStationHistory
import com.example.srbus.data.remote.searchStation.SearchStationItem
import com.example.srbus.data.remote.searchStation.SearchStation

class SearchStationRepository(
    private val context: Context
) : SearchStationSource {

    private val dataSource = SearchStationDataSource(context)

    override fun getSearchStation(callback: SearchStationSource.LoadDataCallBack, stSrch: String) {
        dataSource.getSearchStation(object : SearchStationSource.LoadDataCallBack {
            override fun onLoadData(searchStation: SearchStation?) {
                callback.onLoadData(searchStation)
            }
        }, stSrch)
    }

    override fun insertRecentSearchStation(station: SearchStationItem) {
        dataSource.insertRecentSearchStation(station)
    }

    override fun deleteAllSearchStationHistories() {
        dataSource.deleteAllSearchStationHistories()
    }

    override fun deleteSearchStationHistory(arsId: String) {
        dataSource.deleteSearchStationHistory(arsId)
    }

    override fun getSearchStationHistories(callback: SearchStationSource.LoadRecentSearchStationCallBack) {
        dataSource.getSearchStationHistories(object : SearchStationSource.LoadRecentSearchStationCallBack {
            override fun onLoadData(stations: List<SearchStationHistory>) {
                callback.onLoadData(stations)
            }
        })
    }

    override fun getAllFavoriteStations(): List<FavoriteStation>
            = dataSource.getAllFavoriteStations()

    override fun addFavoriteStation(station: SearchStationHistory) {
        dataSource.addFavoriteStation(station)
    }

    override fun removeFavoriteStation(station: SearchStationHistory) {
        dataSource.removeFavoriteStation(station)
    }
}