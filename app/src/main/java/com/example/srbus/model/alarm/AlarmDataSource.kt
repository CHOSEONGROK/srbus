package com.example.srbus.model.alarm

import android.content.Context
import android.os.Handler
import android.util.Log
import com.example.srbus.data.remote.arrBus.ArrBus
import com.example.srbus.retrofit.NetRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmDataSource(context: Context) : AlarmSource {
    private val TAG = javaClass.simpleName

    override fun getArrBus(callback: AlarmSource.LoadArrBusCallBack, arsId: String) {

        GlobalScope.launch(Dispatchers.Default) {
            NetRetrofit
                .service
                .getArrBusByUidItem(NetRetrofit.getURLArrBusByUidItem(arsId))
                .enqueue(object : Callback<ArrBus> {
                    override fun onResponse(call: Call<ArrBus>, response: Response<ArrBus>) {
                        Log.i(TAG, "onResponse() \ncall: $call \nresponse: $response \nbody: ${response.body().toString()}")
                        callback.onLoadArrBus(response.body())
                    }

                    override fun onFailure(call: Call<ArrBus>, t: Throwable) {
                        Log.e(TAG, "onFailure(), call=$call, t=$t")
                    }
                })
        }
    }
}