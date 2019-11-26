package com.example.srbus.presentation.test

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import com.example.srbus.utils.isContainCoordinate

class MyImageView : ImageView {
    private val TAG = javaClass.simpleName


    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?) : super(context)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
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

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (ev == null)
            return super.onTouchEvent(ev)

//        Log.e(TAG, "isContainCoordinate(ev.x, ev.y)=${isContainCoordinate(ev.x, ev.y)}")
//        if (!isContainCoordinate(ev.x, ev.y)) {
//            return true
//        }

        Log.v(TAG, "onTouchEvent, ev=${when (ev.action) {
            MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
            MotionEvent.ACTION_UP -> "ACTION_UP"
            else -> "ELSE"
        }}")

        return super.onTouchEvent(ev)
    }

//    override fun setOnClickListener(l: OnClickListener?) {
////        l?.onClick(null)
//    }
}