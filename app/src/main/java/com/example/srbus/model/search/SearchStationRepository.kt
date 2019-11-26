package com.example.srbus.model.search

import android.content.Context
import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.local.recentSearchStation.RecentSearchStation
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

    override fun deleteAllRecentSearchStation() {
        dataSource.deleteAllRecentSearchStation()
    }

    override fun deleteRecentSearchStation(arsId: String) {
        dataSource.deleteRecentSearchStation(arsId)
    }

    override fun getRecentSearchStation(callback: SearchStationSource.LoadRecentSearchStationCallBack) {
        dataSource.getRecentSearchStation(object : SearchStationSource.LoadRecentSearchStationCallBack {
            override fun onLoadData(stations: List<RecentSearchStation>) {
                callback.onLoadData(stations)
            }
        })
    }

    override fun getAllFavoriteStations(): List<FavoriteStation>
            = dataSource.getAllFavoriteStations()

    override fun addFavoriteStation(station: SearchStationItem) {
        dataSource.addFavoriteStation(station)
    }

    override fun removeFavoriteStation(station: SearchStationItem) {
        dataSource.removeFavoriteStation(station)
    }
}