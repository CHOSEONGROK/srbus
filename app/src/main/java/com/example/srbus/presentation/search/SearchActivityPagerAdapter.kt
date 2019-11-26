package com.example.srbus.presentation.search

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log

class SearchActivityPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val tabCount: Int
) : FragmentStatePagerAdapter(fragmentManager) {
    private val TAG = javaClass.simpleName

    companion object {
        const val POSITION_SEARCH_BUS = 0
        const val POSITION_SEARCH_STATION = 1
    }

    override fun getCount() = tabCount

    override fun getItem(position: Int): Fragment = when (position) {
        POSITION_SEARCH_BUS -> {
            Log.d(TAG, "getItem($position)")
            SearchBusFragment()
        }
        POSITION_SEARCH_STATION -> {
            Log.d(TAG, "getItem($position)")
            SearchStationFragment()
        }
        else -> Fragment()
    }
}