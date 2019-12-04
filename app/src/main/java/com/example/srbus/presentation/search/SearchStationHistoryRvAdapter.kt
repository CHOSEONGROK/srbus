package com.example.srbus.presentation.search

import android.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.srbus.R
import com.example.srbus.data.local.searchStationHistory.SearchStationHistory
import kotlinx.android.synthetic.main.fragment_search_station_rv_item.view.*

class SearchStationHistoryRvAdapter(
    private val view: SearchStationContract.View,
    private val presenter: SearchStationContract.Presenter
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = javaClass.simpleName

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_FOOTER = 1
    }

    private val context = (view as SearchStationFragment).context!!
    private var recentSearchStations = arrayListOf<SearchStationHistory>()

    override fun getItemCount(): Int = recentSearchStations.size + 1

    override fun getItemViewType(position: Int): Int = when (position < itemCount - 1) {
        true -> VIEW_TYPE_ITEM
        false -> VIEW_TYPE_FOOTER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        VIEW_TYPE_ITEM -> ItemViewHolder(parent)
        VIEW_TYPE_FOOTER -> FooterViewHolder(parent)
        else -> throw IllegalArgumentException("ViewType 에러, viewType=$viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val station = recentSearchStations[position]

            holder.itemView.setOnClickListener {
                view.startStationActivity(station.convert())
            }

            holder.itemView.setOnLongClickListener {
                AlertDialog.Builder(context)
                    .setMessage(R.string.alert_dialog_message_delete_recent_search_station_history)
                    .setPositiveButton("확인") { _, _ -> presenter.deleteSearchStationHistory(station.arsId) }
                    .setNegativeButton("취소") { _, _ -> }
                    .create()
                    .show()
                false
            }

            holder.tvStationName.text = station.stNm
            holder.tvStationNumber.text = station.arsId
            holder.tvSeparator.visibility = View.GONE
            holder.tvNextStation.visibility = View.GONE


            holder.ivFavorite.setColorFilter(station.getFavoriteColor(context))
            holder.ivFavorite.setOnClickListener {
                if (station.isFavorite) {
                    presenter.removeFavoriteStation(station)
                    Toast.makeText(context, R.string.toast_message_remove_favorite, Toast.LENGTH_SHORT).show()
                } else {
                    presenter.addFavoriteStation(station)
                    Toast.makeText(context, R.string.toast_message_add_favorite, Toast.LENGTH_SHORT).show()
                }
                station.isFavorite = !(station.isFavorite)
                holder.ivFavorite.setColorFilter(station.getFavoriteColor(context))
            }

        } else if (holder is FooterViewHolder) {
            holder.itemView.setOnClickListener {
                AlertDialog.Builder(context)
                    .setMessage(R.string.alert_dialog_message_delete_all_recent_search_station_history)
                    .setPositiveButton("확인") { _, _ ->
                        presenter.deleteAllSearchStationHistories()
                        presenter.getSearchStationHistories() }
                    .setNegativeButton("취소") { _, _ -> }
                    .create()
                    .show()
            }
        }
    }

    fun setSearchStationHistories(stations: List<SearchStationHistory>) {
        recentSearchStations.clear()
        stations.forEach {
            recentSearchStations.add(0, it)
        }
        notifyDataSetChanged()
    }

    class ItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_search_station_rv_item, parent, false)) {

        val tvStationName: TextView = itemView.tv_station_name
        val tvStationNumber: TextView = itemView.tv_station_number
        val tvSeparator: TextView = itemView.tv_separator
        val tvNextStation: TextView = itemView.tv_next_station
        val ivFavorite: ImageView = itemView.iv_station_favorite
    }

    class FooterViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_search_station_rv_footer, parent, false))
}