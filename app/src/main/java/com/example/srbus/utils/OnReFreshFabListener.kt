package com.example.srbus.utils

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import com.example.srbus.R

class OnReFreshFabListener(context: Context) : View.OnTouchListener {

    private val animFabActionDown = AnimationUtils.loadAnimation(context, R.anim.fab_action_down)
    private val animFabActionUp = AnimationUtils.loadAnimation(context, R.anim.fab_action_up)

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> v!!.startAnimation(animFabActionDown)
            MotionEvent.ACTION_UP -> {
                v!!.startAnimation(animFabActionUp)
            }
        }
        return false
    }
}