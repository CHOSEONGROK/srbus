package com.example.srbus.presentation.station

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.srbus.R
import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.data.remote.searchStation.SearchStationItem
import com.example.srbus.presentation.BaseActivity
import com.example.srbus.presentation.alarm.AlarmActivity
import com.example.srbus.utils.Constants
import com.example.srbus.utils.OnReFreshFabListener
import com.example.srbus.utils.Util
import kotlinx.android.synthetic.main.activity_station.*


class StationActivity : BaseActivity(), StationContract.View {
    private val TAG = javaClass.simpleName

    private lateinit var rvAdapter: StationRvAdapter
    private lateinit var presenter: StationContract.Presenter

    private lateinit var station: SearchStationItem

    /**
     * ([StationActivity])'s Override functions.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station)
        Util.setNoTranslucentStatusBar(window, R.color.gray600)

        presenter = StationPresenter(this)

        rvAdapter = StationRvAdapter(this, presenter)
        rvAdapter.setHasStableIds(true)
        recycler_view.adapter = rvAdapter

        app_bar.addOnOffsetChangedListener(OnOffsetChangedListener())

        with (intent) {
            station = getSerializableExtra(Constants.INTENT_KEY_SEARCH_STATION) as SearchStationItem
            tv_station_number_large_bar.text = station.arsId
            tv_station_name_small_bar.text = station.stNm
            tv_station_name_large_bar.text = station.stNm
//            tv_next_station_large_bar.text = station.nextStation
        }

        iv_back.setOnClickListener {
            finish()
        }

        val isFavorite = presenter.isFavoriteStation(station.arsId)
        if (isFavorite) {
            setFavoriteStation()
        } else {
            removeFavoriteStation()
        }
        iv_favorite_large_bar.tag = isFavorite
        iv_favorite_large_bar.setOnClickListener {
            setFavoriteButtonOnClickListener()
        }
        iv_favorite_small_bar.setOnClickListener {
            setFavoriteButtonOnClickListener()
        }

        iv_home_small_bar.setOnClickListener {
            finishAllActivitiesButOne()
        }

        fab.setOnTouchListener(OnReFreshFabListener(this))
        fab.setOnClickListener {
            Log.d(TAG, "fab.setOnClickListener()")
            presenter.loadArrBus(station.arsId)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.startAutoRequestTimer(station.arsId)
    }

    override fun onPause() {
        super.onPause()
        presenter.finishAutoRequestTimer()
    }

    override fun onDestroy() {
        presenter.finishAutoRequestTimer()
        super.onDestroy()
    }

    /**
     * ([StationContract.View])'s Override functions.
     */
    override fun showArrBus(arrBus: ArrBus?) {
        if (arrBus == null) {

        } else {
            rvAdapter.showArrBus(arrBus)
        }
    }

    override fun setFavoriteStation() {
        iv_favorite_large_bar.setColorFilter(Util.getColor(this, R.color.yellow500))
        iv_favorite_small_bar.setColorFilter(Util.getColor(this, R.color.yellow500))
    }

    override fun removeFavoriteStation() {
        iv_favorite_large_bar.setColorFilter(Util.getColor(this, android.R.color.transparent))
        iv_favorite_small_bar.setColorFilter(Util.getColor(this, android.R.color.transparent))
    }

    override fun setFavoriteBus(position: Int) {
        rvAdapter.notifyItemChanged(position)
    }

    override fun removeFavoriteBus(position: Int) {
        rvAdapter.notifyItemChanged(position)
    }

    override fun showNextStationDirection(nextDirection: String) {
        tv_next_station_large_bar.text = "$nextDirection 방면"
    }

    override fun decreaseRemainingTimeByOneSecond() {
        rvAdapter.decreaseRemainingTimeByOneSecond()
    }

    override fun showLoadingSpinner() {
        rvAdapter.showLoadingSpinner()
    }
    override fun hideLoadingSpinner() {
        rvAdapter.hideLoadingSpinner()
    }

    override fun startAlarmActivity(arsId: String, stationName: String, busNumber: String) {
        startActivity(
            Intent(this, AlarmActivity::class.java).apply {
                putExtra(Constants.INTENT_KEY_ARSID, arsId)
                putExtra(Constants.INTENT_KEY_STATION_NAME, stationName)
                putExtra(Constants.INTENT_KEY_BUS_NUMBER, busNumber)
            }
        )
    }

    fun addFavoriteStation() {
        presenter.addFavoriteStation(station, tv_next_station_large_bar.text.toString())
        supportFragmentManager
    }

    /**
     * Private Functions.
     */
    private fun setFavoriteButtonOnClickListener() {
        if (iv_favorite_large_bar.tag == true) {
            AlertDialog.Builder(this)
                .setTitle(R.string.alert_dialog_title_remove_favorite_station)
                .setMessage(R.string.alert_dialog_message_remove_favorite_station)
                .setPositiveButton("확인") { _, _ ->
                        presenter.removeFavoriteStation(station)
                        iv_favorite_large_bar.tag = false }
                .setNegativeButton("취소") { _, _ -> }
                .create()
                .show()

        } else if (iv_favorite_large_bar.tag == false) {
            presenter.addFavoriteStation(station, tv_next_station_large_bar.text.toString())
            iv_favorite_large_bar.tag = true
        }
    }

    inner class OnOffsetChangedListener : AppBarLayout.OnOffsetChangedListener {
        override fun onOffsetChanged(appBar: AppBarLayout?, offset: Int) {
//            Log.d(TAG, "appBar!!.totalScrollRange: ${appBar!!.totalScrollRange}, offset: $offset")

            val velocity = 2
            val alphaChangePoint = appBar!!.totalScrollRange * (velocity / 3.0f)

            val alpha = (alphaChangePoint + offset.toFloat()) / alphaChangePoint  // alpha: (+1) → 0 → (-1)

//            Log.d(TAG, "alpha: $alpha")

            cl_top_large_bar.alpha = alpha
            tv_station_name_small_bar.alpha = alpha * (-1) * velocity
            iv_map_small_bar.alpha = alpha * (-1) * velocity
            iv_favorite_small_bar.alpha = alpha * (-1) * velocity

            if (alpha > 0) {
                cl_top_large_bar.visibility = View.VISIBLE
            } else {
                cl_top_large_bar.visibility = View.GONE
            }

            if (alpha * (-1) > 0) {
                tv_station_name_small_bar.visibility = View.VISIBLE
                iv_map_small_bar.visibility = View.VISIBLE
                iv_favorite_small_bar.visibility = View.VISIBLE
            } else {
                tv_station_name_small_bar.visibility = View.GONE
                iv_map_small_bar.visibility = View.GONE
                iv_favorite_small_bar.visibility = View.GONE
            }
        }
    }
}