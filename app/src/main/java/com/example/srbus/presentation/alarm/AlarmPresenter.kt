package com.example.srbus.presentation.alarm

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.srbus.R
import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.data.remote.arrBus.ArrBusItem
import com.example.srbus.model.alarm.AlarmRepository
import com.example.srbus.model.alarm.AlarmSource
import com.example.srbus.service.RideAlarmService
import com.example.srbus.utils.Constants
import java.lang.IllegalStateException
import java.util.*

class AlarmPresenter(
    private val view: AlarmContract.View,
    private val busNumber: String,
    private val stationName: String
) : AlarmContract.Presenter {
    private val TAG = javaClass.simpleName

    enum class RideAlarm { NO_RUNNING, RUNNING_SAME_BUS_AND_SAME_VEHICLE, RUNNING_SAME_BUS_BUT_ANOTHER_VEHICLE, RUNNING_ANOTHER_BUS }

    private val context = view as AlarmActivity
    private val repository = AlarmRepository(context)
    private var timer: Timer? = null

    private var arrBus: ArrBus? = null
    override var arrBusItem: ArrBusItem? = null

    companion object {
        fun isRunningRideAlarmService(context: Context, bus: ArrBusItem): Boolean =
            when (getRideAlarmServiceState(context, bus, 1)) {
                RideAlarm.RUNNING_SAME_BUS_AND_SAME_VEHICLE,
                RideAlarm.RUNNING_SAME_BUS_BUT_ANOTHER_VEHICLE -> true
                else -> false
            }

        fun getRideAlarmServiceState(context: Context, bus: ArrBusItem, ordinal: Int): RideAlarm {
            var result = RideAlarm.NO_RUNNING

            if (Build.VERSION.SDK_INT >= 26) {
                (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).activeNotifications.forEach {
                    if (it.notification.channelId == RideAlarmService.NOTIFICATION_CHANNEL_ID) {
                        val notificationArsId = it.notification.extras.getString(Constants.INTENT_KEY_ARSID)
                        val notificationBusRouteId = it.notification.extras.getString(Constants.INTENT_KEY_BUS_ROUTE_ID)
                        val notificationVehicleId = it.notification.extras.getString(Constants.INTENT_KEY_BUS_VEHICLE_ID)
                        val arsId = bus.arsId
                        val busRouteId = bus.busRouteId
                        val vehId = when (ordinal) {
                            1 -> bus.vehId1
                            2 -> bus.vehId2
                            else -> throw IllegalArgumentException("ordinal number is not valid.")
                        }

                        if (notificationArsId == arsId && notificationBusRouteId == busRouteId) {
                            if (notificationVehicleId == vehId) {
                                result = RideAlarm.RUNNING_SAME_BUS_AND_SAME_VEHICLE
                            } else {
                                result = RideAlarm.RUNNING_SAME_BUS_BUT_ANOTHER_VEHICLE
                            }
                        } else {
                            result = RideAlarm.RUNNING_ANOTHER_BUS
                        }

                        return@forEach
                    }
                }
            }

            return result
        }
    }

    override fun getArrBus(arsId: String?) {
        if (arsId == null)
            return

        repository.getArrBus(object : AlarmSource.LoadArrBusCallBack {
            override fun onLoadArrBus(arrBus: ArrBus?) {
                if (arrBus == null) {
                    Log.e(TAG, "onLoadArrBus(), arrBus==null")
                    return
                }

                this@AlarmPresenter.arrBus = arrBus
                arrBus.msgBody.itemList.forEach {
                    if (it.rtNm == busNumber) {
                        arrBusItem = it
                        updateArrBusInfo(it)

                        updateViewAlarmButton(1)
                        updateViewAlarmButton(2)
                    }
                }
            }
        }, arsId)
    }

    override fun startRideAlarmService(ordinal: Int): Boolean {
        if (arrBusItem == null)
            return false

        val remainingTimeMin = when (ordinal) {
            1 -> arrBusItem!!.arrmsg1_min
            2 -> arrBusItem!!.arrmsg2_min
            else -> throw IllegalArgumentException("ordinal number is not valid")
        }

        val remainingStation = when (ordinal) {
            1 -> arrBusItem!!.firstArrBusRemainingStation
            2 -> arrBusItem!!.secondArrBusRemainingStation
            else -> throw IllegalArgumentException("ordinal number is not valid")
        }

        val vehicleId = when (ordinal) {
            1 -> arrBusItem!!.vehId1
            2 -> arrBusItem!!.vehId2
            else -> throw IllegalArgumentException("ordinal number is not valid")
        }

        if (remainingTimeMin == 0 || remainingStation == null) {
            Toast.makeText(context, R.string.toast_message_bus_arrive_soon, Toast.LENGTH_SHORT).show()
            return false
        }

        context.startService(Intent(context, RideAlarmService::class.java).apply {
            putExtra(Constants.INTENT_KEY_BUS_NUMBER, arrBusItem!!.rtNm)
            putExtra(Constants.INTENT_KEY_BUS_REMAINING_TIME_MIN, remainingTimeMin.toString())
            putExtra(Constants.INTENT_KEY_BUS_REMAINING_STATION, remainingStation)
            putExtra(Constants.INTENT_KEY_STATION_NAME, stationName)
            putExtra(Constants.INTENT_KEY_ARSID, arrBusItem!!.arsId)
            putExtra(Constants.INTENT_KEY_BUS_ROUTE_ID, arrBusItem!!.busRouteId)
            putExtra(Constants.INTENT_KEY_BUS_VEHICLE_ID, vehicleId)
        })

        return true
    }

    override fun stopRideAlarmService() {
        context.stopService(Intent(context, RideAlarmService::class.java))
    }

    override fun startAutoRequestTimer(arsId: String?) {
        if (arsId != null) {
            timer = Timer()
            timer?.schedule(AutoRequestTimerTask(arsId), 0, 1000)
        }
    }

    override fun finishAutoRequestTimer() {
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    /**
     * Private functions.
     */
    private fun updateArrBusInfo(bus: ArrBusItem) {
        view.updateArrBusInfo(
            bus.firstArrBusRemainingTime,
            bus.firstArrBusRemainingStation,
            bus.firstBusFullOrNot,
            getArrivalTime(bus.arrmsg1_min, bus.arrmsg1_sec),
            "${bus.stationNm1} 출발",
            bus.sectNm,
            bus.secondArrBusRemainingTime,
            bus.secondArrBusRemainingStation,
            bus.firstBusFullOrNot,
            getArrivalTime(bus.arrmsg2_min, bus.arrmsg2_sec),
            "${bus.stationNm2} 출발",
            bus.sectNm
        )
    }

    private fun updateViewAlarmButton(ordinal: Int) {
        val alarmState = getRideAlarmServiceState(context, arrBusItem!!, ordinal)
        when (alarmState) {
            RideAlarm.RUNNING_SAME_BUS_AND_SAME_VEHICLE -> {
                view.updateViewAlarmButton(ordinal, true)
            }
            RideAlarm.RUNNING_SAME_BUS_BUT_ANOTHER_VEHICLE,
            RideAlarm.RUNNING_ANOTHER_BUS -> {
                view.updateViewAlarmButton(ordinal, false)
            }
            else -> { }
        }
    }

    private fun getArrivalTime(min: Int, sec: Int): String {
        Calendar.getInstance().let {
            val amOrPm = if (it.get(Calendar.AM_PM) == 0) "오전" else "오후"

            it.set(it.get(Calendar.YEAR), it.get(Calendar.MONTH), it.get(Calendar.DATE),
                it.get(Calendar.HOUR_OF_DAY), it.get(Calendar.MINUTE) + min, it.get(Calendar.SECOND) + sec
            )

            return "$amOrPm ${it.get(Calendar.HOUR)}시 ${it.get(Calendar.MINUTE)}분 도착예정"
        }
    }

    private inner class AutoRequestTimerTask(
        private val arsId: String
    ) : TimerTask() {
        private var timer = -1

        override fun run() {
            if (timer % 10 == 0) Log.d(TAG, "TimerTask($timer)")
            context.runOnUiThread {
                if (timer % 30 == 0) {
                    getArrBus(arsId)
                }

                arrBusItem?.let {
                    it.decreaseArrBusRemainingTimeByOneSecond()
                    updateArrBusInfo(it)
                }
            }
            timer++
        }
    }
}
