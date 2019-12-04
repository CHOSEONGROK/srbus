package com.example.srbus.presentation.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.srbus.R
import com.example.srbus.data.local.searchStationHistory.SearchStationHistory
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

    private lateinit var historyRvAdapter: SearchStationHistoryRvAdapter
    private lateinit var resultRvAdapter: SearchStationResultRvAdapter
    private lateinit var presenter: SearchStationContract.Presenter

    private var isCurrentShowStationHistory = true

    override var searchKeyword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SearchStationPresenter(this)
        historyRvAdapter = SearchStationHistoryRvAdapter(this, presenter)
        resultRvAdapter = SearchStationResultRvAdapter(this, presenter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(context).inflate(R.layout.fragment_search_station, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        with (recycler_view) {
            adapter = historyRvAdapter
            layoutManager = linearLayoutManager
        }

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Handler().post {
                    resultRvAdapter.increase20ItemsToShow(
                        linearLayoutManager.findLastVisibleItemPosition()
                    )
                }
            }
        })

        presenter.getSearchStationHistories()

        (activity as SearchActivity).setOnSearchKeywordListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_STATION_ACTIVITY) {
            if (isCurrentShowStationHistory) {
                presenter.getSearchStationHistories()
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

    override fun showSearchStationHistory(stations: List<SearchStationHistory>) {
        recycler_view.visibility = View.VISIBLE
        recycler_view.adapter = historyRvAdapter
        historyRvAdapter.setSearchStationHistories(stations)
        isCurrentShowStationHistory = true
    }

    override fun showSearchStation(searchStation: SearchStation) {
        recycler_view.visibility = View.VISIBLE
        recycler_view.adapter = resultRvAdapter
        resultRvAdapter.setSearchStations(searchStation)
        isCurrentShowStationHistory = false
    }

    override fun hideRecyclerView() { recycler_view.visibility = View.GONE }

    override fun showNoSearchHistoryMessage() { ll_no_search_station_history.visibility = View.VISIBLE }
    override fun hideNoSearchHistoryMessage() { ll_no_search_station_history.visibility = View.GONE }

    override fun showNoSearchResultMessage() { ll_no_search_station_result.visibility = View.VISIBLE }
    override fun hideNoSearchResultMessage() { ll_no_search_station_result.visibility = View.GONE }

    override fun startStationActivity(station: SearchStationItem) {
        presenter.insertSearchStationHistory(station)

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
            presenter.getSearchStationHistories()
        } else {
            presenter.getSearchStation(keyword)
        }
    }
}