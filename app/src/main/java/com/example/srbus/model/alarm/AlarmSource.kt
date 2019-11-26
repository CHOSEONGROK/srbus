package com.example.srbus.model.alarm

import com.example.srbus.data.remote.arrBus.ArrBus

interface AlarmSource {

    interface LoadArrBusCallBack {
        fun onLoadArrBus(arrBus: ArrBus?)
    }

    fun getArrBus(callback: LoadArrBusCallBack, arsId: String)
}