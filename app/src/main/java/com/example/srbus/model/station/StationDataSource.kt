package com.example.srbus.model.station

import android.content.Context
import android.os.Handler
import android.util.Log
import com.example.srbus.data.local.favorite.FavoriteStationInBus
import com.example.srbus.data.local.favorite.FavoriteStationInBusDB
import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.local.favorite.FavoriteStationDB
import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.data.remote.arrBus.ArrBusItem
import com.example.srbus.data.remote.searchStation.SearchStationItem
import com.example.srbus.retrofit.NetRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StationDataSource(context: Context) : StationSource {
    private val TAG = javaClass.simpleName

    private val favoriteStationDao = FavoriteStationDB
        .getInstance(context)
        .favoriteStationDao()

    private val favoriteBusDao = FavoriteStationInBusDB
        .getInstance(context)
        .favoriteStationInBusDao()

    override fun getBus(callBack: StationSource.LoadDataCallBack, arsId: String) {
        GlobalScope.launch(Dispatchers.Default) {
            val res = NetRetrofit.service.getArrBusByUidItem(NetRetrofit.getURLArrBusByUidItem(arsId))

            res.enqueue(object : Callback<ArrBus> {
                override fun onResponse(call: Call<ArrBus>, response: Response<ArrBus>) {
                    Log.i(TAG, "onResponse() \ncall: $call \nresponse: $response \nbody: ${response.body().toString()}")
                    callBack.onLoadData(response.body().also {
                        for (i in 0 until it?.msgBody!!.itemList!!.size) {
                            it.msgBody.itemList[i].arrmsg1 = it.msgBody.itemList[i].arrmsg1
                            it.msgBody.itemList[i].arrmsg2 = it.msgBody.itemList[i].arrmsg2
                        }
                    })
                }

                override fun onFailure(call: Call<ArrBus>, t: Throwable) {
                    Log.e(TAG, "onFailure() \ncall: $call \nt: $t")

                }
            })
        }
    }

    override fun isFavoriteStation(arsId: String): Boolean {
        return (favoriteStationDao.getFavoriteStation(arsId) != null)
    }

    override fun addFavoriteStation(station: SearchStationItem, nextStation: String) {
        favoriteStationDao.insert(
            FavoriteStation(null, station.arsId, station.stId, station.stNm, nextStation)
        )
    }

    override fun removeFavoriteStation(station: SearchStationItem) {
        favoriteStationDao.delete(station.arsId)
        favoriteBusDao.deleteByArsId(station.arsId)
    }

    override fun getFavoriteBusList(arsId: String): List<FavoriteStationInBus>
            = favoriteBusDao.getItemsByArsId(arsId)

    override fun addFavoriteBus(bus: ArrBusItem) {
        favoriteBusDao.insert(
            FavoriteStationInBus(null, bus.arsId, bus.busRouteId, bus.rtNm, bus.routeType)
        )
    }

    override fun removeFavoriteBus(bus: ArrBusItem) {
        favoriteBusDao.deleteByBusRouteId(bus.busRouteId)
    }
}