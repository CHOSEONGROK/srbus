package com.example.srbus.presentation.station

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.srbus.R
import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.data.remote.arrBus.ArrBusItem
import kotlinx.android.synthetic.main.activity_station_rv_item_contents.view.*
import kotlinx.android.synthetic.main.activity_station_rv_item_header.view.*
import kotlinx.android.synthetic.main.loading_progressbar.view.*

class StationRvAdapter(
    private val view: StationContract.View,
    private val presenter: StationContract.Presenter
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TAG = "StationRvAdapter"

        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ITEM = 1
    }

    private val context = view as StationActivity

    private var arrBus = ArrBus()
    private val listRvItem = arrayListOf<Any>()

    private var isLoading = false

    override fun getItemCount() = listRvItem.size

    override fun getItemId(position: Int): Long {
        return listRvItem[position].hashCode().toLong()
    }

    override fun getItemViewType(position: Int): Int = when (listRvItem[position]) {
        is ArrBusItem -> VIEW_TYPE_ITEM
        else -> VIEW_TYPE_HEADER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(parent)
            VIEW_TYPE_ITEM -> BusViewHolder(parent)
            else -> HeaderViewHolder(parent)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                with(holder) {
                    tvBusType.text = listRvItem[position].toString()
                }
            }
            is BusViewHolder -> {
                with(holder) {
                    val arrBusItem = listRvItem[position] as ArrBusItem

                    ivFavorite.setColorFilter(arrBusItem.getFavoriteColorFilter(context))
                    ivFavorite.setOnClickListener {
                        if (arrBusItem.isFavorite) {
                            presenter.removeFavoriteBus(arrBusItem, position)
                            ivFavorite.setColorFilter(arrBusItem.getFavoriteColorFilter(context))
                            arrBusItem.isFavorite = false
                        } else {
                            if (!presenter.isFavoriteStation(arrBusItem.arsId)) {
                                (view as StationActivity).addFavoriteStation()
                            }
                            presenter.addFavoriteBus(arrBusItem, position)
                            ivFavorite.setColorFilter(arrBusItem.getFavoriteColorFilter(context))
                            arrBusItem.isFavorite = true
                        }
                    }

                    tvBusNumber.text = arrBusItem.rtNm
                    tvBusNumber.setTextColor(arrBusItem.getRouteTypeTextColor(context))
                    tvBusNextStation.text = "${arrBusItem.nxtStn} 방향"

                    if (isLoading) {
                        loadingSpinner.visibility = View.VISIBLE
                        clBusRemainingTime.visibility = View.GONE
                    } else {
                        loadingSpinner.visibility = View.GONE
                        clBusRemainingTime.visibility = View.VISIBLE

                        tvFirstBusRemainingTime.text = arrBusItem.firstArrBusRemainingTime
                        if (arrBusItem.firstArrBusRemainingStation != null) {
                            tvFirstBusRemainingStation.text = arrBusItem.firstArrBusRemainingStation
                            tvFirstBusFullOrNot.text = arrBusItem.firstBusFullOrNot
                            llFirstBusRemainingStation.visibility = View.VISIBLE
                        } else {
                            llFirstBusRemainingStation.visibility = View.GONE
                        }

                        tvSecondBusRemainingTime.text = arrBusItem.secondArrBusRemainingTime
                        if (arrBusItem.secondArrBusRemainingStation != null) {
                            tvSecondBusRemainingStation.text = arrBusItem.secondArrBusRemainingStation
                            tvSecondBusFullOrNot.text = arrBusItem.secondBusFullOrNot
                            llSecondBusRemainingStation.visibility = View.VISIBLE
                        } else {
                            llSecondBusRemainingStation.visibility = View.GONE
                        }
                    }

                    ivBusAlarm.setOnClickListener {
                        view.startAlarmActivity(arrBusItem.arsId, arrBusItem.stNm, arrBusItem.rtNm)
                    }
                }
            }
        }
    }

    fun showArrBus(arrBus: ArrBus) {
        this.arrBus = arrBus
        listRvItem.clear()

        val items = arrBus.msgBody.itemList
        val favoriteBusList = presenter.getFavoriteBusList(items[0].arsId)

        favoriteBusList.forEach {
            Log.d(TAG, "favoriteBusList.forEach{}, \n$it")
        }

        items.forEachIndexed { _, arrBusItem ->
            favoriteBusList.forEach {
                if (arrBusItem.busRouteId == it.busRouteId) {
                    arrBusItem.isFavorite = true
                    return@forEach
                }
            }
        }

        for (i in 0 until items.size) {
            if (i == 0) {
                listRvItem.add(items[0].routeTypeString)
            } else {
                if (items[i - 1].routeType != items[i].routeType) {
                    listRvItem.add(items[i].routeTypeString)
                }
            }
            listRvItem.add(items[i])
        }

        notifyDataSetChanged()
    }

    fun decreaseRemainingTimeByOneSecond() {
        listRvItem.forEach {
            if (it is ArrBusItem) {
                it.decreaseArrBusRemainingTimeByOneSecond()
            }
        }
        notifyDataSetChanged()
    }

    fun showLoadingSpinner() {
        isLoading = true
        notifyDataSetChanged()
    }

    fun hideLoadingSpinner() {
        isLoading = false
        notifyDataSetChanged()
    }

    class HeaderViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_station_rv_item_header, parent, false)) {

        val tvBusType: TextView = itemView.tv_bus_type
    }

    class BusViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_station_rv_item_contents, parent, false)) {

        val ivFavorite: ImageView = itemView.iv_favorite
        val tvBusNumber: TextView = itemView.tv_bus_number
        val tvBusNextStation: TextView = itemView.tv_bus_next_station

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