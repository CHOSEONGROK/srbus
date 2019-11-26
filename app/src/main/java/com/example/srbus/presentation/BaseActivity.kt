package com.example.srbus.presentation

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.srbus.MyApplication
import com.example.srbus.R
import com.example.srbus.utils.Util

@SuppressWarnings("Registered")
open class BaseActivity : AppCompatActivity() {

    private lateinit var myApplication: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Util.setNoTranslucentStatusBar(window, R.color.statusBarColor)

        myApplication = applicationContext as MyApplication
        myApplication.activities.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        myApplication.activities.remove(this)
        Log.d("BaseActivity", "onDestroy(), ${myApplication.activities.size}")
    }

    fun finishAllActivitiesButOne() {
        if (myApplication.activities.size == 1)
            return

        for (i in (myApplication.activities.size - 1) downTo 1) {
            myApplication.activities[i].finish()
            myApplication.activities.removeAt(i)
        }
    }
}