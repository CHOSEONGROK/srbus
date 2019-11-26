package com.example.srbus.model.alarm

import android.content.Context
import com.example.srbus.data.remote.arrBus.ArrBus

class AlarmRepository(context: Context) : AlarmSource {

    private val dataSource = AlarmDataSource(context)

    override fun getArrBus(callback: AlarmSource.LoadArrBusCallBack, arsId: String) {
        dataSource.getArrBus(object : AlarmSource.LoadArrBusCallBack {
            override fun onLoadArrBus(arrBus: ArrBus?) {
                callback.onLoadArrBus(arrBus.also {
                    for (i in 0 until it?.msgBody!!.itemList!!.size) {
                        it.msgBody.itemList[i].arrmsg1 = it.msgBody.itemList[i].arrmsg1
                        it.msgBody.itemList[i].arrmsg2 = it.msgBody.itemList[i].arrmsg2
                    }
                })
            }
        }, arsId)
    }
}