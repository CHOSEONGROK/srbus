package com.example.srbus.presentation.search

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.srbus.R
import com.example.srbus.data.local.recentSearchStation.RecentSearchStation
import com.example.srbus.data.remote.searchStation.SearchStationItem
import com.example.srbus.data.remote.searchStation.SearchStation
import com.example.srbus.presentation.station.StationActivity
import com.example.srbus.utils.Constants
import kotlinx.android.synthetic.main.fragment_search_station.*

class SearchStationFragment : Fragment(), SearchStationContract.View, SearchActivity.OnSearchKeywordListener {
    private val TAG = javaClass.simpleName

    companion object {
        const val REQUEST_CODE_STATION_ACTIVITY = 1000
    }

    private lateinit var rvAdapter: SearchStationFragmentRvAdapter
    private lateinit var presenter: SearchStationContract.Presenter

    override var searchKeyword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SearchStationPresenter(this)
        rvAdapter = SearchStationFragmentRvAdapter(this, presenter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(context).inflate(R.layout.fragment_search_station, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.adapter = rvAdapter
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                Log.d(TAG, "onScrollStateChanged(), newState=$newState")
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.d(TAG, "onScrolled(), dy=$dy")
//                rvAdapter.increase20ItemsToShow(
//                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
//                )
            }
        })

        tv_delete_station_history.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage(R.string.alert_dialog_message_delete_all_recent_search_station_history)
                .setPositiveButton("확인") { _, _ ->
                    presenter.deleteAllRecentSearchStation()
                    presenter.getRecentSearchStation() }
                .setNegativeButton("취소") { _, _ -> }
                .create()
                .show()
        }

        presenter.getRecentSearchStation()

        (activity as SearchActivity).setOnSearchKeywordListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_STATION_ACTIVITY) {
            if (rvAdapter.isShowCurrentRecentStations) {
                presenter.getRecentSearchStation()
            }
        }
    }

    override fun onDetach() {
        (activity as SearchActivity).removeOnSearchKeywordListener(this)
        super.onDetach()
    }

    /**
     * ([SearchStationContract.View])'s override functions
     */
    override fun showSearchStation(searchStation: SearchStation) {
        changeVisibleDeleteHistoryButton(false)
        rvAdapter.showSearchStation(searchStation)
    }

    override fun showRecentSearchStation(stations: List<RecentSearchStation>) {
        changeVisibleDeleteHistoryButton(true)
        rvAdapter.showRecentSearchStation(stations)
    }

    override fun changeVisibleDeleteHistoryButton(isVisible: Boolean) {
        tv_delete_station_history.visibility = if (isVisible)
            View.VISIBLE
        else
            View.GONE
    }

    override fun startStationActivity(station: SearchStationItem) {
        presenter.insertRecentSearchStation(station)

        startActivityForResult(Intent(context, StationActivity::class.java).apply {
            putExtra(Constants.INTENT_KEY_SEARCH_STATION, station)
        }, REQUEST_CODE_STATION_ACTIVITY)
    }

    /**
     * ([SearchActivity.OnSearchKeywordListener])'s override functions
     */
    override fun onSearch(keyword: String) {
        Log.i(TAG, "onSearch($keyword)")
        searchKeyword = keyword
        if (keyword.isEmpty()) {
            presenter.getRecentSearchStation()
        } else {
            presenter.getSearchStation(keyword)
        }
    }
}