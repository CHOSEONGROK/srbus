package com.example.srbus.utils

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.ColorRes
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics




class Util {

    companion object {
        fun getStatusBarHeight(context: Context?): Int {
            if (context != null) {
                val statusBarHeightResourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
                if (statusBarHeightResourceId > 0) {
                    return context.resources.getDimensionPixelSize(statusBarHeightResourceId)
                }
            }
            return 0
        }
        fun getNavigationBarHeight(context: Context?): Int {
            if (context != null) {
                val navigationBarHeightResourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
                if (navigationBarHeightResourceId > 0) {
                    return context.resources.getDimensionPixelSize(navigationBarHeightResourceId)
                }
            }
            return 0
        }

        fun showSoftInput(context: Context, edt: EditText) {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(edt, 0)
        }

        fun hideSoftInput(context: Context, edt: EditText) {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(edt.windowToken, 0)
        }

        fun setTranslucentStatusBar(window: Window) {
            val sdkInt = Build.VERSION.SDK_INT
            if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
                setTranslucentStatusBarLollipop(window)
            } else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatusBarKiKat(window)
            }
        }

        fun setNoTranslucentStatusBar(window: Window, @ColorRes id: Int = com.example.srbus.R.color.statusBarColor) {
            val sdkInt = Build.VERSION.SDK_INT
            if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
                setNoTranslucentStatusBarLollipop(window, id)
            } else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
                setNoTranslucentStatusBarKiKat(window)
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private fun setTranslucentStatusBarLollipop(window: Window) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = window.context.resources.getColor(android.R.color.transparent)
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        private fun setTranslucentStatusBarKiKat(window: Window) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private fun setNoTranslucentStatusBarLollipop(window: Window, @ColorRes id: Int) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = window.context.resources.getColor(id)
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        private fun setNoTranslucentStatusBarKiKat(window: Window) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        fun isInteger(s: String): Boolean {
            try {
                Integer.parseInt(s)
            } catch (e: NumberFormatException) {
                return false
            } catch (e: NullPointerException) {
                return false
            }
            return true
        }

        fun getColor(context: Context, id: Int): Int {
            return if (Build.VERSION.SDK_INT >= 23) {
                ContextCompat.getColor(context, id)
            } else {
                context.resources.getColor(id)
            }
        }

        fun convertDpToPixel(dp: Float, context: Context): Float =
            dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

        fun convertPixelsToDp(px: Float, context: Context): Float =
            px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}