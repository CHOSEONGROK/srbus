package com.example.srbus.presentation.station

import android.util.Log
import android.widget.Toast
import com.example.srbus.R
import com.example.srbus.data.local.favorite.FavoriteStationInBus
import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.data.remote.arrBus.ArrBusItem
import com.example.srbus.data.remote.searchStation.SearchStationItem
import com.example.srbus.model.station.StationRepository
import com.example.srbus.model.station.StationSource
import java.util.*

class StationPresenter(
    private val view: StationContract.View
) : StationContract.Presenter {
    private val TAG = javaClass.simpleName

    private val context = view as StationActivity
    private val repository = StationRepository(context)
    private var timer: Timer? = null


    override fun loadArrBus(arsId: String) {
        view.showLoadingSpinner()
        repository.getBus(object : StationSource.LoadDataCallBack {
            override fun onLoadData(arrBus: ArrBus?) {
                Log.i(TAG, "onLoadData()")
                view.showNextStationDirection(getNextStation(arrBus))
                view.showArrBus(arrBus)
                view.hideLoadingSpinner()
            }
        }, arsId)
    }

    override fun isFavoriteStation(arsId: String)
            = repository.isFavoriteStation(arsId)

    override fun addFavoriteStation(station: SearchStationItem, nextStation: String) {
        repository.addFavoriteStation(station, nextStation)
        view.setFavoriteStation()
        Toast.makeText(context, R.string.toast_message_add_favorite, Toast.LENGTH_SHORT).show()
    }

    override fun removeFavoriteStation(station: SearchStationItem) {
        repository.removeFavoriteStation(station)
        view.removeFavoriteStation()
        loadArrBus(station.arsId)
        Toast.makeText(context, R.string.toast_message_remove_favorite, Toast.LENGTH_SHORT).show()
    }

    override fun getFavoriteBusList(arsId: String): List<FavoriteStationInBus>
            = repository.getFavoriteBusList(arsId)

    override fun addFavoriteBus(bus: ArrBusItem, position: Int) {
        repository.addFavoriteBus(bus)
        view.setFavoriteBus(position)
        Toast.makeText(context, R.string.toast_message_add_favorite, Toast.LENGTH_SHORT).show()
    }

    override fun removeFavoriteBus(bus: ArrBusItem, position: Int) {
        repository.removeFavoriteBus(bus)
        view.removeFavoriteBus(position)
        Toast.makeText(context, R.string.toast_message_remove_favorite, Toast.LENGTH_SHORT).show()
    }

    override fun startAutoRequestTimer(arsId: String) {
        timer = Timer()
        timer?.schedule(AutoRequestTimerTask(arsId), 0, 1000)
    }

    override fun finishAutoRequestTimer() {
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    private fun getNextStation(arrBus: ArrBus?): String {
        val map = HashMap<String, Int>()

        arrBus?.msgBody?.itemList?.forEach {
            map.put(it.nxtStn, (map[it.nxtStn]?: 0) + 1)
        }

        var mostItem = ""
        var numberOfMostItem = 0
        map.forEach {
            if (it.value > numberOfMostItem) {
                mostItem = it.key
                numberOfMostItem = it.value
            }
        }
        return "$mostItem 방면"
    }

    private inner class AutoRequestTimerTask(
        private val arsId: String
    ) : TimerTask() {
        private var timer = -1

        override fun run() {
            if (timer % 10 == 0) Log.d(TAG, "TimerTask($timer)")
            context.runOnUiThread {
                if (timer % 30 == 0) {
                    loadArrBus(arsId)
                }
                view.decreaseRemainingTimeByOneSecond()
            }
            timer++
        }
    }
}