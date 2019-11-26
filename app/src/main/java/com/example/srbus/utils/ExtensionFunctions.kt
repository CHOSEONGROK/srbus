package com.example.srbus.utils

import android.util.Log
import android.view.View

fun View.isContainCoordinate(x: Float, y: Float): Boolean {
//    Log.v("ExtensionFunctions.kt", "View.isContainCoordinate, this.x=${this.x}, x=$x, width=$width, this.y=${this.y}, y=$y, height=$height ")
    return (this.x <= x && x <= (this.x + width) &&
            this.y <= y && y <= (this.y + height))
}
