package com.example.srbus.presentation.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import com.example.srbus.R
import com.example.srbus.presentation.BaseActivity
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : BaseActivity() {
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        frameLayout.setOnClickListener { Log.e(TAG, "frameLayout.setOnClickListener") }
//        frameLayout.setChildView(iv_home)
        iv_home.setOnClickListener { Log.e(TAG, "iv_home.setOnClickListener") }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.i(TAG, "dispatchTouchEvent, ev=${when (ev?.action) {
            MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
            MotionEvent.ACTION_UP -> "ACTION_UP"
            else -> "ELSE"
        }}")
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        Log.v(TAG, "onTouchEvent, ev=${when (ev?.action) {
            MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
            MotionEvent.ACTION_UP -> "ACTION_UP"
            else -> "ELSE"
        }}")
        return super.onTouchEvent(ev)
    }
}