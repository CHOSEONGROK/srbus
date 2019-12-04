package com.example.srbus.presentation.alarm

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.srbus.R
import com.example.srbus.presentation.BaseActivity
import com.example.srbus.utils.Constants
import com.example.srbus.utils.OnReFreshFabListener
import kotlinx.android.synthetic.main.activity_alarm.*
import kotlinx.android.synthetic.main.activity_station.fab
import kotlin.IllegalArgumentException

class AlarmActivity : BaseActivity(), AlarmContract.View {
    private val TAG = javaClass.simpleName

    private lateinit var presenter: AlarmContract.Presenter
    private lateinit var arsId: String
    private lateinit var busNumber: String
    private lateinit var stationName: String

    /**
     * ([BaseActivity])'s override functions.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        Log.e(TAG, "onCreate()")

        with (intent) {
            Log.e(TAG, "onCreate(), intent not null")
            arsId = getStringExtra(Constants.INTENT_KEY_ARSID)
            busNumber = getStringExtra(Constants.INTENT_KEY_BUS_NUMBER)
            stationName = getStringExtra(Constants.INTENT_KEY_STATION_NAME)

            tv_bus_number.text = "$busNumber  ·  "
            tv_station_name.text = stationName

            presenter = AlarmPresenter(this@AlarmActivity, busNumber, stationName)

            iv_first_bus_alarm.setOnClickListener { v ->
                onClickAlarmButton(1)
            }

            iv_second_bus_alarm.setOnClickListener {
                onClickAlarmButton(2)
            }

            fab.setOnTouchListener(OnReFreshFabListener(this@AlarmActivity))
            fab.setOnClickListener {
                presenter.getArrBus(arsId)
            }
        }

        iv_back.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.startAutoRequestTimer(arsId)
    }

    override fun onPause() {
        super.onPause()
        presenter.finishAutoRequestTimer()
    }

    /**
     * ([AlarmContract.View])'s override functions.
     */
    override fun updateArrBusInfo(
        firstBusRemainingTime: String, firstBusRemainingStation: String?, firstBusFullOrNot: String, firstBusArrivalTime: String, firstBusCurrentStation: String, firstBusInfo: String,
        secondBusRemainingTime: String, secondBusRemainingStation: String?, secondBusFullOrNot: String, secondBusArrivalTime: String, secondBusCurrentStation: String, secondBusInfo: String
    ) {
        tv_first_bus_remaining_time.text = firstBusRemainingTime
        if (firstBusRemainingStation == null) {
            ll_first_bus_remaining_station.visibility = View.GONE
        } else {
            tv_first_bus_remaining_station.text = firstBusRemainingStation
            tv_first_bus_full_or_not.text = firstBusFullOrNot
            ll_first_bus_remaining_station.visibility = View.VISIBLE
        }
        tv_first_bus_arrival_time.text = firstBusArrivalTime
        tv_first_bus_info.text = firstBusInfo

        tv_second_bus_remaining_time.text = secondBusRemainingTime
        if (secondBusRemainingStation == null) {
            ll_second_bus_remaining_station.visibility = View.GONE
        } else {
            tv_second_bus_remaining_station.text = secondBusRemainingStation
            tv_second_bus_full_or_not.text = secondBusFullOrNot
            ll_second_bus_remaining_station.visibility = View.VISIBLE
        }
        tv_second_bus_arrival_time.text = secondBusArrivalTime
        tv_second_bus_info.text = secondBusInfo
    }

    override fun updateViewAlarmButton(ordinal: Int, alarmOn: Boolean) {
        val alarmButton: ImageView = when (ordinal) {
            1 -> iv_first_bus_alarm
            2 -> iv_second_bus_alarm
            else -> throw IllegalArgumentException("ordinal number is not valid.")
        }

        with (alarmButton) {
            if (alarmOn) {
                setImageResource(R.drawable.baseline_alarm_on_black_48dp)
                setColorFilter(ContextCompat.getColor(this@AlarmActivity, R.color.white))
                setBackgroundResource(R.drawable.activity_main_add_bus_dialog_button_orange_bg)
            } else {
                setImageResource(R.drawable.baseline_alarm_off_black_48dp)
                setColorFilter(ContextCompat.getColor(this@AlarmActivity, R.color.gray400))
                setBackgroundResource(R.drawable.activity_main_add_bus_dialog_button_white_bg)
            }
        }
    }

    override fun showAlertDialog(ordinal: Int, alarmState: AlarmPresenter.RideAlarm) {
        val message = when (alarmState) {
            AlarmPresenter.RideAlarm.RUNNING_SAME_BUS_AND_SAME_VEHICLE -> {
                resources.getString(R.string.alert_dialog_title_finish_ride_alarm)
            }
            AlarmPresenter.RideAlarm.RUNNING_SAME_BUS_BUT_ANOTHER_VEHICLE,
            AlarmPresenter.RideAlarm.RUNNING_ANOTHER_BUS -> {
                resources.getString(R.string.alert_dialog_title_reset_ride_alarm)
            }
            else -> throw IllegalArgumentException("Invalid Argument.")
        }

        val positiveButtonText = when (alarmState) {
            AlarmPresenter.RideAlarm.RUNNING_SAME_BUS_AND_SAME_VEHICLE -> {
                "종료"
            }
            AlarmPresenter.RideAlarm.RUNNING_SAME_BUS_BUT_ANOTHER_VEHICLE,
            AlarmPresenter.RideAlarm.RUNNING_ANOTHER_BUS -> {
                "재설정"
            }
            else -> throw IllegalArgumentException("Invalid Argument.")
        }

        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ ->
                presenter.stopRideAlarmService()

                when (alarmState) {
                    AlarmPresenter.RideAlarm.RUNNING_SAME_BUS_AND_SAME_VEHICLE -> {
                        updateViewAlarmButton(ordinal, false)
                    }
                    AlarmPresenter.RideAlarm.RUNNING_SAME_BUS_BUT_ANOTHER_VEHICLE -> {
                        if (presenter.startRideAlarmService(ordinal)) {
                            updateViewAlarmButton(3 - ordinal, false)
                            updateViewAlarmButton(ordinal, true)
                        }
                    }
                    AlarmPresenter.RideAlarm.RUNNING_ANOTHER_BUS -> {
                        if (presenter.startRideAlarmService(ordinal)) {
                            updateViewAlarmButton(ordinal, true)
                        }
                    }
                    else -> throw IllegalArgumentException("Invalid Argument.")
                }
            }
            .setNegativeButton("취소") { _, _ -> }
            .create()
            .show()
    }

    /**
     * Private Functions.
     */
    private fun onClickAlarmButton(ordinal: Int) {
        val alarmState = AlarmPresenter.getRideAlarmServiceState(this, presenter.arrBusItem!!, ordinal)

        when (alarmState) {
            AlarmPresenter.RideAlarm.NO_RUNNING -> {
                if (presenter.startRideAlarmService(ordinal)) {
                    updateViewAlarmButton(ordinal, true)
                }
            }
            AlarmPresenter.RideAlarm.RUNNING_SAME_BUS_AND_SAME_VEHICLE,
            AlarmPresenter.RideAlarm.RUNNING_SAME_BUS_BUT_ANOTHER_VEHICLE,
            AlarmPresenter.RideAlarm.RUNNING_ANOTHER_BUS -> {
                showAlertDialog(ordinal, alarmState)
            }
        }
    }
}