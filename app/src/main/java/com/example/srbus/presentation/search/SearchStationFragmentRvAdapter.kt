package com.example.srbus.presentation.search

import android.app.AlertDialog
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.srbus.R
import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.local.recentSearchStation.RecentSearchStation
import com.example.srbus.data.remote.searchStation.SearchStationItem
import com.example.srbus.data.remote.searchStation.SearchStation
import kotlinx.android.synthetic.main.fragment_search_station_rv_item.view.*

class SearchStationFragmentRvAdapter(
    private val view: SearchStationContract.View,
    private val presenter: SearchStationContract.Presenter
) : RecyclerView.Adapter<SearchStationFragmentRvAdapter.ViewHolder>() {
    companion object {
        const val TAG = "SearchStationFragmentRvAdapter"
        const val ITEMS_PER_PAGE = 50
    }

    var isShowCurrentRecentStations = true

    private val context = (view as SearchStationFragment).context!!
    private var searchStation: SearchStation = SearchStation()
    private var itemCountCurrentlyShown = 0
    private var favoriteStations: List<FavoriteStation>? = null

    override fun getItemCount() = itemCountCurrentlyShown

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val station = searchStation.msgBody.itemList[position]

            itemView.setOnClickListener {
                view.startStationActivity(station)
            }

            if (isShowCurrentRecentStations) {
                itemView.setOnLongClickListener {
                    AlertDialog.Builder(context)
                        .setMessage(R.string.alert_dialog_message_delete_recent_search_station_history)
                        .setPositiveButton("확인") { _, _ -> presenter.deleteRecentSearchStation(station.arsId) }
                        .setNegativeButton("취소") { _, _ -> }
                        .create()
                        .show()
                    false
                }
            }

            if (!isShowCurrentRecentStations && view.searchKeyword != null) {
                val start = station.stNm.indexOf(view.searchKeyword!!)

                if (start != -1) {
                    val ssb = SpannableStringBuilder(station.stNm)
                    ssb.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(context, R.color.red400)),
                        start,
                        start + view.searchKeyword!!.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    tvStationName.text = ssb
                } else {
                    tvStationName.text = station.stNm
                }
            } else {
                tvStationName.text = station.stNm
            }

            tvStationNumber.text = station.arsId
            tvSeparator.visibility = View.GONE
            tvNextStation.visibility = View.GONE

            favoriteStations?.forEach {
                if (station.arsId == it.arsId) {
                    station.isFavorite = true
                    return@forEach
                }
            }

            ivFavorite.setColorFilter(station.getFavoriteColor(context))
            ivFavorite.setOnClickListener {
                if (station.isFavorite) {
                    presenter.removeFavoriteStation(station)
                    Toast.makeText(context, R.string.toast_message_remove_favorite, Toast.LENGTH_SHORT).show()
                } else {
                    presenter.addFavoriteStation(station)
                    Toast.makeText(context, R.string.toast_message_add_favorite, Toast.LENGTH_SHORT).show()
                }
                station.isFavorite = !(station.isFavorite)
                ivFavorite.setColorFilter(station.getFavoriteColor(context))
            }
        }
    }

    fun showSearchStation(searchStation: SearchStation) {
        Log.i(TAG, "showRecentSearchStation(), size=${searchStation.msgBody.itemList.size}")
        this.searchStation = searchStation

        itemCountCurrentlyShown = if (searchStation.msgBody.itemList.size < ITEMS_PER_PAGE)
            searchStation.msgBody.itemList.size
        else
            ITEMS_PER_PAGE

        favoriteStations = presenter.getAllFavoriteStations()

        isShowCurrentRecentStations = false
        notifyDataSetChanged()
    }

    fun showRecentSearchStation(stations: List<RecentSearchStation>) {
        searchStation = SearchStation()

        val list = arrayListOf<SearchStationItem>()
        stations.map { it.convert() }
            .forEach { list.add(0, it) }

        searchStation.msgBody.itemList = list

        itemCountCurrentlyShown = if (searchStation.msgBody.itemList.size < ITEMS_PER_PAGE)
            searchStation.msgBody.itemList.size
        else
            ITEMS_PER_PAGE

        favoriteStations = presenter.getAllFavoriteStations()

        isShowCurrentRecentStations = true
        notifyDataSetChanged()
    }

    fun increase20ItemsToShow(lastVisibleItemPosition: Int) {
        Log.v(TAG, "increase20ItemsToShow($lastVisibleItemPosition), ITEMS_PER_PAGE=$ITEMS_PER_PAGE, itemCount=$itemCount")
//        if (lastVisibleItemPosition >= ITEMS_PER_PAGE - 1 &&
//            lastVisibleItemPosition == itemCount - 1) {
//            Log.v(TAG, "increase20ItemsToShow(ok)")
//            itemCountCurrentlyShown += ITEMS_PER_PAGE
////            notifyItemRangeInserted(itemCountCurrentlyShown, ITEMS_PER_PAGE)
////            notifyDataSetChanged()
//        }
//        itemCountCurrentlyShown += ITEMS_PER_PAGE
//        notifyItemRangeInserted(itemCountCurrentlyShown, ITEMS_PER_PAGE)
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_search_station_rv_item, parent, false)) {

        val tvStationName: TextView = itemView.tv_station_name
        val tvStationNumber: TextView = itemView.tv_station_number
        val tvSeparator: TextView = itemView.tv_separator
        val tvNextStation: TextView = itemView.tv_next_station
        val ivFavorite: ImageView = itemView.iv_station_favorite
    }
}