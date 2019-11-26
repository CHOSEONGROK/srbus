package com.example.srbus.presentation.search

import android.app.AlertDialog
import android.util.Log
import com.example.srbus.R
import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.local.recentSearchStation.RecentSearchStation
import com.example.srbus.data.remote.searchStation.SearchStationItem
import com.example.srbus.data.remote.searchStation.SearchStation
import com.example.srbus.model.search.SearchStationRepository
import com.example.srbus.model.search.SearchStationSource

class SearchStationPresenter(
    private val view: SearchStationContract.View
) : SearchStationContract.Presenter {
    private val TAG = javaClass.simpleName

    private val repository = SearchStationRepository((view as SearchStationFragment).context!!)

    override fun getSearchStation(keyword: String) {
        repository.getSearchStation(object : SearchStationSource.LoadDataCallBack {
            override fun onLoadData(searchStation: SearchStation?) {
                if (searchStation != null) {
                    view.showSearchStation(searchStation)
                } else {
                    Log.e(TAG, "onLoadData(null)")
                }
            }
        }, keyword)
    }

    override fun insertRecentSearchStation(station: SearchStationItem) {
        repository.insertRecentSearchStation(station)
    }

    override fun getRecentSearchStation() {
        repository.getRecentSearchStation(object : SearchStationSource.LoadRecentSearchStationCallBack {
            override fun onLoadData(stations: List<RecentSearchStation>) {
                view.showRecentSearchStation(stations)
            }
        })
    }

    override fun deleteAllRecentSearchStation() {
        repository.deleteAllRecentSearchStation()
    }

    override fun deleteRecentSearchStation(arsId: String) {
        repository.deleteRecentSearchStation(arsId)
        repository.getRecentSearchStation(object : SearchStationSource.LoadRecentSearchStationCallBack {
            override fun onLoadData(stations: List<RecentSearchStation>) {
                view.showRecentSearchStation(stations)
            }
        })
    }

    override fun getAllFavoriteStations(): List<FavoriteStation>
            = repository.getAllFavoriteStations()

    override fun addFavoriteStation(station: SearchStationItem) {
        repository.addFavoriteStation(station)
    }

    override fun removeFavoriteStation(station: SearchStationItem) {
        repository.removeFavoriteStation(station)
    }
}