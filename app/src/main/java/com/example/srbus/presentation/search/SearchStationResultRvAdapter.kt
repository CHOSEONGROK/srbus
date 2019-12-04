package com.example.srbus.presentation.search

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
import com.example.srbus.R
import com.example.srbus.data.remote.searchStation.SearchStation
import kotlinx.android.synthetic.main.fragment_search_station_rv_item.view.*

class SearchStationResultRvAdapter(
    private val view: SearchStationContract.View,
    private val presenter: SearchStationContract.Presenter
) : RecyclerView.Adapter<SearchStationResultRvAdapter.ViewHolder>() {
    private val TAG = javaClass.simpleName

    companion object {
        const val ITEMS_PER_PAGE = 20
    }

    private val context = (view as SearchStationFragment).context!!
    private var searchStation: SearchStation = SearchStation()
    private var searchStationItemSize = 0
    private var currentVisibleItemCount = 0

    override fun getItemCount() = currentVisibleItemCount

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with (holder) {
            val station = searchStation.msgBody.itemList[position]

            itemView.setOnClickListener {
                view.startStationActivity(station)
            }

            if (view.searchKeyword != null) {
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
            ivFavorite.visibility = View.GONE
        }
    }

    fun setSearchStations(searchStation: SearchStation) {
        Log.i(TAG, "showRecentSearchStation(), size=${searchStation.msgBody.itemList.size}")
        this.searchStation = searchStation
        searchStationItemSize = searchStation.msgBody.itemList.size

        currentVisibleItemCount = if (searchStation.msgBody.itemList.size < ITEMS_PER_PAGE)
            searchStation.msgBody.itemList.size
        else
            ITEMS_PER_PAGE

        notifyDataSetChanged()
    }

    fun increase20ItemsToShow(lastVisibleItemPosition: Int) {
//        Log.v(TAG, "increase20ItemsToShow($lastVisibleItemPosition), ITEMS_PER_PAGE=$ITEMS_PER_PAGE, itemCount=$itemCount")
        if (lastVisibleItemPosition == currentVisibleItemCount - 1 &&
            currentVisibleItemCount != searchStationItemSize
        ) {
            val preCount = currentVisibleItemCount
            currentVisibleItemCount += ITEMS_PER_PAGE
            if (currentVisibleItemCount > searchStationItemSize) {
                currentVisibleItemCount = searchStationItemSize
            }
            notifyItemRangeInserted(preCount, currentVisibleItemCount - preCount)
        }
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