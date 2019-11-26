package com.example.srbus.presentation.main

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.srbus.R
import com.example.srbus.data.local.favorite.FavoriteStationInBus
import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.remote.arrBus.ArrBus
import kotlinx.android.synthetic.main.activity_main_rv_item_bus.view.*
import kotlinx.android.synthetic.main.activity_main_rv_item_station.view.*
import kotlinx.android.synthetic.main.loading_progressbar.view.*
import java.util.HashMap

class MainRvAdapter(
    private val view: MainContract.View,
    private val presenter: MainContract.Presenter,
    private val layoutManager: MainActivity.CustomLinearLayoutManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = javaClass.simpleName

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_STATION = 1
        const val VIEW_TYPE_BUS = 2
    }

    private val activity = view as MainActivity

    var favorites = arrayListOf<Favorite>()

    override fun getItemCount() = favorites.size + 1

    override fun getItemViewType(position: Int) =
        if (position == 0)
            VIEW_TYPE_HEADER
        else if (favorites[position - 1].bus == null)
            VIEW_TYPE_STATION
        else
            VIEW_TYPE_BUS

    override fun getItemId(position: Int): Long = when (position == 0) {
        true -> 123456
        false -> favorites[position - 1].hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        VIEW_TYPE_HEADER  -> HeaderViewHolder(parent)
        VIEW_TYPE_STATION -> StationViewHolder(parent)
        VIEW_TYPE_BUS     -> BusViewHolder(parent)
        else -> throw IllegalArgumentException("VIewType 에러(viewType=$viewType)")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StationViewHolder -> {
                favorites[position - 1].station?.let {
                    holder.itemView.setOnClickListener { v ->
                        view.startStationActivity(it)
                    }

                    holder.tvStationName.text = it.stNm
                    if (it.nextStation == null || it.nextStation == "") {
                        holder.tvNextStation.visibility = View.GONE
                    } else {
                        holder.tvNextStation.visibility = View.VISIBLE
                        holder.tvNextStation.text = it.nextStation
                    }
                    holder.tvAddFavoriteBus.setOnClickListener { v ->
                        view.showAddBusDialog(it, favorites[position - 1].arrBus!!)
                    }
                }
            }
            is BusViewHolder -> {
                favorites[position - 1].let {
                    holder.tvBusNumber.text = it.bus!!.rtNm
                    holder.tvBusNumber.setTextColor(it.bus!!.getRouteTypeTextColor(activity))

                    if (it.arrBus == null) {
                        holder.loadingSpinner.visibility = View.VISIBLE
                        holder.clBusRemainingTime.visibility = View.GONE
                    } else {
                        holder.loadingSpinner.visibility = View.GONE
                        holder.clBusRemainingTime.visibility = View.VISIBLE

                        it.arrBus?.msgBody!!.itemList[it.indexArrBus].let { busItem ->
                            holder.tvFirstBusRemainingTime.text = busItem.firstArrBusRemainingTime
                            if (busItem.firstArrBusRemainingStation != null) {
                                holder.tvFirstBusRemainingStation.text = busItem.firstArrBusRemainingStation
                                holder.tvFirstBusFullOrNot.text = busItem.firstBusFullOrNot
                                holder.llFirstBusRemainingStation.visibility = View.VISIBLE
                            } else {
                                holder.llFirstBusRemainingStation.visibility = View.GONE
                            }

                            holder.tvSecondBusRemainingTime.text = busItem.secondArrBusRemainingTime
                            if (busItem.secondArrBusRemainingStation != null) {
                                holder.tvSecondBusRemainingStation.text = busItem.secondArrBusRemainingStation
                                holder.tvSecondBusFullOrNot.text = busItem.secondBusFullOrNot
                                holder.llSecondBusRemainingStation.visibility = View.VISIBLE
                            } else {
                                holder.llSecondBusRemainingStation.visibility = View.GONE
                            }

                            holder.ivBusAlarm.setOnClickListener { v ->
                                view.startAlarmActivity(busItem.arsId, busItem.stNm, busItem.rtNm)
                            }
                        }
                    }
                }
            }
        }
    }

    fun showFavoriteStationAndBusInfo(arrBus: ArrBus, arsId: String) {
        favorites.forEachIndexed { index, favorite ->
            Log.d(TAG, "showFavoriteStationAndBusInfo(), favorite=$favorite")

            favorite.station?.let {
                if (it.arsId == arsId) {
                    it.nextStation = getNextStation(arrBus)
                    favorite.arrBus = arrBus
                    notifyItemChanged(index)
                }
            }

            favorite.bus?.let {
                if (it.arsId == arsId) {
                    favorite.arrBus = arrBus

                    arrBus.msgBody.itemList.forEachIndexed { i, arrBusItem ->
                        if (it.busRouteId == arrBusItem.busRouteId) {
                            favorite.indexArrBus = i
                        }
                    }

                    Log.d(TAG, "showFavoriteStationAndBusInfo(), index=$index, favorite.indexArrBus=${favorite.indexArrBus}")
                    notifyItemChanged(index)
                }
            }
        }
    }

    fun showLoadingSpinner() {
        favorites.forEach {
            it.arrBus = null
        }
        notifyDataSetChanged()
    }

    fun decreaseRemainingTimeByOneSecond() {
        favorites.forEach {
            if (it.bus != null && it.arrBus != null) {
                it.arrBus!!.msgBody.itemList[it.indexArrBus].decreaseArrBusRemainingTimeByOneSecond()
            }
        }
        notifyDataSetChanged()
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

    class Favorite {
        var station: FavoriteStation? = null
        var bus: FavoriteStationInBus? = null
        var arrBus: ArrBus? = null
        var indexArrBus = -1

        override fun toString(): String {
            return "Favorite(station=$station, bus=$bus, arrBus=${(arrBus != null)}, indexArrBus=$indexArrBus)\n"
        }
    }

    inner class HeaderViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        HorizontalItemSlider<ConstraintLayout.LayoutParams>(parent.context, activity, layoutManager).apply {
            addView(HorizontalItemSlider.ItemView(R.drawable.baseline_alarm_add_black_48dp, "대신시장, 기업은행 41m", "주변 정류장", 0))
            addView(HorizontalItemSlider.ItemView(R.drawable.baseline_alarm_add_black_48dp, "대신시장, 기업은행 41m", "주변 정류장", 1))
            addView(HorizontalItemSlider.ItemView(R.drawable.baseline_alarm_add_black_48dp, "대신시장, 기업은행 41m", "주변 정류장", 2))
            addView(HorizontalItemSlider.ItemView(R.drawable.baseline_alarm_add_black_48dp, "대신시장, 기업은행 41m", "주변 정류장", 3))
        }
    )

    class StationViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main_rv_item_station, parent, false)) {

        val tvStationName: TextView = itemView.tv_station_name
        val tvNextStation: TextView = itemView.tv_next_station
        val tvAddFavoriteBus: TextView = itemView.tv_add_favorite_bus
    }

    class BusViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main_rv_item_bus, parent, false)) {

        val tvBusNumber: TextView = itemView.tv_bus_number

        val tvFirstBusRemainingTime: TextView = itemView.tv_first_bus_remaining_time
        val tvFirstBusRemainingStation: TextView = itemView.tv_first_bus_remaining_station
        val tvFirstBusFullOrNot: TextView = itemView.tv_first_bus_full_or_not
        val llFirstBusRemainingStation: LinearLayout = itemView.ll_first_bus_remaining_station

        val tvSecondBusRemainingTime: TextView = itemView.tv_second_bus_remaining_time
        val tvSecondBusRemainingStation: TextView = itemView.tv_second_bus_remaining_station
        val tvSecondBusFullOrNot: TextView = itemView.tv_second_bus_full_or_not
        val llSecondBusRemainingStation: LinearLayout = itemView.ll_second_bus_remaining_station

        val ivBusAlarm: ImageView = itemView.iv_bus_alarm

        val clBusRemainingTime: ConstraintLayout = itemView.cl_bus_remaining_time
        val loadingSpinner: ProgressBar = itemView.loading_spinner
    }

}