package com.example.srbus.presentation.main

import android.util.Log
import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.model.main.MainRepository
import com.example.srbus.model.main.MainSource
import java.util.*
import kotlin.collections.ArrayList

class MainPresenter(
    private val view: MainContract.View
) : MainContract.Presenter {
    private val TAG = javaClass.simpleName

    private val context = view as MainActivity
    private val repository = MainRepository(context)
    private var timer: Timer? = null

    override fun getFavoriteStationAndBus(): ArrayList<MainRvAdapter.Favorite> {
        val favorites = arrayListOf<MainRvAdapter.Favorite>()

        val stations = repository.getAllFavoriteStations()
        val buses = repository.getAllFavoriteStationInBuses()

        stations.forEach { station ->
            favorites.add(MainRvAdapter.Favorite().also {
                it.station = station
            })

            buses.forEach { bus ->
                if (station.arsId == bus.arsId) {
                    favorites.add(MainRvAdapter.Favorite().also {
//                        it.station = station
                        it.bus = bus
                    })
                }
            }
        }

        return favorites
    }

    override fun getAllArrBus(favorites: ArrayList<MainRvAdapter.Favorite>) {
        val arsIds = mutableListOf<String>()
        favorites.forEach {
            if (it.station != null) {
                arsIds.add(it.station!!.arsId)
            }
        }

        repository.getArrBus(object : MainSource.LoadArrBusCallBack {
            override fun onLoadArrBus(arrBus: ArrBus?, arsId: String) {
//                Log.d(TAG, "onLoadArrBus() \n$arrBus")
                view.showArrBus(arrBus, arsId)
            }
        }, arsIds)
    }

    override fun getArrBus(arsId: String) {
        repository.getArrBus(object : MainSource.LoadArrBusCallBack {
            override fun onLoadArrBus(arrBus: ArrBus?, arsId: String) {
                view.showArrBus(arrBus, arsId)
            }
        }, listOf(arsId))
    }

    override fun startAutoRequestTimer(favorites: ArrayList<MainRvAdapter.Favorite>) {
        timer = Timer()
        timer?.schedule(AutoRequestTimerTask(favorites), 0, 1000)
    }

    override fun finishAutoRequestTimer() {
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    private inner class AutoRequestTimerTask(
        private val favorites: ArrayList<MainRvAdapter.Favorite>
    ) : TimerTask() {
        private var timer = -1

        override fun run() {
            if (timer % 10 == 0) Log.d(TAG, "TimerTask($timer)")
            context.runOnUiThread {
                if (timer % 30 == 0) {
                    getAllArrBus(favorites)
                }
                view.decreaseRemainingTimeByOneSecond()
            }
            timer++
        }
    }
}