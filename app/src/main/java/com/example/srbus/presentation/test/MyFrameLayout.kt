package com.example.srbus.presentation.test

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.example.srbus.utils.isContainCoordinate

class MyFrameLayout : FrameLayout {
    private val TAG = javaClass.simpleName

    private var childView: View? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.i(TAG, "dispatchTouchEvent, ev=${when (ev?.action) {
            MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
            MotionEvent.ACTION_UP -> "ACTION_UP"
            else -> "ELSE"
        }}")
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG, "onInterceptTouchEvent, ev=${when (ev?.action) {
            MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
            MotionEvent.ACTION_UP -> "ACTION_UP"
            else -> "ELSE"
        }}")
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (ev == null)
            return super.onTouchEvent(ev)

//        Log.e(TAG, "isContainCoordinate(ev.x, ev.y)=${isContainCoordinate(ev.x, ev.y)}")

        Log.v(TAG, "onTouchEvent, ev=${when (ev.action) {
            MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
            MotionEvent.ACTION_UP -> "ACTION_UP"
            else -> "ELSE"
        }}")

//        childView?.let {
//            if (it.x <= ev.x && ev.x <= (it.x + it.width) &&
//                it.y <= ev.y && ev.y <= (it.y + it.height)) {
//                it.onTouchEvent(ev)
//            }
//        }

        return super.onTouchEvent(ev)
    }

    fun setChildView(view: View) {
        childView = view
    }
}