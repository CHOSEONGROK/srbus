package com.example.srbus.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import com.example.srbus.R
import com.example.srbus.data.local.favorite.FavoriteStation
import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.data.remote.searchStation.SearchStationItem
import com.example.srbus.presentation.BaseActivity
import com.example.srbus.presentation.test.TestActivity
import com.example.srbus.presentation.alarm.AlarmActivity
import com.example.srbus.presentation.search.SearchActivity
import com.example.srbus.presentation.station.StationActivity
import com.example.srbus.service.RideAlarmService
import com.example.srbus.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_contents.*

class MainActivity : BaseActivity(), MainContract.View {
    private val TAG = javaClass.simpleName

    private lateinit var presenter: MainContract.Presenter
    private lateinit var mainRvAdapter: MainRvAdapter
    private lateinit var layoutManager: CustomLinearLayoutManager

    /**
     * ([BaseActivity])'s override functions.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent?.let {
            Log.d(TAG, "Intent On")
            Log.d(TAG, "${it.getBooleanExtra(Constants.INTENT_KEY_FINISH_RIDE_ALARM, false)}")
            stopService(Intent(this, RideAlarmService::class.java))
        }

        presenter = MainPresenter(this)

        layoutManager = CustomLinearLayoutManager(this)
        mainRvAdapter = MainRvAdapter(this, presenter, layoutManager)
        mainRvAdapter.setHasStableIds(true)
        recycler_view.adapter = mainRvAdapter
        recycler_view.layoutManager = layoutManager

        iv_drawer_open.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }

        tv_search.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        tv_edit.setOnClickListener {

        }

        initFab()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume()")
        presenter.getFavoriteStationAndBus().let {
            mainRvAdapter.favorites = it
            mainRvAdapter.showLoadingSpinner()
//            presenter.getArrBus(it)
            presenter.startAutoRequestTimer(it)
        }
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
     * ([MainContract.View])'s override functions.
     */
    override fun showArrBus(arrBus: ArrBus?, arsId: String) {
        if (arrBus != null) {
            mainRvAdapter.showFavoriteStationAndBusInfo(arrBus, arsId)
        }
    }

    override fun showLoadingSpinner() {
        mainRvAdapter.showLoadingSpinner()
    }

    override fun decreaseRemainingTimeByOneSecond() {
        mainRvAdapter.decreaseRemainingTimeByOneSecond()
    }

    override fun showAddBusDialog(favoriteStation: FavoriteStation, arrBus: ArrBus) {
        AddBusDialog().apply {
            arguments = Bundle().also {
                it.putSerializable(Constants.INTENT_KEY_FAVORITE_STATION, favoriteStation)
                it.putSerializable(Constants.INTENT_KEY_ARRBUS, arrBus)
            }
        }.show(supportFragmentManager, null)
    }

    override fun startStationActivity(favoriteStation: FavoriteStation) {
        startActivity(
            Intent(this, StationActivity::class.java).apply {
                putExtra(Constants.INTENT_KEY_SEARCH_STATION, SearchStationItem().also {
                    it.arsId = favoriteStation.arsId
                    it.stId = favoriteStation.stId
                    it.stNm = favoriteStation.stNm
                })
            }
        )
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

    fun onDialogDismiss(arsId: String?, isChanged: Boolean) {
        Log.d(TAG, "onDialogDismiss($arsId, $isChanged)")
        if (isChanged) {
            presenter.getFavoriteStationAndBus().let {
                mainRvAdapter.favorites = it
                mainRvAdapter.showLoadingSpinner()
                presenter.getAllArrBus(it)
            }
        }
    }

    /**
     * Private Functions.
     */
    private fun initFab() {
        val animFabActionDown = AnimationUtils.loadAnimation(this, R.anim.fab_action_down)
        val animFabActionUp = AnimationUtils.loadAnimation(this, R.anim.fab_action_up)
        fab.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.startAnimation(animFabActionDown)
                MotionEvent.ACTION_UP -> {
                    v.startAnimation(animFabActionUp)
                    mainRvAdapter.showLoadingSpinner()
                    presenter.getAllArrBus(mainRvAdapter.favorites)
                }
            }
//            startActivity(Intent(this, TestActivity::class.java))
            true
        }
    }

    class CustomLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
        private var isScrollEnabled = true

        fun setScrollEnabled(enabled: Boolean) {
            isScrollEnabled = enabled
        }

        override fun canScrollVertically(): Boolean {
            return isScrollEnabled && super.canScrollVertically()
        }
    }
}