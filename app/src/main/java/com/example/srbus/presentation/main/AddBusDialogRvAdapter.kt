package com.example.srbus.presentation.main

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.srbus.R
import com.example.srbus.data.local.favorite.FavoriteStationInBus
import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.data.remote.arrBus.ArrBusItem
import com.example.srbus.utils.Util
import kotlinx.android.synthetic.main.activity_main_add_bus_dialog_rv_item_bus.view.*
import kotlinx.android.synthetic.main.activity_main_add_bus_dialog_rv_item_bus_type.view.*
import java.lang.IllegalStateException

class AddBusDialogRvAdapter (
    private val fragment: AddBusDialog,
    private val arrBus: ArrBus?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = javaClass.simpleName

    companion object {
        const val VIEW_TYPE_BUS_TYPE = 0
        const val VIEW_TYPE_BUS = 1
    }

    private val items = arrayListOf<Any>()

    init {
        var priorRouteType = "null"
        arrBus?.msgBody?.itemList?.forEachIndexed { index, arrBusItem ->
            if (arrBusItem.routeType != priorRouteType) {
                items.add(arrBusItem.routeTypeString)
            }
            items.add(arrBusItem)
            priorRouteType = arrBusItem.routeType
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is String     -> VIEW_TYPE_BUS_TYPE
        is ArrBusItem -> VIEW_TYPE_BUS
        else -> throw IllegalStateException("getItemViewType($position), ViewType 에러(${items[position]})")
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        VIEW_TYPE_BUS_TYPE -> BusTypeViewHolder(parent)
        VIEW_TYPE_BUS      -> BusViewHolder(parent)
        else -> throw IllegalStateException("onCreateViewHolder($viewType), ViewType 에러")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BusTypeViewHolder -> {
                holder.tvBusType.text = "${items[position] as? String}버스"
            }
            is BusViewHolder -> {

                (items[position] as? ArrBusItem)?.let {
                    holder.tvBusNumber.text = it.rtNm
                    holder.tvBusNumber.setTextColor(it.getRouteTypeTextColor(fragment.context))

                    if (it.isFavorite) {
                        holder.ivAddFavoriteBus.background = ContextCompat.getDrawable(fragment.context!!, R.drawable.activity_main_add_bus_dialog_button_orange_bg)
                        holder.ivAddFavoriteBus.setImageResource(R.drawable.baseline_check_white_48dp)
                        holder.ivAddFavoriteBus.setColorFilter(Util.getColor(fragment.context!!, R.color.white))
                    } else {
                        holder.ivAddFavoriteBus.background = ContextCompat.getDrawable(fragment.context!!, R.drawable.activity_main_add_bus_dialog_button_white_bg)
                        holder.ivAddFavoriteBus.setImageResource(R.drawable.baseline_add_black_48dp)
                        holder.ivAddFavoriteBus.setColorFilter(Util.getColor(fragment.context!!, R.color.gray400))
                    }

                    holder.itemView.setOnClickListener { v ->
                        if (it.isFavorite) {
                            fragment.removeFavoriteBus(it.busRouteId)
                        } else {
                            fragment.addFavoriteBus(
                                FavoriteStationInBus(null, it.arsId, it.busRouteId, it.rtNm, it.routeType)
                            )
                        }
                        it.isFavorite = !(it.isFavorite)
                        notifyItemChanged(position)
                    }
                }
            }
        }
    }

    class BusTypeViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main_add_bus_dialog_rv_item_bus_type, parent, false)) {

        val tvBusType: TextView = itemView.tv_bus_type
    }

    class BusViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main_add_bus_dialog_rv_item_bus, parent, false)) {

        val tvBusNumber: TextView = itemView.tv_bus_number
        val ivAddFavoriteBus: ImageView = itemView.iv_add_favorite_bus
    }
}