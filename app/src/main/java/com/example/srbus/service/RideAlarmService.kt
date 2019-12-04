package com.example.srbus.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.srbus.R
import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.data.remote.arrBus.ArrBusItem
import com.example.srbus.presentation.alarm.AlarmActivity
import com.example.srbus.presentation.main.MainActivity
import com.example.srbus.retrofit.NetRetrofit
import com.example.srbus.utils.Constants
import com.example.srbus.utils.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RideAlarmService : Service() {
    private val TAG = javaClass.simpleName

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "RIDE_BUS_ALARM"
    }

    private var arrBus: ArrBus? = null
    private var arrBusItem: ArrBusItem? = null
    private lateinit var busNumber: String
    private lateinit var stationName: String
    private lateinit var arsId: String
    private lateinit var busRouteId: String
    private lateinit var vehicleId: String
    private var timer: Timer? = null

    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "onCreate()")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.v(TAG, "onStartCommand($intent, $flags, $startId)")

        makeNotificationChannel()

        intent?.let {
            startForeground(12345, getNotification(
                it.getStringExtra(Constants.INTENT_KEY_BUS_NUMBER).also { str -> busNumber = str },
                it.getStringExtra(Constants.INTENT_KEY_BUS_REMAINING_TIME_MIN),
                it.getStringExtra(Constants.INTENT_KEY_BUS_REMAINING_STATION),
                it.getStringExtra(Constants.INTENT_KEY_STATION_NAME).also { str -> stationName = str },
                it.getStringExtra(Constants.INTENT_KEY_ARSID).also { str -> arsId = str },
                it.getStringExtra(Constants.INTENT_KEY_BUS_ROUTE_ID).also { str -> busRouteId = str },
                it.getStringExtra(Constants.INTENT_KEY_BUS_VEHICLE_ID).also { str -> vehicleId = str },
                false
            ))
        }

        startAutoRequestTimer(arsId)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.v(TAG, "onBind($intent)")
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.v(TAG, "onUnbind($intent)")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.v(TAG, "onDestroy()")
        finishAutoRequestTimer()
        super.onDestroy()
    }

    private fun makeNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            NotificationChannel(NOTIFICATION_CHANNEL_ID, "승차알림", NotificationManager.IMPORTANCE_HIGH).let {
                it.description = "승차알림"
                it.enableLights(true)
                it.lightColor = Util.getColor(this, R.color.green600)
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 100, 200)
                it.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

                notificationManager.createNotificationChannel(it)
            }
        }
    }



    private fun getNotification(
        busNumber: String, remainingTimeMin: String, remainingStation: String?,
        stationName: String, arsId: String, bosRouteId: String, vehId: String, isLast: Boolean
    ): Notification {
        val intent = Intent(this, AlarmActivity::class.java).apply {
            putExtra(Constants.INTENT_KEY_ARSID, arsId)
            putExtra(Constants.INTENT_KEY_BUS_NUMBER, busNumber)
            putExtra(Constants.INTENT_KEY_STATION_NAME, stationName)
//            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pending = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val actionIntent = Intent(this, RideAlarmService::class.java).apply {
//            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(Constants.INTENT_KEY_FINISH_RIDE_ALARM, true)
            action = "stopService"
        }
        val actionPendingIntent = PendingIntent.getActivity(
            this, Constants.REQUEST_CODE_FINISH_RIDE_ALARM, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

        val bundle = Bundle().apply {
            putString(Constants.INTENT_KEY_ARSID, arsId)
            putString(Constants.INTENT_KEY_BUS_ROUTE_ID, bosRouteId)
            putString(Constants.INTENT_KEY_BUS_VEHICLE_ID, vehId)
        }

        val contentText = when (remainingTimeMin) {
            "0" -> "곧 도착(1번째전 출발)"
            "도착 또는 지나감" -> "도착 또는 지나감"
            else -> "약 ${remainingTimeMin}분 후 도착($remainingStation 출발)"
        }

        builder.setContentTitle("$busNumber 승차알람")
            .setContentText(contentText)
            .setSmallIcon(R.mipmap.app_icon)
            .addAction(0, when (isLast) {
                true -> "다음 버스 알람"
                false -> "알람 종료"
            }, actionPendingIntent)
            .setShowWhen(true)
            .setContentIntent(pending)
            .setExtras(bundle)
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
            .priority = NotificationCompat.PRIORITY_HIGH

        return builder.build()
    }

    private fun getArrBus(arsId: String) {
        GlobalScope.launch(Dispatchers.Default) {
            val res = NetRetrofit.service.getArrBusByUidItem(NetRetrofit.getURLArrBusByUidItem(arsId))

            res.enqueue(object : Callback<ArrBus> {
                override fun onResponse(call: Call<ArrBus>, response: Response<ArrBus>) {
                    Log.i(TAG, "onResponse() \ncall: $call \nresponse: $response \nbody: ${response.body().toString()}")

                    if (response.body() == null)
                        return

                    var index = 0
                    response.body()!!.msgBody.itemList.forEachIndexed { i, bus ->
                        if (bus.busRouteId == busRouteId) {
                            bus.arrmsg1 = bus.arrmsg1
                            bus.arrmsg2 = bus.arrmsg2
                            index = i
                            return@forEachIndexed
                        }
                    }
                    arrBus = response.body()
                    arrBusItem = arrBus!!.msgBody.itemList[index]
                }

                override fun onFailure(call: Call<ArrBus>, t: Throwable) {
                    Log.e(TAG, "onFailure() \ncall: $call \nt: $t")
                }
            })
        }
    }

    private fun startAutoRequestTimer(arsId: String?) {
        if (arsId != null) {
            timer = Timer()
            timer?.schedule(AutoRequestTimerTask(arsId), 0, 1000)
        }
    }

    private fun finishAutoRequestTimer() {
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    private inner class AutoRequestTimerTask(
        private val arsId: String
    ) : TimerTask() {
        private var timer = -1
        private val alarmCount: Array<Int> = arrayOf(0, 0, 0, 0)
        private var beforeVehicleId: String? = null

        override fun run() {
            if (timer % 10 == 0) Log.d(TAG, "TimerTask($timer)")

            if (timer % 30 == 0) {
                getArrBus(arsId)

                arrBusItem?.let {

                    if (beforeVehicleId == null) {
                        beforeVehicleId = it.vehId1
                    } else if (beforeVehicleId != it.vehId1) {
                        startForeground(12345, getNotification(
                            busNumber, "도착 또는 지나감", null, stationName, arsId, it.busRouteId, vehicleId, true
                        ))
                        finishAutoRequestTimer()
                        stopSelf()
                        return
                    }


                    if (it.arrmsg1_min == 3 && alarmCount[3] < 1) {
                        startForeground(12345, getNotification(
                            busNumber, it.arrmsg1_min.toString(), it.firstArrBusRemainingStation, stationName, arsId, it.busRouteId, vehicleId, false
                        ))
                        alarmCount[3] += 1
                    } else if (it.arrmsg1_min == 2 && alarmCount[2] < 1) {
                        startForeground(12345, getNotification(
                            busNumber, it.arrmsg1_min.toString(), it.firstArrBusRemainingStation, stationName, arsId, it.busRouteId, vehicleId, false
                        ))
                        alarmCount[2] += 1
                    } else if (it.arrmsg1_min == 1 && alarmCount[1] < 1) {
                        startForeground(12345, getNotification(
                            busNumber, it.arrmsg1_min.toString(), it.firstArrBusRemainingStation, stationName, arsId, it.busRouteId, vehicleId, false
                        ))
                        alarmCount[1] += 1
                    } else if (it.firstArrBusRemainingTime == "곧 도착" && alarmCount[0] < 2) {
                        startForeground(12345, getNotification(
                            busNumber, it.arrmsg1_min.toString(), it.firstArrBusRemainingStation, stationName, arsId, it.busRouteId, vehicleId, false
                        ))
                        alarmCount[0] += 1
                    }
                }
            }

            arrBusItem?.decreaseArrBusRemainingTimeByOneSecond()


            if (timer % 10 == 0) {
                Log.d(TAG, "AutoRequestTimerTask(), firstArrBusRemainingTime=${arrBusItem?.firstArrBusRemainingTime}, secondArrBusRemainingTime=${arrBusItem?.secondArrBusRemainingTime}")
            }

            timer++
        }
    }
}