package com.example.srbus.presentation.search

import android.util.Log
import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.local.searchStationHistory.SearchStationHistory
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
                if (searchStation != null && searchStation.msgBody.itemList.isNotEmpty()) {
                    view.hideNoSearchHistoryMessage()
                    view.hideNoSearchResultMessage()
                    view.showSearchStation(searchStation)
                } else {
                    Log.e(TAG, "onLoadData(null)")
                    view.hideRecyclerView()
                    view.hideNoSearchHistoryMessage()
                    view.showNoSearchResultMessage()
                }
            }
        }, keyword)
    }

    override fun insertSearchStationHistory(station: SearchStationItem) {
        repository.insertRecentSearchStation(station)
    }

    override fun getSearchStationHistories() {
        val favoriteStations = getAllFavoriteStations()

        repository.getSearchStationHistories(object : SearchStationSource.LoadRecentSearchStationCallBack {
            override fun onLoadData(stations: List<SearchStationHistory>) {
                if (stations.isNotEmpty()) {
                    stations.forEachIndexed { _, station ->
                        favoriteStations.forEach { favorite ->
                            if (station.stId == favorite.stId) {
                                station.isFavorite = true
                                return@forEach
                            }
                        }
                    }
                    view.hideNoSearchHistoryMessage()
                    view.hideNoSearchResultMessage()
                    view.showSearchStationHistory(stations)
                } else {
                    view.hideRecyclerView()
                    view.hideNoSearchResultMessage()
                    view.showNoSearchHistoryMessage()
                }
            }
        })
    }

    override fun deleteAllSearchStationHistories() {
        repository.deleteAllSearchStationHistories()
    }

    override fun deleteSearchStationHistory(arsId: String) {
        repository.deleteSearchStationHistory(arsId)
        repository.getSearchStationHistories(object : SearchStationSource.LoadRecentSearchStationCallBack {
            override fun onLoadData(stations: List<SearchStationHistory>) {
                view.showSearchStationHistory(stations)
            }
        })
    }

    override fun getAllFavoriteStations(): List<FavoriteStation>
            = repository.getAllFavoriteStations()

    override fun addFavoriteStation(station: SearchStationHistory) {
        repository.addFavoriteStation(station)
    }

    override fun removeFavoriteStation(station: SearchStationHistory) {
        repository.removeFavoriteStation(station)
    }
}