package com.example.srbus

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import android.util.Log

class MyApplication : Application() {
    private val TAG = javaClass.simpleName

    val activities = arrayListOf<Activity>()

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate()")
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "onConfigurationChanged()")
    }
}